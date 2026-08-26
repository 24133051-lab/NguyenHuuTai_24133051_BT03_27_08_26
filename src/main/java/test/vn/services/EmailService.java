package test.vn.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import test.vn.entities.User;
import test.vn.utils.AppConfig;

public class EmailService {

    public void sendActivationOtp(User user, String otp) {
        String baseUrl = AppConfig.get("APP_BASE_URL", "http://localhost:8080/BT02_JPA");
        String activationUrl = baseUrl + "/activate?email="
                + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);

        String body = emailTemplate(
                "Kích hoạt tài khoản",
                "Xin chào " + escapeHtml(user.getFullName()) + ",",
                "Mã OTP kích hoạt tài khoản của bạn là:",
                otp,
                "Mã có hiệu lực trong 5 phút.",
                activationUrl,
                "Kích hoạt tài khoản"
        );
        sendHtml(user.getEmail(), "Mã OTP kích hoạt tài khoản", body);
    }

    public void sendResetPasswordOtp(User user, String otp) {
        String baseUrl = AppConfig.get("APP_BASE_URL", "http://localhost:8080/BT02_JPA");
        String resetUrl = baseUrl + "/reset-password?email="
                + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);

        String body = emailTemplate(
                "Đặt lại mật khẩu",
                "Xin chào " + escapeHtml(user.getFullName()) + ",",
                "Mã OTP đặt lại mật khẩu của bạn là:",
                otp,
                "Mã có hiệu lực trong 5 phút. Nếu bạn không yêu cầu, hãy bỏ qua email này.",
                resetUrl,
                "Đặt lại mật khẩu"
        );
        sendHtml(user.getEmail(), "Mã OTP đặt lại mật khẩu", body);
    }

    private void sendHtml(String recipient, String subject, String html) {
        String username = AppConfig.get("SMTP_USERNAME");
        String password = AppConfig.get("SMTP_PASSWORD");
        if (username == null || password == null) {
            throw new IllegalStateException(
                    "Chưa cấu hình SMTP_USERNAME và SMTP_PASSWORD để gửi email.");
        }

        String host = AppConfig.get("SMTP_HOST", "smtp.gmail.com");
        int port = AppConfig.getInt("SMTP_PORT", 587);
        String from = AppConfig.get("SMTP_FROM", username);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", String.valueOf(port));
        properties.put("mail.smtp.connectiontimeout", "10000");
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from, "Nova Store", StandardCharsets.UTF_8.name()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject, StandardCharsets.UTF_8.name());
            message.setContent(html, "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (Exception exception) {
            throw new IllegalStateException("Không thể gửi email OTP. Vui lòng kiểm tra cấu hình SMTP.", exception);
        }
    }

    private String emailTemplate(String title,
                                 String greeting,
                                 String message,
                                 String otp,
                                 String note,
                                 String actionUrl,
                                 String actionLabel) {
        return """
                <!doctype html><html><body style="margin:0;background:#f4f7fb;font-family:Arial,sans-serif;color:#172033">
                <table width="100%%" cellpadding="0" cellspacing="0"><tr><td align="center" style="padding:36px 16px">
                <table width="560" cellpadding="0" cellspacing="0" style="max-width:100%%;background:#fff;border-radius:18px;overflow:hidden;box-shadow:0 12px 40px rgba(28,44,79,.12)">
                <tr><td style="background:linear-gradient(135deg,#5b5ee8,#7950f2);padding:28px 36px;color:#fff"><b style="font-size:22px">NOVA</b><div style="margin-top:8px;font-size:16px">%s</div></td></tr>
                <tr><td style="padding:34px 36px"><p style="font-size:16px">%s</p><p>%s</p>
                <div style="margin:26px 0;padding:18px;text-align:center;background:#f0efff;border-radius:12px;font-size:32px;font-weight:700;letter-spacing:10px;color:#5b5ee8">%s</div>
                <p style="color:#667085;font-size:14px">%s</p>
                <p style="margin:28px 0"><a href="%s" style="display:inline-block;background:#5b5ee8;color:#fff;text-decoration:none;padding:13px 20px;border-radius:9px;font-weight:700">%s</a></p>
                <p style="color:#98a2b3;font-size:12px">Đây là email tự động, vui lòng không trả lời.</p></td></tr>
                </table></td></tr></table></body></html>
                """.formatted(title, greeting, message, otp, note, actionUrl, actionLabel);
    }

    private String escapeHtml(String value) {
        return value == null ? "" : value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}

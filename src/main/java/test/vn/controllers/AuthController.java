package test.vn.controllers;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import test.vn.entities.User;
import test.vn.services.EmailService;
import test.vn.services.IUserService;
import test.vn.services.IUserService.OtpDelivery;
import test.vn.services.impl.UserServiceImpl;

@WebServlet(urlPatterns = {
        "/register", "/activate", "/activate/resend",
        "/login", "/logout", "/forgot-password", "/reset-password"
})
public class AuthController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE);

    private final IUserService userService = new UserServiceImpl();
    private final EmailService emailService = new EmailService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login?logout=1");
            return;
        }

        if (request.getSession().getAttribute("currentUser") != null
                && ("/login".equals(path) || "/register".equals(path))) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String view = switch (path) {
            case "/register" -> "/WEB-INF/views/auth/register.jsp";
            case "/activate" -> {
                String email = value(request, "email");
                if (email.isBlank()) {
                    Object pending = request.getSession().getAttribute("pendingActivationEmail");
                    email = pending == null ? "" : pending.toString();
                }
                request.setAttribute("email", email);
                yield "/WEB-INF/views/auth/activate.jsp";
            }
            case "/login" -> "/WEB-INF/views/auth/login.jsp";
            case "/forgot-password" -> "/WEB-INF/views/auth/forgot-password.jsp";
            case "/reset-password" -> {
                String email = value(request, "email");
                if (email.isBlank()) {
                    Object pending = request.getSession().getAttribute("pendingResetEmail");
                    email = pending == null ? "" : pending.toString();
                }
                request.setAttribute("email", email);
                yield "/WEB-INF/views/auth/reset-password.jsp";
            }
            default -> null;
        };

        if (view == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            request.getRequestDispatcher(view).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        switch (request.getServletPath()) {
            case "/register" -> register(request, response);
            case "/activate" -> activate(request, response);
            case "/activate/resend" -> resendActivationOtp(request, response);
            case "/login" -> login(request, response);
            case "/forgot-password" -> forgotPassword(request, response);
            case "/reset-password" -> resetPassword(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void register(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = value(request, "fullName");
        String email = value(request, "email").toLowerCase();
        String password = value(request, "password");
        String confirmPassword = value(request, "confirmPassword");
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);

        String validationError = validateRegistration(fullName, email, password, confirmPassword);
        if (validationError != null) {
            forwardError(request, response, "/WEB-INF/views/auth/register.jsp", validationError);
            return;
        }

        try {
            OtpDelivery delivery = userService.register(fullName, email, password);
            request.getSession().setAttribute("pendingActivationEmail", email);
            try {
                emailService.sendActivationOtp(delivery.user(), delivery.otp());
                flash(request, "flashSuccess", "Mã OTP kích hoạt đã được gửi tới email của bạn.");
            } catch (RuntimeException mailException) {
                flash(request, "flashError", mailException.getMessage()
                        + " Tài khoản đã được tạo; hãy cấu hình email rồi chọn Gửi lại OTP.");
            }
            redirectWithEmail(request, response, "/activate", email);
        } catch (IllegalArgumentException exception) {
            forwardError(request, response, "/WEB-INF/views/auth/register.jsp", exception.getMessage());
        }
    }

    private void activate(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        String email = value(request, "email").toLowerCase();
        String otp = value(request, "otp");
        request.setAttribute("email", email);

        if (!EMAIL_PATTERN.matcher(email).matches() || !otp.matches("\\d{6}")) {
            forwardError(request, response, "/WEB-INF/views/auth/activate.jsp",
                    "Email hoặc mã OTP không hợp lệ.");
            return;
        }

        if (!userService.activate(email, otp)) {
            forwardError(request, response, "/WEB-INF/views/auth/activate.jsp",
                    "Mã OTP không đúng hoặc đã hết hạn.");
            return;
        }

        request.getSession().removeAttribute("pendingActivationEmail");
        flash(request, "flashSuccess", "Kích hoạt tài khoản thành công. Bạn có thể đăng nhập ngay.");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void resendActivationOtp(HttpServletRequest request,
                                     HttpServletResponse response)
            throws IOException {
        String email = value(request, "email").toLowerCase();
        try {
            OtpDelivery delivery = userService.createActivationOtp(email);
            if (delivery == null) {
                flash(request, "flashError", "Tài khoản không tồn tại hoặc đã được kích hoạt.");
            } else {
                emailService.sendActivationOtp(delivery.user(), delivery.otp());
                flash(request, "flashSuccess", "Đã gửi một mã OTP mới tới email của bạn.");
            }
        } catch (RuntimeException exception) {
            flash(request, "flashError", exception.getMessage());
        }
        redirectWithEmail(request, response, "/activate", email);
    }

    private void login(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        String email = value(request, "email").toLowerCase();
        String password = value(request, "password");
        request.setAttribute("email", email);

        User user = userService.authenticate(email, password);
        if (user == null) {
            User existingUser = userService.findByEmail(email);
            String message = existingUser != null && !existingUser.isActive()
                    ? "Tài khoản chưa được kích hoạt. Vui lòng nhập OTP đã nhận."
                    : "Email hoặc mật khẩu không chính xác.";
            if (existingUser != null && !existingUser.isActive()) {
                request.setAttribute("activationEmail", email);
            }
            forwardError(request, response, "/WEB-INF/views/auth/login.jsp", message);
            return;
        }

        HttpSession oldSession = request.getSession(false);
        Object redirectAfterLogin = oldSession == null ? null
                : oldSession.getAttribute("redirectAfterLogin");
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("currentUser", user);
        newSession.setMaxInactiveInterval(30 * 60);

        String destination = redirectAfterLogin instanceof String savedPath
                && savedPath.startsWith(request.getContextPath() + "/")
                ? savedPath
                : request.getContextPath() + "/";
        response.sendRedirect(destination);
    }

    private void forgotPassword(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {
        String email = value(request, "email").toLowerCase();
        request.setAttribute("email", email);
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            forwardError(request, response, "/WEB-INF/views/auth/forgot-password.jsp",
                    "Vui lòng nhập một địa chỉ email hợp lệ.");
            return;
        }

        try {
            OtpDelivery delivery = userService.createResetOtp(email);
            if (delivery == null) {
                forwardError(request, response, "/WEB-INF/views/auth/forgot-password.jsp",
                        "Không tìm thấy tài khoản đã kích hoạt với email này.");
                return;
            }
            emailService.sendResetPasswordOtp(delivery.user(), delivery.otp());
            request.getSession().setAttribute("pendingResetEmail", email);
            flash(request, "flashSuccess", "Mã OTP đặt lại mật khẩu đã được gửi tới email của bạn.");
            redirectWithEmail(request, response, "/reset-password", email);
        } catch (RuntimeException exception) {
            forwardError(request, response, "/WEB-INF/views/auth/forgot-password.jsp",
                    exception.getMessage());
        }
    }

    private void resetPassword(HttpServletRequest request,
                               HttpServletResponse response)
            throws ServletException, IOException {
        String email = value(request, "email").toLowerCase();
        String otp = value(request, "otp");
        String password = value(request, "password");
        String confirmPassword = value(request, "confirmPassword");
        request.setAttribute("email", email);

        if (!EMAIL_PATTERN.matcher(email).matches() || !otp.matches("\\d{6}")) {
            forwardError(request, response, "/WEB-INF/views/auth/reset-password.jsp",
                    "Email hoặc mã OTP không hợp lệ.");
            return;
        }
        if (!isStrongEnough(password)) {
            forwardError(request, response, "/WEB-INF/views/auth/reset-password.jsp",
                    "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ và số.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            forwardError(request, response, "/WEB-INF/views/auth/reset-password.jsp",
                    "Mật khẩu xác nhận không khớp.");
            return;
        }
        if (!userService.resetPassword(email, otp, password)) {
            forwardError(request, response, "/WEB-INF/views/auth/reset-password.jsp",
                    "Mã OTP không đúng hoặc đã hết hạn.");
            return;
        }

        request.getSession().removeAttribute("pendingResetEmail");
        flash(request, "flashSuccess", "Đổi mật khẩu thành công. Hãy đăng nhập bằng mật khẩu mới.");
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private String validateRegistration(String fullName,
                                        String email,
                                        String password,
                                        String confirmPassword) {
        if (fullName.length() < 2 || fullName.length() > 120) {
            return "Họ tên phải có từ 2 đến 120 ký tự.";
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Địa chỉ email không hợp lệ.";
        }
        if (!isStrongEnough(password)) {
            return "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ và số.";
        }
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp.";
        }
        return null;
    }

    private boolean isStrongEnough(String password) {
        return password.length() >= 8
                && password.chars().anyMatch(Character::isLetter)
                && password.chars().anyMatch(Character::isDigit);
    }

    private String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private void forwardError(HttpServletRequest request,
                              HttpServletResponse response,
                              String view,
                              String error) throws ServletException, IOException {
        request.setAttribute("error", error);
        request.getRequestDispatcher(view).forward(request, response);
    }

    private void flash(HttpServletRequest request, String key, String value) {
        request.getSession().setAttribute(key, value);
    }

    private void redirectWithEmail(HttpServletRequest request,
                                   HttpServletResponse response,
                                   String path,
                                   String email) throws IOException {
        response.sendRedirect(request.getContextPath() + path + "?email="
                + URLEncoder.encode(email, StandardCharsets.UTF_8));
    }
}

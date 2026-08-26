package test.vn.services.impl;

import java.time.LocalDateTime;

import test.vn.dao.IUserDAO;
import test.vn.dao.impl.UserDAO;
import test.vn.entities.User;
import test.vn.services.IUserService;
import test.vn.utils.AppConfig;
import test.vn.utils.PasswordUtils;

public class UserServiceImpl implements IUserService {

    private static final int OTP_VALID_MINUTES = 5;

    private final IUserDAO userDAO = new UserDAO();

    @Override
    public OtpDelivery register(String fullName, String email, String password) {
        String normalizedEmail = normalizeEmail(email);
        if (userDAO.findByEmail(normalizedEmail) != null) {
            throw new IllegalArgumentException("Email này đã được sử dụng.");
        }

        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(PasswordUtils.hashPassword(password));
        user.setActive(false);

        String adminEmail = normalizeEmail(AppConfig.get("APP_ADMIN_EMAIL", ""));
        user.setRole(normalizedEmail.equals(adminEmail) ? "ADMIN" : "USER");

        String otp = PasswordUtils.generateOtp();
        user.setActivationOtpHash(PasswordUtils.hashOtp(otp));
        user.setActivationOtpExpiresAt(LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES));
        userDAO.insert(user);
        return new OtpDelivery(user, otp);
    }

    @Override
    public OtpDelivery createActivationOtp(String email) {
        User user = userDAO.findByEmail(normalizeEmail(email));
        if (user == null || user.isActive()) {
            return null;
        }

        String otp = PasswordUtils.generateOtp();
        user.setActivationOtpHash(PasswordUtils.hashOtp(otp));
        user.setActivationOtpExpiresAt(LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES));
        userDAO.update(user);
        return new OtpDelivery(user, otp);
    }

    @Override
    public boolean activate(String email, String otp) {
        User user = userDAO.findByEmail(normalizeEmail(email));
        if (user == null || user.isActive()
                || user.getActivationOtpHash() == null
                || user.getActivationOtpExpiresAt() == null
                || LocalDateTime.now().isAfter(user.getActivationOtpExpiresAt())
                || !PasswordUtils.matchesOtp(otp, user.getActivationOtpHash())) {
            return false;
        }

        user.setActive(true);
        user.setActivationOtpHash(null);
        user.setActivationOtpExpiresAt(null);
        userDAO.update(user);
        return true;
    }

    @Override
    public User authenticate(String email, String password) {
        User user = userDAO.findByEmail(normalizeEmail(email));
        if (user == null || !user.isActive()
                || !PasswordUtils.matchesPassword(password, user.getPasswordHash())) {
            return null;
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(normalizeEmail(email));
    }

    @Override
    public OtpDelivery createResetOtp(String email) {
        User user = userDAO.findByEmail(normalizeEmail(email));
        if (user == null || !user.isActive()) {
            return null;
        }

        String otp = PasswordUtils.generateOtp();
        user.setResetOtpHash(PasswordUtils.hashOtp(otp));
        user.setResetOtpExpiresAt(LocalDateTime.now().plusMinutes(OTP_VALID_MINUTES));
        userDAO.update(user);
        return new OtpDelivery(user, otp);
    }

    @Override
    public boolean resetPassword(String email, String otp, String newPassword) {
        User user = userDAO.findByEmail(normalizeEmail(email));
        if (user == null || !user.isActive()
                || user.getResetOtpHash() == null
                || user.getResetOtpExpiresAt() == null
                || LocalDateTime.now().isAfter(user.getResetOtpExpiresAt())
                || !PasswordUtils.matchesOtp(otp, user.getResetOtpHash())) {
            return false;
        }

        user.setPasswordHash(PasswordUtils.hashPassword(newPassword));
        user.setResetOtpHash(null);
        user.setResetOtpExpiresAt(null);
        userDAO.update(user);
        return true;
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}

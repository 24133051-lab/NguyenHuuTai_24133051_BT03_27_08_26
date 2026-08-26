package test.vn.services;

import test.vn.entities.User;

public interface IUserService {

    OtpDelivery register(String fullName, String email, String password);

    OtpDelivery createActivationOtp(String email);

    boolean activate(String email, String otp);

    User authenticate(String email, String password);

    User findByEmail(String email);

    OtpDelivery createResetOtp(String email);

    boolean resetPassword(String email, String otp, String newPassword);

    record OtpDelivery(User user, String otp) {
    }
}

package test.vn.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class PasswordUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int BCRYPT_COST = 12;

    private PasswordUtils() {
    }

    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, password.toCharArray());
    }

    public static boolean matchesPassword(String password, String passwordHash) {
        if (password == null || passwordHash == null) {
            return false;
        }
        return BCrypt.verifyer().verify(password.toCharArray(), passwordHash).verified;
    }

    public static String generateOtp() {
        return String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
    }

    public static String hashOtp(String otp) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String pepper = AppConfig.get("OTP_PEPPER", "bt02-jpa-local-development");
            byte[] hash = digest.digest((pepper + ':' + otp).getBytes(StandardCharsets.UTF_8));
            return toHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    public static boolean matchesOtp(String otp, String expectedHash) {
        if (otp == null || expectedHash == null) {
            return false;
        }
        return MessageDigest.isEqual(
                hashOtp(otp.trim()).getBytes(StandardCharsets.UTF_8),
                expectedHash.getBytes(StandardCharsets.UTF_8)
        );
    }

    private static String toHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            builder.append(String.format("%02x", value));
        }
        return builder.toString();
    }
}

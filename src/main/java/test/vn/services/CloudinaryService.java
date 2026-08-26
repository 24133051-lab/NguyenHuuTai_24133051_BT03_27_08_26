package test.vn.services;

import java.io.IOException;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.servlet.http.Part;

import test.vn.utils.AppConfig;

public class CloudinaryService {

    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        String cloudinaryUrl = AppConfig.get("CLOUDINARY_URL");
        if (cloudinaryUrl != null) {
            cloudinary = new Cloudinary(cloudinaryUrl);
            cloudinary.config.secure = true;
            return;
        }

        String cloudName = AppConfig.get("CLOUDINARY_CLOUD_NAME");
        String apiKey = AppConfig.get("CLOUDINARY_API_KEY");
        String apiSecret = AppConfig.get("CLOUDINARY_API_SECRET");
        if (cloudName == null || apiKey == null || apiSecret == null) {
            cloudinary = null;
            return;
        }

        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

    public UploadResult uploadImage(Part part, String folder) {
        validateImage(part);
        ensureConfigured();
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    part.getInputStream().readAllBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "image",
                            "use_filename", true,
                            "unique_filename", true,
                            "overwrite", false
                    )
            );
            return new UploadResult(
                    String.valueOf(result.get("secure_url")),
                    String.valueOf(result.get("public_id"))
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Không thể tải ảnh lên Cloudinary.", exception);
        }
    }

    public void deleteImage(String publicId) {
        if (publicId == null || publicId.isBlank() || cloudinary == null) {
            return;
        }
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException exception) {
            throw new IllegalStateException("Không thể xóa ảnh cũ trên Cloudinary.", exception);
        }
    }

    public boolean hasFile(Part part) {
        return part != null
                && part.getSize() > 0
                && part.getSubmittedFileName() != null
                && !part.getSubmittedFileName().isBlank();
    }

    private void validateImage(Part part) {
        if (!hasFile(part)) {
            throw new IllegalArgumentException("Vui lòng chọn ảnh cần tải lên.");
        }
        if (part.getSize() > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("Ảnh không được vượt quá 5 MB.");
        }
        String contentType = part.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            throw new IllegalArgumentException("Tệp tải lên phải là hình ảnh.");
        }
    }

    private void ensureConfigured() {
        if (cloudinary == null) {
            throw new IllegalStateException(
                    "Chưa cấu hình CLOUDINARY_URL hoặc bộ biến CLOUDINARY_CLOUD_NAME/API_KEY/API_SECRET.");
        }
    }

    public record UploadResult(String url, String publicId) {
    }
}

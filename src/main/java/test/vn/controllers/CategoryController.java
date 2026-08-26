package test.vn.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import test.vn.entities.Category;
import test.vn.services.CloudinaryService;
import test.vn.services.CloudinaryService.UploadResult;
import test.vn.services.ICategoryService;
import test.vn.services.impl.CategoryServiceImpl;

@MultipartConfig(maxFileSize = 5L * 1024 * 1024, maxRequestSize = 6L * 1024 * 1024)
@WebServlet(urlPatterns = {
        "/admin/categories",
        "/admin/category/add",
        "/admin/category/insert",
        "/admin/category/edit",
        "/admin/category/update",
        "/admin/category/delete"
})
public class CategoryController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ICategoryService categoryService = new CategoryServiceImpl();
    private final CloudinaryService cloudinaryService = new CloudinaryService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/admin/categories".equals(path)) {
            request.setAttribute("listcate", categoryService.findAll());
            request.getRequestDispatcher("/admin/category-list.jsp").forward(request, response);
            return;
        }

        if ("/admin/category/add".equals(path)) {
            request.getRequestDispatcher("/admin/category-add.jsp").forward(request, response);
            return;
        }

        if ("/admin/category/edit".equals(path)) {
            Category category = findCategory(request, response);
            if (category == null) {
                return;
            }
            request.setAttribute("cate", category);
            request.getRequestDispatcher("/admin/category-edit.jsp").forward(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();

        if ("/admin/category/insert".equals(path)) {
            insert(request, response);
        } else if ("/admin/category/update".equals(path)) {
            update(request, response);
        } else if ("/admin/category/delete".equals(path)) {
            delete(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void insert(HttpServletRequest request,
                        HttpServletResponse response)
            throws ServletException, IOException {
        String name = value(request, "categoryname");
        if (name.isBlank()) {
            request.setAttribute("error", "Tên danh mục không được để trống.");
            request.getRequestDispatcher("/admin/category-add.jsp").forward(request, response);
            return;
        }
        if (categoryService.findByCategoryName(name) != null) {
            request.setAttribute("error", "Tên danh mục đã tồn tại.");
            request.getRequestDispatcher("/admin/category-add.jsp").forward(request, response);
            return;
        }

        Category category = new Category();
        category.setCategoryname(name);
        category.setStatus(parseStatus(request));
        category.setImages(value(request, "imageUrl"));

        UploadResult uploadedImage = null;
        try {
            Part imageFile = request.getPart("imageFile");
            if (cloudinaryService.hasFile(imageFile)) {
                uploadedImage = cloudinaryService.uploadImage(imageFile, "bt02_jpa/categories");
                category.setImages(uploadedImage.url());
                category.setImagePublicId(uploadedImage.publicId());
            }
            categoryService.insert(category);
            flash(request, "flashSuccess", "Đã thêm danh mục mới.");
            redirectToList(request, response);
        } catch (RuntimeException exception) {
            if (uploadedImage != null) {
                safelyDeleteImage(uploadedImage.publicId());
            }
            request.setAttribute("error", exception.getMessage());
            request.setAttribute("formCategory", category);
            request.getRequestDispatcher("/admin/category-add.jsp").forward(request, response);
        }
    }

    private void update(HttpServletRequest request,
                        HttpServletResponse response)
            throws ServletException, IOException {
        Category category = findCategory(request, response);
        if (category == null) {
            return;
        }

        String name = value(request, "categoryname");
        if (name.isBlank()) {
            request.setAttribute("error", "Tên danh mục không được để trống.");
            request.setAttribute("cate", category);
            request.getRequestDispatcher("/admin/category-edit.jsp").forward(request, response);
            return;
        }

        String oldImageUrl = category.getImages();
        String oldPublicId = category.getImagePublicId();
        UploadResult uploadedImage = null;
        try {
            category.setCategoryname(name);
            category.setStatus(parseStatus(request));

            Part imageFile = request.getPart("imageFile");
            if (cloudinaryService.hasFile(imageFile)) {
                uploadedImage = cloudinaryService.uploadImage(imageFile, "bt02_jpa/categories");
                category.setImages(uploadedImage.url());
                category.setImagePublicId(uploadedImage.publicId());
            } else if (!value(request, "imageUrl").isBlank()
                    && !value(request, "imageUrl").equals(oldImageUrl)) {
                category.setImages(value(request, "imageUrl"));
                category.setImagePublicId(null);
            }

            categoryService.update(category);
            if (oldPublicId != null && !oldPublicId.equals(category.getImagePublicId())) {
                safelyDeleteImage(oldPublicId);
            }
            flash(request, "flashSuccess", "Đã cập nhật danh mục.");
            redirectToList(request, response);
        } catch (RuntimeException exception) {
            if (uploadedImage != null) {
                safelyDeleteImage(uploadedImage.publicId());
                category.setImages(oldImageUrl);
                category.setImagePublicId(oldPublicId);
            }
            request.setAttribute("error", exception.getMessage());
            request.setAttribute("cate", category);
            request.getRequestDispatcher("/admin/category-edit.jsp").forward(request, response);
        }
    }

    private void delete(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Category category = categoryService.findById(id);
            if (category != null) {
                categoryService.delete(id);
                safelyDeleteImage(category.getImagePublicId());
            }
            flash(request, "flashSuccess", "Đã xóa danh mục.");
        } catch (RuntimeException exception) {
            flash(request, "flashError", exception.getMessage());
        }
        redirectToList(request, response);
    }

    private Category findCategory(HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        String rawId = request.getParameter("id");
        if (rawId == null) {
            rawId = request.getParameter("categoryid");
        }
        try {
            Category category = categoryService.findById(Integer.parseInt(rawId));
            if (category == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy danh mục.");
            }
            return category;
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã danh mục không hợp lệ.");
            return null;
        }
    }

    private int parseStatus(HttpServletRequest request) {
        return "1".equals(request.getParameter("status")) ? 1 : 0;
    }

    private String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private void safelyDeleteImage(String publicId) {
        try {
            cloudinaryService.deleteImage(publicId);
        } catch (RuntimeException ignored) {
            // Database operation has succeeded; stale cloud media must not break the request.
        }
    }

    private void flash(HttpServletRequest request, String key, String value) {
        request.getSession().setAttribute(key, value);
    }

    private void redirectToList(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/admin/categories");
    }
}

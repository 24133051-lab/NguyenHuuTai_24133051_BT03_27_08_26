package test.vn.controllers;

import java.io.IOException;
import java.math.BigDecimal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import test.vn.entities.Category;
import test.vn.entities.Product;
import test.vn.services.CloudinaryService;
import test.vn.services.CloudinaryService.UploadResult;
import test.vn.services.ICategoryService;
import test.vn.services.IProductService;
import test.vn.services.impl.CategoryServiceImpl;
import test.vn.services.impl.ProductServiceImpl;

@MultipartConfig(maxFileSize = 5L * 1024 * 1024, maxRequestSize = 6L * 1024 * 1024)
@WebServlet(urlPatterns = {
        "/admin/products", "/admin/product/add", "/admin/product/insert",
        "/admin/product/edit", "/admin/product/update", "/admin/product/delete"
})
public class AdminProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final BigDecimal MAX_PRICE = new BigDecimal("1000000000");

    private final IProductService productService = new ProductServiceImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl();
    private final CloudinaryService cloudinaryService = new CloudinaryService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/admin/products".equals(path)) {
            request.setAttribute("products", productService.findAll());
            request.getRequestDispatcher("/admin/product-list.jsp").forward(request, response);
            return;
        }
        if ("/admin/product/add".equals(path)) {
            request.setAttribute("categories", categoryService.findAll());
            request.getRequestDispatcher("/admin/product-add.jsp").forward(request, response);
            return;
        }
        if ("/admin/product/edit".equals(path)) {
            Product product = findProduct(request, response);
            if (product == null) {
                return;
            }
            request.setAttribute("product", product);
            request.setAttribute("categories", categoryService.findAll());
            request.getRequestDispatcher("/admin/product-edit.jsp").forward(request, response);
            return;
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        switch (request.getServletPath()) {
            case "/admin/product/insert" -> insert(request, response);
            case "/admin/product/update" -> update(request, response);
            case "/admin/product/delete" -> delete(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void insert(HttpServletRequest request,
                        HttpServletResponse response)
            throws ServletException, IOException {
        Product product = new Product();
        UploadResult uploadedImage = null;
        try {
            populateProduct(product, request);
            Part imageFile = request.getPart("imageFile");
            if (cloudinaryService.hasFile(imageFile)) {
                uploadedImage = cloudinaryService.uploadImage(imageFile, "bt02_jpa/products");
                product.setImageUrl(uploadedImage.url());
                product.setImagePublicId(uploadedImage.publicId());
            } else {
                product.setImageUrl(value(request, "imageUrl"));
            }

            productService.insert(product);
            flash(request, "flashSuccess", "Đã thêm sản phẩm mới.");
            redirectToList(request, response);
        } catch (RuntimeException exception) {
            if (uploadedImage != null) {
                safelyDeleteImage(uploadedImage.publicId());
            }
            showFormError(request, response, "/admin/product-add.jsp", product, exception.getMessage());
        }
    }

    private void update(HttpServletRequest request,
                        HttpServletResponse response)
            throws ServletException, IOException {
        Product product = findProduct(request, response);
        if (product == null) {
            return;
        }

        String oldImageUrl = product.getImageUrl();
        String oldPublicId = product.getImagePublicId();
        UploadResult uploadedImage = null;
        try {
            populateProduct(product, request);
            Part imageFile = request.getPart("imageFile");
            if (cloudinaryService.hasFile(imageFile)) {
                uploadedImage = cloudinaryService.uploadImage(imageFile, "bt02_jpa/products");
                product.setImageUrl(uploadedImage.url());
                product.setImagePublicId(uploadedImage.publicId());
            } else if (!value(request, "imageUrl").isBlank()
                    && !value(request, "imageUrl").equals(oldImageUrl)) {
                product.setImageUrl(value(request, "imageUrl"));
                product.setImagePublicId(null);
            }

            productService.update(product);
            if (oldPublicId != null && !oldPublicId.equals(product.getImagePublicId())) {
                safelyDeleteImage(oldPublicId);
            }
            flash(request, "flashSuccess", "Đã cập nhật sản phẩm.");
            redirectToList(request, response);
        } catch (RuntimeException exception) {
            if (uploadedImage != null) {
                safelyDeleteImage(uploadedImage.publicId());
                product.setImageUrl(oldImageUrl);
                product.setImagePublicId(oldPublicId);
            }
            showFormError(request, response, "/admin/product-edit.jsp", product, exception.getMessage());
        }
    }

    private void delete(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Product product = productService.findById(id);
            if (product != null) {
                productService.delete(id);
                safelyDeleteImage(product.getImagePublicId());
            }
            flash(request, "flashSuccess", "Đã xóa sản phẩm.");
        } catch (RuntimeException exception) {
            flash(request, "flashError", "Không thể xóa sản phẩm: " + exception.getMessage());
        }
        redirectToList(request, response);
    }

    private void populateProduct(Product product, HttpServletRequest request) {
        String name = value(request, "name");
        if (name.length() < 2 || name.length() > 255) {
            throw new IllegalArgumentException("Tên sản phẩm phải có từ 2 đến 255 ký tự.");
        }

        BigDecimal price;
        int quantity;
        int categoryId;
        try {
            price = new BigDecimal(value(request, "price"));
            quantity = Integer.parseInt(value(request, "quantity"));
            categoryId = Integer.parseInt(value(request, "categoryId"));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Giá, số lượng hoặc danh mục không hợp lệ.");
        }
        if (price.signum() < 0 || quantity < 0) {
            throw new IllegalArgumentException("Giá và số lượng không được là số âm.");
        }
        if (price.compareTo(MAX_PRICE) > 0) {
            throw new IllegalArgumentException("Giá sản phẩm không được vượt quá 1 tỷ đồng.");
        }

        Category category = categoryService.findById(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("Danh mục đã chọn không tồn tại.");
        }

        product.setName(name);
        product.setDescription(value(request, "description"));
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategory(category);
        product.setActive("1".equals(request.getParameter("active")));
    }

    private Product findProduct(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        String rawId = request.getParameter("id");
        try {
            Product product = productService.findById(Long.parseLong(rawId));
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm.");
            }
            return product;
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã sản phẩm không hợp lệ.");
            return null;
        }
    }

    private void showFormError(HttpServletRequest request,
                               HttpServletResponse response,
                               String view,
                               Product product,
                               String message) throws ServletException, IOException {
        request.setAttribute("error", message);
        request.setAttribute("product", product);
        request.setAttribute("categories", categoryService.findAll());
        request.getRequestDispatcher(view).forward(request, response);
    }

    private String value(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private void safelyDeleteImage(String publicId) {
        try {
            cloudinaryService.deleteImage(publicId);
        } catch (RuntimeException ignored) {
            // Keep the successful database operation even when media cleanup fails.
        }
    }

    private void flash(HttpServletRequest request, String key, String value) {
        request.getSession().setAttribute(key, value);
    }

    private void redirectToList(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
}

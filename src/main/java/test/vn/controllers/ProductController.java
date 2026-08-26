package test.vn.controllers;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import test.vn.entities.Product;
import test.vn.services.IProductService;
import test.vn.services.impl.ProductServiceImpl;

@WebServlet(urlPatterns = {"/product", "/product/detail"})
public class ProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6;

    private final IProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        if ("/product/detail".equals(request.getServletPath())) {
            showDetail(request, response);
        } else {
            showProducts(request, response);
        }
    }

    private void showProducts(HttpServletRequest request,
                              HttpServletResponse response)
            throws ServletException, IOException {
        int totalProducts = productService.countActive();
        int totalPages = Math.max(1, (int) Math.ceil(totalProducts / (double) PAGE_SIZE));
        int page = parsePositiveInt(request.getParameter("page"), 1);
        page = Math.min(page, totalPages);

        request.setAttribute("products", productService.findActivePage(page - 1, PAGE_SIZE));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalProducts", totalProducts);
        request.getRequestDispatcher("/WEB-INF/views/product/list.jsp").forward(request, response);
    }

    private void showDetail(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException {
        long id;
        try {
            id = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã sản phẩm không hợp lệ.");
            return;
        }

        Product product = productService.findActiveById(id);
        if (product == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm.");
            return;
        }
        request.setAttribute("product", product);
        request.getRequestDispatcher("/WEB-INF/views/product/detail.jsp").forward(request, response);
    }

    private int parsePositiveInt(String rawValue, int defaultValue) {
        try {
            return Math.max(1, Integer.parseInt(rawValue));
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }
}

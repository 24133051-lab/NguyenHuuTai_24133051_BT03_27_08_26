package test.vn.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import test.vn.entities.Category;
import test.vn.services.ICategoryService;
import test.vn.services.impl.CategoryServiceImpl;

@MultipartConfig
@WebServlet(urlPatterns = {
        "/admin/categories",
        "/admin/category/add",
        "/admin/category/insert",
        "/admin/category/edit",
        "/admin/category/update",
        "/admin/category/delete"
})
public class CategoryController extends HttpServlet {

    private ICategoryService categoryService =
            new CategoryServiceImpl();

    // =========================
    // GET
    // =========================
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        // Danh sách Category
        if (path.equals("/admin/categories")) {

            List<Category> list =
                    categoryService.findAll();

            req.setAttribute("listcate", list);

            req.getRequestDispatcher(
                    "/admin/category-list.jsp"
            ).forward(req, resp);

        }

        // Trang thêm
        else if (path.equals("/admin/category/add")) {

            req.getRequestDispatcher(
                    "/admin/category-add.jsp"
            ).forward(req, resp);

        }

        // Trang sửa
        else if (path.equals("/admin/category/edit")) {

            int id = Integer.parseInt(
                    req.getParameter("id")
            );

            Category category =
                    categoryService.findById(id);

            req.setAttribute("cate", category);

            req.getRequestDispatcher(
                    "/admin/category-edit.jsp"
            ).forward(req, resp);

        }

        // Xóa
        else if (path.equals("/admin/category/delete")) {

            int id = Integer.parseInt(
                    req.getParameter("id")
            );

            categoryService.delete(id);

            resp.sendRedirect(
                    req.getContextPath()
                    + "/admin/categories"
            );
        }
    }

    // =========================
    // POST
    // =========================
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String path = req.getServletPath();

        // =====================
        // INSERT
        // =====================
        if (path.equals("/admin/category/insert")) {

            String categoryname =
                    req.getParameter("categoryname");

            int status = Integer.parseInt(
                    req.getParameter("status")
            );

            String images =
                    req.getParameter("images");

            Category category =
                    new Category();

            category.setCategoryname(categoryname);
            category.setStatus(status);
            category.setImages(images);

            categoryService.insert(category);

            resp.sendRedirect(
                    req.getContextPath()
                    + "/admin/categories"
            );
        }

        // =====================
        // UPDATE
        // =====================
        else if (path.equals("/admin/category/update")) {

            int id = Integer.parseInt(
                    req.getParameter("categoryid")
            );

            String categoryname =
                    req.getParameter("categoryname");

            int status = Integer.parseInt(
                    req.getParameter("status")
            );

            String images =
                    req.getParameter("images");

            Category category =
                    categoryService.findById(id);

            if (category != null) {

                category.setCategoryname(categoryname);
                category.setStatus(status);
                category.setImages(images);

                categoryService.update(category);
            }

            resp.sendRedirect(
                    req.getContextPath()
                    + "/admin/categories"
            );
        }
    }
}
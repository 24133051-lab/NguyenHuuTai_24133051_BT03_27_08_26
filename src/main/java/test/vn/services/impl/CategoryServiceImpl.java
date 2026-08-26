package test.vn.services.impl;

import java.util.List;

import test.vn.dao.ICategoryDAO;
import test.vn.dao.impl.CategoryDAO;
import test.vn.entities.Category;
import test.vn.services.ICategoryService;

public class CategoryServiceImpl
        implements ICategoryService {

    private ICategoryDAO categoryDAO =
            new CategoryDAO();

    @Override
    public void insert(Category category) {

        Category old =
                findByCategoryName(
                    category.getCategoryname()
                );

        if (old == null) {

            categoryDAO.insert(category);
        }
    }

    @Override
    public void update(Category category) {

        Category old =
                findById(
                    category.getCategoryid()
                );

        if (old != null) {

            categoryDAO.update(category);
        }
    }

    @Override
    public void delete(int id) {

        categoryDAO.delete(id);
    }

    @Override
    public Category findById(int id) {

        return categoryDAO.findById(id);
    }

    @Override
    public Category findByCategoryName(
            String name) {

        return categoryDAO
                .findByCategoryName(name);
    }

    @Override
    public List<Category> findAll() {

        return categoryDAO.findAll();
    }

    @Override
    public List<Category> searchByName(
            String keyword) {

        return categoryDAO
                .searchByName(keyword);
    }

    @Override
    public List<Category> findAll(
            int page,
            int pageSize) {

        return categoryDAO.findAll(
                page,
                pageSize);
    }

    @Override
    public int count() {

        return categoryDAO.count();
    }
}
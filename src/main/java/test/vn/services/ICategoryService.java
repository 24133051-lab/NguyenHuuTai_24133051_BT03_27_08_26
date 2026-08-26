package test.vn.services;

import java.util.List;
import test.vn.entities.Category;

public interface ICategoryService {

    void insert(Category category);

    void update(Category category);

    void delete(int id);

    Category findById(int id);

    Category findByCategoryName(String name);

    List<Category> findAll();

    List<Category> searchByName(String keyword);

    List<Category> findAll(int page, int pageSize);

    int count();
}
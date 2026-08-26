package test.vn.services;

import java.util.List;

import test.vn.entities.Product;

public interface IProductService {

    void insert(Product product);

    void update(Product product);

    void delete(long id);

    Product findById(long id);

    Product findActiveById(long id);

    List<Product> findAll();

    List<Product> findActivePage(int page, int pageSize);

    List<Product> findNewestActive(int limit);

    int countActive();
}

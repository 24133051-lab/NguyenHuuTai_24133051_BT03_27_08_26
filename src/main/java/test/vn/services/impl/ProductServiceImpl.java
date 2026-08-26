package test.vn.services.impl;

import java.util.List;

import test.vn.dao.IProductDAO;
import test.vn.dao.impl.ProductDAO;
import test.vn.entities.Product;
import test.vn.services.IProductService;

public class ProductServiceImpl implements IProductService {

    private final IProductDAO productDAO = new ProductDAO();

    @Override
    public void insert(Product product) {
        productDAO.insert(product);
    }

    @Override
    public void update(Product product) {
        productDAO.update(product);
    }

    @Override
    public void delete(long id) {
        productDAO.delete(id);
    }

    @Override
    public Product findById(long id) {
        return productDAO.findById(id);
    }

    @Override
    public Product findActiveById(long id) {
        return productDAO.findActiveById(id);
    }

    @Override
    public List<Product> findAll() {
        return productDAO.findAll();
    }

    @Override
    public List<Product> findActivePage(int page, int pageSize) {
        return productDAO.findActivePage(page, pageSize);
    }

    @Override
    public List<Product> findNewestActive(int limit) {
        return productDAO.findNewestActive(limit);
    }

    @Override
    public int countActive() {
        return productDAO.countActive();
    }
}

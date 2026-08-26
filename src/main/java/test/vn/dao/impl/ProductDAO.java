package test.vn.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import test.vn.configs.JPAConfig;
import test.vn.dao.IProductDAO;
import test.vn.entities.Product;

public class ProductDAO implements IProductDAO {

    @Override
    public void insert(Product product) {
        executeInTransaction(entityManager -> entityManager.persist(product));
    }

    @Override
    public void update(Product product) {
        executeInTransaction(entityManager -> entityManager.merge(product));
    }

    @Override
    public void delete(long id) {
        executeInTransaction(entityManager -> {
            Product product = entityManager.find(Product.class, id);
            if (product != null) {
                entityManager.remove(product);
            }
        });
    }

    @Override
    public Product findById(long id) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            List<Product> products = entityManager.createQuery(
                            "SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id",
                            Product.class)
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .getResultList();
            return products.isEmpty() ? null : products.get(0);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Product findActiveById(long id) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            List<Product> products = entityManager.createQuery(
                            "SELECT p FROM Product p JOIN FETCH p.category "
                                    + "WHERE p.id = :id AND p.active = true",
                            Product.class)
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .getResultList();
            return products.isEmpty() ? null : products.get(0);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findAll() {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT p FROM Product p JOIN FETCH p.category "
                                    + "ORDER BY p.createdAt DESC, p.id DESC",
                            Product.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findActivePage(int page, int pageSize) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT p FROM Product p JOIN FETCH p.category "
                                    + "WHERE p.active = true "
                                    + "ORDER BY p.createdAt DESC, p.id DESC",
                            Product.class)
                    .setFirstResult(Math.max(0, page) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findNewestActive(int limit) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT p FROM Product p JOIN FETCH p.category "
                                    + "WHERE p.active = true "
                                    + "ORDER BY p.createdAt DESC, p.id DESC",
                            Product.class)
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public int countActive() {
        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            return entityManager.createQuery(
                            "SELECT COUNT(p) FROM Product p WHERE p.active = true",
                            Long.class)
                    .getSingleResult()
                    .intValue();
        } finally {
            entityManager.close();
        }
    }

    private void executeInTransaction(EntityWork work) {
        EntityManager entityManager = JPAConfig.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            work.execute(entityManager);
            transaction.commit();
        } catch (RuntimeException exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @FunctionalInterface
    private interface EntityWork {
        void execute(EntityManager entityManager);
    }
}

package test.vn.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import test.vn.configs.JPAConfig;
import test.vn.dao.ICategoryDAO;
import test.vn.entities.Category;

public class CategoryDAO implements ICategoryDAO {

    @Override
    public void insert(Category category) {

        EntityManager em =
                JPAConfig.getEntityManager();

        EntityTransaction trans =
                em.getTransaction();

        try {

            trans.begin();

            em.persist(category);

            trans.commit();

        } catch (Exception e) {

            if (trans.isActive()) {
                trans.rollback();
            }

            throw new IllegalStateException("Không thể thêm danh mục.", e);

        } finally {

            em.close();
        }
    }

    @Override
    public void update(Category category) {

        EntityManager em =
                JPAConfig.getEntityManager();

        EntityTransaction trans =
                em.getTransaction();

        try {

            trans.begin();

            em.merge(category);

            trans.commit();

        } catch (Exception e) {

            if (trans.isActive()) {
                trans.rollback();
            }

            throw new IllegalStateException("Không thể cập nhật danh mục.", e);

        } finally {

            em.close();
        }
    }

    @Override
    public void delete(int id) {

        EntityManager em =
                JPAConfig.getEntityManager();

        EntityTransaction trans =
                em.getTransaction();

        try {

            trans.begin();

            Category category =
                    em.find(Category.class, id);

            if (category != null) {
                em.remove(category);
            }

            trans.commit();

        } catch (Exception e) {

            if (trans.isActive()) {
                trans.rollback();
            }

            throw new IllegalStateException("Không thể xóa danh mục đang được sử dụng.", e);

        } finally {

            em.close();
        }
    }

    @Override
    public Category findById(int id) {

        EntityManager em =
                JPAConfig.getEntityManager();

        try {

            return em.find(
                    Category.class,
                    id
            );

        } finally {

            em.close();
        }
    }

    @Override
    public Category findByCategoryName(String name) {

        EntityManager em =
                JPAConfig.getEntityManager();

        try {

            String jpql =
                    "SELECT c FROM Category c "
                    + "WHERE c.categoryname = :name";

            List<Category> list =
                    em.createQuery(
                            jpql,
                            Category.class)
                      .setParameter(
                            "name",
                            name)
                      .setMaxResults(1)
                      .getResultList();

            if (list.isEmpty()) {
                return null;
            }

            return list.get(0);

        } finally {

            em.close();
        }
    }

    @Override
    public List<Category> findAll() {

        EntityManager em =
                JPAConfig.getEntityManager();

        try {

            return em.createNamedQuery(
                    "Category.findAll",
                    Category.class)
                    .getResultList();

        } finally {

            em.close();
        }
    }

    @Override
    public List<Category> searchByName(
            String keyword) {

        EntityManager em =
                JPAConfig.getEntityManager();

        try {

            String jpql =
                    "SELECT c FROM Category c "
                    + "WHERE LOWER(c.categoryname) "
                    + "LIKE LOWER(:keyword)";

            TypedQuery<Category> query =
                    em.createQuery(
                            jpql,
                            Category.class);

            query.setParameter(
                    "keyword",
                    "%" + keyword + "%"
            );

            return query.getResultList();

        } finally {

            em.close();
        }
    }

    @Override
    public List<Category> findAll(
            int page,
            int pageSize) {

        EntityManager em =
                JPAConfig.getEntityManager();

        try {

            TypedQuery<Category> query =
                    em.createNamedQuery(
                            "Category.findAll",
                            Category.class);

            query.setFirstResult(
                    page * pageSize
            );

            query.setMaxResults(
                    pageSize
            );

            return query.getResultList();

        } finally {

            em.close();
        }
    }

    @Override
    public int count() {

        EntityManager em =
                JPAConfig.getEntityManager();

        try {

            Long count =
                    em.createQuery(
                            "SELECT COUNT(c) "
                            + "FROM Category c",
                            Long.class)
                      .getSingleResult();

            return count.intValue();

        } finally {

            em.close();
        }
    }
}

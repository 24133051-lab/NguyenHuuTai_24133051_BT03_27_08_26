package test.vn.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import test.vn.configs.JPAConfig;
import test.vn.dao.IUserDAO;
import test.vn.entities.User;

public class UserDAO implements IUserDAO {

    @Override
    public void insert(User user) {
        executeInTransaction(entityManager -> entityManager.persist(user));
    }

    @Override
    public void update(User user) {
        executeInTransaction(entityManager -> entityManager.merge(user));
    }

    @Override
    public User findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        EntityManager entityManager = JPAConfig.getEntityManager();
        try {
            List<User> users = entityManager.createQuery(
                            "SELECT u FROM User u WHERE LOWER(u.email) = :email",
                            User.class)
                    .setParameter("email", email.trim().toLowerCase())
                    .setMaxResults(1)
                    .getResultList();
            return users.isEmpty() ? null : users.get(0);
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

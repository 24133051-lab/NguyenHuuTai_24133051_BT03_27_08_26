package test.vn.configs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAConfig {

    private static final EntityManagerFactory factory =
            Persistence.createEntityManagerFactory(
                    "jpa-hibernate-mysql"
            );

    public static EntityManager getEntityManager() {

        return factory.createEntityManager();
    }

    public static void shutdown() {

        if (factory.isOpen()) {
            factory.close();
        }
    }
}
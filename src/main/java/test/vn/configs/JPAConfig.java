package test.vn.configs;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import test.vn.utils.AppConfig;

public class JPAConfig {

    private static final EntityManagerFactory factory = createFactory();

    private static EntityManagerFactory createFactory() {
        Map<String, Object> properties = new HashMap<>();

        putIfPresent(properties, "jakarta.persistence.jdbc.url", AppConfig.get("DB_URL"));
        putIfPresent(properties, "jakarta.persistence.jdbc.user", AppConfig.get("DB_USER"));
        putIfPresent(properties, "jakarta.persistence.jdbc.password", AppConfig.get("DB_PASSWORD"));

        return Persistence.createEntityManagerFactory(
                "jpa-hibernate-mysql",
                properties
        );
    }

    private static void putIfPresent(Map<String, Object> properties,
                                     String key,
                                     String value) {
        if (value != null && !value.isBlank()) {
            properties.put(key, value);
        }
    }

    public static EntityManager getEntityManager() {

        return factory.createEntityManager();
    }

    public static void shutdown() {

        if (factory.isOpen()) {
            factory.close();
        }
    }
}

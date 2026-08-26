package test.vn.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import test.vn.configs.JPAConfig;

@WebListener
public class ApplicationLifecycleListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        JPAConfig.shutdown();
    }
}

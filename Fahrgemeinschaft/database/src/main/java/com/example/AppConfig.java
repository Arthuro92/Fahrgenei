package com.example;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

/**
 * Created by Maxi on 20.06.2016.
 */
@Configuration
@EnableJpaRepositories("com.example.repositories")
@EnableTransactionManagement
@ComponentScan("com.example")
public class AppConfig {

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        transactionManager.afterPropertiesSet();
        return transactionManager;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");
//        entityManagerFactoryBean.setPackagesToScan("com.example");
//        entityManagerFactoryBean.setPersistenceUnitName("manager");
        entityManagerFactoryBean.afterPropertiesSet();
        return entityManagerFactoryBean.getObject();
    }

    @Bean
    public RepositoryCollection mainBean() {
        return new RepositoryCollection();
    }

}

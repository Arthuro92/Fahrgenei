package com.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Maxi on 20.06.2016.
 */
@EnableJpaRepositories("com.example")
@ComponentScan("com.example")
public class InitDatabaseRepositories {

    public static RepositoryCollection init() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();

        RepositoryCollection bean = ctx.getBean(RepositoryCollection.class);
        return bean;
    }

//    public static void main(String[] args) {
//       RepositoryCollection app = init();
//        User user = new User("lenni1", "ABC", "lennart", "lennart.c.land@gmail.com");
//        app.getUserRepository().save(user);
////app.start();
//        System.out.println(app.getUserRepository().findOne("lenni1").getName());
//    }
}

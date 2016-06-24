package com.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Maxi on 20.06.2016.
 */
@EnableJpaRepositories("com.example")
@ComponentScan("com.example")
public class DatabaseRepositorie {

    static DatabaseRepositorie databaseRepositorie;
    static RepositoryCollection repositoryCollection;

    private DatabaseRepositorie() {
    }

    public static DatabaseRepositorie getInstance() {
        if (databaseRepositorie == null) {
            databaseRepositorie = new DatabaseRepositorie();
            init();
        }
            return databaseRepositorie;
        }


    private static void init() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();

        repositoryCollection = ctx.getBean(RepositoryCollection.class);
    }

    public RepositoryCollection getRepositoryCollection() {
        return repositoryCollection;
    }

//    public static void main(String[] args) {
//       RepositoryCollection app = init();
//        User user = new User("lenni1", "ABC", "lennart", "lennart.c.land@gmail.com");
//        app.getUserRepository().save(user);
////app.start();
//        System.out.println(app.getUserRepository().findOne("lenni1").getName());
//    }
}

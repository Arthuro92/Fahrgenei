package com.example;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Lennart on 20.06.2016.
 * Is a singleton and providing access to repositoryCollection in these all repositories are stored
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

    /**
     * creating AnnotationConfigApplicationContext and register the AppConfig file
     * with refresh The Config getting loaded and with .getBean Spring providing us access to Bean managed Classes such
     * as RepositoryCollection is. Because its an Bean Managed Class it enables access to all Repositories
     */
    private static void init() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();

        repositoryCollection = ctx.getBean(RepositoryCollection.class);
    }

    /**
     * Getting the Repository Collection
     * @return Repository Collection
     */
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

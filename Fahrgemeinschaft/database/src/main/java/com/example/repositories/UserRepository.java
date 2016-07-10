package com.example.repositories;

import com.example.dataobjects.User;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Lennart on 20.06.2016.
 * Database connection point, creating SQL Queries in runtime
 */
public interface UserRepository extends CrudRepository<User, String> {

    public User findByToken(String token);

    public User findByEmail(String email);

//    public ArrayList<User> findByIdIn(ArrayList<String> idList);
}

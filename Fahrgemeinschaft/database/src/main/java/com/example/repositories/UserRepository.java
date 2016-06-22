package com.example.repositories;

import com.example.dataobjects.User;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Maxi on 20.06.2016.
 */
public interface UserRepository extends CrudRepository<User, String> {
}

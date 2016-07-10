package com.example.repositories;

import com.example.dataobjects.Groups;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Lennart on 20.06.2016.
 * Database connection point, creating SQL Queries in runtime
 */
public interface GroupsRepository extends CrudRepository<Groups, String> {
}

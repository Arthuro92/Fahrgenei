package com.example.repositories;

import com.example.dataobjects.Groups;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Maxi on 20.06.2016.
 */
public interface GroupsRepository extends CrudRepository<Groups, String> {
}

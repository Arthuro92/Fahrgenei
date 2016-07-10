package com.example.repositories;

import com.example.dataobjects.UserInGroup;
import com.example.repositories.keys.UserInGroupId;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by Lennart on 20.06.2016.
 * Database connection point, creating SQL Queries in runtime
 */
public interface UserInGroupRepository extends CrudRepository<UserInGroup, UserInGroupId> {

    ArrayList<UserInGroup> findByGid(String gid);
}

package com.example.repositories;

import com.example.dataobjects.UserInGroup;
import com.example.repositories.keys.UserInGroupId;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Maxi on 20.06.2016.
 */
public interface UserInGroupRepository extends CrudRepository<UserInGroup, UserInGroupId> {
}

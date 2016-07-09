package com.example.repositories;

import com.example.dataobjects.Task;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by Maxi on 20.06.2016.
 */
public interface TaskRepository extends CrudRepository<Task, String> {
    ArrayList<Task> findByGid(String gid);
}

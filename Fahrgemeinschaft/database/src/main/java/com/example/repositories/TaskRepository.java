package com.example.repositories;

import com.example.dataobjects.Task;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Maxi on 20.06.2016.
 */
public interface TaskRepository extends CrudRepository<Task, String> {

}

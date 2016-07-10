package com.example.repositories;

import com.example.dataobjects.Appointment;
import com.example.repositories.keys.AppointmentId;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by Lennart on 20.06.2016.
 * Database connection point, creating SQL Queries in runtime
 */
public interface AppointmentRepository extends CrudRepository<Appointment, AppointmentId> {
    public ArrayList<Appointment> findByGid(String gid);
}

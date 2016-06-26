package com.example.repositories;

import com.example.dataobjects.Appointment;
import com.example.repositories.keys.AppointmentId;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by Maxi on 20.06.2016.
 */
public interface AppointmentRepository extends CrudRepository<Appointment, AppointmentId> {
    public ArrayList<Appointment> findByGid(String gid);
}

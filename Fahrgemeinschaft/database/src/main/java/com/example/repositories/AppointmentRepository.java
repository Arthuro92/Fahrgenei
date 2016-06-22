package com.example.repositories;

import com.example.dataobjects.Appointment;
import com.example.repositories.keys.AppointmentId;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Maxi on 20.06.2016.
 */
public interface AppointmentRepository extends CrudRepository<Appointment, AppointmentId> {

}

package com.example.repositories;

import com.example.repositories.keys.UserInAppointmentId;
import com.example.dataobjects.Appointment;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Maxi on 20.06.2016.
 */
public interface UserInAppointmentRepository extends CrudRepository<Appointment, UserInAppointmentId> {
}

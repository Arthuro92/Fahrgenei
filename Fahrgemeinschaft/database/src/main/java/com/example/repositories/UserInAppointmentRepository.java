package com.example.repositories;

import com.example.dataobjects.UserInAppointment;
import com.example.repositories.keys.UserInAppointmentId;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Maxi on 20.06.2016.
 */
public interface UserInAppointmentRepository extends CrudRepository<UserInAppointment, UserInAppointmentId> {
}
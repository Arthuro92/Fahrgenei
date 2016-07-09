package com.example.repositories;

import com.example.dataobjects.UserInAppointment;
import com.example.repositories.keys.UserInAppointmentId;

import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * Created by Maxi on 20.06.2016.
 */
public interface UserInAppointmentRepository extends CrudRepository<UserInAppointment, UserInAppointmentId> {
    ArrayList<UserInAppointment> findByGidAndAid(String gid, int aid);
    ArrayList<UserInAppointment> findByUid(String uid);
    ArrayList<UserInAppointment> findByGid(String gid);
}

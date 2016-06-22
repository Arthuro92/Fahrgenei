package com.example;

import com.example.repositories.AppointmentRepository;
import com.example.repositories.GroupsRepository;
import com.example.repositories.TaskRepository;
import com.example.repositories.UserInAppointmentRepository;
import com.example.repositories.UserInGroupRepository;
import com.example.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by Maxi on 20.06.2016.
 */
@EnableJpaRepositories
@ComponentScan("com.example")
public class RepositoryCollection {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public GroupsRepository groupsRepository;

    @Autowired
    public AppointmentRepository appointmentRepository;

    @Autowired
    public TaskRepository taskRepository;

    @Autowired
    public UserInAppointmentRepository userInAppointmentRepository;

    @Autowired
    public UserInGroupRepository userInGroupRepository;


    public GroupsRepository getGroupsRepository() {
        return groupsRepository;
    }

    public AppointmentRepository getAppointmentRepository() {
        return appointmentRepository;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public UserInAppointmentRepository getUserInAppointmentRepository() {
        return userInAppointmentRepository;
    }

    public UserInGroupRepository getUserInGroupRepository() {
        return userInGroupRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setGroupsRepository(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    public void setAppointmentRepository(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void setUserInAppointmentRepository(UserInAppointmentRepository userInAppointmentRepository) {
        this.userInAppointmentRepository = userInAppointmentRepository;
    }

    public void setUserInGroupRepository(UserInGroupRepository userInGroupRepository) {
        this.userInGroupRepository = userInGroupRepository;
    }
}
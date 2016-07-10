package server.observer;

import com.example.DatabaseRepositorie;
import com.example.RepositoryCollection;
import com.example.repositories.AppointmentRepository;
import com.example.repositories.GroupsRepository;
import com.example.repositories.TaskRepository;
import com.example.repositories.UserInAppointmentRepository;
import com.example.repositories.UserInGroupRepository;
import com.example.repositories.UserRepository;

/**
 * Created by Lennart on 23.06.2016.
 * This class getting extended in other classes to provide access to Repositorys
 */
public class RepositorieConnector {
    protected AppointmentRepository appointmentRepository;
    protected GroupsRepository groupsRepository;
    protected TaskRepository taskRepository;
    protected UserInAppointmentRepository userInAppointmentRepository;
    protected UserInGroupRepository userInGroupRepository;
    protected UserRepository userRepository;

    /**
     * Init all repositories
     */
    public void initRepositories() {
        DatabaseRepositorie databaseRepositorie = DatabaseRepositorie.getInstance();
        RepositoryCollection repositoryCollection = databaseRepositorie.getRepositoryCollection();
        this.appointmentRepository = repositoryCollection.getAppointmentRepository();
        this.groupsRepository = repositoryCollection.getGroupsRepository();
        this.taskRepository = repositoryCollection.getTaskRepository();
        this.userInAppointmentRepository = repositoryCollection.getUserInAppointmentRepository();
        this.userInGroupRepository = repositoryCollection.getUserInGroupRepository();
        this.userRepository = repositoryCollection.getUserRepository();
    }
}

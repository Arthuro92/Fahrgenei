package server;

import com.example.DatabaseRepositorie;
import com.example.RepositoryCollection;
import com.example.dataobjects.Appointment;
import com.example.dataobjects.Groups;
import com.example.dataobjects.User;
import com.example.dataobjects.UserInAppointment;
import com.example.dataobjects.UserInGroup;
import com.example.repositories.AppointmentRepository;
import com.example.repositories.GroupsRepository;
import com.example.repositories.TaskRepository;
import com.example.repositories.UserInAppointmentRepository;
import com.example.repositories.UserInGroupRepository;
import com.example.repositories.UserRepository;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;

import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.Algorithm.Algorithm;
import server.smackccsclient.SmackCcsClient;

/**
 * Created by Lennart on 03.05.2016.
 * This Class is currently for testingpurpose but later it will be the starter for the server.smackccsclient.SmackCcsClient
 */
public class Test {


    protected static AppointmentRepository appointmentRepository;
    protected static GroupsRepository groupsRepository;
    protected static TaskRepository taskRepository;
    protected static UserInAppointmentRepository userInAppointmentRepository;
    protected static UserInGroupRepository userInGroupRepository;
    protected static UserRepository userRepository;


    public static void main(String[] args) throws Exception {
        final long senderId = 634948576599L; // your GCM sender id
        final String password = "AIzaSyCJGcfOGX9KrxznVVy_4DJoLAK-vF8KS3s";

        SmackConfiguration.DEBUG_ENABLED = false;
        SmackCcsClient ccsClient = SmackCcsClient.getInstance();
        ccsClient.connect(senderId, password);


//        ArrayList<String> result = Databaseoperator.getAppointments("lennart1234", "100732276496073160540");

//       System.out.println(result.get(0));

//        Databaseoperator.insertNewAppointment();

//        testchat(ccsClient);
//        TestAlgorithm();

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void TestAlgorithm() {
        initRepositories();
        User user1 = new User("1", "ABC", "lennart", "lennart.land", true, 3);
        User user2 = new User("2", "ABCD", "hans", "hans.land", true, 2);
        User user3 = new User("3", "ABCDE", "peter", "peter.land", true, 3);
        User user4 = new User("4", "ABCDEF", "knut", "knut.land", true, 2);
        User user5 = new User("5", "ABCDEFG", "fred", "fred.land", false, 2);

        Groups groups1 = new Groups("Grp 1", "1", "lennart", "1");
        UserInGroup userInGroup1 = new UserInGroup("1","1", 1);
        UserInGroup userInGroup2 = new UserInGroup("2","1", 1);
        UserInGroup userInGroup3 = new UserInGroup("3","1", 1);
        UserInGroup userInGroup4 = new UserInGroup("4","1", 1);
        UserInGroup userInGroup5 = new UserInGroup("5","1", 1);
        userInGroup1.setDrivingCount(8);
        userInGroup2.setDrivingCount(9);
        userInGroup3.setDrivingCount(10);
        userInGroup4.setDrivingCount(10);


        Appointment appointment = new Appointment(1,"1","wurst", "", "", "", "");
        UserInAppointment userInAppointment1 = new UserInAppointment(1,"1", "1", 1);
        UserInAppointment userInAppointment2 = new UserInAppointment(1,"1", "2", 1);
        UserInAppointment userInAppointment3 = new UserInAppointment(1,"1", "3", 1);
        UserInAppointment userInAppointment4 = new UserInAppointment(1,"1", "4", 1);
        UserInAppointment userInAppointment5 = new UserInAppointment(1,"1", "5", 1);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        groupsRepository.save(groups1);
        userInGroupRepository.save(userInGroup1);
        userInGroupRepository.save(userInGroup2);
        userInGroupRepository.save(userInGroup3);
        userInGroupRepository.save(userInGroup4);
        userInGroupRepository.save(userInGroup5);
        appointmentRepository.save(appointment);
        userInAppointmentRepository.save(userInAppointment1);
        userInAppointmentRepository.save(userInAppointment2);
        userInAppointmentRepository.save(userInAppointment3);
        userInAppointmentRepository.save(userInAppointment4);
        userInAppointmentRepository.save(userInAppointment5);

        Algorithm algorithm = new Algorithm();
        algorithm.calculateDrivers(userInAppointment1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        algorithm.calculateDrivers(userInAppointment1);

    }
    public static void initRepositories() {
        DatabaseRepositorie databaseRepositorie = DatabaseRepositorie.getInstance();
        RepositoryCollection repositoryCollection = databaseRepositorie.getRepositoryCollection();
        appointmentRepository = repositoryCollection.getAppointmentRepository();
        groupsRepository = repositoryCollection.getGroupsRepository();
        taskRepository = repositoryCollection.getTaskRepository();
        userInAppointmentRepository = repositoryCollection.getUserInAppointmentRepository();
        userInGroupRepository = repositoryCollection.getUserInGroupRepository();
        userRepository = repositoryCollection.getUserRepository();
    }
    @SuppressWarnings("unchecked")
    public static void testchat(SmackCcsClient ccsClient) throws SmackException.NotConnectedException {
        String myTime = DateFormat.getDateTimeInstance().format(new Date());
//        Chat co = new Chat("Don", myTime, "Cookies",);
//        Chat po = new Chat("Jon", myTime, "Potatoes");
//        Chat no = new Chat("Ron", myTime, "Noodles");
//        Chat pa = new Chat("Bon", myTime, "Pasta");
//        Chat me = new Chat("Zon", myTime, "Meat");
//        Chat cu = new Chat("Hon", myTime, "Cucumber");
//     ccsClient.sendDownstreamMessage("chat", "chat", "/topics/global", co);

//
//        ccsClient.sendDownstreamMessage("chat", "chat", "/topics/global", po);
//     ccsClient.sendDownstreamMessage("chat","chat","/topics/global", no );
//     ccsClient.sendDownstreamMessage("chat","chat","/topics/global", pa );
//     ccsClient.sendDownstreamMessage("chat","chat","/topics/global", me );

    }
}

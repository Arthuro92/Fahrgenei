package server;

import com.example.repositories.AppointmentRepository;
import com.example.repositories.GroupsRepository;
import com.example.repositories.TaskRepository;
import com.example.repositories.UserInAppointmentRepository;
import com.example.repositories.UserInGroupRepository;
import com.example.repositories.UserRepository;

import org.jivesoftware.smack.SmackConfiguration;

import java.util.logging.Level;
import java.util.logging.Logger;

import server.smackccsclient.SmackCcsClient;

/**
 * Created by Lennart on 03.05.2016.
 * This class starts the Server and the hibernate/Spring data jpa
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

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

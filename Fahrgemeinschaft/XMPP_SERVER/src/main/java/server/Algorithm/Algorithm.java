package server.algorithm;

import com.example.dataobjects.Appointment;
import com.example.dataobjects.JsonCollection;
import com.example.dataobjects.User;
import com.example.dataobjects.UserInAppointment;
import com.example.dataobjects.UserInGroup;
import com.example.repositories.keys.AppointmentId;
import com.example.repositories.keys.UserInGroupId;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.observer.RepositorieConnector;
import server.smackccsclient.SmackCcsClient;

/**
 * Created by Lennart on 07.07.2016.
 * Compute Drivers, priority is that all drivers with the lowest drivercount getting choosen to driver
 * Extending RepositorieConnector for getting access to Repositorys
 */
public class Algorithm extends RepositorieConnector  {
    private static final Logger logger = Logger.getLogger("Algorithm ");
    private UserInAppointment userInAppointment;
    private ArrayList<UserInAppointment> userInAppointmentArrayListOldDrivers = new ArrayList<UserInAppointment>();
    private ArrayList<UserInGroup> userInGroupArrayListOld = new ArrayList<UserInGroup>();

    public Algorithm() {
        initRepositories();
    }

    /**
     * Main Method to be called when you want to calculate Drivers
     * @param userInAppointment
     */
    public void calculateDrivers(UserInAppointment userInAppointment) {
        this.userInAppointment = userInAppointment;

        findOldDrivers();
        findNewDrivers();
    }

    /**
     * First find Old Drivers which were choosen for the appointment
     */
    private void findOldDrivers() {
        ArrayList<UserInAppointment> userInAppointmentArrayList = userInAppointmentRepository.findByGidAndAid(userInAppointment.getGid(), userInAppointment.getAid());

        for (UserInAppointment userInAppointment1 : userInAppointmentArrayList) {
            if (userInAppointment1.isDriver()) {
                userInAppointment1.setDriver(false);
                userInAppointmentArrayListOldDrivers.add(userInAppointment1);
                userInAppointmentRepository.save(userInAppointment1);

                UserInGroupId userInGroupId = new UserInGroupId(userInAppointment1.getUid(), userInAppointment1.getGid());
                UserInGroup userInGroup = userInGroupRepository.findOne(userInGroupId);
                userInGroup.setDrivingCount(userInGroup.getDrivingCount() - 1);
                userInGroupArrayListOld.add(userInGroup);
                userInGroupRepository.save(userInGroup);
            }
        }
    }

    /**
     * Find new Drivers
     */
    private void findNewDrivers() {
        ArrayList<UserInAppointment> userInAppointmentArrayList = userInAppointmentRepository.findByGidAndAid(userInAppointment.getGid(), userInAppointment.getAid());
        ArrayList<User> userArrayList = new ArrayList<>();
        ArrayList<UserInGroup> userInGroupArrayList = new ArrayList<>();
        AppointmentId appointmentId = new AppointmentId(userInAppointment.getGid(), userInAppointment.getAid());
        Appointment appointment = appointmentRepository.findOne(appointmentId);

        int membercounter = 0;

        for(UserInAppointment userInAppointment1 : userInAppointmentArrayList) {
            if (userInAppointment1.getIsParticipant() == 1) {
                membercounter++;
                User user = userRepository.findOne(userInAppointment1.getUid());
                if (user.isDriver() && !userInAppointment1.isDriver()) {
                    userArrayList.add(user);
                    UserInGroupId userInGroupId = new UserInGroupId(userInAppointment1.getUid(), userInAppointment1.getGid());
                    userInGroupArrayList.add(userInGroupRepository.findOne(userInGroupId));
                }
            }
        }
        logger.log(Level.INFO, "Total Members in Appointment: " + membercounter);
        appointment.setMembers(membercounter);
        appointment.setFreeSeats(0);

        //noinspection unchecked
        Collections.sort(userInGroupArrayList);
        ArrayList<User> drivers = new ArrayList<>();
        while(membercounter > 0) {
            if(userInGroupArrayList.size() == 0) {
                logger.log(Level.INFO, "Not enough Driver for group");
                break;
            }
            User user = userRepository.findOne(userInGroupArrayList.get(0).getUid());
            userInGroupArrayList.remove(0);
            drivers.add(user);
            membercounter -= user.getFreeSeats();
            appointment.setFreeSeats(appointment.getFreeSeats() + user.getFreeSeats());
        }

        printSolution(drivers, membercounter);
        saveSolution(drivers, appointment);
    }

    /**
     * Save Solution in Database
     * @param drivers List of Drivers
     * @param appointment Appointment where the drivers were choosen for
     */
    private void saveSolution(ArrayList<User> drivers, Appointment appointment) {
        ArrayList<UserInAppointment> userInAppointmentArrayList = new ArrayList<>();
        ArrayList<UserInGroup> userInGroupArrayList = new ArrayList<>();

        for(User user : drivers) {
            UserInAppointment userInAppointment = new UserInAppointment(this.userInAppointment.getAid(), this.userInAppointment.getGid(),user.getId(),1);
            userInAppointment.setDriver(true);
            UserInGroupId userInGroupId = new UserInGroupId(user.getId(), userInAppointment.getGid());
            UserInGroup userInGroup = userInGroupRepository.findOne(userInGroupId);

            userInGroup.setDrivingCount(userInGroup.getDrivingCount() + 1);
            userInAppointmentArrayList.add(userInAppointment);
            userInGroupArrayList.add(userInGroup);
            userInGroupRepository.save(userInGroup);
            userInAppointmentRepository.save(userInAppointment);
        }
        appointmentRepository.save(appointment);

        sendSolution(userInAppointmentArrayList, userInGroupArrayList, appointment);
    }

    /**
     * Send Solution to Group
     * @param userInAppointments UserInApointmentList for knowing the drivers
     * @param userInGroups UserInGroup for drivercount
     * @param appointment for the appointment which drivers got calculated for
     */
    private void sendSolution(ArrayList<UserInAppointment> userInAppointments, ArrayList<UserInGroup> userInGroups, Appointment appointment) {
        try {
            String[] solutionarray = new String[5];
            solutionarray[0] = JsonCollection.objectToJson(userInGroupArrayListOld);
            solutionarray[1] = JsonCollection.objectToJson(userInAppointmentArrayListOldDrivers);
            solutionarray[2] = JsonCollection.objectToJson(userInGroups);
            solutionarray[3] = JsonCollection.objectToJson(userInAppointments);
            solutionarray[4] = appointment.getJsonInString();
            SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
            smackCcsClient.sendDownstreamMessage("appointment", "newdrivers", "/topics/" + userInAppointment.getGid(), solutionarray);
            System.out.println("SENDED DRIVERS");
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print Solution for Logging
     * @param drivers drivers
     * @param membercount Member in Appointment
     */
    private void printSolution(ArrayList<User> drivers, int membercount) {
        String solution = "";
        for(User user : drivers) {
            solution += user.getName() + ", ";
        }
        logger.log(Level.INFO,"Drivers got computed: " + solution);
        logger.log(Level.INFO, "With Membercount: " + membercount);
    }

}

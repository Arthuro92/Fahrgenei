package database;

import com.dataobjects.Group;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Lennart on 24.05.2016.
 */
public class Databaseoperator {
    private static final Logger logger = Logger.getLogger("Databaseoperator");
    private static final String CON_URL = "jdbc:mysql://localhost:3306/testschema";
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "android";


    static public boolean insertNewGroup(String gid, String jsonInString) {
        if (!checkGroupInDatabase(gid)) {
            logger.log(Level.INFO, "Try adding new Group");
            Connection con = null;
            try {
                con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
                Statement stmt = con.createStatement();
                String query = "INSERT INTO groups (gid, objectstring) VALUES ('" + gid + "','" + jsonInString + "')";
                stmt.executeUpdate(query);
                stmt.close();
                con.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    static public boolean checkGroupInDatabase(String gid) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM groups WHERE gid = '" + gid + "'";
            ResultSet rs = stmt.executeQuery(query);

            if (!rs.next()) {
                logger.log(Level.INFO, "Group not in Database!");
                con.close();
                rs.close();
                return false;
            }
            con.close();
            rs.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public boolean deleteGroup(String gid) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "DELETE FROM groups WHERE gid = '" + gid + "'";
            stmt.executeUpdate(query);
            stmt.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public ArrayList<Group> getGroupList() throws NullPointerException {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM groups";
            ResultSet rs = stmt.executeQuery(query);

            ArrayList<Group> grplist = new ArrayList();

            while (rs.next()) {
                String objectstring = rs.getString("objectstring");
                Gson gson = new Gson();
                Group grpobject = gson.fromJson(objectstring, Group.class);
                grplist.add(grpobject);
                //todo this has bad perfomance since the object has to be transformed back to String for sending it
            }

            stmt.close();
            con.close();
            return grplist;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public boolean deleteUserIsInGroup(String gid, String userid) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "DELETE FROM users WHERE gid = '" + gid + "' AND uid = '" + userid + "'";
            stmt.executeUpdate(query);
            stmt.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public boolean insertNewUser(String id, String token, String objectstring, String email) {
        String userindatabase = getUserById(id);
        if (userindatabase == null || !userindatabase.equals(objectstring)) {

            String query;

            if (userindatabase == null) {
                query = "INSERT INTO users (userid, token, objectstring, email) VALUES ('" + id + "','" + token + "','" + objectstring + "','" + email + "');";
            } else {
                query = "UPDATE user SET token = " + "'" + token + "'" + ", objectstring = '" + objectstring + "' WHERE userid = '" + id + "'";
            }

            Connection con = null;
            try {
                con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                stmt.close();
                con.close();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    static public String getUserById(String id) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT objectstring FROM users WHERE userid =" + "'" + id + "'";
            ResultSet rs = stmt.executeQuery(query);


            if (rs.next()) {
                String objectstring = rs.getString("objectstring");
                return objectstring;
            }

            stmt.close();
            con.close();
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public String getUserTokenByEmail(String email) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM users WHERE email =" + "'" + email + "'";
            ResultSet rs = stmt.executeQuery(query);


            if (rs.next()) {
                String token = rs.getString("token");
                return token;
            }

            stmt.close();
            con.close();
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public String getUserIdByEmail(String email) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT userid FROM users WHERE email =" + "'" + email + "'";
            ResultSet rs = stmt.executeQuery(query);


            if (rs.next()) {
                String userid = rs.getString("userid");
                return userid;
            }

            stmt.close();
            con.close();
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public String getUserIdByToken(String token) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT userid FROM users WHERE token =" + "'" + token + "'";
            ResultSet rs = stmt.executeQuery(query);


            if (rs.next()) {
                String userid = rs.getString("userid");
                return userid;
            }

            stmt.close();
            con.close();
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    static public boolean userIsInGroup(String gid, String uid, int isJoined) {
        if (!checkIsUserInGroup(gid, uid)) {
            Connection con = null;

            try {
                con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
                Statement stmt = con.createStatement();
                String query = "INSERT INTO is_in_group (gid, uid, isJoined) VALUES ('" + gid + "','" + uid + "'," + isJoined + ");";
                stmt.executeUpdate(query);
                stmt.close();
                con.close();
                return true;


            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    static public boolean checkIsUserInGroup(String gid, String uid) {
        Connection con = null;

        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM is_in_group WHERE gid = '" + gid + "'" + " AND uid = '" + uid + "'";
            ResultSet rs = stmt.executeQuery(query);

            if (!rs.next()) {
                logger.log(Level.INFO, "User not in is_in_group Table!");
                return false;
            }
            return true;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean userIsInAppointment(String aid, String gid, String uid, int isParticipant) {

        if (!checkIsUserInAppointment(aid, gid, uid)) {
            Connection con = null;

            try {
                con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
                Statement stmt = con.createStatement();
                String query = "INSERT INTO user_in_appointment (aid, gid, uid, isParticipant) VALUES ('" + aid + "','" + gid + "','" + uid + "'," + isParticipant + ");";
                stmt.executeUpdate(query);
                stmt.close();
                con.close();
                return true;


            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean checkIsUserInAppointment(String aid, String gid, String uid) {
        Connection con = null;

        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM user_in_appointment WHERE aid = '"+ aid + "' AND gid = '" + gid + "'" + " AND uid = '" + uid + "'";
            ResultSet rs = stmt.executeQuery(query);

            if (!rs.next()) {
                logger.log(Level.INFO, "User not in user_in_appointment Table!");
                return false;
            }
            return true;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUserIsInGroup(String gid, String uid, int isJoined) {
        Connection con = null;
        logger.log(Level.INFO, "updateUserIsInGroup");
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "UPDATE is_in_group SET isJoined = " + isJoined + " WHERE gid = '" + gid + "'" + "AND uid = '" + uid + "'";
            stmt.executeUpdate(query);
            return true;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public boolean insertNewAppointment(String aid, String gid, String jsonInString) {
        if (!checkAppointmentInDatabase(aid, gid)) {
            Connection con = null;
            logger.log(Level.INFO, "Try adding new Appointment");
            try {
                con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
                Statement stmt = con.createStatement();
                String query = "INSERT INTO appointment (aid, gid, objectstring) VALUES ('" + aid + "','" + gid + "','" + jsonInString + "')";
                stmt.executeUpdate(query);
                stmt.close();
                con.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    static public boolean checkAppointmentInDatabase(String aid, String gid ) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM appointment WHERE aid = '" + aid + "' AND gid = '" + gid + "'";
            ResultSet rs = stmt.executeQuery(query);

            if (!rs.next()) {
                logger.log(Level.INFO, "Appointment not in Database!");
                con.close();
                rs.close();
                return false;
            }
            con.close();
            rs.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public ArrayList<String> getAppointments(String gid, String uid) {
        ArrayList<String> appointmentlist = new ArrayList();

        if (!checkIsUserInGroup(gid, uid)) {
            appointmentlist.add("error:noAccess");
            return appointmentlist;
        }

        Connection con = null;

        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT objectstring FROM appointment WHERE gid = '" + gid + "'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String objectstring = rs.getString("objectstring");
                appointmentlist.add(objectstring);
            }
            return appointmentlist;

        } catch (SQLException e) {
            e.printStackTrace();
            appointmentlist.add("error:sqlexception");
            return appointmentlist;
        }
    }

    public static boolean deleteAppointment(String aid, String gid) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "DELETE FROM appointment WHERE aid = '" + aid + "' AND + gid = '" + gid + "'";
            stmt.executeUpdate(query);
            stmt.close();
            con.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

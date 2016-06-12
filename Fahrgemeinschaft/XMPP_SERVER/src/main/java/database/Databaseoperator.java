package database;

import com.dataobjects.Appointment;
import com.dataobjects.Group;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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
    

    static public boolean  insertNewGroup(String id, String object) {
        Connection con = null;
        try {
            con = DriverManager.getConnection( CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "INSERT INTO groups (gid, objectstring) VALUES ('" + id + "','" + object + "')";
            stmt.executeUpdate( query );
            stmt.close();
            con.close();
            return true;
        }

        catch ( SQLException e )
        {
            e.printStackTrace();
            return false;
        }
    }

    static public boolean deleteGroup(String gid) {
        Connection con = null;
        try {
            con = DriverManager.getConnection( CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "DELETE FROM groups WHERE gid = '" + gid + "'";
            stmt.executeUpdate( query );
            stmt.close();
            con.close();
            return true;
        }

        catch ( SQLException e )
        {
            e.printStackTrace();
            return false;
        }
    }

    static public boolean deleteUserIsInGroup(String gid, String userid) {
        Connection con = null;
        try {
            con = DriverManager.getConnection( CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "DELETE FROM users WHERE gid = '" + gid + "' AND uid = '" + userid + "'";
            stmt.executeUpdate( query );
            stmt.close();
            con.close();
            return true;
        }

        catch ( SQLException e )
        {
            e.printStackTrace();
            return false;
        }
    }

    static public boolean insertNewUser(String id, String token, String objectstring, String email) {
        String userindatabase = getUserById(id);
        if(userindatabase == null || !userindatabase.equals(objectstring)) {

            String query;

            if(userindatabase == null) {
                query = "INSERT INTO users (userid, token, objectstring, email) VALUES ('" + id + "','" + token + "','" + objectstring + "','" + email + "');";
            } else {
                query = "UPDATE user SET token = " + "'" + token + "'" + ", objectstring = '"+ objectstring + "' WHERE userid = '" + id + "'";
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
            String query = "SELECT objectstring FROM users WHERE userid =" +"'" + id + "'";
            ResultSet rs = stmt.executeQuery( query );


            if(rs.next()) {
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
            String query = "SELECT * FROM users WHERE email =" +"'" + email + "'";
            ResultSet rs = stmt.executeQuery( query );


            if(rs.next()) {
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
            String query = "SELECT userid FROM users WHERE email =" +"'" + email + "'";
            ResultSet rs = stmt.executeQuery( query );


            if(rs.next()) {
                String token = rs.getString("userid");
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

    static public ArrayList<Group> getGroupList() throws NullPointerException {
        Connection con = null;
        try {
            con = DriverManager.getConnection( CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM groups";
            ResultSet rs = stmt.executeQuery( query );

            ArrayList<Group> grplist = new ArrayList();

            while(rs.next()) {
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
    
    static public ArrayList<String> getAppointments(String gid, String uid) {
        ArrayList<String> appointmentlist = new ArrayList();

        if(!checkUserInGroup(gid, uid)) {
            appointmentlist.add("error:noAccess");
            return appointmentlist;
        }

        Connection con = null;

        try {
            con = DriverManager.getConnection( CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT objectstring FROM appointment WHERE gid = '" + gid + "'";
            ResultSet rs = stmt.executeQuery( query );

            while(rs.next()) {
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

    static public boolean userIsInGroup(String gid, String uid, int isJoined) {
        Connection con = null;

        try {
            con = DriverManager.getConnection( CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "INSERT INTO is_in_group (gid, uid, isJoined) VALUES ('" + gid + "','" + uid  + "'," + isJoined + ");";
            stmt.executeUpdate(query);
            stmt.close();
            con.close();
            return true;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    static public boolean checkUserInGroup(String gid, String uid) {
        Connection con = null;

        try {
            con = DriverManager.getConnection( CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM is_in_group WHERE gid = '" + gid + "'" + " AND uid = '" + uid +"'";
            ResultSet rs = stmt.executeQuery( query );

            if(!rs.next()) {
                logger.log(Level.INFO, "Access for getting Appointment denied!");
                return false;
            }
            return true;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUserIsInGroup(String extra0, String extra1) {
        Connection con = null;
        logger.log(Level.INFO, "updateUserIsInGroup");
        try {
            con = DriverManager.getConnection( CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "UPDATE is_in_group SET isJoined = " + 1 + " WHERE gid = '" + extra0 + "'" + "AND uid = '" + extra1 + "'";
            stmt.executeUpdate(query);
            return true;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    static public void insertNewAppointment() {
        Appointment gapm1 = new Appointment("1", "Termin1", "testgrp1", new Date(2016, 10, 10, 10, 00), new Date(2016, 10, 10, 9, 45), "Uni", "Wolfsburg");
        Appointment gapm2 = new Appointment("2", "Termin2", "testgrp1", new Date(2016, 9, 9, 9, 00), new Date(2016, 10, 10, 8, 45), "Sportplatz", "Sportplatz");
        Appointment gapm3 = new Appointment("3", "Termin3", "testgrp1", new Date(2016, 8, 8, 8, 00), new Date(2016, 10, 10, 8, 45), "Bahnhof", "Hannover");
        Gson gson = new Gson();
        String objectstring2 = gson.toJson(gapm1);

        Connection con = null;
        try {
            con = DriverManager.getConnection( CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "INSERT INTO appointment (aid ,gid, objectstring) VALUES ('" + gapm1.getAid() + "','lennart1234',' " + objectstring2 + "')";
            stmt.executeUpdate( query );
            stmt.close();
            con.close();
        }

        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }

}

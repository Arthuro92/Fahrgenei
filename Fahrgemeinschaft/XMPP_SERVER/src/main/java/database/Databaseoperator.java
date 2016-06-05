package database;

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

import dataobjects.Appointment;
import dataobjects.Group;

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

    static public boolean insertNewUser(String id, String token, String object) {
        String userindatabase = getUser(id);
        if(userindatabase == null | !userindatabase.equals(object)) {

            String query;

            if(!userindatabase.equals(object)) {
                System.out.println(userindatabase);
                System.out.println(object);
                query = "UPDATE user SET token = " + "'" + token + "'" + ", objectstring = '"+ object + "' WHERE userid = '" + id + "'";
                System.out.println("update");
            } else {
                query = "INSERT INTO user (userid, token, objectstring) VALUES ('" + id + "','" + token + "','" + object + "');";
                System.out.println("insert");
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

    static public String getUser(String id) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(CON_URL, USERNAME, PASSWORD);
            Statement stmt = con.createStatement();
            String query = "SELECT objectstring FROM user WHERE userid =" +"'" + id + "'";
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
    
    static public ArrayList<String> getAppointment(String gid, String uid) {
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

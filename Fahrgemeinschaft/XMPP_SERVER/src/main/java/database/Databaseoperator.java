package database;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import dataobjects.Group;

/**
 * Created by Lennart on 24.05.2016.
 */
public class Databaseoperator {
    private static final Logger logger = Logger.getLogger("Databaseoperator");

    static public boolean  insertnewgroup(String id, String object) {
        Connection con = null;
        try {
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/testschema", "testuser", "android" );
            Statement stmt = con.createStatement();
            String query = "INSERT INTO group (gid, objectstring) VALUES ('" + id + "','" + object + "')";
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

    static public boolean insertnewuser(String id, String token, String object) {
        String userindatabase = getuser(id);
        if(userindatabase == null || !userindatabase.equals(object)) {
            Connection con = null;
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testschema", "testuser", "android");
                Statement stmt = con.createStatement();
                String query = "INSERT INTO user (userid, token, objectstring) VALUES ('" + id + "','" + token + "','" + object + "')";
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

    static public String getuser(String id) throws NullPointerException{
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testschema", "testuser", "android");
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

    static public ArrayList<Group> getgrouplist() throws NullPointerException {
        Connection con = null;
        try {
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/testschema", "testuser", "android" );
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM group";
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
}

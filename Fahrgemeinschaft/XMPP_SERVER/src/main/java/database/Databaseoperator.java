package database;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dataobjects.Group;

/**
 * Created by Lennart on 24.05.2016.
 */
public class Databaseoperator {

    static public void  insertnewgroup(String id, String object) {
        Connection con = null;
        try {
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/testschema", "testuser", "android" );
            Statement stmt = con.createStatement();
            String query = "INSERT INTO gruppe (gid, objectstring) VALUES ('" + id + "','" + object + "')";
            stmt.executeUpdate( query );
            stmt.close();
            con.close();
        }

        catch ( SQLException e )
        {
            e.printStackTrace();
        }

    }

    static public ArrayList<Group> getgrouplist() {
        Connection con = null;
        try {
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/testschema", "testuser", "android" );
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM gruppe";
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

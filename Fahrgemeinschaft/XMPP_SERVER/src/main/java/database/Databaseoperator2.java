package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Lennart on 24.05.2016.
 */
public class Databaseoperator2 {

    static public void  insertnewgroup(String id, String object) {
        Connection con = null;
        try {
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/testschema", "testuser", "android" );
            Statement stmt = con.createStatement();
            String query = "INSERT INTO gruppe (gid, objectstring) VALUES ('" + id + "','" + object + "')";
//            String query = "INSERT INTO gruppe (gid, objectstring) VALUES ( '1', 'gruppe1')";
            stmt.executeUpdate( query );
        }

        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }
}

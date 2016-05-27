import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Lennart on 24.05.2016.
 */
public class Testinsert2 {

    public Testinsert2(String id, String object) {
        Connection con = null;
        try {
            con = DriverManager.getConnection( "jdbc:mysql://localhost:3306/testschema", "testuser", "android" );
            Statement stmt = con.createStatement();
            String query = "INSERT INTO gruppe (gid, objectstring) VALUES ("+ id + "," + object + ")";
            stmt.executeUpdate( query );
        }

        catch ( SQLException e )
        {
            e.printStackTrace();
        }
    }
}

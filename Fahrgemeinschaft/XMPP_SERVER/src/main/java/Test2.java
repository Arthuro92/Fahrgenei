import java.util.logging.Level;
import java.util.logging.Logger;

import dataobjects.Group;

/**
 * Created by Lennart on 03.05.2016.
 * This Class is currently for testingpurpose but later it will be the starter for the SmackCcsClient
 */
public class Test2 {
    public static void main(String[] args) throws Exception {
        final long senderId = 634948576599L; // your GCM sender id
        final String password = "AIzaSyCJGcfOGX9KrxznVVy_4DJoLAK-vF8KS3s";

        SmackCcsClient ccsClient = new SmackCcsClient();

        ccsClient.connect(senderId, password);

        String toRegId = "c2SaOvAnEDk:APA91bF_wS1wqH2C17UyuczLbi4vg472aQY9qBzk6WXIE_KdvLNVvHgDE9HW9ZEdpk6abyH-btZZ92VOSmHWYX-NZaaKwU5dVP7ajPZJkW_7rs9wbQmgHv99UawwNLEWIvlzw2ZcrKnE";

        Group grp = new Group("test");
        ccsClient.sendDownstreamMessage("chat","test","/topics/global", grp );
        ccsClient.sendDownstreamMessage("chat","test",toRegId, grp );
//        database.databaseoperator insert = new database.databaseoperator("10", "gruppe1");
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Test2.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}}

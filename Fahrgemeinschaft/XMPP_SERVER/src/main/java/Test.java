import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import SmackCcsClient.SmackCcsClient;
import dataobjects.Chat;

/**
 * Created by Lennart on 03.05.2016.
 * This Class is currently for testingpurpose but later it will be the starter for the SmackCcsClient.SmackCcsClient
 */
public class Test {
    public static void main(String[] args) throws Exception {
        final long senderId = 634948576599L; // your GCM sender id
        final String password = "AIzaSyCJGcfOGX9KrxznVVy_4DJoLAK-vF8KS3s";

        SmackCcsClient ccsClient = SmackCcsClient.getInstance();
        ccsClient.connect(senderId, password);

        String toRegId = "c2SaOvAnEDk:APA91bF_wS1wqH2C17UyuczLbi4vg472aQY9qBzk6WXIE_KdvLNVvHgDE9HW9ZEdpk6abyH-btZZ92VOSmHWYX-NZaaKwU5dVP7ajPZJkW_7rs9wbQmgHv99UawwNLEWIvlzw2ZcrKnE";

        String myTime = DateFormat.getDateTimeInstance().format(new Date());
        Chat co = new Chat("Don", myTime, "Cookies");
        Chat po = new Chat("Jon", myTime, "Potatoes");
        Chat no = new Chat("Ron", myTime, "Noodles");
        Chat pa = new Chat("Bon", myTime, "Pasta");
        Chat me = new Chat("Zon", myTime, "Meat");
        Chat cu = new Chat("Hon", myTime, "Cucumber");
        ccsClient.sendDownstreamMessage("chat", "chat", "/topics/global", co);
//        ccsClient.sendDownstreamMessage("chat","chat","dGYXwNq6pDY:APA91bEkcA2oE6cRt5RgT8ZbyCjhqWExrc9NrN_unfBck-rlXEYl7OsKKsTCswq5EKzCB0PLy6ltZlCKIomgwCMHdS8rotMBoUv-cdl3dgWNK4sT3LcJn12VOVcQFtKLfGGXIpED-oNt", co );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", po );

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                    }
        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", no );

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                        }
        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", pa );

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                            }
        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", me );

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                                }
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", cu );
//        ccsClient.sendDownstreamMessage("chat","test",toRegId, grp );
//        database.Databaseoperator insert = new database.Databaseoperator("10", "gruppe1");
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}}

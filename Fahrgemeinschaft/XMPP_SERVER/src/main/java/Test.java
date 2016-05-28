import java.util.logging.Level;
import java.util.logging.Logger;

import SmackCcsClient.SmackCcsClient;

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

//        Group grp1 = new Group("gruppe1", 2, "lennart", "lennart", "lennart1234");
//        Group grp2 = new Group("gruppe2", 3, "lennart", "lennart", "lennart12345");
//        Group grp3 = new Group("gruppe3", 1, "lennart", "lennart", "lennart123456");
//        Gson gson = new Gson();
//        String javaobjectstring1 = gson.toJson(grp1);
//        String javaobjectstring2 = gson.toJson(grp2);
//        String javaobjectstring3 = gson.toJson(grp3);
////
////
//        Databaseoperator.insertnewgroup(grp1.getGid(),javaobjectstring1);
//        Databaseoperator.insertnewgroup(grp2.getGid(),javaobjectstring2);
//        Databaseoperator.insertnewgroup(grp3.getGid(),javaobjectstring3);
//        String myTime = DateFormat.getDateTimeInstance().format(new Date());
//        Chat co = new Chat("Don", myTime, "Cookies");
//        Chat po = new Chat("Jon", myTime, "Potatoes");
//        Chat no = new Chat("Ron", myTime, "Noodles");
//        Chat pa = new Chat("Bon", myTime, "Pasta");
//        Chat me = new Chat("Zon", myTime, "Meat");
//        Chat cu = new Chat("Hon", myTime, "Cucumber");
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", co );
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", po );
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", no );
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", pa );
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", me );
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", cu );
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", co );
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", po );
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", no );
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", pa );
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", me );
//        ccsClient.sendDownstreamMessage("chat","chat","/topics/global", cu );
//        ccsClient.sendDownstreamMessage("group","grouparray","/topics/global", grp);
//        database.Databaseoperator insert = new database.Databaseoperator("10", "gruppe1");
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}}

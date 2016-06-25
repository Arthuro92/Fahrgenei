import com.dataobjects.Chat;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;

import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import SmackCcsClient.SmackCcsClient;
import cryptography.AsymmetricEncryptionServer;

/**
 * Created by Lennart on 03.05.2016.
 * This Class is currently for testingpurpose but later it will be the starter for the SmackCcsClient.SmackCcsClient
 */
public class Test {
    public static void main(String[] args) throws Exception {
        final long senderId = 634948576599L; // your GCM sender id
        final String password = "AIzaSyCJGcfOGX9KrxznVVy_4DJoLAK-vF8KS3s";

        SmackConfiguration.DEBUG_ENABLED = false;
        SmackCcsClient ccsClient = SmackCcsClient.getInstance();
        ccsClient.connect(senderId, password);

//        ArrayList<String> result = Databaseoperator.getAppointments("lennart1234", "100732276496073160540");

//       System.out.println(result.get(0));

//        Databaseoperator.insertNewAppointment();

//        testchat(ccsClient);
        testSecurity(ccsClient);


        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}

 @SuppressWarnings("unchecked")
 public static void testchat(SmackCcsClient ccsClient) throws SmackException.NotConnectedException {
     String myTime = DateFormat.getDateTimeInstance().format(new Date());
     Chat co = new Chat("Don", myTime, "Cookies");
     Chat po = new Chat("Jon", myTime, "Potatoes");
     Chat no = new Chat("Ron", myTime, "Noodles");
     Chat pa = new Chat("Bon", myTime, "Pasta");
     Chat me = new Chat("Zon", myTime, "Meat");
     Chat cu = new Chat("Hon", myTime, "Cucumber");
     ccsClient.sendDownstreamMessage("chat", "chat", "/topics/global", co);

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
      }

    public static void testSecurity(SmackCcsClient ccsClient) throws SmackException.NotConnectedException {
        try {
            AsymmetricEncryptionServer asymmetricEncryptionServer = AsymmetricEncryptionServer.getInstance();
            ccsClient.sendDownstreamMessage("security", "public_key", "dz6_cNXPbvk:APA91bFMdkiRviyxt-kDTGU-fnlCJei3AyiK_SO_-pOJIMqKMQZ6BPfuT2SBKv3eQGu5-JFHm4WKeb5mpPLednp2pa1TGgs6Kw_5dLY9_QugPJnBtL4hJUtt8N6IETDvPfFv2WJcQcog", asymmetricEncryptionServer.getPublicKeyString());
        } catch(Exception exception) {
            System.err.println("EXCEPTION CAUGHT: " + exception.toString());
        }
    }
}

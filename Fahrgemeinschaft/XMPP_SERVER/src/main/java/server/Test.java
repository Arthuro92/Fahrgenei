package server;

import de.dataobjects.Chat;
import com.example.RepositoryCollection;
import com.example.InitDatabaseRepositories;
import com.example.dataobjects.User;

import org.jivesoftware.smack.SmackException;

import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.smackccsclient.SmackCcsClient;

/**
 * Created by Lennart on 03.05.2016.
 * This Class is currently for testingpurpose but later it will be the starter for the server.smackccsclient.SmackCcsClient
 */
public class Test {
    public static void main(String[] args) throws Exception {
        final long senderId = 634948576599L; // your GCM sender id
        final String password = "AIzaSyCJGcfOGX9KrxznVVy_4DJoLAK-vF8KS3s";

//        SmackConfiguration.DEBUG_ENABLED = false;
//        SmackCcsClient ccsClient = SmackCcsClient.getInstance();
//        ccsClient.connect(senderId, password);

        RepositoryCollection repositoryCollection = InitDatabaseRepositories.init();
        User user = new User("lenni1", "ABC", "lennart", "lennart.c.land@gmail.com");
        repositoryCollection.getUserRepository().save(user);
        System.out.println(repositoryCollection.getUserRepository().findOne("lenni1").getName());

//        ArrayList<String> result = Databaseoperator.getAppointments("lennart1234", "100732276496073160540");

//       System.out.println(result.get(0));

//        Databaseoperator.insertNewAppointment();

//        testchat(ccsClient);
        while (true) {
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
//     ccsClient.sendDownstreamMessage("chat", "chat", "/topics/global", co);


        ccsClient.sendDownstreamMessage("chat", "chat", "/topics/global", po);
     ccsClient.sendDownstreamMessage("chat","chat","/topics/global", no );
     ccsClient.sendDownstreamMessage("chat","chat","/topics/global", pa );
     ccsClient.sendDownstreamMessage("chat","chat","/topics/global", me );

    }
}

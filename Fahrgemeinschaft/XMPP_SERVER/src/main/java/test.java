import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Lennart on 03.05.2016.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        final long senderId = 634948576599L; // your GCM sender id
        final String password = "AIzaSyCJGcfOGX9KrxznVVy_4DJoLAK-vF8KS3s";

        SmackCcsClient ccsClient = new SmackCcsClient();

        ccsClient.connect(senderId, password);

        // Send a sample hello downstream message to a device.
        String toRegId = "c2SaOvAnEDk:APA91bF_wS1wqH2C17UyuczLbi4vg472aQY9qBzk6WXIE_KdvLNVvHgDE9HW9ZEdpk6abyH-btZZ92VOSmHWYX-NZaaKwU5dVP7ajPZJkW_7rs9wbQmgHv99UawwNLEWIvlzw2ZcrKnE";
        String messageId = ccsClient.nextMessageId();
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("Hello", "World");
        payload.put("CCS", "Dummy Message");
        payload.put("EmbeddedMessageId", messageId);
        String collapseKey = "sample";
        Long timeToLive = 10000L;
        String message = new JsonMessage(toRegId, messageId, payload, collapseKey, timeToLive, true).getMessage();

        ccsClient.sendDownstreamMessage(message);
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}}

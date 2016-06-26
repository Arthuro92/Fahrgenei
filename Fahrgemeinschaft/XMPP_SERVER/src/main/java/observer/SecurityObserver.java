package observer;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import SmackCcsClient.SmackCcsClient;
import cryptography.AsymmetricEncryptionServer;

/**
 * Created by david on 24.06.2016.
 */
public class SecurityObserver implements MessageObserver {
    //version 1
    private static final Logger logger = Logger.getLogger("SecurityObserver");
    private Map<String, Object> jsonObject;
    private Map<String, String> payload;

    private String adjustKeyString(String key) {
        key = key.replaceAll("\\\\u003d", "=");
        key = key.replaceAll("\"", "");
        return key;
    }

    private void establishSecurity() {
        try {
            AsymmetricEncryptionServer asymmetricEncryptionServer = AsymmetricEncryptionServer.getInstance();
            System.out.println("PUBLIC KEY ENCRYPT SYMMETRIC: " + adjustKeyString(this.payload.get("content")));
            asymmetricEncryptionServer.setSymmetricKey(asymmetricEncryptionServer.decryptPrivateKey(adjustKeyString(this.payload.get("content"))));
        } catch(Exception exception) {
            System.err.println(exception.toString());
        }
    }

    private void requestPublicKey() {
        try {
            AsymmetricEncryptionServer asymmetricEncryptionServer = AsymmetricEncryptionServer.getInstance();
            SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
            System.out.println("PUBLIC KEY SEND TO: " + this.jsonObject.get("from").toString());
            smackCcsClient.sendDownstreamMessage("security", "public_key", this.jsonObject.get("from").toString(), asymmetricEncryptionServer.getPublicKeyString());
        } catch(Exception exception) {
            System.err.println(exception.toString());
        }
    }

    /**
     * Updates the Map payload for this object to the jsonObject.
     * @param jsonObject a Map the payload for this object is updated to
     */
    public void updateMessageObserver(Map<String, Object> jsonObject) {
        this.jsonObject = jsonObject;
        if(jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if(this.payload.get("task_category").equals("security") && this.payload.get("task").equals("symmetric_key")) {
                establishSecurity();
            } else if(this.payload.get("task_category").equals("security") && this.payload.get("task").equals("public_key")) {
                requestPublicKey();
            }
        }
    }


    /**
     * Constructs a new SecurityObserver and registers it to a MessageSubject
     * @param messageSubject a MessageSubject to register to
     */
    public SecurityObserver(MessageSubject messageSubject) {
        messageSubject.registerMessageObserver(this);
        logger.log(Level.INFO, "SECURITYOBSERVER REGISTERED");
    }
}

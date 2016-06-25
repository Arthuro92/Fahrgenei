package com.android.cows.fahrgemeinschaft.observer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.cryptography.AsymmetricEncryptionClient;
import com.android.cows.fahrgemeinschaft.gcm.MyGcmSend;

/**
 * Created by david on 14.06.2016.
 */
public class SecurityObserver implements MessageObserver {
    //new new new new new
    private static final String TAG = "SecurityObserver";
    private Context context = GlobalAppContext.getAppContext();
    private Bundle payload;

    private void establishSecurity() {
        try {
            AsymmetricEncryptionClient asymmetricEncryptionClient = AsymmetricEncryptionClient.getInstance();
            asymmetricEncryptionClient.setPublicKey(this.payload.getString("content"));
            String encryptedKey = asymmetricEncryptionClient.encryptPublicKey();
            MyGcmSend myGcmSend = new MyGcmSend();
            myGcmSend.send("security", "symmetric_key", encryptedKey, context);
            System.out.println("PUBLIC KEY ENCRYPT SYMMETRIC: " + encryptedKey);
        } catch(Exception exception) {
            System.out.println("DECODING ERROR");
        }
    }

    /**
     * Updates the Bundle payload for this object to the jsonObject.
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMessageObserver(Bundle jsonObject) {
        this.payload = jsonObject;
        if(this.payload.getString("task_category").equals("security") && this.payload.get("task").equals("public_key")) {
            establishSecurity();
        }
    }

    /**
     * Constructs a new ChatObserver and registers it to a MessageSubject
     * @param messageSubject a MessageSubject to register to
     */
    public SecurityObserver(MessageSubject messageSubject) {
        messageSubject.registerMessageObserver(this);
        Log.i(TAG, "SECURITYOBSERVER REGISTERED");
    }
}

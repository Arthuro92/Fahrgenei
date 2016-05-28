package com.android.cows.fahrgemeinschaft.observer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.android.cows.fahrgemeinschaft.ChatActivity;
import com.android.cows.fahrgemeinschaft.GlobalAppContext;
import com.android.cows.fahrgemeinschaft.R;

/**
 * Created by david on 25.05.2016.
 */
public class NotificationObserver implements MessageObserver {
    private Bundle payload;
    private Context con = GlobalAppContext.getAppContext();
    public static final int NID = 987654321;

    /**
     * Sets the intent to launch ChatActivity
     * @return Intent that launches ChatActivity
     */
    private Intent setChatIntent() {
        return new Intent(con, ChatActivity.class);
    }

    /**
     * Sets and issues a Notification concerning the contents of the jsonObject
     */
    private void setNotification(Intent i) {
        System.out.println("NOTIFICATION SET TO: " + this.payload.toString() + " MESSAGE SET TO: " + this.payload.getString("content"));
        PendingIntent pIntent = PendingIntent.getActivity(con, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager nm = (NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncb = new NotificationCompat.Builder(con);
        ncb.setSmallIcon(R.drawable.ic_stat_ic_notification);
        ncb.setContentTitle(this.payload.getString("task_category"));
        ncb.setContentText(this.payload.getString("content"));
        ncb.setWhen(System.currentTimeMillis());
        ncb.setContentIntent(pIntent);
        nm.notify(NID, ncb.build());
    }

    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setNotification method so long as the task_category key of payload equals notification
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMO(Bundle jsonObject) {
        this.payload = jsonObject;
        if(this.payload.getString("task_category").equals("chat")) {
            setNotification(setChatIntent());
        }
    }

    /**
     * Constructs a new NotificationObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public NotificationObserver(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("NOTIFICATIONOBSERVER REGISTERED");
    }
}

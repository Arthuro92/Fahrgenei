package com.android.cows.fahrgemeinschaft.observer;

import android.os.Bundle;

/**
 * Created by david on 24.05.2016.
 */
public class AppointmentObserver implements MessageObserver{
    private Bundle payload;

    /**
     *
     */
    public void setAppointment() {
        System.out.println("APPOINTMENT SET TO: ");
    }

    /**
     * Updates the Bundle payload for this object to the jsonObject. Also calls the setAppointment method so long as the task_category key of payload equals appointment
     * @param jsonObject a Bundle the payload for this object is updated to
     */
    public void updateMO(Bundle jsonObject) {
        this.payload = jsonObject;
        if(this.payload.getString("task_category").equals("appointment")) {
            setAppointment();
        }
    }

    /**
     * Constructs a new AppointmentObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public AppointmentObserver(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("APPOINTMENTOBSERVER REGISTERED");
    }
}

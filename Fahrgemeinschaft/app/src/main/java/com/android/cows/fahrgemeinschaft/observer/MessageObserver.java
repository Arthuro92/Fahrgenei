package com.android.cows.fahrgemeinschaft.observer;

import android.os.Bundle;

/**
 * Created by david on 17.05.2016.
 */
public interface MessageObserver {
    //version 1
    /**
     * Updates the MessageObserver for this object on changes in the jsonObject
     * @param jsonObject a Bundle the MessageObserver is updated on
     */
    public abstract void updateMessageObserver(Bundle jsonObject);
}

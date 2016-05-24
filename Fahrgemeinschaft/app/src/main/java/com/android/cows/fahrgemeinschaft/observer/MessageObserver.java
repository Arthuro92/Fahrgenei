package com.android.cows.fahrgemeinschaft.observer;

import java.util.Map;

/**
 * Created by david on 17.05.2016.
 */
public interface MessageObserver {
    /**
     * Updates the MessageObserver for this object on changes in the jsonObject
     * @param jsonObject a Map the MessageObserver is updated on
     */
    public abstract void updateMO(Map<String, Object> jsonObject);
}

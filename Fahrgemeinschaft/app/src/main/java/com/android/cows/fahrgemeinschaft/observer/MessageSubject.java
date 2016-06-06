package com.android.cows.fahrgemeinschaft.observer;

import android.os.Bundle;
import android.util.Log;

import java.util.ListIterator;
import java.util.Vector;

/**
 * Created by david on 17.05.2016.
 */
public class MessageSubject {
    //new new
    private static final String TAG = "MessageSubject";
    private Vector<MessageObserver> messageObservers;
    private Bundle jsonObject;

    /**
     * Registers or rather adds a MessageObserver to the set of MessageObservers for this object
     * @param messageObserver a MessageObserver to be registered
     * @return true if MessageObserver was registered successfully, false else
     * @throws NullPointerException if mo equals null
     */
    public synchronized boolean registerMessageObserver(MessageObserver messageObserver) {
        if(messageObserver == null) {
            throw new NullPointerException();
        }
        if(!this.messageObservers.contains(messageObserver)) {
            return this.messageObservers.add(messageObserver);
        }
        return false;
    }

    /**
     * Unregisters or rather removes a MessageObserver from the set of MessageObservers for this object
     * @param messageObserver a MessageObserver to be unregistered
     * @return true if MessageObserver was unregistered successfully, false else
     */
    public synchronized boolean unregisterMessageObserver(MessageObserver messageObserver) {
        return this.messageObservers.remove(messageObserver);
    }

    /**
     * Notifies all MessageObservers in the set of MessageObservers for this object about a change in the jsonObject
     */
    public synchronized void notifyMessageObservers() {
        ListIterator<MessageObserver> listIterator = this.messageObservers.listIterator();
        while(listIterator.hasNext()) {
            listIterator.next().updateMessageObserver(this.jsonObject);
        }
    }

    /**
     * Sets or rather changes the referenced jsonObject for this object to the jsonObject parameter as well as calls  the notifyMOs method afterwards
     * @param jsonObject a Map the jsonObject for this object is set to
     */
    public synchronized void setJsonObject(Bundle jsonObject) {
        this.jsonObject = jsonObject;
        notifyMessageObservers();
    }

    /**
     * Constructs a new MessageSubject with a Vector of MessageObservers for this object
     */
    public MessageSubject() {
        this.messageObservers = new Vector<MessageObserver>();
        Log.i(TAG, "SUBJECTCREATED");
    }
}

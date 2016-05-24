package observer;

import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import observer.MessageObserver;

/**
 * Created by david on 17.05.2016.
 */
public class MessageSubject {
    private Vector<MessageObserver> mos;
    private Map<String, Object> jsonObject;

    /**
     * Registers or rather adds a MessageObserver to the set of MessageObservers for this object
     * @param mo a MessageObserver to be registered
     * @return true if MessageObserver was registered successfully, false else
     * @throws NullPointerException if mo equals null
     */
    public synchronized boolean registerMO(MessageObserver mo) {
        if(mo == null) {
            throw new NullPointerException();
        }
        if(!mos.contains(mo)) {
            return this.mos.add(mo);
        }
        return false;
    }

    /**
     * Unregisters or rather removes a MessageObserver from the set of MessageObservers for this object
     * @param mo a MessageObserver to be unregistered
     * @return true if MessageObserver was unregistered successfully, false else
     */
    public synchronized boolean unregisterMO(MessageObserver mo) {
        return this.mos.remove(mo);
    }

    /**
     * Notifies all MessageObservers in the set of MessageObservers for this object about a change in the jsonObject
     */
    public synchronized void notifyMOs() {
        ListIterator<MessageObserver> moli = mos.listIterator();
        while(moli.hasNext()) {
            moli.next().updateMO(this.jsonObject);
        }
    }

    /**
     * Sets or rather changes the referenced jsonObject for this object to the jsonObject parameter as well as calls  the notifyMOs method afterwards
     * @param jsonObject a Map the jsonObject for this object is set to
     */
    public synchronized void setJsonObject(Map<String, Object> jsonObject) {
        this.jsonObject = jsonObject;
        notifyMOs();
    }

    /**
     * Constructs a new MessageSubject with a Vector of MessageObservers for this object
     */
    public MessageSubject() {
        this.mos = new Vector<MessageObserver>();
        System.out.println("SUBJECTCREATED");
    }
}

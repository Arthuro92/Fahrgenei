package server.observer;

import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by david on 17.05.2016.
 */
public class MessageSubject {
    //new
    private static final Logger logger = Logger.getLogger("MessageSubject");
    private Vector<MessageObserver> messageObservers;
    private Map<String, Object> jsonObject;

    /**
     * Registers or rather adds a MessageObserver to the set of MessageObservers for this object
     *
     * @param messageObserver a MessageObserver to be registered
     * @return true if MessageObserver was registered successfully, false else
     * @throws NullPointerException if mo equals null
     */
    public synchronized boolean registerMessageObserver(MessageObserver messageObserver) {
        if (messageObserver == null) {
            throw new NullPointerException();
        }
        if (!messageObservers.contains(messageObserver)) {
            return this.messageObservers.add(messageObserver);
        }
        return false;
    }

    /**
     * Unregisters or rather removes a MessageObserver from the set of MessageObservers for this object
     *
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
        while (listIterator.hasNext()) {
            listIterator.next().updateMessageObserver(this.jsonObject);
        }
    }

    /**
     * Sets or rather changes the referenced jsonObject for this object to the jsonObject parameter as well as calls  the notifyMOs method afterwards
     *
     * @param jsonObject a Map the jsonObject for this object is set to
     */
    public synchronized void setJsonObject(Map<String, Object> jsonObject) {
        this.jsonObject = jsonObject;
        notifyMessageObservers();
    }

    /**
     * Constructs a new MessageSubject with a Vector of MessageObservers for this object
     */
    public MessageSubject() {
        this.messageObservers = new Vector<MessageObserver>();
        logger.log(Level.INFO, "SUBJECT CREATED");
    }
}

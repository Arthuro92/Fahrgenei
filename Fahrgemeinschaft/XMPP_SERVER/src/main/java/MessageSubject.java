import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

/**
 * Created by david on 17.05.2016.
 */
public class MessageSubject {
    private Vector<MessageObserver> mos;
    private Map<String, Object> jsonObject;
    public synchronized boolean registerMO(MessageObserver mo) {
        if(mo == null) {
            throw new NullPointerException();
        }
        if(!mos.contains(mo)) {
            return this.mos.add(mo);
        }
        return false;
    }
    public synchronized boolean unregisterMO(MessageObserver mo) {
        return this.mos.remove(mo);
    }
    public synchronized void notifyMOs() {
        ListIterator<MessageObserver> moli = mos.listIterator();
        while(moli.hasNext()) {
            moli.next().updateMO(this.jsonObject);
        }
    }
    public synchronized void setJsonObject(Map<String, Object> jsonObject) {
        this.jsonObject = jsonObject;
        notifyMOs();
    }
    public MessageSubject() {
        this.mos = new Vector<MessageObserver>();
        System.out.println("SUBJECTCREATED!");
    }
}

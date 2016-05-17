import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.Map;

/**
 * Created by david on 17.05.2016.
 */
public class CategoryObserverTest implements MessageObserver{
    private Map<String, Object> jsonObject;
    public void updateMO(Map<String, Object> jsonObject) {
        @SuppressWarnings("unchecked")
        Map<String, String> payload = (Map<String, String>) jsonObject.get("data");
        String myMessage = payload.get("task");
        if(myMessage.equals("create_group")) {
            this.jsonObject = jsonObject;
            System.out.println(myMessage);
        } else {
            System.out.println("Wrong task!");
        }
    }

    public CategoryObserverTest(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("OBSERVERCREATED!");
    }
}

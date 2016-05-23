package observer;

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
//            this.jsonObject = jsonObject;
//            Gson gson = new Gson();
//            Groups grp = new Groups("Uni");
//            JSONObject jGcmData = new JSONObject();
//            JSONObject jData = new JSONObject();
//            jData.put("message", "test");
//            jData.put("dataload", gson.toJson(grp));
//            jGcmData.put("to", "/topics/global");
//            jGcmData.put("data", jData);
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

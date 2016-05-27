package observer;

import com.google.gson.Gson;
import java.util.Map;
import dataobjects.Chat;

/**
 * Created by david on 23.05.2016.
 */
public class ChatObserver implements MessageObserver {
    private Map<String, String> payload;
    private Chat c;

    /**
     * Parses certain parts of the jsonObject to a Chat object
     */
    public void setChat() {
        System.out.println("CHAT SET TO: " + this.payload.toString());
        System.out.println("MESSAGE SET TO: " + this.payload.get("content"));
        Gson gson = new Gson();
        String jsonInString = this.payload.get("content");
        this.c = gson.fromJson(jsonInString, Chat.class);
        System.out.println("SERVER RECIEVED CHAT MESSAGE: " + this.c.getChatMessageText());

        //todo how to handle chat messages
    }

    /**
     * Updates the Map payload for this object to the jsonObject. Also calls the setChat method so long as the task_category key of payload equals chat
     * @param jsonObject a Map the payload for this object is updated to
     */
    public void updateMO(Map<String, Object> jsonObject) {
        if(jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if(this.payload.get("task_category").equals("chat")) {
                setChat();
            }
        }
    }

    /**
     * Constructs a new ChatObserver and registers it to a MessageSubject
     * @param ms a MessageSubject to register to
     */
    public ChatObserver(MessageSubject ms) {
        ms.registerMO(this);
        System.out.println("CHATOBSERVER REGISTERED");
    }
}

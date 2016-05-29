package observer;

import com.google.gson.Gson;

import org.jivesoftware.smack.SmackException;

import java.util.Map;

import SmackCcsClient.SmackCcsClient;
import dataobjects.Chat;

/**
 * Created by david on 23.05.2016.
 */
public class ChatObserver implements MessageObserver {
    private Map<String, String> payload;

    /**
     * Parses certain parts of the jsonObject to a Chat object
     * @param jsonInString a Json String to be parsed
     * @return a resulting Chat object
     */
    private Chat setChatMessage(String jsonInString){
        Gson gson = new Gson();
        return gson.fromJson(jsonInString, Chat.class);
    }

    /**
     * Broadcasts chatMessage to all subscribers of a topic
     * @param chatMessage a Chat object to be broadcast
     */
    public void broadcastChatMessage(Chat chatMessage) {
        System.out.println("SERVER RECIEVED CHAT MESSAGE: " + chatMessage.getChatMessageText());
        SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
        try {
            smackCcsClient.sendDownstreamMessage("chat","chat","/topics/global", chatMessage);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the Map payload for this object to the jsonObject. Also calls the setChat method so long as the task_category key of payload equals chat
     * @param jsonObject a Map the payload for this object is updated to
     */
    public void updateMO(Map<String, Object> jsonObject) {
        if(jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if(this.payload.get("task_category").equals("chat")) {
                broadcastChatMessage(setChatMessage(this.payload.get("content")));
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

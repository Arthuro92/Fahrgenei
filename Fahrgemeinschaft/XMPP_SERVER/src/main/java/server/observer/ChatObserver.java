package server.observer;

import com.example.dataobjects.Chat;
import com.example.dataobjects.JsonCollection;

import org.jivesoftware.smack.SmackException;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.smackccsclient.SmackCcsClient;

/**
 * Created by david on 23.05.2016.
 * Handle all Messages with task_category = chat
 */
public class ChatObserver implements MessageObserver {
    //version 1
    private Map<String, String> payload;
    private static final Logger logger = Logger.getLogger("ChatObserver");

    /**
     * Parses certain parts of the jsonObject to a Chat object
     * @param jsonInString a Json String to be parsed
     * @return a resulting Chat object
     */
    private Chat setChatMessage(String jsonInString) {
        return JsonCollection.jsonToChat(jsonInString);
    }

    /**
     * Broadcasts chatMessage to all subscribers of a topic
     * @param chatMessage a Chat object to be broadcast
     */
    public void broadcastChatMessage(Chat chatMessage) {
        logger.log(Level.INFO, "SERVER RECEIVED CHAT MESSAGE:" + chatMessage.getChatMessageText());
        SmackCcsClient smackCcsClient = SmackCcsClient.getInstance();
        try {
            smackCcsClient.sendDownstreamMessage("chat", "chat", "/topics/" + chatMessage.getGid(), chatMessage);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the Map payload for this object to the jsonObject. Also calls the setChat method so long as the task_category key of payload equals chat
     * @param jsonObject a Map the payload for this object is updated to
     */
    public void updateMessageObserver(Map<String, Object> jsonObject) {
        if (jsonObject.containsKey("data")) {
            this.payload = (Map<String, String>) jsonObject.get("data");
            if (this.payload.get("task_category").equals("chat")) {
                broadcastChatMessage(setChatMessage(this.payload.get("content")));
            }
        }
    }

    /**
     * Constructs a new ChatObserver and registers it to a MessageSubject
     * @param messageSubject a MessageSubject to register to
     */
    public ChatObserver(MessageSubject messageSubject) {
        messageSubject.registerMessageObserver(this);
        logger.log(Level.INFO, "CHATOBSERVER REGISTERED");
    }
}

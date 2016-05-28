package SmackCcsClient;

import org.json.simple.JSONValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lennart on 11.05.2016.
 */
public class JsonMessage {
    Map<String, Object> message = new HashMap<String, Object>();
    /**
     * Creates a JSON encoded GCM message.
     *
     * @param to             RegistrationId of the target device (Required).
     * @param messageId      Unique messageId for which CCS will send an
     *                       "ack/nack" (Required).
     * @param payload        Message content intended for the application. (Optional).
     * @param collapseKey    GCM collapse_key parameter (Optional).
     * @param timeToLive     GCM time_to_live parameter (Optional).
     * @param delayWhileIdle GCM delay_while_idle parameter (Optional).
     * @return JSON encoded GCM message.
     */
    public JsonMessage(String to, String messageId, Map<String, String> payload, String collapseKey, Long timeToLive, Boolean delayWhileIdle) {

        message.put("to", to);
        if (collapseKey != null) {
            message.put("collapse_key", collapseKey);
        }
        if (timeToLive != null) {
            message.put("time_to_live", timeToLive);
        }
        if (delayWhileIdle != null && delayWhileIdle) {
            message.put("delay_while_idle", true);
        }
        message.put("message_id", messageId);
        message.put("data", payload);
    }

    public String getMessage() {
        return JSONValue.toJSONString(message);
    }
}

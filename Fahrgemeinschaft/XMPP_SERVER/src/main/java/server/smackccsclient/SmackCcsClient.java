package server.smackccsclient;

import com.example.dataobjects.JsonCollection;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.util.StringUtils;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocketFactory;

import server.observer.AppointmentObserver;
import server.observer.ChatObserver;
import server.observer.GroupObserver;
import server.observer.MessageSubject;
import server.observer.TaskObserver;
import server.observer.UserObserver;
import observer.SecurityObserver;

/**
 * Sample Smack implementation of a client for GCM Cloud Connection Server. This
 * code can be run as a standalone CCS client.
 * <p>
 * <p>For illustration purposes only.
 */
public class SmackCcsClient<T> {
    private MessageSubject ms = new MessageSubject();
    private UserObserver uo = new UserObserver(ms);
    private GroupObserver go = new GroupObserver(ms);
    private ChatObserver co = new ChatObserver(ms);
    private AppointmentObserver ao = new AppointmentObserver(ms);
    private SecurityObserver securityObserver = new SecurityObserver(ms);
    private TaskObserver taskObserver = new TaskObserver(ms);


    private static final Logger logger = Logger.getLogger("SmackCcsClient");
    private static final String GCM_SERVER = "gcm.googleapis.com";
//    private static final String GCM_SERVER = "fcm-xmpp.googleapis.com";
    private static final int GCM_PORT = 5235;

    private static final String GCM_ELEMENT_NAME = "gcm";
    private static final String GCM_NAMESPACE = "google:mobile:data";
    private static SmackCcsClient instance = null;

    static {

        ProviderManager.addExtensionProvider(GCM_ELEMENT_NAME, GCM_NAMESPACE,
                new PacketExtensionProvider() {

                    public PacketExtension parseExtension(XmlPullParser parser) throws
                            Exception {
                        String json = parser.nextText();
                        return new GcmPacketExtension(json);
                    }
                });
    }

    private SmackCcsClient() {
//        Exists only to defeat instantiation
    }

    /**
     * getinstance for Singleton
     * @return instance of SmackCssClient
     */
    public static SmackCcsClient getInstance() {
        if (instance == null) {
            instance = new SmackCcsClient();
        }
        return instance;
    }

    private XMPPConnection connection;

    /**
     * Indicates whether the connection is in draining state, which means that it
     * will not accept any new downstream messages.
     */
    protected volatile boolean connectionDraining = false;

    /**
     * @param task_category valid categorys: chat, group, appointment, user
     * @param task valid tasks
     * @param to cam ne Topic (/topics/...) or a specific Token
     * @param javaobject every valid javaobject / can be null
     * @return true when send success, and false when send failed
     * @throws NotConnectedException
     */
    public boolean sendDownstreamMessage(String task_category, String task, String to, Object javaobject) throws NotConnectedException {
        if (!connectionDraining) {
            String messageId = nextMessageId();
            Map<String, String> payload = new HashMap<String, String>();
            payload.put("task_category", task_category);
            payload.put("task", task);

            if (javaobject != null) {
                String javaobjectstring = JsonCollection.objectToJson(javaobject);
                payload.put("content", javaobjectstring);
            }



            String collapseKey = null;
//            String collapseKey = "sample";
            String priority = "high";
            Long timeToLive = 10000L;
            String message = new JsonMessage(to, messageId, payload, collapseKey, priority, timeToLive, true).getMessage();
            send(message);
            return true;
        }
        logger.info("Dropping downstream message since the connection is draining");
        return false;
    }


    /**
     * Returns a random message id to uniquely identify a message.
     * <p>
     * <p>Note: This is generated by a pseudo random number generator for
     * illustration purpose, and is not guaranteed to be unique.
     */
    public String nextMessageId() {
        return "m-" + UUID.randomUUID().toString();
    }
    /**
     * Sends a packet with contents provided.
     */
    protected void send(String jsonRequest) throws NotConnectedException {
        Packet request = new GcmPacketExtension(jsonRequest).toPacket();
        connection.sendPacket(request);
    }

    /**
     * Handles an upstream data message from a device application.
     * <p>
     * <p>This sample echo server sends an echo message back to the device.
     * Subclasses should override this method to properly process upstream messages.
     */
    protected void handleUpstreamMessage(Map<String, Object> jsonObject) {
        logger.log(Level.INFO, "Handle incoming Message " + (String) jsonObject.get("from"));
        ms.setJsonObject(jsonObject);
    }

    /**
     * Handles an ACK.
     * <p>
     * <p>Logs a INFO message, but subclasses could override it to
     * properly handle ACKs.
     */
    protected void handleAckReceipt(Map<String, Object> jsonObject) {
        String messageId = (String) jsonObject.get("message_id");
        String from = (String) jsonObject.get("from");
        logger.log(Level.INFO, "handleAckReceipt() from: " + from + ", messageId: " + messageId);
    }

    /**
     * Handles a NACK.
     * <p>
     * <p>Logs a INFO message, but subclasses could override it to
     * properly handle NACKs.
     */
    protected void handleNackReceipt(Map<String, Object> jsonObject) {
        String messageId = (String) jsonObject.get("message_id");
        String from = (String) jsonObject.get("from");
        String error =  (String) jsonObject.get("error");
        String errordescription = (String) jsonObject.get("error_description");

        logger.log(Level.INFO, "handleNackReceipt() from: " + from + ", messageId: " + messageId + " Error: "
                + error + " Error Description: " + errordescription);
    }

    protected void handleControlMessage(Map<String, Object> jsonObject) {
        logger.log(Level.INFO, "handleControlMessage(): " + jsonObject);
        String controlType = (String) jsonObject.get("control_type");
        if ("CONNECTION_DRAINING".equals(controlType)) {
            connectionDraining = true;
        } else {
            logger.log(Level.INFO, "Unrecognized control type: %s. This could happen if new features are " + "added to the CCS protocol.",
                    controlType);
        }
    }


    /**
     * Creates a JSON encoded ACK message for an upstream message received
     * from an application.
     *
     * @param to        RegistrationId of the device who sent the upstream message.
     * @param messageId messageId of the upstream message to be acknowledged to CCS.
     * @return JSON encoded ack.
     */
    protected static String createJsonAck(String to, String messageId) {
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("message_type", "ack");
        message.put("to", to);
        message.put("message_id", messageId);
        return JSONValue.toJSONString(message);
    }

    /**
     * Connects to GCM Cloud Connection Server using the supplied credentials.
     *
     * @param senderId Your GCM project number
     * @param apiKey   API Key of your project
     */
    public void connect(long senderId, String apiKey) throws XMPPException, IOException, SmackException {
        ConnectionConfiguration config = new ConnectionConfiguration(GCM_SERVER, GCM_PORT);
        config.setSecurityMode(SecurityMode.enabled);
        config.setReconnectionAllowed(true);
        config.setRosterLoadedAtLogin(false);
        config.setSendPresence(false);
        config.setSocketFactory(SSLSocketFactory.getDefault());

        connection = new XMPPTCPConnection(config);
        connection.connect();

        connection.addConnectionListener(new LoggingConnectionListener());

        // Handle incoming packets
        connection.addPacketListener(new PacketListener() {


            public void processPacket(Packet packet) {
                logger.log(Level.INFO, "Received: " + packet.toXML());
                Message incomingMessage = (Message) packet;
                GcmPacketExtension gcmPacket = (GcmPacketExtension) incomingMessage.getExtension(GCM_NAMESPACE);
                String json = gcmPacket.getJson();
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> jsonObject = (Map<String, Object>) JSONValue.parseWithException(json);

                    // present for "ack"/"nack", null otherwise
                    Object messageType = jsonObject.get("message_type");

                    if (messageType == null) {
                        // Normal upstream data message
                        handleUpstreamMessage(jsonObject);

                        // Send ACK to CCS
                        String messageId = (String) jsonObject.get("message_id");
                        String from = (String) jsonObject.get("from");
                        String ack = createJsonAck(from, messageId);
                        send(ack);
                    } else if ("ack".equals(messageType.toString())) {
                        // Process Ack
                        handleAckReceipt(jsonObject);
                    } else if ("nack".equals(messageType.toString())) {
                        // Process Nack
                        handleNackReceipt(jsonObject);
                    } else if ("control".equals(messageType.toString())) {
                        // Process control message
                        handleControlMessage(jsonObject);
                    } else {
                        logger.log(Level.WARNING,
                                "Unrecognized message type (%s)",
                                messageType.toString());
                    }
                } catch (ParseException e) {
                    logger.log(Level.SEVERE, "Error parsing JSON " + json, e);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Failed to process packet", e);
                }
            }
        }, new PacketTypeFilter(Message.class));

        // Log all outgoing packets
        connection.addPacketInterceptor(new PacketInterceptor() {

            public void interceptPacket(Packet packet) {
                logger.log(Level.INFO, "Sent: {0}", packet.toXML());
            }
        }, new PacketTypeFilter(Message.class));

        connection.login(senderId + "@gcm.googleapis.com", apiKey);


    }


    /**
     * XMPP Packet Extension for GCM Cloud Connection Server.
     */
    private static final class GcmPacketExtension extends DefaultPacketExtension {

        private final String json;

        public GcmPacketExtension(String json) {
            super(GCM_ELEMENT_NAME, GCM_NAMESPACE);
            this.json = json;
        }

        public String getJson() {
            return json;
        }

        @Override
        public String toXML() {
            return String.format("<%s xmlns=\"%s\">%s</%s>",
                    GCM_ELEMENT_NAME, GCM_NAMESPACE,
                    StringUtils.escapeForXML(json), GCM_ELEMENT_NAME);
        }

        public Packet toPacket() {
            Message message = new Message();
            message.addExtension(this);
            return message;
        }
    }

    private static final class LoggingConnectionListener implements ConnectionListener {


        public void connected(XMPPConnection xmppConnection) {
            logger.info("Connected.");
        }


        public void authenticated(XMPPConnection xmppConnection) {
            logger.info("Authenticated.");
        }


        public void reconnectionSuccessful() {
            logger.info("Reconnecting..");
        }


        public void reconnectionFailed(Exception e) {
            logger.log(Level.INFO, "Reconnection failed.. ", e);
        }


        public void reconnectingIn(int seconds) {
            logger.log(Level.INFO, "Reconnecting in %d secs", seconds);
        }


        public void connectionClosedOnError(Exception e) {
            logger.info("Connection closed on error.");
        }


        public void connectionClosed() {
            logger.info("Connection closed.");
        }
    }
}
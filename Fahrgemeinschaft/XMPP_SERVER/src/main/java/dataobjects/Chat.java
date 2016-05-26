package dataobjects;

/**
 * Created by david on 26.05.2016.
 */
public class Chat {
    private String chatMessageFrom;
    private String chatMessageTime;
    private String chatMessageText;

    public String getChatMessageFrom() {
        return chatMessageFrom;
    }

    public String getChatMessageTime() {
        return chatMessageTime;
    }

    public String getChatMessageText() {
        return chatMessageText;
    }

    public void setChatMessageFrom(String chatMessageFrom) {
        this.chatMessageFrom = chatMessageFrom;
    }

    public void setChatMessageTime(String chatMessageTime) {
        this.chatMessageTime = chatMessageTime;
    }

    public void setChatMessageText(String chatMessageText) {
        this.chatMessageText = chatMessageText;
    }

    public Chat(String chatMessageFrom, String chatMessageTime, String chatMessageText) {
        this.chatMessageFrom = chatMessageFrom;
        this.chatMessageTime = chatMessageTime;
        this.chatMessageText = chatMessageText;
    }
}

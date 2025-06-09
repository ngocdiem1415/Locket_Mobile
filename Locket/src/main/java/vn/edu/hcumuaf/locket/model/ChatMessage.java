package vn.edu.hcumuaf.locket.model;

public class ChatMessage {
    private MessageType type;
    private String senderId;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }


    public enum MessageType {
        JOIN, LEAVE, CHAT
    }
}
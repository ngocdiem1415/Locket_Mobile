package vn.edu.hcumuaf.locket.model;

public class Message {
    private String id;
    private MessageType type;
    private String senderId;
    private String receiverId;
    private String content;
    private long timestamp;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

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

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public enum MessageType {
        JOIN, LEAVE, CHAT
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
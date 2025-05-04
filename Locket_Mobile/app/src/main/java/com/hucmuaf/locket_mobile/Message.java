package com.hucmuaf.locket_mobile;

public class Message {
   private String type;
   private String message;
    private String senderId; // Thêm senderId để biết ai gửi tin nhắn

    public Message() {
    }

    public Message(String type, String message, String senderId) {
        this.type = type;
        this.message = message;
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}

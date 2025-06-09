package vn.edu.hcumuaf.locket.model.entity;

import lombok.Data;

@Data
public class Message {
    private String messageId;
    private String content;
    private String senderId;
    private String receiverId;
    private long timestamp;
}

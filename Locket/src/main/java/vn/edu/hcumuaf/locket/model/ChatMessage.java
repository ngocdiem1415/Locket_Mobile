package vn.edu.hcumuaf.locket.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
//@Entity
public class ChatMessage {
    //    @Id
//    private int id;
    private MessageType messageType;
    private String message;
    private String sender;

    public enum MessageType {
        JOIN,
        CHAT,
        LEAVE
    }
}

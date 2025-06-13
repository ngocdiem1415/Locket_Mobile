package vn.edu.hcumuaf.locket.model.entity;

public class Rooms {
    private String id;
    private String ownerId;
    private String receiverId;
    private String owner_receiver;
    private long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void getOwner_receiver(String owner_receiver) {
        this.owner_receiver = owner_receiver;
    }

    public void setOwner_receiver(String owner_receiver) {
        this.owner_receiver = owner_receiver;
    }

    public long getTimestamp() {
        return timestamp;

    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Rooms{" +
                "id=" + id +
                ", ownerId='" + ownerId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", owner_receiver='" + owner_receiver + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}

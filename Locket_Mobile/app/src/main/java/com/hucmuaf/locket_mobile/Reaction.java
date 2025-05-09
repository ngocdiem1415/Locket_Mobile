package com.hucmuaf.locket_mobile;

import java.time.LocalDateTime;

public class Activity {
   private String userId;
   private String imageId;
   private String icon;
   private LocalDateTime timestamp;

    public Activity() {
    }

    public Activity(String userId, String imageId, String icon, LocalDateTime timestamp) {
        this.userId = userId;
        this.imageId = imageId;
        this.icon = icon;
        this.timestamp = timestamp;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

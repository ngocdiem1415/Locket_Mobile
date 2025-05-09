package com.hucmuaf.locket_mobile;

import java.time.LocalDateTime;

public class Friend {
    public String userId;     // người dùng hiện tại
    public String friendId;   // bạn của người dùng
    public long since;        // thời điểm kết bạn

    public Friend() {}

    public Friend(String userId, String friendId, long since) {
        this.userId = userId;
        this.friendId = friendId;
        this.since = since;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public long getSince() {
        return since;
    }

    public void setSince(long since) {
        this.since = since;
    }
}
package com.hucmuaf.locket_mobile;

public class Friend {
   private String id;
   private String nameOfFriend;

    public Friend() {
    }

    public Friend(String id, String nameOfFriend) {
        this.id = id;
        this.nameOfFriend = nameOfFriend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameOfFriend() {
        return nameOfFriend;
    }

    public void setNameOfFriend(String nameOfFriend) {
        this.nameOfFriend = nameOfFriend;
    }
}

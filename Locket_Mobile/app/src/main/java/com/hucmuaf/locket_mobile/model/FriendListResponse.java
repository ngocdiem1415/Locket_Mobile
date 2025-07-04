package com.hucmuaf.locket_mobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//total friend, max friend, friends, suggestions???
public class FriendListResponse {
    @SerializedName("friends")
    private List<User> friends;

    @SerializedName("totalFriends")
    private int totalFriends;


    @SerializedName("suggestions")
    private List<User> suggestions;

    public FriendListResponse() {
    }

    public FriendListResponse(List<User> friends, int totalFriends, List<User> suggestions) {
        this.friends = friends;
        this.totalFriends = totalFriends;
        this.suggestions = suggestions;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public int getTotalFriends() {
        return totalFriends;
    }

    public void setTotalFriends(int totalFriends) {
        this.totalFriends = totalFriends;
    }


    public List<User> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<User> suggestions) {
        this.suggestions = suggestions;
    }
} 
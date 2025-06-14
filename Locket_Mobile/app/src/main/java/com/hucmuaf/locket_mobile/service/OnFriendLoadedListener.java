package com.hucmuaf.locket_mobile.service;

import com.hucmuaf.locket_mobile.modedb.User;

import java.util.List;

public interface OnFriendLoadedListener {
    void onSuccess(List<User> users);
    void onFailure(String error);
}

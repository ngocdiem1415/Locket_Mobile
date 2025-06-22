package com.hucmuaf.locket_mobile.inteface;

import java.util.List;

public interface onFriendLoaded {
    void onSuccess(List<String> friendIds);
    void onFailure(Exception e);
}

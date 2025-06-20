package com.hucmuaf.locket_mobile.inteface;

import com.hucmuaf.locket_mobile.modedb.Message;

import java.util.List;

public interface onMessageLoaded {
    void onSuccess(List<Message> mess);

    void onFailure(Exception e);

}

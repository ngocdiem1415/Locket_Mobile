package com.hucmuaf.locket_mobile.repo;

import com.google.firebase.database.DatabaseReference;
import com.hucmuaf.locket_mobile.inteface.onMessageLoaded;
import com.hucmuaf.locket_mobile.modedb.Message;
import com.hucmuaf.locket_mobile.service.FirebaseService;

import java.util.List;

public class MessageRepository implements onMessageLoaded {
    private DatabaseReference db;
public MessageRepository(){
    this.db = FirebaseService.getInstance().getDatabase().getReference();
}
    @Override
    public void onSuccess(List<Message> mess) {

    }

    @Override
    public void onFailure(Exception e) {

    }
}

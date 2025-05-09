package com.hucmuaf.locket_mobile.model;

import android.content.ClipData;

public class ItemFriend {
    private String icon;
    private String name;
    public ItemFriend(String icon, String name){
        this.icon = icon;
        this.name = name;

    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}

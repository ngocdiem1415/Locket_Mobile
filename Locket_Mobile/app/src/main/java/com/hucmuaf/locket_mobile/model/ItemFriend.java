package com.hucmuaf.locket_mobile.model;

import android.content.ClipData;

// where is node itemfriend for this class???
//if you using create feature reactions for image then why use node reactions?
public class ItemFriend {
    private String icon;
    private String name;

    public ItemFriend(String icon, String name) {
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

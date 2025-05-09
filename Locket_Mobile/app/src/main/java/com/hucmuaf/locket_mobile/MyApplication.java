package com.hucmuaf.locket_mobile;

import android.app.Application;

import androidx.emoji2.bundled.BundledEmojiCompatConfig;
import androidx.emoji2.text.EmojiCompat;

//import androidx.emoji.bundled.BundledEmojiCompatConfig;
//import androidx.emoji.text.EmojiCompat;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);
    }
}

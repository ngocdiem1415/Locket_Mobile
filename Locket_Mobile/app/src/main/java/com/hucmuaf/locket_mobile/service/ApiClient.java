package com.hucmuaf.locket_mobile.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hucmuaf.locket_mobile.inteface.FriendListApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
//         private static final String BASE_URL = "http://10.51.0.1:8080/"; // For Android emulator

    private static final String BASE_URL = "http://172.16.0.227:8080/"; // For real device, your computer's IP

    // private static final String BASE_URL = "http://localhost:8080/"; // For testing


    private static Retrofit retrofit = null;
    private static FriendListApiService friendListApiService = null;
    private static MessageListAPIService messageListAPIService;
    ;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Cấu hình Gson với lenient mode để xử lý JSON malformed
            Gson gson = new GsonBuilder()
                    .setLenient() // Cho phép xử lý JSON không đúng chuẩn
                    .setDateFormat("yyyy-MM-dd HH:mm:ss") // Định dạng ngày tháng
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static FriendListApiService getFriendListApiService() {
        if (friendListApiService == null) {
            friendListApiService = getClient().create(FriendListApiService.class);
        }
        return friendListApiService;
    }

    public static MessageListAPIService getMessageListApiService() {
        if (messageListAPIService == null) {
            messageListAPIService = getClient().create(MessageListAPIService.class);
        }
        return messageListAPIService;

    }
}
package com.hucmuaf.locket_mobile.service;

import androidx.webkit.internal.ApiFeature;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hucmuaf.locket_mobile.activity.PageComponentActivity;
import com.hucmuaf.locket_mobile.inteface.FriendListApiService;
import com.hucmuaf.locket_mobile.modedb.UploadImageResponse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //     private static final String BASE_URL = "http://10.50.0.1:8080/"; // For Android emulator

    private static final String BASE_URL = "http://172.16.1.25:8080/"; // For real device, your computer's IP

    private static Retrofit retrofit = null;
    private static FriendListApiService friendListApiService = null;
    private static MessageListAPIService messageListAPIService;
    private static FriendRequestService friendRequestService;
    private static UploadImageService uploadImageService;
    private static ImageService imageService;
    private static ReactionService reactionService;
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

    public static ImageService getImageService(){
        if(imageService == null) {
            imageService = getClient().create(ImageService.class);
        }
        return imageService;
    }
    public static UploadImageService getUploadImageService(){
        if(uploadImageService == null){
            uploadImageService =  getClient().create(UploadImageService.class);
        }
        return uploadImageService;
    }

    public static FriendListApiService getFriendListApiService() {
        if (friendListApiService == null) {
            friendListApiService = getClient().create(FriendListApiService.class);
        }
        return friendListApiService;
    }
    public static FriendRequestService getFriendRequestService() {
        if (friendRequestService == null) {
            friendRequestService = getClient().create(FriendRequestService.class);
        }
        return friendRequestService;
    }

    public static MessageListAPIService getMessageListApiService() {
        if (messageListAPIService == null) {
            messageListAPIService = getClient().create(MessageListAPIService.class);
        }
        return messageListAPIService;

    }
    public static ReactionService getReactionService(){
        if (reactionService == null) {
            reactionService = getClient().create(ReactionService.class);
        }
        return reactionService;
    }
}
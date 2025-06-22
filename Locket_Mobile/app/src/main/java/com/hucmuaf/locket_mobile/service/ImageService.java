package com.hucmuaf.locket_mobile.service;

import com.hucmuaf.locket_mobile.modedb.Image;
import com.hucmuaf.locket_mobile.modedb.SaveResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ImageService {
    @GET("api/images/{imageId}")
    Call<Image> getImageById(@Path("imageId") String imageId);

    @GET("api/images/all/{userId}")
    Call<List<Image>> getAllImages(@Path("userId") String userId);

    @GET("api/images/user/{userId}")
    Call<List<Image>> getImageOfUser(@Path("userId") String userId);

    @POST("api/images/save")
    Call<SaveResponse> saveImage(@Body Image image);

    @GET("api/images/pair")
    Call<List<Image>> getImageBySenderIdAndReceiverId(@Query("senderId") String senderId, @Query("receiverId") String receiverId);

}

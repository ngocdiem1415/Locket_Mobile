package com.hucmuaf.locket_mobile.service;

import com.hucmuaf.locket_mobile.modedb.Image;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageService {
    @GET("api/images/{imageId}")
    Call<Image> getImageById(@Path("imageId") String imageId);

    @GET("api/images/all/{userId}")
    Call<List<Image>> getAllImages(@Path("userId") String userId);

    @GET("api/images/user/{userId}")
    Call<List<Image>> getImageOfUser(@Path("userId") String userId);

}

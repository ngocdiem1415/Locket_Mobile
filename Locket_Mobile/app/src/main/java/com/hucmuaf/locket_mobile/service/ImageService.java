package com.hucmuaf.locket_mobile.service;

import com.hucmuaf.locket_mobile.modedb.Image;
import com.hucmuaf.locket_mobile.repo.UploadResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageService {
    @GET("images/{imageId}")
    Call<Image> getImageById(@Path("imageId") String imageId);

    @GET("images/all/{userId}")
    Call<List<Image>> getAllImages(@Path("userId") String userId);

    @GET("images/user/{userId}")
    Call<List<Image>> getImageOfUser(@Path("userId") String userId);

    @Multipart
    @POST("api/upload")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);

}

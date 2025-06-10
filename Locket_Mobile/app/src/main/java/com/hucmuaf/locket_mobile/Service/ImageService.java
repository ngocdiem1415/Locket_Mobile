package com.hucmuaf.locket_mobile.Service;

import com.hucmuaf.locket_mobile.ModelDB.Image;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageService {
    @GET("images/{imageId}")
    Call<Image> getImageById(@Path("imageID") String imageId);
}

package com.hucmuaf.locket_mobile.Service;

import java.util.Set;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FriendRequestService {
    @GET("friends/listFriendId/{userId}")
    Call<Set<String>> getFriendIdsByUserId(@Path("userId") String userId);
}

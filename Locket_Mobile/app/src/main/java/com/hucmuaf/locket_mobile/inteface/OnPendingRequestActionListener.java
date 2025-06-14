package com.hucmuaf.locket_mobile.inteface;

import com.hucmuaf.locket_mobile.model.FriendRequest;

public interface OnPendingRequestActionListener {
    void onAcceptRequest(FriendRequest request);  // Khi user bấm nút chấp nhận
    void onRejectRequest(FriendRequest request);  // Khi user bấm nút từ chối
}
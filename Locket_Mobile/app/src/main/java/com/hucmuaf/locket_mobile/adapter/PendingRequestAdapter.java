package com.hucmuaf.locket_mobile.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.holder.PendingRequestViewHolder;
import com.hucmuaf.locket_mobile.inteface.OnPendingRequestActionListener;
import com.hucmuaf.locket_mobile.model.FriendRequest;

import java.util.List;

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestViewHolder> {

    private final List<FriendRequest> pendingRequestsList;
    public OnPendingRequestActionListener listener;

    public PendingRequestAdapter(List<FriendRequest> pendingRequestsList, OnPendingRequestActionListener listener) {
        this.pendingRequestsList = pendingRequestsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PendingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pending_request, parent, false);
        return new PendingRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingRequestViewHolder holder, int position) {
        // Gán dữ liệu cho view holder tại vị trí position
        if (pendingRequestsList != null && position < pendingRequestsList.size()) {
            FriendRequest request = pendingRequestsList.get(position);
            if (request != null) {
                holder.bind(request);
                holder.btnAccept.setOnClickListener(v -> {
                    if (listener != null) listener.onAcceptRequest(request);
                });

                holder.btnReject.setOnClickListener(v -> {
                    if (listener != null) listener.onRejectRequest(request);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng lời mời kết bạn trong danh sách
        return pendingRequestsList != null ? pendingRequestsList.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<FriendRequest> newRequests) {
        this.pendingRequestsList.clear();
        this.pendingRequestsList.addAll(newRequests);
        notifyDataSetChanged();
    }
} 
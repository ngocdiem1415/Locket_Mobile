package com.hucmuaf.locket_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.inteface.OnSentRequestActionListener;
import com.hucmuaf.locket_mobile.model.FriendRequest;

import java.util.List;

public class SentRequestAdapter extends RecyclerView.Adapter<SentRequestAdapter.SentRequestViewHolder> {

    private List<FriendRequest> sentRequests;
    private Context context;
    private OnSentRequestActionListener listener;

    public SentRequestAdapter(Context context, List<FriendRequest> sentRequests, OnSentRequestActionListener listener) {
        this.context = context;
        this.sentRequests = sentRequests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SentRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sent_request, parent, false);
        return new SentRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SentRequestViewHolder holder, int position) {
        FriendRequest request = sentRequests.get(position);
        holder.bind(request);
    }

    @Override
    public int getItemCount() {
        return sentRequests.size();
    }

    public void updateSentRequests(List<FriendRequest> newSentRequests) {
        this.sentRequests = newSentRequests;
        notifyDataSetChanged();
    }

    class SentRequestViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView nameTextView;
        TextView statusTextView;
        ImageButton cancelButton;

        public SentRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.sentRequestAvatar);
            nameTextView = itemView.findViewById(R.id.sentRequestName);
            cancelButton = itemView.findViewById(R.id.btnCancel);
        }

        void bind(FriendRequest request) {
            // Hiển thị tên người nhận lời mời
            if (request.getSenderName() != null) {
                nameTextView.setText(request.getSenderName());
            } else {
                nameTextView.setText("Unknown User");
            }

            // Load avatar của người nhận
            if (request.getSender() != null && request.getSender().getUrlAvatar() != null) {
                Glide.with(context)
                        .load(request.getSender().getUrlAvatar())
                        .placeholder(R.drawable.avt)
                        .error(R.drawable.avt)
                        .circleCrop()
                        .into(avatarImageView);
            } else {
                avatarImageView.setImageResource(R.drawable.avt);
            }

            // Xử lý sự kiện hủy lời mời
            cancelButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCancelRequest(request);
                }
            });
        }
    }
} 
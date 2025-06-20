package com.hucmuaf.locket_mobile.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.inteface.OnPendingRequestActionListener;
import com.hucmuaf.locket_mobile.model.FriendRequest;

public class PendingRequestViewHolder extends RecyclerView.ViewHolder {
    public ImageView avatar;
    private final TextView name;
    public ImageButton btnAccept;
    public ImageButton btnReject;

    public PendingRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        // Ánh xạ các view từ layout item_pending_request.xml
        avatar = itemView.findViewById(R.id.pendingRequestAvatar);
        name = itemView.findViewById(R.id.pendingRequestName);
        btnAccept = itemView.findViewById(R.id.btnAccept);
        btnReject = itemView.findViewById(R.id.btnReject);
    }

    public void bind(FriendRequest request) {
        if (request == null) return;

        String senderName = getName(request);

        if (name != null) {
            name.setText(senderName);
        }

        if (avatar != null) {
            if (request.getSender() != null && request.getSender().getUrlAvatar() != null && !request.getSender().getUrlAvatar().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(request.getSender().getUrlAvatar())
                        .transform(new CircleCrop())
                        .placeholder(R.drawable.avt)
                        .error(R.drawable.avt)
                        .into(avatar);
            } else {
                avatar.setImageResource(R.drawable.avt);
            }
        }
    }

    private static String getName(FriendRequest request) {
        String senderName;
        if (request.getSenderName() != null && !request.getSenderName().isEmpty()) {
            senderName = request.getSenderName();
        } else if (request.getSender() != null && request.getSender().getFullName() != null && !request.getSender().getFullName().isEmpty()) {
            senderName = request.getSender().getFullName();
        } else if (request.getSender() != null && request.getSender().getUserName() != null && !request.getSender().getUserName().isEmpty()) {
            senderName = request.getSender().getUserName();
        } else if (request.getSenderId() != null && !request.getSenderId().isEmpty()) {
            senderName = request.getSenderId();
        } else {
            senderName = "Unknown User";
        }
        return senderName;
    }
}
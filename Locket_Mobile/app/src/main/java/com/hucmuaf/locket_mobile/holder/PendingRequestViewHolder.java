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
    private ImageView avatar;        // Avatar của người gửi lời mời
    private TextView name;           // Tên của người gửi lời mời
    private ImageButton acceptButton;     // Nút chấp nhận (✓) - màu xanh lá
    private ImageButton rejectButton;     // Nút từ chối (✕) - màu đỏ
    private OnPendingRequestActionListener listener;  // Listener để xử lý sự kiện chấp nhận/từ chối

    public PendingRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        // Ánh xạ các view từ layout item_pending_request.xml
        avatar = itemView.findViewById(R.id.pendingRequestAvatar);
        name = itemView.findViewById(R.id.pendingRequestName);
        acceptButton = itemView.findViewById(R.id.btnAccept);
        rejectButton = itemView.findViewById(R.id.btnReject);
    }

    public void bind(FriendRequest request) {
        if (request == null) return;

        // ===== HIỂN THỊ TÊN NGƯỜI GỬI =====
        // Logic ưu tiên hiển thị tên: senderName > sender.fullName > sender.userName > senderId > "Unknown User"
        String senderName;
        if (request.getSenderName() != null && !request.getSenderName().isEmpty()) {
            // Ưu tiên 1: senderName từ backend (tên thật đã được xử lý)
            senderName = request.getSenderName();
        } else if (request.getSender() != null && request.getSender().getFullName() != null && !request.getSender().getFullName().isEmpty()) {
            // Ưu tiên 2: fullName từ sender object
            senderName = request.getSender().getFullName();
        } else if (request.getSender() != null && request.getSender().getUserName() != null && !request.getSender().getUserName().isEmpty()) {
            // Ưu tiên 3: userName từ sender object
            senderName = request.getSender().getUserName();
        } else if (request.getSenderId() != null && !request.getSenderId().isEmpty()) {
            // Ưu tiên 4: senderId (fallback cuối cùng)
            senderName = request.getSenderId();
        } else {
            // Fallback cuối cùng nếu không có thông tin gì
            senderName = "Unknown User";
        }

        if (name != null) {
            name.setText(senderName);
        }

        // ===== LOAD AVATAR NGƯỜI GỬI =====
        // Sử dụng Glide để load avatar từ URL, với fallback về avatar mặc định
        if (avatar != null) {
            if (request.getSender() != null && request.getSender().getUrlAvatar() != null && !request.getSender().getUrlAvatar().isEmpty()) {
                // Load avatar từ URL với hiệu ứng tròn và placeholder
                Glide.with(itemView.getContext())
                        .load(request.getSender().getUrlAvatar())
                        .transform(new CircleCrop())  // Làm tròn avatar
                        .placeholder(R.drawable.avt)  // Avatar mặc định khi đang load
                        .error(R.drawable.avt)        // Avatar mặc định khi lỗi
                        .into(avatar);
            } else {
                // Set avatar mặc định nếu không có URL avatar
                avatar.setImageResource(R.drawable.avt);
            }
        }

        // ===== SETUP CLICK LISTENER CHO NÚT CHẤP NHẬN =====
        // Khi user bấm nút ✓ (màu xanh lá)
        if (acceptButton != null) {
            acceptButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAcceptRequest(request);
                }
            });
        }

        // ===== SETUP CLICK LISTENER CHO NÚT TỪ CHỐI =====
        // Khi user bấm nút ✕ (màu đỏ)
        if (rejectButton != null) {
            rejectButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRejectRequest(request);
                }
            });
        }
    }
}
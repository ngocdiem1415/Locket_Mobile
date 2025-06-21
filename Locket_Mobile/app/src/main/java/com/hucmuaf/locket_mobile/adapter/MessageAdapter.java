package com.hucmuaf.locket_mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.activity.ChatActivity;
import com.hucmuaf.locket_mobile.holder.MessageViewHolder;
import com.hucmuaf.locket_mobile.model.Message;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<Message> messageList;
    private Context context;
    private String currentUserId;

    public MessageAdapter(Context context, List<Message> messageList, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_list, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        String otherUserId = message.getSenderId().equals(currentUserId) ? message.getReceiverId() : message.getSenderId();
        holder.getUsername().setText(otherUserId);
        // Hiển thị tin nhắn cuối cùng
        holder.getLastMessage().setText(message.getContent());

        // Định dạng thời gian
        SimpleDateFormat sdf = new SimpleDateFormat("m'm'", Locale.getDefault());
        String timeDiff = getTimeDifference(message.getTimestamp());
        holder.getTimestamp().setText(timeDiff);

        Glide.with(context)
                .load(R.mipmap.account_icon)
                .circleCrop()
                .placeholder(R.drawable.default_avatar)
                .into(holder.getAvatar());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("otherUserId", otherUserId);
            context.startActivity(intent);
        });
    }

    private String getTimeDifference(long timestamp) {
        long diff = (System.currentTimeMillis() - timestamp) / 1000;
        if (diff < 3600) {
            return (diff / 60) + "m";
        } else if (diff < 86400) {
            return (diff / 3600) + "h";
        } else { // Trên 1 ngày
            return (diff / 86400) + "d";
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

}
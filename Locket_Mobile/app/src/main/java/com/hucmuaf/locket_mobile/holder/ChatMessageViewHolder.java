package com.hucmuaf.locket_mobile.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hucmuaf.locket_mobile.R;

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
    ImageView messageAvatar;
    TextView messageContent;
    TextView messageTimestamp;

    public ChatMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        messageAvatar = itemView.findViewById(R.id.messageAvatar);
        messageContent = itemView.findViewById(R.id.recyclerViewMessages);
        messageTimestamp = itemView.findViewById(R.id.messageTimestamp);
    }

    public ImageView getMessageAvatar() {
        return messageAvatar;
    }

    public void setMessageAvatar(ImageView messageAvatar) {
        this.messageAvatar = messageAvatar;
    }

    public TextView getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(TextView messageContent) {
        this.messageContent = messageContent;
    }

    public TextView getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(TextView messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }
}
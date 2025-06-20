package com.hucmuaf.locket_mobile.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.holder.ChatMessageViewHolder;
import com.hucmuaf.locket_mobile.model.Message;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageViewHolder> {
    private List<Message> listMess;
    //    private MessageViewHolder messageViewHolder;
//    private Message message;
    private String currentUser;

    public ChatMessageAdapter(List<Message> listMess, String currentUser) {
        this.listMess = listMess;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_message, null);
        return new ChatMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        Message message = listMess.get(position);
        holder.getMessageContent().setText(message.getContent());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        holder.getMessageTimestamp().setText((int) System.currentTimeMillis());

        if (message.getSenderId().equals(currentUser)) {
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            holder.getMessageContent().setBackgroundResource(R.drawable.message_background_sent);
            holder.getMessageAvatar().setVisibility(View.GONE);
            holder.getMessageContent().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            holder.getMessageContent().setBackgroundResource(R.drawable.message_background_sent);
            holder.getMessageAvatar().setVisibility(View.VISIBLE);
            holder.getMessageContent().setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }

    @Override
    public int getItemCount() {
        return listMess.size();
    }

    public void updateMessages(List<Message> messages) {
        this.listMess = messages;
        notifyDataSetChanged();
    }
}

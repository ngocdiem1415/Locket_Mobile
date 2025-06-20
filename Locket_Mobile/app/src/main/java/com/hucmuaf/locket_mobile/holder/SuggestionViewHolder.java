package com.hucmuaf.locket_mobile.holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.inteface.OnSuggestionActionListener;
import com.hucmuaf.locket_mobile.model.User;

public class SuggestionViewHolder extends RecyclerView.ViewHolder {
    private final ImageView avatar;
    private final TextView name;
    private final Button addButton;
    private OnSuggestionActionListener listener;

    public SuggestionViewHolder(@NonNull View itemView) {
        super(itemView);
        avatar = itemView.findViewById(R.id.suggestionAvatar);
        name = itemView.findViewById(R.id.suggestionName);
        addButton = itemView.findViewById(R.id.btnAdd);
    }

    public void bind(User suggestion) {
        name.setText(suggestion.getFullName() != null ? suggestion.getFullName() : suggestion.getUserName());

        // Load avatar image using Glide
        if (suggestion.getUrlAvatar() != null && !suggestion.getUrlAvatar().isEmpty()) {
            Glide.with(itemView.getContext())
                    .load(suggestion.getUrlAvatar())
                    .transform(new CircleCrop())
                    .placeholder(R.drawable.avt)
                    .error(R.drawable.avt)
                    .into(avatar);
        } else {
            // Set default avatar
            avatar.setImageResource(R.drawable.avt);
        }

        addButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddFriend(suggestion);
            }
        });
    }
}
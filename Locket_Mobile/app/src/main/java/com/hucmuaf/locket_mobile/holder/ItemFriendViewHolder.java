package com.hucmuaf.locket_mobile.holder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hucmuaf.locket_mobile.R;

import com.hucmuaf.locket_mobile.adapter.ItemFriendAdapter;
import com.hucmuaf.locket_mobile.modedb.User;

public class ItemFriendViewHolder extends RecyclerView.ViewHolder {
    ImageView ivIcon;
    TextView tvName;

    @SuppressLint("WrongViewCast")
    public ItemFriendViewHolder(@NonNull View itemView) {
        super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvName = itemView.findViewById(R.id.tvName);
    }

    public void bind(User user, ItemFriendAdapter.OnFriendClickListener listener){
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFriendClick(user);
            }
        });
    }

    public ImageView getIvIcon() {
        return ivIcon;
    }

    public void setIvIcon(ImageView ivIcon) {
        this.ivIcon = ivIcon;
    }

    public TextView getTvName() {
        return tvName;
    }

    public void setTvName(TextView tvName) {
        this.tvName = tvName;
    }
}

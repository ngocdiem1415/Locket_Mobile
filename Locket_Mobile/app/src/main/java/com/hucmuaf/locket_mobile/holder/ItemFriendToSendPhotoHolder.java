package com.hucmuaf.locket_mobile.holder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.inteface.OnFriendToSendListenter;
import com.hucmuaf.locket_mobile.modedb.User;

public class ItemFriendToSendPhotoHolder extends RecyclerView.ViewHolder {
    ImageView ivIcon;
    TextView tvName;
    LinearLayout itemFriend;
    boolean isSelected;

    @SuppressLint("WrongViewCast")
    public ItemFriendToSendPhotoHolder(@NonNull View itemView) {
        super(itemView);
        ivIcon = itemView.findViewById(R.id.accAvatar);
        tvName = itemView.findViewById(R.id.accName);
        itemFriend = itemView.findViewById(R.id.item_friends_send);
        isSelected = false;
    }

    public void bind(User user,  OnFriendToSendListenter listener) {
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.sendTo(user);
                if (isSelected) {
                    itemFriend.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.circle_border_gray));
                    isSelected = false;
                } else {
                    itemFriend.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.circle_border_blue));
                    isSelected = true;
                }
            }
        });
    }

    public LinearLayout getItemFriend(){
        return itemFriend;
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
    public void setSelected(boolean f){
        this.isSelected = f;
    }
}

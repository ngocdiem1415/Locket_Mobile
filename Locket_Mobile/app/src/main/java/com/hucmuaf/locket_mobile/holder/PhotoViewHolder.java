package com.hucmuaf.locket_mobile.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hucmuaf.locket_mobile.R;

public class PhotoViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView captionText;
    ImageView avatarImage;
    TextView accNameText;
    TextView timestampText;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
        captionText = itemView.findViewById(R.id.caption);
        avatarImage = itemView.findViewById(R.id.avatar);
        accNameText = itemView.findViewById(R.id.accName);
        timestampText = itemView.findViewById(R.id.timestamp);

    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getCaptionText() {
        return captionText;
    }

    public void setCaptionText(TextView captionText) {
        this.captionText = captionText;
    }

    public ImageView getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(ImageView avatarImage) {
        this.avatarImage = avatarImage;
    }

    public TextView getAccNameText() {
        return accNameText;
    }

    public void setAccNameText(TextView accNameText) {
        this.accNameText = accNameText;
    }

    public TextView getTimestampText() {
        return timestampText;
    }

    public void setTimestampText(TextView timestampText) {
        this.timestampText = timestampText;
    }
}
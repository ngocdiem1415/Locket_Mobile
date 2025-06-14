
package com.hucmuaf.locket_mobile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.hucmuaf.locket_mobile.R;

import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.holder.PhotoViewHolder;
import com.hucmuaf.locket_mobile.modedb.Image;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
    private Context context;
    private List<Image> listImage; // Nếu dùng ảnh local (drawable)

    public PhotoAdapter(Context context, List<Image> listImage) {
        this.context = context;
        this.listImage = listImage;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_react, parent, false);
        return new PhotoViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Image item = listImage.get(position);
        String url = item.getUrlImage();
        String caption = item.getCaption();
        String avatar = "";
        String accName = item.getSenderId();
        long timestamp = item.getTimestamp();

        // Load ảnh từ URL bằng Glide
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.default_img) // ảnh tạm khi đang load
                .error(R.drawable.default_img) // ảnh khi load lỗi
                .into(holder.getImageView());
        holder.getCaptionText().setText(caption);
        holder.getAvatarImage().setImageResource(R.drawable.default_img);
        holder.getAccNameText().setText(accName);
        String hour = timestamp + "h";
        holder.getTimestampText().setText(hour);
   }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

}

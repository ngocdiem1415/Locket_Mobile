package com.hucmuaf.locket_mobile.AllImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hucmuaf.locket_mobile.ModelDB.Image;
import com.hucmuaf.locket_mobile.R;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.PhotoViewHolder> {

    private List<Image> photoList;
    private Context context;
    private OnPhotoClickListener listener;

    public interface OnPhotoClickListener {
        void onPhotoClick(Image image);
    }

    public ImageAdapter(Context context, List<Image> photoList, OnPhotoClickListener listener) {
        this.context = context;
        this.photoList = photoList;
        this.listener = listener;
    }

    public void updateList(List<Image> newList) {
        photoList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Image photo = photoList.get(position);
        Glide.with(context)
                .load(photo.getUrlImage())
                .placeholder(R.drawable.image1)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> {
            if (listener != null) listener.onPhotoClick(photo);
        });
    }

    @Override
    public int getItemCount() {
        return photoList != null ? photoList.size() : 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

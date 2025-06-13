package com.hucmuaf.locket_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hucmuaf.locket_mobile.model.Image;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
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

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Image item = listImage.get(position);
//        String url = item.getUrlImage();
        String caption = "item.getCaption()";
        String avatar = "";
        String accName = "duize";
        long timestamp = item.getTimestamp();
        int resId = context.getResources().getIdentifier("trathanhtuyen.png", "mipmap", context.getPackageName());
        holder.imageView.setImageResource(resId);
        holder.captionText.setText(caption);
        holder.avatarImage.setImageResource(resId);
        holder.accNameText.setText(accName);
        holder.timestampText.setText(timestamp+ "h");
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView captionText;
        ImageView avatarImage;
        TextView accNameText;
        TextView timestampText;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            captionText = itemView.findViewById(R.id.caption);
            avatarImage = itemView.findViewById(R.id.avatar);
            accNameText = itemView.findViewById(R.id.accName);
            timestampText = itemView.findViewById(R.id.timestamp);

        }
    }
}

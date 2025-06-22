////package com.hucmuaf.locket_mobile.adapter;
////
////import android.app.Activity;
////import android.content.Context;
////import android.content.Intent;
////import android.util.Log;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.ImageView;
////import android.widget.TextView;
////
////import androidx.annotation.NonNull;
////import androidx.recyclerview.widget.RecyclerView;
////
////import com.bumptech.glide.Glide;
////import com.hucmuaf.locket_mobile.R;
////import com.hucmuaf.locket_mobile.activity.ChatActivity;
////import com.hucmuaf.locket_mobile.holder.MessageViewHolder;
////import com.hucmuaf.locket_mobile.model.Message;
////
////import java.util.ArrayList;
////import java.util.List;
////
////public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
////    private List<Message> messageList;
////    private Context context;
////    private List<String> receiverIds;
////    private String currentUserId;
////    private RecyclerView recyclerView;
////
////
////    public MessageAdapter(Context context, List<Message> messageList, List<String> receiverIds, String currentUserId, RecyclerView recyclerView) {
////        this.context = context;
////        this.messageList = messageList != null ? messageList : new ArrayList<>();
////        this.receiverIds = receiverIds != null ? receiverIds : new ArrayList<>();
////        this.currentUserId = currentUserId;
////        this.recyclerView = recyclerView;
////    }
////
////    @NonNull
////    @Override
////    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(context).inflate(R.layout.item_message_list, parent, false);
////        return new MessageViewHolder(view);
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
////        String receiverId = receiverIds.get(position);
////        Log.w("MessageAdapter", "Position" + position);
////        Log.w("MessageAdapter", "Receiver ID: " + receiverId);
////
////        holder.getUsername().setText(receiverId);
////        holder.getLastMessage().setText("");
////        holder.getTimestamp().setText("");
////
////        Glide.with(context)
////                .load(R.drawable.default_avatar)
////                .circleCrop()
////                .placeholder(R.drawable.default_avatar)
////                .error(R.drawable.default_avatar)
////                .into(holder.getAvatar());
////
////        holder.itemView.setOnClickListener(v -> {
////            Intent intent = new Intent(context, ChatActivity.class);
////            intent.putExtra("otherUserId", receiverId);
////            context.startActivity(intent);
////        });
////
////        if (position < messageList.size() && messageList.get(position) != null) {
////            Message message = messageList.get(position);
////            holder.getLastMessage().setText(message.getContent());
////            String timeDiff = getTimeDifference(message.getTimestamp());
////            holder.getTimestamp().setText(timeDiff);
////        }
////    }
////
////    private String getTimeDifference(long timestamp) {
////        long diff = (System.currentTimeMillis() - timestamp) / 1000;
////        if (diff < 3600) {
////            return (diff / 60) + "m";
////        } else if (diff < 86400) {
////            return (diff / 3600) + "h";
////        } else { // Trên 1 ngày
////            return (diff / 86400) + "d";
////        }
////    }
////
////    public void updateUserInfo(int position, String name, String avtUrl) {
////        if (position >= 0 && position < messageList.size()) {
////            Message message = messageList.get(position);
////            MessageViewHolder holder = (MessageViewHolder) recyclerViewFindViewHolder(position);
////            if (holder != null) {
////                if (name != null) {
////                    holder.getUsername().setText(name);
////                } else {
////                    holder.getUsername().setText(message.getReceiverId());
////                }
////
////                if (avtUrl != null && !avtUrl.isEmpty()) {
////                    Glide.with(context)
////                            .load(avtUrl)
////                            .circleCrop()
////                            .placeholder(R.drawable.default_avatar)
////                            .into(holder.getAvatar());
////                } else {
////                    Glide.with(context)
////                            .load(R.drawable.default_avatar)
////                            .circleCrop()
////                            .into(holder.getAvatar());
////                }
////                notifyItemChanged(position);
////            }
////        } else {
////            Log.w("MessageAdapter", "Invalid position for updateUserInfo: " + position);
////        }
////    }
////
////    private RecyclerView.ViewHolder recyclerViewFindViewHolder(int position) {
////        RecyclerView recyclerView = ((Activity) context).findViewById(R.id.recyclerViewMessages);
////        if (recyclerView != null) {
////            return recyclerView.findViewHolderForAdapterPosition(position);
////        }
////        return null;
////    }
////    public void updateMessage(int position, Message message) {
////        if (position >= 0 && position < receiverIds.size()) {
////            while (messageList.size() <= position) {
////                messageList.add(null);
////            }
////            messageList.set(position, message);
////            notifyItemChanged(position);
////        }
////    }
////    @Override
////    public int getItemCount() {
////        return messageList.size();
////    }
////
////}
//
//package com.hucmuaf.locket_mobile.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//import com.hucmuaf.locket_mobile.R;
//import com.hucmuaf.locket_mobile.activity.ChatActivity;
//import com.hucmuaf.locket_mobile.holder.MessageViewHolder;
//import com.hucmuaf.locket_mobile.model.Message;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
//    private List<Message> messageList;
//    private Context context;
//    private List<String> receiverIds;
//    private String currentUserId;
//    private RecyclerView recyclerView;
//
//    public MessageAdapter(Context context, List<Message> messageList, List<String> receiverIds, String currentUserId, RecyclerView recyclerView) {
//        this.context = context;
//        this.messageList = messageList != null ? messageList : new ArrayList<>();
//        this.receiverIds = receiverIds != null ? receiverIds : new ArrayList<>();
//        this.currentUserId = currentUserId;
//        this.recyclerView = recyclerView;
//    }
//
//    @NonNull
//    @Override
//    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_message_list, parent, false);
//        return new MessageViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
//        // Validate position bounds for receiverIds
//        if (position >= receiverIds.size()) {
//            Log.e("MessageAdapter", "Position " + position + " is out of bounds for receiverIds (size: " + receiverIds.size() + ")");
//            return;
//        }
//
//        String receiverId = receiverIds.get(position);
//        Log.w("MessageAdapter", "Position: " + position);
//        Log.w("MessageAdapter", "Receiver ID: " + receiverId);
//
//        // Set default values
//        holder.getUsername().setText(receiverId);
//        holder.getLastMessage().setText("");
//        holder.getTimestamp().setText("");
//
//        // Load default avatar
////        Glide.with(context)
////                .asBitmap()
////                .load(R.drawable.default_avatar)
////                .circleCrop()
////                .placeholder(R.drawable.default_avatar)
////                .into(holder.getAvatar());
////        // Set click listener
////        holder.itemView.setOnClickListener(v -> {
////            Intent intent = new Intent(context, ChatActivity.class);
////            intent.putExtra("otherUserId", receiverId);
////            context.startActivity(intent);
////        });
//        loadAvatarImage(holder.getAvatar(), null);
//
//        // Update with message data if available
//        if (position < messageList.size() && messageList.get(position) != null) {
//            Message message = messageList.get(position);
//            holder.getLastMessage().setText(message.getContent());
//            String timeDiff = getTimeDifference(message.getTimestamp());
//            holder.getTimestamp().setText(timeDiff);
//        }
//    }
//
//    private void loadAvatarImage(ImageView imageView, String imageUrl) {
//        RequestOptions requestOptions = new RequestOptions()
//                .circleCrop()
//                .placeholder(R.drawable.default_avatar)
//                .error(R.drawable.default_avatar)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .skipMemoryCache(false);
//
//        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
//            Log.d("MessageAdapter", "Loading image from URL: " + imageUrl);
//
//            Glide.with(context)
//                    .load(imageUrl)
//                    .apply(requestOptions)
//                    .into(imageView);
//        } else {
//            Glide.with(context)
//                    .load(R.drawable.default_avatar)
//                    .apply(requestOptions)
//                    .into(imageView);
//        }
//    }
//
//    private String getTimeDifference(long timestamp) {
//        long diff = (System.currentTimeMillis() - timestamp) / 1000;
//        if (diff < 60) {
//            return "now";
//        } else if (diff < 3600) {
//            return (diff / 60) + "m";
//        } else if (diff < 86400) {
//            return (diff / 3600) + "h";
//        } else {
//            return (diff / 86400) + "d";
//        }
//    }
//
//    public void updateUserInfo(int position, String name, String avtUrl) {
//        if (position < 0 || position >= receiverIds.size()) {
//            Log.w("MessageAdapter", "Invalid position for updateUserInfo: " + position + " (receiverIds size: " + receiverIds.size() + ")");
//            return;
//        }
//        while (messageList.size() <= position) {
//            messageList.add(null);
//        }
//
//        MessageViewHolder holder = (MessageViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
//        if (holder != null) {
//            if (name != null && !name.trim().isEmpty()) {
//                holder.getUsername().setText(name);
//            } else {
//                holder.getUsername().setText(receiverIds.get(position));
//            }
//
//            if (avtUrl != null && !avtUrl.trim().isEmpty()) {
//                Glide.with(context)
//                        .load(avtUrl)
//                        .circleCrop()
//                        .placeholder(R.drawable.default_avatar)
//                        .error(R.drawable.default_avatar)
//                        .into(holder.getAvatar());
//                Log.w("MessageAdapter", "Load avatar: " + avtUrl);
//            } else {
//                Glide.with(context)
//                        .load(R.drawable.default_avatar)
//                        .circleCrop()
//                        .into(holder.getAvatar());
//            }
//
//
//            holder.getAvatar().setOnClickListener(v -> {
//                String receiverId = receiverIds.get(position);
//                Intent intent = new Intent(context, ChatActivity.class);
//                intent.putExtra("otherUserId", receiverId);
//                context.startActivity(intent);
//            });
//            holder.getUsername().setOnClickListener(v -> {
//                String receiverId = receiverIds.get(position);
//                Intent intent = new Intent(context, ChatActivity.class);
//                intent.putExtra("otherUserId", receiverId);
//                context.startActivity(intent);
//            });
//            holder.getLastMessage().setOnClickListener(v -> {
//                String receiverId = receiverIds.get(position);
//                Intent intent = new Intent(context, ChatActivity.class);
//                intent.putExtra("otherUserId", receiverId);
//                context.startActivity(intent);
//            });
//            notifyItemChanged(position);
//        }
//    }
//
//    public void updateMessage(int position, Message message) {
//        if (position < 0 || position >= receiverIds.size()) {
//            Log.w("MessageAdapter", "Invalid position for updateMessage: " + position + " (receiverIds size: " + receiverIds.size() + ")");
//            return;
//        }
//
//        while (messageList.size() <= position) {
//            messageList.add(null);
//        }
//
//        messageList.set(position, message);
//        notifyItemChanged(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return receiverIds.size();
//    }
//
//    public void addReceiver(String receiverId) {
//        receiverIds.add(receiverId);
//        messageList.add(null); // Add placeholder message
//        notifyItemInserted(receiverIds.size() - 1);
//    }
//
//    public void removeReceiver(int position) {
//        if (position >= 0 && position < receiverIds.size()) {
//            receiverIds.remove(position);
//            if (position < messageList.size()) {
//                messageList.remove(position);
//            }
//            notifyItemRemoved(position);
//        }
//    }
//
//    public void clearAll() {
//        int size = receiverIds.size();
//        receiverIds.clear();
//        messageList.clear();
//        notifyItemRangeRemoved(0, size);
//    }
//}

package com.hucmuaf.locket_mobile.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.activity.ChatActivity;
import com.hucmuaf.locket_mobile.holder.MessageViewHolder;
import com.hucmuaf.locket_mobile.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private List<Message> messageList;
    private Context context;
    private List<String> receiverIds;
    private String currentUserId;
    private RecyclerView recyclerView;

    private List<String> userNames;
    private List<String> avatarUrls;


    public MessageAdapter(Context context, List<Message> messageList, List<String> receiverIds, String currentUserId, RecyclerView recyclerView) {
        this.context = context;
        this.messageList = messageList != null ? messageList : new ArrayList<>();
        this.receiverIds = receiverIds != null ? receiverIds : new ArrayList<>();
        this.currentUserId = currentUserId;
        this.recyclerView = recyclerView;

        this.userNames = new ArrayList<>();
        this.avatarUrls = new ArrayList<>();

        for (int i = 0; i < receiverIds.size(); i++) {
            userNames.add(null);
            avatarUrls.add(null);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_list, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        if (position >= receiverIds.size()) {
            Log.e("MessageAdapter", "Position " + position + " is out of bounds for receiverIds (size: " + receiverIds.size() + ")");
            return;
        }

        String receiverId = receiverIds.get(position);
        Log.w("MessageAdapter", "Position: " + position);
        Log.w("MessageAdapter", "Receiver ID: " + receiverId);

        String displayName = (position < userNames.size() && userNames.get(position) != null)
                ? userNames.get(position)
                : receiverId;
        holder.getUsername().setText(displayName);

        holder.getLastMessage().setText("");
        holder.getTimestamp().setText("");

        String avatarUrl = (position < avatarUrls.size()) ? avatarUrls.get(position) : null;
        loadAvatarImage(holder.getAvatar(), avatarUrl);

        View.OnClickListener clickListener = v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("otherUserId", receiverId);
            context.startActivity(intent);
        };

        holder.itemView.setOnClickListener(clickListener);
        holder.getAvatar().setOnClickListener(clickListener);
        holder.getUsername().setOnClickListener(clickListener);
        holder.getLastMessage().setOnClickListener(clickListener);

        if (position < messageList.size() && messageList.get(position) != null) {
            Message message = messageList.get(position);
            holder.getLastMessage().setText(message.getContent());
            String timeDiff = getTimeDifference(message.getTimestamp());
            holder.getTimestamp().setText(timeDiff);
        }
    }

    public void loadAvatarImage(ImageView imageView, String imageUrl) {
        try {
            RequestOptions requestOptions = new RequestOptions()
                    .circleCrop()
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false);

            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                Log.d("MessageAdapter", "Loading image from URL: " + imageUrl);

                Glide.with(context)
                        .load(imageUrl)
                        .apply(requestOptions)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(R.drawable.default_avatar)
                        .apply(requestOptions)
                        .into(imageView);
            }
        } catch (Exception e) {
            Log.e("MessageAdapter", "Error loading avatar image", e);
            try {
                Glide.with(context)
                        .load(R.drawable.default_avatar)
                        .circleCrop()
                        .into(imageView);
            } catch (Exception ex) {
                Log.e("MessageAdapter", "Error loading default avatar", ex);
            }
        }
    }

    private String getTimeDifference(long timestamp) {
        long diff = (System.currentTimeMillis() - timestamp) / 1000;
        if (diff < 60) {
            return "now";
        } else if (diff < 3600) {
            return (diff / 60) + "m";
        } else if (diff < 86400) {
            return (diff / 3600) + "h";
        } else {
            return (diff / 86400) + "d";
        }
    }

    public void updateUserInfo(int position, String name, String avtUrl) {
        if (position < 0 || position >= receiverIds.size()) {
            Log.w("MessageAdapter", "Invalid position for updateUserInfo: " + position + " (receiverIds size: " + receiverIds.size() + ")");
            return;
        }

        while (messageList.size() <= position) {
            messageList.add(null);
        }
        while (userNames.size() <= position) {
            userNames.add(null);
        }
        while (avatarUrls.size() <= position) {
            avatarUrls.add(null);
        }

        if (name != null && !name.trim().isEmpty()) {
            userNames.set(position, name);
        }
        if (avtUrl != null && !avtUrl.trim().isEmpty()) {
            avatarUrls.set(position, avtUrl);
            Log.w("MessageAdapter", "Updated avatar URL for position " + position + ": " + avtUrl);
        }

        notifyItemChanged(position);
    }

    public void updateMessage(int position, Message message) {
        if (position < 0 || position >= receiverIds.size()) {
            Log.w("MessageAdapter", "Invalid position for updateMessage: " + position + " (receiverIds size: " + receiverIds.size() + ")");
            return;
        }

        while (messageList.size() <= position) {
            messageList.add(null);
        }

        messageList.set(position, message);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return receiverIds.size();
    }

    public void addReceiver(String receiverId) {
        receiverIds.add(receiverId);
        messageList.add(null);
        userNames.add(null);
        avatarUrls.add(null);
        notifyItemInserted(receiverIds.size() - 1);
    }

    public void removeReceiver(int position) {
        if (position >= 0 && position < receiverIds.size()) {
            receiverIds.remove(position);
            if (position < messageList.size()) {
                messageList.remove(position);
            }
            if (position < userNames.size()) {
                userNames.remove(position);
            }
            if (position < avatarUrls.size()) {
                avatarUrls.remove(position);
            }
            notifyItemRemoved(position);
        }
    }

    public void clearAll() {
        int size = receiverIds.size();
        receiverIds.clear();
        messageList.clear();
        userNames.clear();
        avatarUrls.clear();
        notifyItemRangeRemoved(0, size);
    }
}
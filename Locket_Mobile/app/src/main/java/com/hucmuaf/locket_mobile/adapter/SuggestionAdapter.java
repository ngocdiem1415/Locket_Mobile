package com.hucmuaf.locket_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.holder.SuggestionViewHolder;
import com.hucmuaf.locket_mobile.inteface.OnSuggestionActionListener;
import com.hucmuaf.locket_mobile.model.User;

import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionViewHolder> {

    private final List<User> suggestionsList;
    private final OnSuggestionActionListener listener;

    public SuggestionAdapter(List<User> suggestionsList, OnSuggestionActionListener listener) {
        this.suggestionsList = suggestionsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suggestion, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        User suggestion = suggestionsList.get(position);
        holder.bind(suggestion);
    }

    @Override
    public int getItemCount() {
        return suggestionsList.size();
    }

    public OnSuggestionActionListener getListener() {
        return listener;
    }
}
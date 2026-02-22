package com.tien.it_notebook_app.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tien.it_notebook_app.R;

import java.util.ArrayList;
import java.util.List;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {

    public interface OnRecentSearchListener {
        void onSearchClick(String keyword);
        void onDeleteClick(String keyword);
    }

    private List<String> items = new ArrayList<>();
    private OnRecentSearchListener listener;

    public void setOnRecentSearchListener(OnRecentSearchListener listener) {
        this.listener = listener;
    }

    public void submitList(List<String> newItems) {
        items = newItems != null ? newItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String keyword = items.get(position);
        holder.tvKeyword.setText(keyword);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onSearchClick(keyword);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(keyword);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvKeyword;
        final ImageView btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvKeyword = itemView.findViewById(R.id.tvKeyword);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

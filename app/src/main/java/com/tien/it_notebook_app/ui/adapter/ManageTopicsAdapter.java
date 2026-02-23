package com.tien.it_notebook_app.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tien.it_notebook_app.data.model.Topic;
import com.tien.it_notebook_app.databinding.ItemTopicManageBinding;

public class ManageTopicsAdapter extends ListAdapter<Topic, ManageTopicsAdapter.ViewHolder> {

    public interface OnTopicManageListener {
        void onEditClick(Topic topic);
        void onDeleteClick(Topic topic);
        void onItemClick(Topic topic);


    }

    private OnTopicManageListener listener;

    public ManageTopicsAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setOnTopicManageListener(OnTopicManageListener listener) {
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Topic> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Topic>() {
                @Override
                public boolean areItemsTheSame(@NonNull Topic oldItem, @NonNull Topic newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Topic oldItem, @NonNull Topic newItem) {
                    return oldItem.getName().equals(newItem.getName())
                            && oldItem.getColor() == newItem.getColor();
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTopicManageBinding binding = ItemTopicManageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTopicManageBinding binding;

        ViewHolder(ItemTopicManageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getItem(pos));
                }
            });

            binding.btnEdit.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEditClick(getItem(pos));
                }
            });

            binding.btnDelete.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDeleteClick(getItem(pos));
                }
            });
        }

        void bind(Topic topic) {
            binding.tvName.setText(topic.getName());
            if (topic.getColor() != 0) {
                binding.imgIcon.setColorFilter(topic.getColor());
            }
        }
    }
}

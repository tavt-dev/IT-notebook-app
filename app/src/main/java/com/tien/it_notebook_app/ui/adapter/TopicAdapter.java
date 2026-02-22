package com.tien.it_notebook_app.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tien.it_notebook_app.R;
import com.tien.it_notebook_app.data.model.Topic;
import com.tien.it_notebook_app.databinding.ItemTopicBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TopicAdapter extends ListAdapter<Topic, TopicAdapter.TopicViewHolder> {

    public interface OnTopicClickListener {
        void onTopicClick(Topic topic);
    }

    private OnTopicClickListener listener;
    private Map<Integer, Integer> formulaCounts = new HashMap<>();

    public TopicAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setOnTopicClickListener(OnTopicClickListener listener) {
        this.listener = listener;
    }

    public void setFormulaCounts(Map<Integer, Integer> counts) {
        this.formulaCounts = counts;
        notifyDataSetChanged();
    }

    private static final DiffUtil.ItemCallback<Topic> DIFF_CALLBACK = new DiffUtil.ItemCallback<Topic>() {
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
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTopicBinding binding = ItemTopicBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TopicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {

        private final ItemTopicBinding binding;

        TopicViewHolder(ItemTopicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onTopicClick(getItem(pos));
                }
            });
        }

        void bind(Topic topic) {
            binding.tvTitle.setText(topic.getName());

            int color = topic.getColor();
            if (color != 0) {
                int bgColor = Color.argb(51, Color.red(color), Color.green(color), Color.blue(color));
                binding.iconContainer.setCardBackgroundColor(bgColor);
                binding.imgIcon.setColorFilter(color);
            }

            Integer count = formulaCounts.get(topic.getId());
            int c = count != null ? count : 0;
            binding.tvCount.setText(binding.getRoot().getContext()
                    .getString(R.string.formula_count, c));

            // Created date
            long createdAt = topic.getCreatedAt();
            if (createdAt > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                binding.tvCreatedAt.setText("Created: " + sdf.format(new Date(createdAt)));
            } else {
                binding.tvCreatedAt.setText("");
            }
        }
    }
}

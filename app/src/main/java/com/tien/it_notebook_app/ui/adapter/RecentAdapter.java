package com.tien.it_notebook_app.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.databinding.ItemRecentBinding;

public class RecentAdapter extends ListAdapter<Formula, RecentAdapter.RecentViewHolder> {

    public interface OnFormulaClickListener {
        void onFormulaClick(Formula formula);
    }

    private OnFormulaClickListener listener;

    public RecentAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setOnFormulaClickListener(OnFormulaClickListener listener) {
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Formula> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Formula>() {
                @Override
                public boolean areItemsTheSame(@NonNull Formula oldItem, @NonNull Formula newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Formula oldItem, @NonNull Formula newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle())
                            && oldItem.getUpdatedAt() == newItem.getUpdatedAt();
                }
            };

    @NonNull
    @Override
    public RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecentBinding binding = ItemRecentBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RecentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class RecentViewHolder extends RecyclerView.ViewHolder {

        private final ItemRecentBinding binding;

        RecentViewHolder(ItemRecentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onFormulaClick(getItem(pos));
                }
            });
        }

        void bind(Formula formula) {
            binding.tvTitle.setText(formula.getTitle());
            // Code preview
            String code = formula.getContent();
            if (code != null && !code.isEmpty()) {
                binding.tvCodePreview.setText(code);
            } else {
                binding.tvCodePreview.setText("");
            }
            // Show a snippet of content or explanation as description
            String desc = formula.getExplanation();
            if (desc == null || desc.isEmpty()) {
                desc = formula.getContent();
            }
            binding.tvDesc.setText(desc != null ? desc : "");
        }
    }
}

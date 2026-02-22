package com.tien.it_notebook_app.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tien.it_notebook_app.R;
import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.databinding.ItemFormulaCardBinding;

public class FormulaListAdapter extends ListAdapter<Formula, FormulaListAdapter.FormulaViewHolder> {

    public interface OnFormulaActionListener {
        void onFormulaClick(Formula formula);
        void onFavoriteToggle(Formula formula);
    }

    private OnFormulaActionListener listener;

    public FormulaListAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setOnFormulaActionListener(OnFormulaActionListener listener) {
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
                            && oldItem.isFavorite() == newItem.isFavorite()
                            && oldItem.getUpdatedAt() == newItem.getUpdatedAt();
                }
            };

    @NonNull
    @Override
    public FormulaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFormulaCardBinding binding = ItemFormulaCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new FormulaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FormulaViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FormulaViewHolder extends RecyclerView.ViewHolder {

        private final ItemFormulaCardBinding binding;

        FormulaViewHolder(ItemFormulaCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onFormulaClick(getItem(pos));
                }
            });

            binding.btnFavorite.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onFavoriteToggle(getItem(pos));
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

            // Description
            String desc = formula.getExplanation();
            if (desc != null && !desc.isEmpty()) {
                binding.tvDescription.setText(desc);
            } else {
                binding.tvDescription.setText("");
            }

            // Favorite icon
            binding.btnFavorite.setImageResource(
                    formula.isFavorite() ? R.drawable.ic_star_filled : R.drawable.ic_star_outline
            );
            binding.btnFavorite.setColorFilter(
                    formula.isFavorite() ? 0xFFFFD700 : 0xFF9AA0C4
            );
        }
    }
}

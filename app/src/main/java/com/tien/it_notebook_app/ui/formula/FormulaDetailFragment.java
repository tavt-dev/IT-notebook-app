package com.tien.it_notebook_app.ui.formula;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.tien.it_notebook_app.R;
import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.databinding.FragmentFormulaDetailBinding;

public class FormulaDetailFragment extends Fragment {

    private FragmentFormulaDetailBinding binding;
    private FormulaDetailViewModel viewModel;
    private Formula currentFormula;
    private int formulaId;

    public FormulaDetailFragment() {
        super(R.layout.fragment_formula_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentFormulaDetailBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(FormulaDetailViewModel.class);

        if (getArguments() != null) {
            formulaId = getArguments().getInt("formulaId", -1);
        }

        if (formulaId != -1) {
            viewModel.updateLastViewed(formulaId);
            observeData();
        }

        setupActions();
    }

    private void observeData() {
        viewModel.getFormula(formulaId).observe(getViewLifecycleOwner(), formula -> {
            if (formula == null) return;
            currentFormula = formula;
            bindFormula(formula);
        });
    }

    private void bindFormula(Formula formula) {
        binding.tvTitle.setText(formula.getTitle());
        binding.tvContent.setText(formula.getContent());
        binding.tvExplanation.setText(formula.getExplanation());

        // Examples
        String examples = formula.getExamples();
        if (examples != null && !examples.isEmpty()) {
            binding.tvExamples.setText(examples);
            binding.tvExamples.setVisibility(View.VISIBLE);
            binding.tvExamplesLabel.setVisibility(View.VISIBLE);
        } else {
            binding.tvExamples.setVisibility(View.GONE);
            binding.tvExamplesLabel.setVisibility(View.GONE);
        }

        // Tags
        binding.chipGroupTags.removeAllViews();
        if (formula.getTags() != null) {
            for (String tag : formula.getTags()) {
                Chip chip = new Chip(requireContext());
                chip.setText("#" + tag);
                chip.setTextColor(0xFF9AA0C4);
                chip.setChipBackgroundColorResource(android.R.color.transparent);
                chip.setChipStrokeColorResource(android.R.color.darker_gray);
                chip.setChipStrokeWidth(1f);
                chip.setClickable(false);
                binding.chipGroupTags.addView(chip);
            }
        }

        // Favorite icon
        updateFavoriteIcon(formula.isFavorite());
    }

    private void updateFavoriteIcon(boolean isFavorite) {
        binding.btnFavorite.setImageResource(
                isFavorite ? R.drawable.ic_star_filled : R.drawable.ic_star_outline
        );
        binding.btnFavorite.setColorFilter(
                isFavorite ? 0xFFFFD700 : 0xFF9AA0C4
        );
    }

    private void setupActions() {
        binding.btnBack.setOnClickListener(v ->
                Navigation.findNavController(requireView()).navigateUp());

        binding.btnFavorite.setOnClickListener(v -> {
            if (currentFormula != null) {
                viewModel.toggleFavorite(currentFormula);
            }
        });

        binding.btnCopy.setOnClickListener(v -> {
            if (currentFormula != null && currentFormula.getContent() != null) {
                ClipboardManager clipboard = (ClipboardManager)
                        requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("formula", currentFormula.getContent());
                clipboard.setPrimaryClip(clip);
                Snackbar.make(binding.coordinatorLayout, "✓ Copied to clipboard", Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.btnEdit.setOnClickListener(v -> {
            if (currentFormula != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("formulaId", currentFormula.getId());
                bundle.putInt("topicId", currentFormula.getTopicId());
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_formulaDetail_to_addEditFormula, bundle);
            }
        });

        binding.btnDelete.setOnClickListener(v -> {
            if (currentFormula != null) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Delete Formula")
                        .setMessage("This will permanently remove the formula \"" +
                                currentFormula.getTitle() + "\". This action cannot be undone.")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            viewModel.deleteFormula(currentFormula);
                            Navigation.findNavController(requireView()).navigateUp();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

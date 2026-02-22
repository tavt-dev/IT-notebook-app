package com.tien.it_notebook_app.ui.saved;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.tien.it_notebook_app.R;
import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.databinding.FragmentSavedBinding;
import com.tien.it_notebook_app.ui.adapter.FormulaListAdapter;

import java.util.List;

public class SavedFragment extends Fragment {

    private FragmentSavedBinding binding;
    private SavedViewModel viewModel;
    private FormulaListAdapter adapter;
    private LiveData<List<Formula>> currentFavorites;

    public SavedFragment() {
        super(R.layout.fragment_saved);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSavedBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(SavedViewModel.class);

        setupRecycler();
        setupTopicChips();
        observeFavorites(null);
    }

    private void setupRecycler() {
        adapter = new FormulaListAdapter();
        binding.rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvFavorites.setAdapter(adapter);

        adapter.setOnFormulaActionListener(new FormulaListAdapter.OnFormulaActionListener() {
            @Override
            public void onFormulaClick(Formula formula) {
                Bundle bundle = new Bundle();
                bundle.putInt("formulaId", formula.getId());
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_saved_to_formulaDetail, bundle);
            }

            @Override
            public void onFavoriteToggle(Formula formula) {
                viewModel.toggleFavorite(formula);
            }
        });
    }

    private void setupTopicChips() {
        // Add "All" chip first
        Chip allChip = new Chip(requireContext());
        allChip.setText("All");
        allChip.setCheckable(true);
        allChip.setChecked(true);
        allChip.setChipBackgroundColorResource(R.color.primary);
        allChip.setTextColor(0xFFFFFFFF);
        binding.chipGroupTopics.addView(allChip);

        allChip.setOnCheckedChangeListener((c, isChecked) -> {
            if (isChecked) {
                observeFavorites(null);
            }
        });

        // Observe topics and add chips
        viewModel.getAllTopics().observe(getViewLifecycleOwner(), topics -> {
            // Remove all except "All" chip
            int childCount = binding.chipGroupTopics.getChildCount();
            if (childCount > 1) {
                binding.chipGroupTopics.removeViews(1, childCount - 1);
            }

            if (topics != null) {
                for (int i = 0; i < topics.size(); i++) {
                    final int topicId = topics.get(i).getId();
                    Chip chip = new Chip(requireContext());
                    chip.setText(topics.get(i).getName());
                    chip.setCheckable(true);
                    chip.setTextColor(0xFFBBBBBB);
                    chip.setChipBackgroundColorResource(android.R.color.transparent);
                    chip.setChipStrokeColorResource(android.R.color.darker_gray);
                    chip.setChipStrokeWidth(1f);

                    chip.setOnCheckedChangeListener((c, isChecked) -> {
                        if (isChecked) {
                            allChip.setChecked(false);
                            observeFavorites(topicId);
                        }
                    });
                    binding.chipGroupTopics.addView(chip);
                }
            }
        });
    }

    private void observeFavorites(Integer topicId) {
        // Remove previous observer
        if (currentFavorites != null) {
            currentFavorites.removeObservers(getViewLifecycleOwner());
        }

        if (topicId == null) {
            currentFavorites = viewModel.getAllFavorites();
        } else {
            currentFavorites = viewModel.getFavoritesByTopic(topicId);
        }

        currentFavorites.observe(getViewLifecycleOwner(), formulas -> {
            adapter.submitList(formulas);
            boolean empty = formulas == null || formulas.isEmpty();
            binding.emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.rvFavorites.setVisibility(empty ? View.GONE : View.VISIBLE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
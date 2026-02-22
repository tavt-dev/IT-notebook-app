package com.tien.it_notebook_app.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;

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
import com.tien.it_notebook_app.databinding.FragmentSearchBinding;
import com.tien.it_notebook_app.ui.adapter.FormulaListAdapter;
import com.tien.it_notebook_app.ui.adapter.RecentSearchAdapter;

import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;
    private FormulaListAdapter resultAdapter;
    private RecentSearchAdapter recentSearchAdapter;
    private String currentTag = null;
    private LiveData<List<Formula>> currentResults;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSearchBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        setupResultsRecycler();
        setupRecentSearches();
        setupSearchInput();
        setupTagChips();
        observeRecentSearches();
    }

    private void setupResultsRecycler() {
        resultAdapter = new FormulaListAdapter();
        binding.rvResults.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvResults.setAdapter(resultAdapter);

        resultAdapter.setOnFormulaActionListener(new FormulaListAdapter.OnFormulaActionListener() {
            @Override
            public void onFormulaClick(Formula formula) {
                Bundle bundle = new Bundle();
                bundle.putInt("formulaId", formula.getId());
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_search_to_formulaDetail, bundle);
            }

            @Override
            public void onFavoriteToggle(Formula formula) {
                // Not primary action on search screen
            }
        });
    }

    private void setupRecentSearches() {
        recentSearchAdapter = new RecentSearchAdapter();
        binding.rvRecentSearches.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvRecentSearches.setAdapter(recentSearchAdapter);

        recentSearchAdapter.setOnRecentSearchListener(new RecentSearchAdapter.OnRecentSearchListener() {
            @Override
            public void onSearchClick(String keyword) {
                binding.etSearch.setText(keyword);
                binding.etSearch.setSelection(keyword.length());
                performSearch(keyword);
            }

            @Override
            public void onDeleteClick(String keyword) {
                viewModel.removeRecentSearch(keyword);
            }
        });

        binding.btnClearAll.setOnClickListener(v -> viewModel.clearAllRecent());
    }

    private void setupSearchInput() {
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    showRecentSearches(true);
                    resultAdapter.submitList(null);
                    binding.emptyState.setVisibility(View.GONE);
                } else {
                    showRecentSearches(false);
                    performSearch(query);
                }
            }
        });

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = binding.etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                }
                return true;
            }
            return false;
        });
    }

    private void setupTagChips() {
        // Pre-defined common tags
        String[] defaultTags = {"sql", "java", "python", "algorithms", "regex", "data-structures"};
        for (String tag : defaultTags) {
            Chip chip = new Chip(requireContext());
            chip.setText("#" + tag);
            chip.setCheckable(true);
            chip.setTextColor(0xFF9AA0C4);
            chip.setChipBackgroundColorResource(android.R.color.transparent);
            chip.setChipStrokeWidth(1f);
            chip.setChipStrokeColorResource(android.R.color.darker_gray);
            chip.setOnCheckedChangeListener((c, isChecked) -> {
                if (isChecked) {
                    currentTag = tag;
                } else {
                    currentTag = null;
                }
                String query = binding.etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                }
            });
            binding.chipGroupTags.addView(chip);
        }
    }

    private void performSearch(String keyword) {
        // Remove old observer
        if (currentResults != null) {
            currentResults.removeObservers(getViewLifecycleOwner());
        }

        currentResults = viewModel.search(keyword, currentTag);
        currentResults.observe(getViewLifecycleOwner(), formulas -> {
            resultAdapter.submitList(formulas);
            boolean empty = formulas == null || formulas.isEmpty();
            binding.emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.rvResults.setVisibility(empty ? View.GONE : View.VISIBLE);
        });
    }

    private void observeRecentSearches() {
        viewModel.getRecentSearches().observe(getViewLifecycleOwner(), searches -> {
            recentSearchAdapter.submitList(searches);
            // Show recent only when search field is empty
            String query = binding.etSearch.getText().toString().trim();
            if (query.isEmpty()) {
                showRecentSearches(searches != null && !searches.isEmpty());
            }
        });
    }

    private void showRecentSearches(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        binding.recentSearchHeader.setVisibility(visibility);
        binding.rvRecentSearches.setVisibility(visibility);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

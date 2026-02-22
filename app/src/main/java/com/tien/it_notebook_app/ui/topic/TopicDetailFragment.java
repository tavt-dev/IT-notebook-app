package com.tien.it_notebook_app.ui.topic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;

import com.tien.it_notebook_app.R;
import com.tien.it_notebook_app.databinding.FragmentTopicDetailBinding;
import com.tien.it_notebook_app.ui.adapter.FormulaListAdapter;

public class TopicDetailFragment extends Fragment {

    private FragmentTopicDetailBinding binding;
    private TopicDetailViewModel viewModel;
    private FormulaListAdapter adapter;
    private int topicId;

    public TopicDetailFragment() {
        super(R.layout.fragment_topic_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentTopicDetailBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(TopicDetailViewModel.class);

        // Get topic ID from arguments
        if (getArguments() != null) {
            topicId = getArguments().getInt("topicId", -1);
        }

        setupRecycler();
        setupChips();
        setupToolbar();

        if (topicId != -1) {
            viewModel.setTopicId(topicId);
            observeData();
        }
    }

    private void setupRecycler() {
        adapter = new FormulaListAdapter();
        binding.rvFormulas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvFormulas.setAdapter(adapter);

        adapter.setOnFormulaActionListener(new FormulaListAdapter.OnFormulaActionListener() {
            @Override
            public void onFormulaClick(com.tien.it_notebook_app.data.model.Formula formula) {
                Bundle bundle = new Bundle();
                bundle.putInt("formulaId", formula.getId());
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_topicDetail_to_formulaDetail, bundle);
            }

            @Override
            public void onFavoriteToggle(com.tien.it_notebook_app.data.model.Formula formula) {
                viewModel.toggleFavorite(formula);
            }
        });
    }

    private void setupChips() {
        binding.chipAZ.setOnCheckedChangeListener((chip, isChecked) -> {
            if (isChecked) {
                viewModel.setFilter("az");
                reobserveFormulas();
            }
        });
        binding.chipRecent.setOnCheckedChangeListener((chip, isChecked) -> {
            if (isChecked) {
                viewModel.setFilter("recent");
                reobserveFormulas();
            }
        });
        binding.chipFavorites.setOnCheckedChangeListener((chip, isChecked) -> {
            if (isChecked) {
                viewModel.setFilter("favorites");
                reobserveFormulas();
            }
        });
    }

    private void setupToolbar() {
        binding.btnBack.setOnClickListener(v ->
                Navigation.findNavController(requireView()).navigateUp());

        binding.fabAdd.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("topicId", topicId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_topicDetail_to_addEditFormula, bundle);
        });
    }

    private void observeData() {
        viewModel.getTopic().observe(getViewLifecycleOwner(), topic -> {
            if (topic != null) {
                binding.tvTopicName.setText(topic.getName());
            }
        });
        reobserveFormulas();
    }

    private void reobserveFormulas() {
        if (viewModel.getFormulas() != null) {
            viewModel.getFormulas().observe(getViewLifecycleOwner(), formulas -> {
                adapter.submitList(formulas);
                boolean empty = formulas == null || formulas.isEmpty();
                binding.emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
                binding.rvFormulas.setVisibility(empty ? View.GONE : View.VISIBLE);
                binding.tvFormulaCount.setText(
                        (formulas != null ? formulas.size() : 0) + " formulas");
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

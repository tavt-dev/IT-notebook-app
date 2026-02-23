package com.tien.it_notebook_app.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.PopupMenu;

import androidx.navigation.Navigation;

import com.tien.it_notebook_app.R;
import com.tien.it_notebook_app.databinding.FragmentHomeBinding;
import com.tien.it_notebook_app.ui.adapter.RecentAdapter;
import com.tien.it_notebook_app.ui.adapter.TopicAdapter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private TopicAdapter topicAdapter;
    private RecentAdapter recentAdapter;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentHomeBinding.bind(view);

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupRecycler();
        setupClickListeners();
        observeData();
    }

    private void setupRecycler() {
        recentAdapter = new RecentAdapter();
        binding.rvRecent.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvRecent.setAdapter(recentAdapter);

        topicAdapter = new TopicAdapter();
        binding.rvTopics.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.rvTopics.setAdapter(topicAdapter);

        topicAdapter.setOnTopicClickListener(topic -> {
            Bundle bundle = new Bundle();
            bundle.putInt("topicId", topic.getId());
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_home_to_topicDetail, bundle);
        });

        recentAdapter.setOnFormulaClickListener(formula -> {
            Bundle bundle = new Bundle();
            bundle.putInt("formulaId", formula.getId());
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_home_to_formulaDetail, bundle);
        });
    }

    private void setupClickListeners() {
        binding.searchBar.setOnClickListener(v -> {
            com.google.android.material.bottomnavigation.BottomNavigationView bottomNav =
                    requireActivity().findViewById(R.id.bottomNav);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.historyFragment);
            }
        });

        binding.fabAdd.setOnClickListener(v -> Navigation.findNavController(requireView())
                .navigate(R.id.action_home_to_addEditFormula));

        binding.btnManageTopics.setOnClickListener(v ->
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_home_to_manageTopics));

        binding.btnOverflow.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), v);
            popup.getMenuInflater().inflate(R.menu.menu_home_overflow, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_manage_topics) {
                    Navigation.findNavController(requireView())
                            .navigate(R.id.action_home_to_manageTopics);
                    return true;
                }
                return false;
            });
            popup.show();
        });
    }

    private void observeData() {
        viewModel.getAllTopics().observe(getViewLifecycleOwner(), topics -> {
            topicAdapter.submitList(topics);
            if (topics != null && !topics.isEmpty()) {
                viewModel.loadFormulaCounts(topics);
            }
        });

        viewModel.getFormulaCounts().observe(getViewLifecycleOwner(), counts -> {
            if (counts != null && !counts.isEmpty()) {
                topicAdapter.setFormulaCounts(counts);
            }
        });

        viewModel.getRecentlyViewed().observe(getViewLifecycleOwner(), formulas -> {
            boolean hasRecent = formulas != null && !formulas.isEmpty();
            binding.recentHeader.setVisibility(hasRecent ? View.VISIBLE : View.GONE);
            binding.rvRecent.setVisibility(hasRecent ? View.VISIBLE : View.GONE);
            binding.emptyRecentState.setVisibility(hasRecent ? View.GONE : View.VISIBLE);
            if (hasRecent) {
                recentAdapter.submitList(formulas);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
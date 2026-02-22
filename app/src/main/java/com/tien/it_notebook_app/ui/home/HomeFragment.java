package com.tien.it_notebook_app.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;

import com.tien.it_notebook_app.R;
import com.tien.it_notebook_app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentHomeBinding.bind(view);

        setupRecycler();
    }

    private void setupRecycler() {
        binding.rvRecent.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        binding.rvTopics.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
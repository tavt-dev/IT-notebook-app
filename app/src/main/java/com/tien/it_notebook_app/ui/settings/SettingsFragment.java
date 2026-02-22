package com.tien.it_notebook_app.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tien.it_notebook_app.R;
import com.tien.it_notebook_app.data.db.AppDatabase;
import com.tien.it_notebook_app.databinding.FragmentSettingsBinding;

import java.util.concurrent.Executors;

public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "app_settings";
    private static final String KEY_DARK_MODE = "dark_mode";

    private FragmentSettingsBinding binding;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSettingsBinding.bind(view);

        setupDarkMode();
        setupClearHistory();
        setupManageTopics();
    }

    private void setupDarkMode() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, 0);
        boolean isDark = prefs.getBoolean(KEY_DARK_MODE, true);
        binding.switchDarkMode.setChecked(isDark);

        binding.switchDarkMode.setOnCheckedChangeListener((btn, isChecked) -> {
            prefs.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });
    }

    private void setupClearHistory() {
        binding.btnClearHistory.setOnClickListener(v ->
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Clear History")
                    .setMessage("This will clear all recently viewed formulas. Continue?")
                    .setPositiveButton("Clear", (d, w) -> {
                        Executors.newSingleThreadExecutor().execute(() ->
                            AppDatabase.getInstance(requireContext())
                                    .formulaDao().clearRecentlyViewed()
                        );
                        Toast.makeText(getContext(), "History cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show()
        );
    }

    private void setupManageTopics() {
        binding.btnManageTopics.setOnClickListener(v ->
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_settings_to_manageTopics)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.tien.it_notebook_app.ui.topic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tien.it_notebook_app.R;
import com.tien.it_notebook_app.data.model.Topic;
import com.tien.it_notebook_app.data.repository.TopicRepository;
import com.tien.it_notebook_app.databinding.FragmentManageTopicsBinding;
import com.tien.it_notebook_app.ui.adapter.ManageTopicsAdapter;

public class ManageTopicsFragment extends Fragment {

    private FragmentManageTopicsBinding binding;
    private TopicRepository topicRepository;
    private ManageTopicsAdapter adapter;

    public ManageTopicsFragment() {
        super(R.layout.fragment_manage_topics);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentManageTopicsBinding.bind(view);
        topicRepository = new TopicRepository(requireActivity().getApplication());

        setupRecycler();
        setupToolbar();
        observeTopics();
    }

    private void setupRecycler() {
        adapter = new ManageTopicsAdapter();
        binding.rvTopics.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTopics.setAdapter(adapter);

        adapter.setOnTopicManageListener(new ManageTopicsAdapter.OnTopicManageListener() {
            @Override
            public void onEditClick(Topic topic) {
                showAddEditDialog(topic);
            }

            @Override
            public void onDeleteClick(Topic topic) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Delete Topic")
                        .setMessage("Deleting \"" + topic.getName() + "\" will also remove ALL formulas in this topic. This action cannot be undone.")
                        .setPositiveButton("Delete", (d, w) -> {
                            topicRepository.delete(topic);
                            Toast.makeText(getContext(), "Topic deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            @Override
            public void onItemClick(Topic topic) {
                Bundle bundle = new Bundle();
                bundle.putInt("topicId", topic.getId());
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_manageTopics_to_topicDetail, bundle);
            }
        });
    }

    private void setupToolbar() {
        binding.btnBack.setOnClickListener(v ->
                Navigation.findNavController(requireView()).navigateUp());

        binding.fabAdd.setOnClickListener(v -> showAddEditDialog(null));
    }

    private void observeTopics() {
        topicRepository.getAllTopics().observe(getViewLifecycleOwner(), topics -> {
            adapter.submitList(topics);
            boolean empty = topics == null || topics.isEmpty();
            binding.emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.rvTopics.setVisibility(empty ? View.GONE : View.VISIBLE);
        });
    }

    private void showAddEditDialog(@Nullable Topic existingTopic) {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_edit_topic, null);

        TextView tvTitle = dialogView.findViewById(R.id.tvDialogTitle);
        EditText etName = dialogView.findViewById(R.id.etTopicName);
        EditText etIcon = dialogView.findViewById(R.id.etIcon);

        // Color selection
        final int[] selectedColor = {0xFF4D7CFF}; // default blue
        View colorBlue = dialogView.findViewById(R.id.colorBlue);
        View colorGreen = dialogView.findViewById(R.id.colorGreen);
        View colorOrange = dialogView.findViewById(R.id.colorOrange);
        View colorRed = dialogView.findViewById(R.id.colorRed);
        View colorPurple = dialogView.findViewById(R.id.colorPurple);

        final View[] colorViews = {colorBlue, colorGreen, colorOrange, colorRed, colorPurple};
        final int[] colors = {0xFF4D7CFF, 0xFF4CAF50, 0xFFFF9800, 0xFFFF6B6B, 0xFF9C27B0};

        View.OnClickListener colorClickListener = v -> {
            for (int i = 0; i < colorViews.length; i++) {
                if (colorViews[i].getId() == v.getId()) {
                    selectedColor[0] = colors[i];
                    colorViews[i].animate().scaleX(0.8f).scaleY(0.8f).setDuration(150);
                } else {
                    colorViews[i].animate().scaleX(1.0f).scaleY(1.0f).setDuration(150);
                }
            }
        };

        for (View colorView : colorViews) {
            colorView.setOnClickListener(colorClickListener);
        }

        if (existingTopic != null) {
            tvTitle.setText("Edit Topic");
            etName.setText(existingTopic.getName());
            etIcon.setText(existingTopic.getIcon());
            selectedColor[0] = existingTopic.getColor();
            for (int i = 0; i < colors.length; i++) {
                if (colors[i] == selectedColor[0]) {
                    colorViews[i].setScaleX(0.8f);
                    colorViews[i].setScaleY(0.8f);
                }
            }
        } else {
            colorViews[0].setScaleX(0.8f);
            colorViews[0].setScaleY(0.8f);
        }

        new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setPositiveButton(existingTopic != null ? "Save" : "Create", (d, w) -> {
                    String name = etName.getText().toString().trim();
                    String icon = etIcon.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(getContext(), "Name is required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (existingTopic != null) {
                        existingTopic.setName(name);
                        existingTopic.setIcon(icon);
                        existingTopic.setColor(selectedColor[0]);
                        topicRepository.update(existingTopic);
                        binding.rvTopics.setAdapter(adapter);
                        Toast.makeText(getContext(), "Topic updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Topic topic = new Topic(name, icon, selectedColor[0], System.currentTimeMillis());
                        topicRepository.insert(topic);
                        Toast.makeText(getContext(), "Topic created", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.tien.it_notebook_app.ui.formula;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tien.it_notebook_app.R;
import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.data.model.Topic;
import com.tien.it_notebook_app.databinding.FragmentAddEditFormulaBinding;

import java.util.ArrayList;
import java.util.List;

public class AddEditFormulaFragment extends Fragment {

    private FragmentAddEditFormulaBinding binding;
    private AddEditFormulaViewModel viewModel;

    private int formulaId = -1;
    private int topicId = -1;
    private boolean isEditMode = false;
    private Formula existingFormula;

    private List<Topic> topicList = new ArrayList<>();
    private ArrayAdapter<String> topicSpinnerAdapter;

    public AddEditFormulaFragment() {
        super(R.layout.fragment_add_edit_formula);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentAddEditFormulaBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(AddEditFormulaViewModel.class);

        if (getArguments() != null) {
            formulaId = getArguments().getInt("formulaId", -1);
            topicId = getArguments().getInt("topicId", -1);
        }
        isEditMode = formulaId != -1;

        setupUI();
        setupTopicSpinner();

        if (isEditMode) {
            binding.tvScreenTitle.setText("Edit Formula");
            binding.btnSave.setText("Save Changes");
            loadFormula();
        }
    }

    private void setupUI() {
        binding.btnBack.setOnClickListener(v -> handleBackPress());

        binding.btnSave.setOnClickListener(v -> saveFormula());
    }

    private void handleBackPress() {
        // Check if user has entered any data
        String title = binding.etTitle.getText().toString().trim();
        String content = binding.etContent.getText().toString().trim();

        if (!title.isEmpty() || !content.isEmpty()) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Discard changes?")
                    .setMessage("You have unsaved changes. Are you sure you want to go back?")
                    .setPositiveButton("Discard", (d, w) ->
                            Navigation.findNavController(requireView()).navigateUp())
                    .setNegativeButton("Keep editing", null)
                    .show();
        } else {
            Navigation.findNavController(requireView()).navigateUp();
        }
    }

    private void setupTopicSpinner() {
        topicSpinnerAdapter = new ArrayAdapter<>(requireContext(),
                R.layout.item_spinner_topic, new ArrayList<>());
        topicSpinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown_topic);
        binding.spinnerTopic.setAdapter(topicSpinnerAdapter);

        viewModel.getAllTopics().observe(getViewLifecycleOwner(), topics -> {
            topicList = topics;
            List<String> names = new ArrayList<>();
            int selectedIndex = 0;
            for (int i = 0; i < topics.size(); i++) {
                names.add(topics.get(i).getName());
                if (topics.get(i).getId() == topicId) {
                    selectedIndex = i;
                }
            }
            topicSpinnerAdapter.clear();
            topicSpinnerAdapter.addAll(names);
            topicSpinnerAdapter.notifyDataSetChanged();
            if (!topics.isEmpty()) {
                binding.spinnerTopic.setSelection(selectedIndex);
            }
        });
    }

    private void loadFormula() {
        viewModel.getFormula(formulaId).observe(getViewLifecycleOwner(), formula -> {
            if (formula == null) return;
            existingFormula = formula;

            binding.etTitle.setText(formula.getTitle());
            binding.etContent.setText(formula.getContent());
            binding.etExplanation.setText(formula.getExplanation());
            binding.etExamples.setText(formula.getExamples());

            // Tags
            if (formula.getTags() != null) {
                binding.etTags.setText(String.join(", ", formula.getTags()));
            }

            // Set topic spinner
            topicId = formula.getTopicId();
            for (int i = 0; i < topicList.size(); i++) {
                if (topicList.get(i).getId() == topicId) {
                    binding.spinnerTopic.setSelection(i);
                    break;
                }
            }
        });
    }

    private void saveFormula() {
        String title = binding.etTitle.getText().toString().trim();
        String content = binding.etContent.getText().toString().trim();
        String explanation = binding.etExplanation.getText().toString().trim();
        String examples = binding.etExamples.getText().toString().trim();
        String tagsStr = binding.etTags.getText().toString().trim();

        // Validation
        if (title.isEmpty()) {
            binding.etTitle.setError("Title is required");
            binding.etTitle.requestFocus();
            return;
        }
        if (content.isEmpty()) {
            binding.etContent.setError("Content is required");
            binding.etContent.requestFocus();
            return;
        }
        if (topicList.isEmpty()) {
            Toast.makeText(getContext(), "Please create a topic first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse tags
        String[] tags = null;
        if (!tagsStr.isEmpty()) {
            String[] rawTags = tagsStr.split(",");
            List<String> tagList = new ArrayList<>();
            for (String t : rawTags) {
                String trimmed = t.trim();
                if (!trimmed.isEmpty()) {
                    tagList.add(trimmed);
                }
            }
            tags = tagList.toArray(new String[0]);
        }

        int selectedTopicId = topicList.get(binding.spinnerTopic.getSelectedItemPosition()).getId();
        long now = System.currentTimeMillis();

        if (isEditMode && existingFormula != null) {
            existingFormula.setTitle(title);
            existingFormula.setContent(content);
            existingFormula.setExplanation(explanation);
            existingFormula.setExamples(examples);
            existingFormula.setTags(tags);
            existingFormula.setTopicId(selectedTopicId);
            existingFormula.setUpdatedAt(now);
            viewModel.updateFormula(existingFormula);
            Toast.makeText(getContext(), "Formula updated!", Toast.LENGTH_SHORT).show();
        } else {
            Formula formula = new Formula(title, selectedTopicId, content, explanation,
                    examples, tags, false, now, now, 0);
            viewModel.insertFormula(formula);
            Toast.makeText(getContext(), "Formula created!", Toast.LENGTH_SHORT).show();
        }

        Navigation.findNavController(requireView()).navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

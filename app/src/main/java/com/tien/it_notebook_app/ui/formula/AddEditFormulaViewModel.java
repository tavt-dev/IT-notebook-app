package com.tien.it_notebook_app.ui.formula;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.data.model.Topic;
import com.tien.it_notebook_app.data.repository.FormulaRepository;
import com.tien.it_notebook_app.data.repository.TopicRepository;

import java.util.List;

public class AddEditFormulaViewModel extends AndroidViewModel {

    private final FormulaRepository formulaRepository;
    private final TopicRepository topicRepository;
    private final LiveData<List<Topic>> allTopics;

    public AddEditFormulaViewModel(@NonNull Application application) {
        super(application);
        formulaRepository = new FormulaRepository(application);
        topicRepository = new TopicRepository(application);
        allTopics = topicRepository.getAllTopics();
    }

    public LiveData<List<Topic>> getAllTopics() {
        return allTopics;
    }

    public LiveData<Formula> getFormula(int id) {
        return formulaRepository.getById(id);
    }

    public void insertFormula(Formula formula) {
        formulaRepository.insert(formula);
    }

    public void updateFormula(Formula formula) {
        formulaRepository.update(formula);
    }
}

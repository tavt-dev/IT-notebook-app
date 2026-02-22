package com.tien.it_notebook_app.ui.saved;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.data.model.Topic;
import com.tien.it_notebook_app.data.repository.FormulaRepository;
import com.tien.it_notebook_app.data.repository.TopicRepository;

import java.util.List;

public class SavedViewModel extends AndroidViewModel {

    private final FormulaRepository formulaRepository;
    private final TopicRepository topicRepository;

    private final LiveData<List<Formula>> allFavorites;
    private final LiveData<List<Topic>> allTopics;

    public SavedViewModel(@NonNull Application application) {
        super(application);
        formulaRepository = new FormulaRepository(application);
        topicRepository = new TopicRepository(application);
        allFavorites = formulaRepository.getFavorites();
        allTopics = topicRepository.getAllTopics();
    }

    public LiveData<List<Formula>> getAllFavorites() {
        return allFavorites;
    }

    public LiveData<List<Formula>> getFavoritesByTopic(int topicId) {
        return formulaRepository.getFavoritesByTopic(topicId);
    }

    public LiveData<List<Topic>> getAllTopics() {
        return allTopics;
    }

    public void toggleFavorite(Formula formula) {
        formulaRepository.setFavorite(formula.getId(), !formula.isFavorite());
    }
}

package com.tien.it_notebook_app.ui.topic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.data.model.Topic;
import com.tien.it_notebook_app.data.repository.FormulaRepository;
import com.tien.it_notebook_app.data.repository.TopicRepository;

import java.util.List;

public class TopicDetailViewModel extends AndroidViewModel {

    private final TopicRepository topicRepository;
    private final FormulaRepository formulaRepository;

    private final MutableLiveData<Integer> topicId = new MutableLiveData<>();
    private final MutableLiveData<String> filterMode = new MutableLiveData<>("az");

    private LiveData<Topic> topic;
    private LiveData<List<Formula>> formulas;

    public TopicDetailViewModel(@NonNull Application application) {
        super(application);
        topicRepository = new TopicRepository(application);
        formulaRepository = new FormulaRepository(application);
    }

    public void setTopicId(int id) {
        topicId.setValue(id);
        topic = topicRepository.getTopicById(id);
        applyFilter();
    }

    public void setFilter(String mode) {
        filterMode.setValue(mode);
        applyFilter();
    }

    private void applyFilter() {
        Integer id = topicId.getValue();
        if (id == null) return;
        String mode = filterMode.getValue();
        if (mode == null) mode = "az";

        switch (mode) {
            case "recent":
                formulas = formulaRepository.getAllByTopicRecent(id);
                break;
            case "favorites":
                formulas = formulaRepository.getFavoritesByTopic(id);
                break;
            case "az":
            default:
                formulas = formulaRepository.getAllByTopic(id);
                break;
        }
    }

    public LiveData<Topic> getTopic() {
        return topic;
    }

    public LiveData<List<Formula>> getFormulas() {
        return formulas;
    }

    public void toggleFavorite(Formula formula) {
        formulaRepository.setFavorite(formula.getId(), !formula.isFavorite());
    }
}

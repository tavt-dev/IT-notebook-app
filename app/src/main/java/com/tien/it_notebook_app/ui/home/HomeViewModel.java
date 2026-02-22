package com.tien.it_notebook_app.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.data.model.Topic;
import com.tien.it_notebook_app.data.repository.FormulaRepository;
import com.tien.it_notebook_app.data.repository.TopicRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeViewModel extends AndroidViewModel {

    private final TopicRepository topicRepository;
    private final FormulaRepository formulaRepository;
    private final ExecutorService executor;

    private final LiveData<List<Topic>> allTopics;
    private final LiveData<List<Formula>> recentlyViewed;
    private final MutableLiveData<Map<Integer, Integer>> formulaCounts = new MutableLiveData<>(new HashMap<>());

    public HomeViewModel(@NonNull Application application) {
        super(application);
        topicRepository = new TopicRepository(application);
        formulaRepository = new FormulaRepository(application);
        executor = Executors.newSingleThreadExecutor();

        allTopics = topicRepository.getAllTopics();
        recentlyViewed = formulaRepository.getRecentlyViewed();
    }

    public LiveData<List<Topic>> getAllTopics() {
        return allTopics;
    }

    public LiveData<List<Formula>> getRecentlyViewed() {
        return recentlyViewed;
    }

    public LiveData<Map<Integer, Integer>> getFormulaCounts() {
        return formulaCounts;
    }

    public void loadFormulaCounts(List<Topic> topics) {
        executor.execute(() -> {
            Map<Integer, Integer> counts = new HashMap<>();
            for (Topic topic : topics) {
                int count = formulaRepository.getFormulaCountByTopic(topic.getId());
                counts.put(topic.getId(), count);
            }
            formulaCounts.postValue(counts);
        });
    }
}

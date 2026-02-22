package com.tien.it_notebook_app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.tien.it_notebook_app.data.db.AppDatabase;
import com.tien.it_notebook_app.data.db.TopicDao;
import com.tien.it_notebook_app.data.model.Topic;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TopicRepository {

    private final TopicDao topicDao;
    private final LiveData<List<Topic>> allTopics;
    private final ExecutorService executor;

    public TopicRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        topicDao = db.topicDao();
        allTopics = topicDao.getAll();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Topic>> getAllTopics() {
        return allTopics;
    }

    public LiveData<Topic> getTopicById(int id) {
        return topicDao.getById(id);
    }

    public void insert(Topic topic) {
        executor.execute(() -> topicDao.insert(topic));
    }

    public void update(Topic topic) {
        executor.execute(() -> topicDao.update(topic));
    }

    public void delete(Topic topic) {
        executor.execute(() -> topicDao.delete(topic));
    }

    public void deleteById(int id) {
        executor.execute(() -> topicDao.deleteById(id));
    }
}

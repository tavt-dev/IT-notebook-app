package com.tien.it_notebook_app.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.tien.it_notebook_app.data.db.AppDatabase;
import com.tien.it_notebook_app.data.db.FormulaDao;
import com.tien.it_notebook_app.data.model.Formula;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FormulaRepository {

    private final FormulaDao formulaDao;
    private final ExecutorService executor;

    public FormulaRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        formulaDao = db.formulaDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Formula>> getAll() {
        return formulaDao.getAll();
    }

    public LiveData<List<Formula>> getAllByTopic(int topicId) {
        return formulaDao.getAllByTopic(topicId);
    }

    public LiveData<List<Formula>> getAllByTopicRecent(int topicId) {
        return formulaDao.getAllByTopicRecent(topicId);
    }

    public LiveData<List<Formula>> getFavorites() {
        return formulaDao.getFavorites();
    }

    public LiveData<List<Formula>> getFavoritesByTopic(int topicId) {
        return formulaDao.getFavoritesByTopic(topicId);
    }

    public LiveData<List<Formula>> getRecentlyViewed() {
        return formulaDao.getRecentlyViewed();
    }

    public LiveData<Formula> getById(int id) {
        return formulaDao.getById(id);
    }

    public LiveData<List<Formula>> searchByKeyword(String keyword) {
        return formulaDao.searchByKeyword(keyword);
    }

    public LiveData<List<Formula>> searchByKeywordAndTag(String keyword, String tag) {
        return formulaDao.searchByKeywordAndTag(keyword, tag);
    }

    public void insert(Formula formula) {
        executor.execute(() -> formulaDao.insert(formula));
    }

    public void update(Formula formula) {
        executor.execute(() -> formulaDao.update(formula));
    }

    public void delete(Formula formula) {
        executor.execute(() -> formulaDao.delete(formula));
    }

    public void deleteById(int id) {
        executor.execute(() -> formulaDao.deleteById(id));
    }

    public void setFavorite(int id, boolean isFavorite) {
        executor.execute(() -> formulaDao.setFavorite(id, isFavorite));
    }

    public void updateLastViewedAt(int id, long timestamp) {
        executor.execute(() -> formulaDao.updateLastViewedAt(id, timestamp));
    }

    public void clearRecentlyViewed() {
        executor.execute(formulaDao::clearRecentlyViewed);
    }

    public int getFormulaCountByTopic(int topicId) {
        return formulaDao.getFormulaCountByTopic(topicId);
    }
}

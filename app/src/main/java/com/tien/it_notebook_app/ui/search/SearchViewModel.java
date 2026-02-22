package com.tien.it_notebook_app.ui.search;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.data.repository.FormulaRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchViewModel extends AndroidViewModel {

    private static final String PREFS_NAME = "search_prefs";
    private static final String KEY_RECENT = "recent_searches";
    private static final int MAX_RECENT = 10;

    private final FormulaRepository formulaRepository;
    private final SharedPreferences prefs;

    private final MutableLiveData<List<String>> recentSearches = new MutableLiveData<>();
    private LiveData<List<Formula>> searchResults;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        formulaRepository = new FormulaRepository(application);
        prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadRecentSearches();
    }

    public LiveData<List<Formula>> search(String keyword, String tag) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            saveRecentSearch(keyword.trim());
        }

        if (tag != null && !tag.isEmpty()) {
            searchResults = formulaRepository.searchByKeywordAndTag(
                    keyword != null ? keyword.trim() : "", tag);
        } else {
            searchResults = formulaRepository.searchByKeyword(
                    keyword != null ? keyword.trim() : "");
        }
        return searchResults;
    }

    public LiveData<List<Formula>> getSearchResults() {
        return searchResults;
    }

    public LiveData<List<String>> getRecentSearches() {
        return recentSearches;
    }

    private void saveRecentSearch(String keyword) {
        List<String> current = recentSearches.getValue();
        if (current == null) current = new ArrayList<>();

        // Remove if exists, add to front
        current.remove(keyword);
        current.add(0, keyword);

        // Limit size
        if (current.size() > MAX_RECENT) {
            current = current.subList(0, MAX_RECENT);
        }

        recentSearches.setValue(current);
        // Persist
        prefs.edit().putStringSet(KEY_RECENT, new HashSet<>(current)).apply();
    }

    private void loadRecentSearches() {
        Set<String> saved = prefs.getStringSet(KEY_RECENT, new HashSet<>());
        recentSearches.setValue(new ArrayList<>(saved));
    }

    public void removeRecentSearch(String keyword) {
        List<String> current = recentSearches.getValue();
        if (current != null) {
            current.remove(keyword);
            recentSearches.setValue(current);
            prefs.edit().putStringSet(KEY_RECENT, new HashSet<>(current)).apply();
        }
    }

    public void clearAllRecent() {
        recentSearches.setValue(new ArrayList<>());
        prefs.edit().remove(KEY_RECENT).apply();
    }
}

package com.tien.it_notebook_app.ui.formula;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tien.it_notebook_app.data.model.Formula;
import com.tien.it_notebook_app.data.repository.FormulaRepository;

public class FormulaDetailViewModel extends AndroidViewModel {

    private final FormulaRepository formulaRepository;

    public FormulaDetailViewModel(@NonNull Application application) {
        super(application);
        formulaRepository = new FormulaRepository(application);
    }

    public LiveData<Formula> getFormula(int id) {
        return formulaRepository.getById(id);
    }

    public void toggleFavorite(Formula formula) {
        formulaRepository.setFavorite(formula.getId(), !formula.isFavorite());
    }

    public void updateLastViewed(int id) {
        formulaRepository.updateLastViewedAt(id, System.currentTimeMillis());
    }

    public void deleteFormula(Formula formula) {
        formulaRepository.delete(formula);
    }
}

package com.example.android.bakingapp.roomdatabase;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

public class IngredientViewModel extends AndroidViewModel {

    public IngredientRepository mRepository;
    // Using LiveData and caching what getAllIngredients returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    public LiveData<List<IngredientEntry>> mAllIngredients;

    public IngredientViewModel (Application application) {
        super(application);
        mRepository = new IngredientRepository(application);
        mAllIngredients = mRepository.getAllIngredients();
    }

    public LiveData<List<IngredientEntry>> getAllIngredients() { return mAllIngredients; }

    public void insert(IngredientEntry ingredient) { mRepository.insert(ingredient); }
}


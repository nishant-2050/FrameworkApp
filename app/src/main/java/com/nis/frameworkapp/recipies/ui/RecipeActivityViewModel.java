package com.nis.frameworkapp.recipies.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.recipies.data.model.RecipeList;
import com.nis.frameworkapp.recipies.data.repo.RecipeDataRepo;

public class RecipeActivityViewModel extends ViewModel {
    private final LiveData<ResultStatus<RecipeList>> liveData;
    private final RecipeDataRepo mRepository;

    public RecipeActivityViewModel(RecipeDataRepo repository) {
        mRepository = repository;
        liveData = mRepository.getRecipeList();
    }

    public LiveData<ResultStatus<RecipeList>> getAllRecipies() {
        return liveData;
    }
}

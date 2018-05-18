package com.nis.frameworkapp.recipies.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.recipies.data.model.RecipeList;
import com.nis.frameworkapp.recipies.data.repo.RecipeDataRepo;

public class RecipeActivityViewModel extends ViewModel {
    private final RecipeDataRepo mRepository;

    public RecipeActivityViewModel(RecipeDataRepo repository) {
        mRepository = repository;
    }

    public LiveData<ResultStatus<RecipeList>> getAllRecipies() {
        MediatorLiveData<ResultStatus<RecipeList>> liveData = new MediatorLiveData<>();
        liveData.addSource(mRepository.getRecipeList(), resultStatus -> {
            liveData.setValue(resultStatus);
        });
        return liveData;
    }
}

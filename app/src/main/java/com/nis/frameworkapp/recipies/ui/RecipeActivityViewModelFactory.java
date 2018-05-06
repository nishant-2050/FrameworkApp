package com.nis.frameworkapp.recipies.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.nis.frameworkapp.recipies.data.repo.RecipeDataRepo;

public class RecipeActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipeDataRepo recipeDataRepo;

    public RecipeActivityViewModelFactory(RecipeDataRepo recipeDataRepo) {
        this.recipeDataRepo = recipeDataRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeActivityViewModel(recipeDataRepo);
    }
}

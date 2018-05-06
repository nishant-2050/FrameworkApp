package com.nis.frameworkapp.recipies.data.repo;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.nis.frameworkapp.common.AppExecutors;
import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.recipies.data.handler.RecipeDataHandlerNetwork;
import com.nis.frameworkapp.recipies.data.model.RecipeList;


public class RecipeDataRepo {
    public static final String TAG = RecipeDataRepo.class.getSimpleName();
    public static final Object LOCK = new Object();
    private static RecipeDataRepo rInstance;
    private RecipeDataHandlerNetwork recipeDataHandlerNetwork;
    private final AppExecutors mExecutors;

    private RecipeDataRepo(Context context, AppExecutors executors) {
        mExecutors = executors;
        recipeDataHandlerNetwork = new RecipeDataHandlerNetwork(context, executors);
    }

    public synchronized static RecipeDataRepo getInstance(Context context, AppExecutors
            executors) {
        if (rInstance == null) {
            synchronized (LOCK) {
                rInstance = new RecipeDataRepo(context, executors);
            }
        }
        return rInstance;
    }

    public LiveData<ResultStatus<RecipeList>> getRecipeList() {
        return recipeDataHandlerNetwork.startFetchRecipeService();
    }

}

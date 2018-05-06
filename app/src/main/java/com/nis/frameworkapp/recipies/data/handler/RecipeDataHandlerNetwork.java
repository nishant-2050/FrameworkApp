package com.nis.frameworkapp.recipies.data.handler;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import com.nis.frameworkapp.common.AppExecutors;
import com.nis.frameworkapp.common.AppUtil;
import com.nis.frameworkapp.common.NetworkUtils;
import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.common.ResultStatus.ProgressStatus;
import com.nis.frameworkapp.common.RetrofitClient;
import com.nis.frameworkapp.recipies.data.model.RecipeList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class RecipeDataHandlerNetwork {
    public static final String TAG = RecipeDataHandlerNetwork.class.getSimpleName();
    private final Context mContext;
    private final AppExecutors mExecutors;
    private MutableLiveData<ResultStatus<RecipeList>> recipeListLiveData;
    private ResultStatus resultStatus;

    public static final String RECIPE_LIST_URL_NETWORK = "http://www.recipepuppy.com/api/";
    public static final String RECIPE_LIST_URL_ASSETS = "recipe_list.json";

    public interface RecipeListResultRetroFitService {
        @GET(".")
        Call<RecipeList> getRecipeListData();
    }

    public RecipeDataHandlerNetwork(Context context, AppExecutors executors) {
        this.mContext = context;
        this.mExecutors = executors;
        this.recipeListLiveData = new MutableLiveData<>();
        this.resultStatus = new ResultStatus();
    }

    public MutableLiveData<ResultStatus<RecipeList>> startFetchRecipeService() {
        resultStatus.progressStatus = ProgressStatus.STARTED;
        recipeListLiveData.postValue(resultStatus);
        fetchAndParseRecipeListByRetrofit();
        return recipeListLiveData;
    }

    private void fetchAndParseRecipeListByRetrofit() {
        RecipeListResultRetroFitService retroFitService = RetrofitClient.getClient
                (RECIPE_LIST_URL_NETWORK)
                .create(RecipeListResultRetroFitService.class);
        retroFitService.getRecipeListData().enqueue(new Callback<RecipeList>() {
            @Override
            public void onResponse(@NonNull Call<RecipeList> call, @NonNull Response<RecipeList>
                    response) {
                if (response.isSuccessful()) {
                    resultStatus.progressStatus = ProgressStatus.SUCCESS;
                    resultStatus.result = response.body();
                } else {
                    resultStatus.progressStatus = ProgressStatus.FAILURE;
                    resultStatus.error.errorMessage = "Recipe List Error";
                    resultStatus.error.errorCode = response.code();
                }
                recipeListLiveData.postValue(resultStatus);
            }

            @Override
            public void onFailure(@NonNull Call<RecipeList> call, @NonNull Throwable t) {
                Log.d(TAG, "Service Failure");
                resultStatus.progressStatus = ProgressStatus.FAILURE;
                resultStatus.error.errorMessage = "Recipe List Error";
                recipeListLiveData.postValue(resultStatus);
            }
        });
    }

    /**
     * Optional method to get response from OKHttp
     */
    private void fetchRecipeListFromOkHttp() {
        mExecutors.networkIO().execute(() -> {
            try {
                URL recipeRequestUrl = new URL(RECIPE_LIST_URL_NETWORK);
                String jsonRecipeResponse = NetworkUtils.getResponseByOkHttp(recipeRequestUrl);
                resultStatus.progressStatus = ProgressStatus.SUCCESS;
                resultStatus.result = parseRecipeList(jsonRecipeResponse);
            } catch (IOException | JSONException e) {
                Log.e(TAG, "Networking or parsing error for fetch recipe list");
                resultStatus.progressStatus = ProgressStatus.FAILURE;
                resultStatus.error.errorMessage = "Recipe List NW Error";
            }
            recipeListLiveData.postValue(resultStatus);
        });
    }

    private RecipeList parseRecipeList(final String response) throws JSONException {
        Gson gson = new Gson();
        RecipeList recipeList = gson.fromJson(response, RecipeList.class);
        return recipeList;
    }

    /**
     * Read file from Assets and parse
     */

    private void fetchRecipeListFromAssets() {
        mExecutors.diskIO().execute(() -> {
            try {
                String jsonRecipeResponse = AppUtil.getAssetFileContentAsString(mContext,
                        RECIPE_LIST_URL_ASSETS);
                resultStatus.progressStatus = ProgressStatus.SUCCESS;
                resultStatus.result = parseRecipeList(jsonRecipeResponse);
            } catch (JSONException ae) {
                Log.e(TAG, "Exception is ->" + ae.toString());
                resultStatus.progressStatus = ProgressStatus.FAILURE;
                resultStatus.error.errorMessage = "Recipe List Error from Assets";
            }
            recipeListLiveData.postValue(resultStatus);
        });
    }
}

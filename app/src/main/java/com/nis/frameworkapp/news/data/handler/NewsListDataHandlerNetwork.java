package com.nis.frameworkapp.news.data.handler;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;

import com.nis.frameworkapp.common.AppExecutors;
import com.nis.frameworkapp.common.AppUtil;
import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.common.ResultStatus.ProgressStatus;
import com.nis.frameworkapp.news.data.model.NewsList;

public class NewsListDataHandlerNetwork {
    public static final String TAG = NewsListDataHandlerNetwork.class.getSimpleName();
    private final Context mContext;
    private final AppExecutors mExecutors;
    private final MutableLiveData<ResultStatus<NewsList>> newsListLiveData;
    private ResultStatus resultStatus;

    public static final String NEWS_LIST_URL_ASSETS = "news_list.json";

    public NewsListDataHandlerNetwork(Context context, AppExecutors appExecutors) {
        this.mContext = context;
        this.mExecutors = appExecutors;
        this.newsListLiveData = new MutableLiveData<>();
        this.resultStatus = new ResultStatus();
    }

    public MutableLiveData<ResultStatus<NewsList>> startNewsService() {
        resultStatus.progressStatus = ProgressStatus.STARTED;
        newsListLiveData.setValue(resultStatus);
        fetchNresListFromNetwork();
        return newsListLiveData;
    }

    private void fetchNresListFromNetwork() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //instead of getReference("news"), we are asking for full db snapshot
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    resultStatus.progressStatus = ProgressStatus.SUCCESS;
                    resultStatus.result = dataSnapshot.getValue(NewsList.class);
                } else {
                    resultStatus.progressStatus = ProgressStatus.FAILURE;
                    resultStatus.error.errorMessage = "News List Error from Firebase";
                }
                newsListLiveData.setValue(resultStatus);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                resultStatus.progressStatus = ProgressStatus.FAILURE;
                resultStatus.error.errorMessage = "News List Error from Firebase";
                newsListLiveData.setValue(resultStatus);
            }
        });
    }

    /**
     * Read file from Assets and parse
     */

    private void fetchNewsListFromAssets() {
        mExecutors.diskIO().execute(() -> {
            try {
                String jsonNewsResponse = AppUtil.getAssetFileContentAsString(mContext,
                        NEWS_LIST_URL_ASSETS);
                resultStatus.progressStatus = ProgressStatus.SUCCESS;
                resultStatus.result = parseNewsList(jsonNewsResponse);
            } catch (JSONException ae) {
                Log.e(TAG, "Exception is ->" + ae.toString());
                resultStatus.progressStatus = ProgressStatus.FAILURE;
                resultStatus.error.errorMessage = "News List Error from Assets";
            }
            newsListLiveData.postValue(resultStatus);
        });
    }

    private NewsList parseNewsList(final String response) throws JSONException {
        Gson gson = new Gson();
        NewsList newsList = gson.fromJson(response, NewsList.class);
        return newsList;
    }
}

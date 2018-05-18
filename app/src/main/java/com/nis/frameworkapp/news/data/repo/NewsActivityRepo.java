package com.nis.frameworkapp.news.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;

import com.nis.frameworkapp.common.AppExecutors;
import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.news.data.handler.NewsListDataHandlerNetwork;
import com.nis.frameworkapp.news.data.model.NewsList;

public class NewsActivityRepo {
    public static final String TAG = NewsActivityRepo.class.getSimpleName();
    public static final Object LOCK = new Object();
    private static NewsActivityRepo rInstance;
    private NewsListDataHandlerNetwork newsListDataHandlerNetwork;
    private final AppExecutors mExecutors;

    private NewsActivityRepo(Context context, AppExecutors executors) {
        mExecutors = executors;
        newsListDataHandlerNetwork = new NewsListDataHandlerNetwork(context, executors);
    }

    public synchronized static NewsActivityRepo getInstance(Context context, AppExecutors
            executors) {
        if (rInstance == null) {
            synchronized (LOCK) {
                rInstance = new NewsActivityRepo(context, executors);
            }
        }
        return rInstance;
    }

    public LiveData<ResultStatus<NewsList>> getNewsList() {
        MediatorLiveData<ResultStatus<NewsList>> liveData = new MediatorLiveData<>();
        liveData.addSource(newsListDataHandlerNetwork.startNewsService(), resultStatus -> {
            liveData.setValue(resultStatus);
        });
        return liveData;
    }
}

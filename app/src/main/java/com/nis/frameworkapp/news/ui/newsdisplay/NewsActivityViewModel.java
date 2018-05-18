package com.nis.frameworkapp.news.ui.newsdisplay;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.news.data.model.NewsList;
import com.nis.frameworkapp.news.data.repo.NewsActivityRepo;

public class NewsActivityViewModel extends ViewModel {
    private final NewsActivityRepo mRepository;

    public NewsActivityViewModel(NewsActivityRepo repository) {
        mRepository = repository;
    }


    public LiveData<ResultStatus<NewsList>> getAllNews() {
        MediatorLiveData<ResultStatus<NewsList>> liveData = new MediatorLiveData<>();
        liveData.addSource(mRepository.getNewsList(), resultStatus -> {
            liveData.setValue(resultStatus);
        });
        return liveData;
    }
}
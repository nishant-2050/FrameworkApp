package com.nis.frameworkapp.news.ui.newsdisplay;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.news.data.model.NewsList;
import com.nis.frameworkapp.news.data.repo.NewsActivityRepo;

public class NewsActivityViewModel extends ViewModel {
    private final LiveData<ResultStatus<NewsList>> liveData;
    private final NewsActivityRepo mRepository;

    public NewsActivityViewModel(NewsActivityRepo repository) {
        mRepository = repository;
        liveData = mRepository.getNewsList();
    }


    public LiveData<ResultStatus<NewsList>> getAllNews() {
        return liveData;
    }
}
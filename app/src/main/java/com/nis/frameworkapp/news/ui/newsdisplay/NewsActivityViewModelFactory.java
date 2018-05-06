package com.nis.frameworkapp.news.ui.newsdisplay;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.nis.frameworkapp.news.data.repo.NewsActivityRepo;

public class NewsActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final NewsActivityRepo newsActivityRepo;

    public NewsActivityViewModelFactory(NewsActivityRepo newsActivityRepo) {
        this.newsActivityRepo = newsActivityRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new NewsActivityViewModel(newsActivityRepo);
    }
}
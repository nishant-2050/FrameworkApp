package com.nis.frameworkapp.news.ui.login;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.nis.frameworkapp.news.data.repo.LoginActivityRepo;

public class LoginActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private final LoginActivityRepo loginActivityRepo;

    public LoginActivityViewModelFactory(LoginActivityRepo loginActivityRepo) {
        this.loginActivityRepo = loginActivityRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new LoginActivityViewModel(loginActivityRepo);
    }
}

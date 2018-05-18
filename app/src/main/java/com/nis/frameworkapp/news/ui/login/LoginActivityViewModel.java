package com.nis.frameworkapp.news.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.news.data.repo.LoginActivityRepo;

public class LoginActivityViewModel extends ViewModel{
    private final LoginActivityRepo mLoginActivityRepo;

    public LoginActivityViewModel(LoginActivityRepo loginActivityRepo){
        mLoginActivityRepo = loginActivityRepo;
    }

    public LiveData<ResultStatus<FirebaseUser>> doLogin(String email, String password){
        MediatorLiveData<ResultStatus<FirebaseUser>> liveDataLogin = new MediatorLiveData<>();
        liveDataLogin.addSource(mLoginActivityRepo.doLogin(email, password), resultStatus -> {
            liveDataLogin.setValue(resultStatus);
        });
        return liveDataLogin;
    }

    public LiveData<ResultStatus<FirebaseUser>> addUser(String email, String password){
        MediatorLiveData<ResultStatus<FirebaseUser>> liveDataRegister = new MediatorLiveData<>();
        liveDataRegister.addSource(mLoginActivityRepo.addUser(email, password), resultStatus -> {
            liveDataRegister.setValue(resultStatus);
        });
        return liveDataRegister;
    }
}

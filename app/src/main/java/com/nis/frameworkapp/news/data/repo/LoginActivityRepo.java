package com.nis.frameworkapp.news.data.repo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.news.data.handler.LoginDataHandlerNetwork;

public class LoginActivityRepo {
    public static final String TAG = LoginActivityRepo.class.getSimpleName();
    public static final Object LOCK = new Object();
    private static LoginActivityRepo rInstance;
    private LoginDataHandlerNetwork loginDataHandlerNetwork;

    private LoginActivityRepo(Context context) {
        loginDataHandlerNetwork = new LoginDataHandlerNetwork(context);
    }

    public synchronized static LoginActivityRepo getInstance(Context context) {
        if (rInstance == null) {
            synchronized (LOCK) {
                rInstance = new LoginActivityRepo(context);
            }
        }
        return rInstance;
    }

    public LiveData<ResultStatus<FirebaseUser>> doLogin(String email, String password) {
        MediatorLiveData<ResultStatus<FirebaseUser>> liveDataLogin = new MediatorLiveData<>();
        liveDataLogin.addSource(loginDataHandlerNetwork.doLogin(email, password), resultStatus -> {
            liveDataLogin.setValue(resultStatus);
        });
        return liveDataLogin;
    }

    public LiveData<ResultStatus<FirebaseUser>> addUser(String email, String password) {
        MediatorLiveData<ResultStatus<FirebaseUser>> liveDataRegister = new MediatorLiveData<>();
        liveDataRegister.addSource(loginDataHandlerNetwork.addUser(email, password), resultStatus -> {
            liveDataRegister.setValue(resultStatus);
        });
        return liveDataRegister;
    }
}

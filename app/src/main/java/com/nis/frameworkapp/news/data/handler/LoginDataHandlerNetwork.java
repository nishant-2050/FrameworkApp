package com.nis.frameworkapp.news.data.handler;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.common.ResultStatus.ProgressStatus;

public class LoginDataHandlerNetwork {
    public static final String TAG = LoginDataHandlerNetwork.class.getSimpleName();
    private MutableLiveData<ResultStatus<FirebaseUser>> loginResultLive;
    private MutableLiveData<ResultStatus<FirebaseUser>> registrationResultLive;
    private ResultStatus resultStatus;

    public LoginDataHandlerNetwork(Context context) {
        this.loginResultLive = new MutableLiveData<>();
        this.registrationResultLive = new MutableLiveData<>();
        this.resultStatus = new ResultStatus();
    }

    public MutableLiveData<ResultStatus<FirebaseUser>> doLogin(String email, String password) {
        resultStatus.progressStatus = ProgressStatus.STARTED;
        loginResultLive.setValue(resultStatus);
        doFirebaseLogin(email, password);
        return loginResultLive;
    }

    public MutableLiveData<ResultStatus<FirebaseUser>> addUser(String email, String password) {
        resultStatus.progressStatus = ProgressStatus.STARTED;
        registrationResultLive.setValue(resultStatus);
        addFirebaseUser(email, password);
        return registrationResultLive;
    }

    private void doFirebaseLogin(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                resultStatus.progressStatus = ProgressStatus.SUCCESS;
                resultStatus.result = task.getResult().getUser();
            } else {
                resultStatus.progressStatus = ProgressStatus.FAILURE;
                resultStatus.error.errorMessage = "Error during Login";
            }
            loginResultLive.setValue(resultStatus);
        });
    }

    private void addFirebaseUser(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                resultStatus.progressStatus = ProgressStatus.SUCCESS;
                resultStatus.result = task.getResult().getUser();
            } else {
                resultStatus.progressStatus = ProgressStatus.FAILURE;
                resultStatus.error.errorMessage = "Error during registration";
            }
            loginResultLive.setValue(resultStatus);
        });
    }
}

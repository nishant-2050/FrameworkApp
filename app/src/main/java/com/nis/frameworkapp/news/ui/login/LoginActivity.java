package com.nis.frameworkapp.news.ui.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import com.nis.frameworkapp.R;
import com.nis.frameworkapp.common.BaseActivity;
import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.common.ResultStatus.ProgressStatus;
import com.nis.frameworkapp.news.data.repo.LoginActivityRepo;
import com.nis.frameworkapp.news.ui.newsdisplay.NewsActivity;

public class LoginActivity extends BaseActivity{
    public static final String TAG = LoginActivity.class.getSimpleName();
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private LoginActivityViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.news_login, true, false);
        initView();
    }

    private void initView(){
        removeHomeIcon();
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((TextView textView, int id, KeyEvent keyEvent)->{
            if(id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL){
                doLogin();
                return true;
            }
            return false;
        });
        Button signinButton = findViewById(R.id.sign_in_button);
        signinButton.setOnClickListener((View view)->{
            doLogin();
        });
        Button registerButton = findViewById(R.id.register_in_button);
        registerButton.setOnClickListener((View view)->{
            doRegister();
        });
        mProgressView = findViewById(R.id.login_progress);
        LoginActivityRepo loginActivityRepo = LoginActivityRepo.getInstance(this);
        LoginActivityViewModelFactory modelFactory = new LoginActivityViewModelFactory(loginActivityRepo);
        mViewModel = ViewModelProviders.of(this, modelFactory).get(LoginActivityViewModel.class);
    }

    private void handleDataStateChanges(ResultStatus resultStatus){
        ProgressStatus progressStatus = resultStatus.progressStatus;
        switch (progressStatus){
            case STARTED:
                showProgress(mProgressView, true);
                break;
            case SUCCESS:
                showProgress(mProgressView, false);
                startActivity(new Intent(this, NewsActivity.class));
                break;
            case FAILURE:
                showProgress(mProgressView, false);
                Toast.makeText(this, resultStatus.error.errorMessage, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void doLogin(){
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if(!isInputInvalid(email, password)){
            mViewModel.doLogin(email, password).observe(this, (ResultStatus<FirebaseUser> resultStatus)->{
                handleDataStateChanges(resultStatus);
            });
        }
    }

    private void doRegister(){
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if(isInputInvalid(email, password)){
            mViewModel.addUser(email, password).observe(this, (ResultStatus<FirebaseUser> resultStatus)->{
                handleDataStateChanges(resultStatus);
            });
        }
    }

    private boolean isInputInvalid(String email, String password){
        boolean cancel = false;
        View focusView = null;
        if(TextUtils.isEmpty(email)){
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }else if(!isEmailValid(email)){
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }else if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }else if(!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if(cancel){
            focusView.requestFocus();
        }
        return cancel;
    }

    private boolean isEmailValid(String email){
        return email.contains("@");
    }

    private boolean isPasswordValid(String password){
        return password.length() > 4;
    }


}

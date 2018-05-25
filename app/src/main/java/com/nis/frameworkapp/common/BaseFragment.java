package com.nis.frameworkapp.common;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
    BaseActivity baseActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) getActivity();
    }

    protected View setupView(int rootLayout, ViewGroup container) {
        View view = getLayoutInflater().inflate(rootLayout, container, false);
        baseActivity.setupContent(view, getHamburgerMenuType());
        return view;
    }

    protected BaseActivity.HamburgerMenuType getHamburgerMenuType(){
        return BaseActivity.HamburgerMenuType.NONE;
    }

    protected void showProgress(View progressView, boolean show) {
        baseActivity.showProgress(progressView, show);
    }
}

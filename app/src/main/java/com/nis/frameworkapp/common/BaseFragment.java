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

    protected View setupView(int rootLayout, ViewGroup container, boolean
            isLockDrawer,
                             boolean isBackButtonEnabled) {
        View view = getLayoutInflater().inflate(rootLayout, container, false);
        if (isLockDrawer) {
            baseActivity.lockDrawer();
        } else {
            baseActivity.unlockDrawer();
        }
        baseActivity.setupContent(view);
        if (isBackButtonEnabled) {
            baseActivity.setBackButtonToolbar();
        } else {
            baseActivity.setHomeButtonToolbar();
        }
        return view;
    }

    protected void showProgress(View progressView, boolean show) {
        baseActivity.showProgress(progressView, show);
    }
}

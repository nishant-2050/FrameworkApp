package com.nis.frameworkapp.news.ui.newsdisplay;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nis.frameworkapp.R;
import com.nis.frameworkapp.common.BaseActivity;
import com.nis.frameworkapp.news.ui.newsdisplay.detail.NewsDetailFragment;
import com.nis.frameworkapp.news.ui.newsdisplay.list.NewsListFragment;

public class NewsActivity extends BaseActivity implements NewsListFragment
        .NewsListActionListener {
    public static final String TAG = NewsActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        showNewsListScreen();
    }

    private void showNewsListScreen() {
        NewsListFragment listFragment = new NewsListFragment();
        showFragment(listFragment, R.id.id_container, null, false,
                "NewsListFragment");
    }

    @Override
    public void showNewsDetailScreen(int selectedItem) {
        NewsDetailFragment detailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("itemnum", selectedItem);
        showFragment(detailFragment, R.id.id_container, bundle, true,
                "NewsDetailFragment");
    }
}

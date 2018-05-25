package com.nis.frameworkapp.news.ui.newsdisplay.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.nis.frameworkapp.R;
import com.nis.frameworkapp.common.AppExecutors;
import com.nis.frameworkapp.common.BaseActivity;
import com.nis.frameworkapp.common.BaseFragment;
import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.news.data.model.News;
import com.nis.frameworkapp.news.data.model.NewsList;
import com.nis.frameworkapp.news.data.repo.NewsActivityRepo;
import com.nis.frameworkapp.news.ui.newsdisplay.NewsActivityViewModel;
import com.nis.frameworkapp.news.ui.newsdisplay.NewsActivityViewModelFactory;

public class NewsDetailFragment extends BaseFragment {
    public static final String TAG = NewsDetailFragment.class.getSimpleName();
    private ProgressBar mProgressView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = setupView(R.layout.news_detail, container);
        initView(view);
        return view;
    }

    @Override
    protected BaseActivity.HamburgerMenuType getHamburgerMenuType() {
        return BaseActivity.HamburgerMenuType.BACK_ARROW;
    }

    protected void initView(View view) {
        Bundle bundle = getArguments();
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id
                .collapsing_toolbar);

        NewsActivityRepo repository = NewsActivityRepo.getInstance(getContext(), AppExecutors
                .getsInstance());
        NewsActivityViewModelFactory modelFactory = new NewsActivityViewModelFactory
                (repository);
        NewsActivityViewModel viewModel = ViewModelProviders.of(getActivity(), modelFactory)
                .get(NewsActivityViewModel.class);

        ImageView imageView = view.findViewById(R.id.backdrop);
        mProgressView = view.findViewById(R.id.pb_loading_indicator);
        showProgress(mProgressView, true);
        WebView webView = view.findViewById(R.id.id_webview);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                showProgress(mProgressView, false);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);

        viewModel.getAllNews().observe(this, (ResultStatus<NewsList> resultStatus)->{
            NewsList newsList = resultStatus.result;
            News selectedNews = newsList.news.get(bundle.getInt("itemnum", -1));
            collapsingToolbarLayout.setTitle(selectedNews.title);
            Glide.with(getActivity())
                    .load(selectedNews.thumbnail)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView);
            webView.loadUrl(selectedNews.link);
        });
    }
}

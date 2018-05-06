package com.nis.frameworkapp.news.ui.newsdisplay.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nis.frameworkapp.R;
import com.nis.frameworkapp.common.AppExecutors;
import com.nis.frameworkapp.common.BaseFragment;
import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.news.data.model.NewsList;
import com.nis.frameworkapp.news.data.repo.NewsActivityRepo;
import com.nis.frameworkapp.news.ui.newsdisplay.NewsActivityViewModel;
import com.nis.frameworkapp.news.ui.newsdisplay.NewsActivityViewModelFactory;

public class NewsListFragment extends BaseFragment implements
        NewsListAdapter.NewsAdapterOnItemClickHandler {


    public static final String TAG = NewsListFragment.class.getSimpleName();

    private NewsListAdapter mNewsListAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressView;
    private NewsActivityViewModel mViewModel;
    private NewsListActionListener listActionListener;

    public interface NewsListActionListener {
        void showNewsDetailScreen(int selectedItem);
    }

    @Override
    public void onItemClick(int selectedItem) {
        listActionListener.showNewsDetailScreen(selectedItem);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = setupView(R.layout.container_fixed, R.layout.news_list_content, container,
                false, false);
        initView(view);
        return view;
    }

    protected void initView(View view) {
        Context context = getActivity();
        mProgressView = view.findViewById(R.id.pb_loading_indicator);

        //setup RecyclerView
        mRecyclerView = view.findViewById(R.id.recyclerview_newslist);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);//improves layout performance
        mNewsListAdapter = new NewsListAdapter(context, this);
        mRecyclerView.setAdapter(mNewsListAdapter);

        //feed data to RecyclerView
        NewsActivityRepo repository = NewsActivityRepo.getInstance(context, AppExecutors
                .getsInstance());
        NewsActivityViewModelFactory modelFactory = new NewsActivityViewModelFactory
                (repository);
        mViewModel = ViewModelProviders.of(getActivity(), modelFactory).get
                (NewsActivityViewModel.class);
        mViewModel.getAllNews().observe(this, (ResultStatus<NewsList> resultStatus) -> {
            handleDataStateChanges(resultStatus);
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewsListActionListener) {
            listActionListener = (NewsListActionListener) context;
        } else {
            throw new ClassCastException("Activity must implement NewsListActionListener");
        }
    }

    private void handleDataStateChanges(ResultStatus<NewsList> resultStatus) {
        ResultStatus.ProgressStatus progressStatus = resultStatus.progressStatus;
        switch (progressStatus) {
            case STARTED:
                showProgress(mProgressView, true);
                break;
            case SUCCESS:
                showProgress(mProgressView, false);
                mNewsListAdapter.swapNewsList(resultStatus.result);
                break;
            case FAILURE:
                showProgress(mProgressView, false);
                Toast.makeText(getContext(), resultStatus.error.errorMessage, Toast.LENGTH_SHORT)
                        .show();
                break;
        }
    }
}
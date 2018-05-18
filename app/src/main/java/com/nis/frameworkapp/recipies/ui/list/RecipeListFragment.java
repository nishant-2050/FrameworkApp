package com.nis.frameworkapp.recipies.ui.list;

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
import com.nis.frameworkapp.recipies.data.model.Recipe;
import com.nis.frameworkapp.recipies.data.model.RecipeList;
import com.nis.frameworkapp.recipies.data.repo.RecipeDataRepo;
import com.nis.frameworkapp.recipies.ui.RecipeActivityViewModel;
import com.nis.frameworkapp.recipies.ui.RecipeActivityViewModelFactory;

public class RecipeListFragment extends BaseFragment implements
        RecipeListAdapter.RecipeAdapterOnItemClickHandler {


    public static final String TAG = RecipeListFragment.class.getSimpleName();

    private RecipeListAdapter mRecipeListAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressView;
    private RecipeActivityViewModel mViewModel;
    private RecipeListActionListener listActionListener;

    public interface RecipeListActionListener {
        void showRecipeDetailScreen(int selectedItem);
    }

    @Override
    public void onItemClick(Recipe selectedItem) {
        listActionListener.showRecipeDetailScreen(selectedItem.position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = setupView(R.layout.container_fixed, R.layout.recipe_list_content, container,
                false, false);
        initView(view);
        return view;
    }

    protected void initView(View view) {
        Context context = getActivity();
        mProgressView = view.findViewById(R.id.pb_loading_indicator);

        //setup RecyclerView
        mRecyclerView = view.findViewById(R.id.recyclerview_recipelist);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);//improves layout performance
        mRecipeListAdapter = new RecipeListAdapter(context, this);
        mRecyclerView.setAdapter(mRecipeListAdapter);

        //feed data to RecyclerView
        RecipeDataRepo repository = RecipeDataRepo.getInstance(context, AppExecutors
                .getsInstance());
        RecipeActivityViewModelFactory modelFactory = new RecipeActivityViewModelFactory
                (repository);
        mViewModel = ViewModelProviders.of(getActivity(), modelFactory).get
                (RecipeActivityViewModel.class);
        mViewModel.getAllRecipies().observe(this, (ResultStatus<RecipeList> resultStatus) -> {
            handleDataStateChanges(resultStatus);
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeListActionListener) {
            listActionListener = (RecipeListActionListener) context;
        } else {
            throw new ClassCastException("Activity must implement RecipeListActionListener");
        }
    }

    private void handleDataStateChanges(ResultStatus<RecipeList> resultStatus) {
        ResultStatus.ProgressStatus progressStatus = resultStatus.progressStatus;
        switch (progressStatus) {
            case STARTED:
                showProgress(mProgressView, true);
                break;
            case SUCCESS:
                showProgress(mProgressView, false);
                mRecipeListAdapter.swapRecipeList(resultStatus.result);
                break;
            case FAILURE:
                showProgress(mProgressView, false);
                Toast.makeText(getContext(), resultStatus.error.errorMessage, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

package com.nis.frameworkapp.recipies.ui.detail;

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
import com.nis.frameworkapp.common.BaseFragment;
import com.nis.frameworkapp.common.ResultStatus;
import com.nis.frameworkapp.recipies.data.model.Recipe;
import com.nis.frameworkapp.recipies.data.model.RecipeList;
import com.nis.frameworkapp.recipies.data.repo.RecipeDataRepo;
import com.nis.frameworkapp.recipies.ui.RecipeActivityViewModel;
import com.nis.frameworkapp.recipies.ui.RecipeActivityViewModelFactory;

public class RecipeDetailFragment extends BaseFragment {
    public static final String TAG = RecipeDetailFragment.class.getSimpleName();
    private ProgressBar mProgressView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = setupView(R.layout.recipe_detail,
                container, true, true);
        initView(view);
        return view;
    }

    protected void initView(View view) {
        Bundle bundle = getArguments();
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id
                .collapsing_toolbar);

        RecipeDataRepo repository = RecipeDataRepo.getInstance(getContext(), AppExecutors
                .getsInstance());
        RecipeActivityViewModelFactory modelFactory = new RecipeActivityViewModelFactory
                (repository);
        RecipeActivityViewModel viewModel = ViewModelProviders.of(getActivity(), modelFactory)
                .get(RecipeActivityViewModel.class);

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

        viewModel.getAllRecipies().observe(this, (ResultStatus<RecipeList> resultStatus)->{
            RecipeList recipeList = resultStatus.result;
            Recipe selectedRecipe = recipeList.recipes.get(bundle.getInt("itemnum", -1));
            collapsingToolbarLayout.setTitle(selectedRecipe.title);
            Glide.with(getActivity())
                    .load(selectedRecipe.thumbnail)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView);
            webView.loadUrl(selectedRecipe.link);
        });
    }
}

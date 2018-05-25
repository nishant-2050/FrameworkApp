package com.nis.frameworkapp.recipies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nis.frameworkapp.R;
import com.nis.frameworkapp.common.BaseActivity;
import com.nis.frameworkapp.recipies.ui.detail.RecipeDetailFragment;
import com.nis.frameworkapp.recipies.ui.list.RecipeListFragment;

public class RecipeActivity extends BaseActivity implements RecipeListFragment
        .RecipeListActionListener {
    public static final String TAG = RecipeActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        showRecipeListScreen();
    }

    private void showRecipeListScreen() {
        RecipeListFragment listFragment = new RecipeListFragment();
        showFragment(listFragment, R.id.id_container, null, false,
                "NewsListFragment");
    }

    @Override
    public void showRecipeDetailScreen(int selectedItem) {
        RecipeDetailFragment detailFragment = new RecipeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("itemnum", selectedItem);
        showFragment(detailFragment, R.id.id_container, bundle, true,
                "NewsDetailFragment");
    }
}

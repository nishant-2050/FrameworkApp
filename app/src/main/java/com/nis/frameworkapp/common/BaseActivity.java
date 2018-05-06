package com.nis.frameworkapp.common;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nis.frameworkapp.R;


/**
 * Base Activity class who will hold root view. It can be used to setup navigation pane, tool
 * bar, theme changes, show fragment,
 * show progress dialog, log activity life cycles
 */
public class BaseActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFabButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        setupNavPanel();
    }

    private void setupNavPanel() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener((MenuItem menuItem) -> {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            });
        }
    }

    protected void lockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    protected void unlockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    protected void showFab() {
        mFabButton.show();
    }

    protected void hideFab() {
        mFabButton.hide();
    }

    protected void setupView(int rootLayout, int sourceLayout, boolean isLockDrawer, boolean
            isBackButtonEnabled) {
        ViewGroup root = findViewById(R.id.id_container);
        View view = getLayoutInflater().inflate(rootLayout, root, true);
        setupContent(view, sourceLayout);
        if (isLockDrawer) {
            lockDrawer();
        } else {
            unlockDrawer();
        }
        //setupContent(view, sourceLayout);
        if (isBackButtonEnabled) {
            setBackButtonToolbar();
        } else {
            setHomeButtonToolbar();
        }
    }

    protected void setupContent(View view, int layoutID) {
        ViewGroup vg = view.findViewById(R.id.id_placeholder_content);
        vg.removeAllViews();
        getLayoutInflater().inflate(layoutID, vg, true);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFabButton = view.findViewById(R.id.fab);
        hideFab();
    }

    protected void setHomeButtonToolbar() {
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    protected void setBackButtonToolbar() {
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    protected void removeHomeIcon() {
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() <= 0) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    getSupportFragmentManager().popBackStack();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showFragment(Fragment fragment, int containerID, Bundle args,
                                boolean shouldAddToBackStack, String tag) {
        if (args != null) {
            fragment.setArguments(args);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, fragment);
        if (shouldAddToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    protected void showProgress(View progressView, boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

}

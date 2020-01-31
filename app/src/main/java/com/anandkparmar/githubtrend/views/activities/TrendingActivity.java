package com.anandkparmar.githubtrend.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anandkparmar.githubtrend.R;
import com.anandkparmar.githubtrend.viewmodels.TrendingViewModel;
import com.anandkparmar.githubtrend.viewmodels.TrendingViewModelFactory;
import com.anandkparmar.githubtrend.views.adapters.RepositoryListAdapter;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class TrendingActivity extends DaggerAppCompatActivity {
    private static final String TAG = "TrendingActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.shimmer_recycler_view)
    ShimmerRecyclerView shimmerRecyclerView;

    @BindView(R.id.retry_layout_id)
    LinearLayout retryLayout;

    @BindView(R.id.repository_list_view)
    ExpandableListView repositoryListView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    TrendingViewModelFactory viewModelFactory;

    private TrendingViewModel viewModel;
    private RepositoryListAdapter repositoryAdapter;
    private LifecycleRegistry lifecycleRegistry;
    private boolean isRefreshing = false;
    private int lastExpandedPosition = -1;

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        if (lifecycleRegistry == null) {
            lifecycleRegistry = new LifecycleRegistry(this);
        }
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initialize();
        setupSwipeRefreshLayout();
        setupRepositoryListView();
        setupViewModel();
    }

    private void initialize() {
        showShimmerLayout();
    }

    void setupSwipeRefreshLayout() {
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            isRefreshing = true;
            listenForDataChange(true);
        });
    }

    void setupRepositoryListView() {
        repositoryAdapter = new RepositoryListAdapter(this, new ArrayList<>());
        repositoryListView.setAdapter(repositoryAdapter);
        repositoryListView.setOnGroupExpandListener(groupPosition -> {
            if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                repositoryListView.collapseGroup(lastExpandedPosition);
            }
            lastExpandedPosition = groupPosition;
        });
    }

    void setupViewModel() {
        this.viewModel = viewModelFactory.create(TrendingViewModel.class);
        this.listenForDataRefresh();
        this.listenForDataChange(false);
    }

    private void listenForDataRefresh() {
        this.viewModel.isFetchingFromServer().observe(this, aBoolean -> {
            isRefreshing =  aBoolean;
        });
    }

    void listenForDataChange(boolean shouldFetchFromRemote) {
        this.viewModel.getTrendingRepositories(this.getApplicationContext(), shouldFetchFromRemote).observe(this, responseList -> {
            if (isRefreshing) {
                if (responseList != null && responseList.size() > 0) {
                    repositoryAdapter.updateData(responseList);
                    showRepositoryListLayout();
                } else {
                    showShimmerLayout();
                }
            } else {
                if (responseList == null) {
                    repositoryAdapter.updateData(new ArrayList<>());
                    showRetryLayout();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    repositoryAdapter.updateData(responseList);
                    showRepositoryListLayout();
                }
            }
        });
    }

    private void showShimmerLayout() {
        shimmerRecyclerView.setVisibility(View.VISIBLE);
        shimmerRecyclerView.showShimmerAdapter();

        retryLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    private void showRetryLayout() {
        retryLayout.setVisibility(View.VISIBLE);

        shimmerRecyclerView.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    private void showRepositoryListLayout() {
        swipeRefreshLayout.setVisibility(View.VISIBLE);

        shimmerRecyclerView.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.retry_button)
    void onRetryButtonClicked() {
        this.isRefreshing = true;
        this.listenForDataChange(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trending, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_stars:
                repositoryAdapter.sortData(true);
                break;
            case R.id.sort_by_name:
                repositoryAdapter.sortData(false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

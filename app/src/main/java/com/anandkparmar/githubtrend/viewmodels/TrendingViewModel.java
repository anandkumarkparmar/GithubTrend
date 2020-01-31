package com.anandkparmar.githubtrend.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.anandkparmar.githubtrend.network.responses.TrendingRepositoriesResponse;
import com.anandkparmar.githubtrend.repository.GithubRepository;

import java.util.List;

public class TrendingViewModel extends ViewModel {

    private GithubRepository githubRepository;

    public TrendingViewModel(GithubRepository githubRepository) {
        this.githubRepository = githubRepository;
    }

    public LiveData<List<TrendingRepositoriesResponse>> getTrendingRepositories(Context context, boolean shouldFetchFromRemote) {
        return this.githubRepository.getTrendingRepositories(context, shouldFetchFromRemote);
    }

    public LiveData<Boolean> isFetchingFromServer() {
        return this.githubRepository.getIsFetchingFromServer();
    }
}

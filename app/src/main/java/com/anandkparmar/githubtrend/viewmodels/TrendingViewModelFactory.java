package com.anandkparmar.githubtrend.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.anandkparmar.githubtrend.repository.GithubRepository;

import javax.inject.Inject;

public class TrendingViewModelFactory implements ViewModelProvider.Factory {

    @Inject
    GithubRepository githubRepository;

    public TrendingViewModelFactory(GithubRepository githubRepository) {
        this.githubRepository = githubRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TrendingViewModel.class)) {
            return (T) new TrendingViewModel(githubRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}

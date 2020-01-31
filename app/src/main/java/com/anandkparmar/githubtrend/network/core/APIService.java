package com.anandkparmar.githubtrend.network.core;

import com.anandkparmar.githubtrend.network.responses.TrendingRepositoriesResponse;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class APIService {
    private API apiInterface;

    public APIService(API apiInterface) {
        this.apiInterface = apiInterface;
    }

    public void getTrendingRepositories(Observer<List<TrendingRepositoriesResponse>> observer) {
        apiInterface.getTrendingRepositories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}

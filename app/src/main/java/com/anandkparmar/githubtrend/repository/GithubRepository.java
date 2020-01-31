package com.anandkparmar.githubtrend.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.anandkparmar.githubtrend.network.core.APIService;
import com.anandkparmar.githubtrend.network.responses.TrendingRepositoriesResponse;
import com.anandkparmar.githubtrend.utils.AppConstants;
import com.anandkparmar.githubtrend.utils.SharedPrefUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class GithubRepository {
    private static final String TAG = "GithubRepository";

    private APIService apiService;

    private MutableLiveData<List<TrendingRepositoriesResponse>> trendingRepositoriesList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFetchingFromServer = new MutableLiveData<>();

    public GithubRepository(APIService apiService) {
        this.apiService = apiService;
        isFetchingFromServer.postValue(false);
    }

    public LiveData<Boolean> getIsFetchingFromServer() {
        return isFetchingFromServer;
    }

    public LiveData<List<TrendingRepositoriesResponse>> getTrendingRepositories(Context context, boolean shouldFetchFromRemote) {
        long lastTimestamp = SharedPrefUtil.getLastDataSavedTimeStamp(context);
        String dataString = SharedPrefUtil.getTrendGithubRepository(context);

        if ((System.currentTimeMillis() - lastTimestamp < AppConstants.DATA_EXPIRY_DURATION_IN_MILLIS) && !dataString.isEmpty() && !shouldFetchFromRemote) {
            isFetchingFromServer.postValue(false);
            List<TrendingRepositoriesResponse> data = new Gson().fromJson(dataString, new TypeToken<List<TrendingRepositoriesResponse>>(){}.getType());
            trendingRepositoriesList.postValue(data);
        } else {
            isFetchingFromServer.postValue(true);
            apiService.getTrendingRepositories(new Observer<List<TrendingRepositoriesResponse>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.d(TAG,"Subscribed");
                }

                @Override
                public void onNext(List<TrendingRepositoriesResponse> trendingRepositoriesResponse) {
                    Log.d(TAG,"Get Result - " + new Gson().toJson(trendingRepositoriesResponse));
                    isFetchingFromServer.postValue(false);
                    List<TrendingRepositoriesResponse> data = trendingRepositoriesResponse;
                    updateSharedPreferences(context, trendingRepositoriesResponse);
                    trendingRepositoriesList.postValue(data);
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG,"Error", e);
                    isFetchingFromServer.postValue(false);
                    trendingRepositoriesList.postValue(null);
                }

                @Override
                public void onComplete() {
                    Log.d(TAG,"Complete");
                }
            });
        }

        return trendingRepositoriesList;
    }

    private void updateSharedPreferences(Context context, List<TrendingRepositoriesResponse> responseList) {
        SharedPrefUtil.setLastDataSavedTimeStamp(context, System.currentTimeMillis());
        SharedPrefUtil.setTrendGithubRepository(context, new Gson().toJson(responseList));
    }
}

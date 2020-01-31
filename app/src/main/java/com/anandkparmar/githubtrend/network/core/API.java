package com.anandkparmar.githubtrend.network.core;

import com.anandkparmar.githubtrend.network.responses.TrendingRepositoriesResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

import static com.anandkparmar.githubtrend.utils.NetworkConstants.*;

public interface API {

    @GET(GET_REPOSITORIES_END_POINT)
    Observable<List<TrendingRepositoriesResponse>> getTrendingRepositories();
}

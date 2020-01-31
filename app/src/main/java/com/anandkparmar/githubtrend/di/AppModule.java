package com.anandkparmar.githubtrend.di;

import com.anandkparmar.githubtrend.network.core.API;
import com.anandkparmar.githubtrend.network.core.APIService;
import com.anandkparmar.githubtrend.repository.GithubRepository;
import com.anandkparmar.githubtrend.utils.NetworkConstants;
import com.anandkparmar.githubtrend.viewmodels.TrendingViewModelFactory;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    public OkHttpClient provideHttpClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder()
                .connectTimeout(NetworkConstants.CONNECTION_TIMEOUT_IN_MINS, TimeUnit.MINUTES)
                .readTimeout(NetworkConstants.READ_TIMEOUT_IN_MINS, TimeUnit.MINUTES)
                .writeTimeout(NetworkConstants.WRITE_TIMEOUT_IN_MINS, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false);

        if (NetworkConstants.IS_LOGGING_ENABLE) {
            okHttpClientBuilder
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        return okHttpClientBuilder.build();
    }

    @Singleton
    @Provides
    public Converter.Factory provideConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Singleton
    @Provides
    public CallAdapter.Factory provideAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient httpClient, Converter.Factory converterFactory, CallAdapter.Factory adapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(NetworkConstants.BASE_URL)
                .client(httpClient)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(adapterFactory)
                .build();
    }

    @Singleton
    @Provides
    public API provideNetworkInterface(Retrofit retrofit) {
        return retrofit.create(API.class);
    }

    @Singleton
    @Provides
    public APIService provideNetworkService(API api) {
        return new APIService(api);
    }

    @Singleton
    @Provides
    public GithubRepository provideGithubRepository(APIService apiService) {
        return new GithubRepository(apiService);
    }

    @Singleton
    @Provides
    public TrendingViewModelFactory provideTrendingViewModelFactory(GithubRepository repository) {
        return new TrendingViewModelFactory(repository);
    }
}

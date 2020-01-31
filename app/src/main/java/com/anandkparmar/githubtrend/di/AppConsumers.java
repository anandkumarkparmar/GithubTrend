package com.anandkparmar.githubtrend.di;

import com.anandkparmar.githubtrend.views.activities.TrendingActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AppConsumers {

    @ContributesAndroidInjector
    abstract TrendingActivity bindTrendingActivity();
}

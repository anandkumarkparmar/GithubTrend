package com.anandkparmar.githubtrend.di;

import com.anandkparmar.githubtrend.GithubTrendApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, AppConsumers.class})
public interface AppComponent extends AndroidInjector<GithubTrendApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder appication(GithubTrendApplication application);

        AppComponent build();
    }
}

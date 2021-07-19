package com.priceline.app.di.module;

import android.content.Context;
import android.content.SharedPreferences;


import com.priceline.app.PricelineApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    final private PricelineApplication application;

    public AppModule(PricelineApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public PricelineApplication provideMyApplication() {
        return application;
    }

    @Provides
    public Context provideContext() {
        return application.getApplicationContext();
    }

    @Provides
    public AppModule provideAppModule() {
        return this;
    }


}




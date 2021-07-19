package com.priceline.app.di.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.priceline.app.PricelineApplication;
import com.priceline.app.api.common.LiveDataCallAdapterFactory;
import com.priceline.app.service.UserRestService;
import com.priceline.app.utils.Constants;
import com.priceline.app.utils.DateSerializer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gagan
 */
@Module
public class NetModule {

    final private String baseUrl;

    public NetModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    public Cache provideHttpCache(PricelineApplication application) {
        long cacheSize = 10 * 1024 * 1024L;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .registerTypeAdapter(Date.class, new DateSerializer());
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(Constants.HTTPLogLevel);
        return logInterceptor;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkhttpClient(Cache cache, HttpLoggingInterceptor loggingInterceptor) {

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(Constants.HTTPLogLevel);
        OkHttpClient.Builder client = (new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS).
                        addInterceptor(loggingInterceptor).addNetworkInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    CacheControl control = originalRequest.cacheControl();
                    int maxAge = control.maxAgeSeconds();
                    Request request = originalRequest.newBuilder()
                            .header("MD-app-os", "android")
                            .build();
                    Response response = chain.proceed(request);

                    if (maxAge > 0) {
                        String cacheHeaderValue = "private, max-age=" + control.maxAgeSeconds();
                        return response.newBuilder()
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .header("Cache-Control", cacheHeaderValue)
                                .build();
                    }
                    return response;
                }));
        client.cache(cache);


        return client.build();
    }


    // can add interceptor here.
    @Provides
    @Singleton
    public UserRestService provideRestService(Gson gson, OkHttpClient okHttpClient) { //dependencies!
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
        return retrofit.create(UserRestService.class);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .build();
    }

}

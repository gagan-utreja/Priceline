package com.priceline.app.api.common;


import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A Retrofit adapterthat converts the Call into a LiveData of ApiResponse.
 * @param <R>
 */
final class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<ApiResponse<R>>> {
    private final Type responseType;
    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }


    public LiveData<ApiResponse<R>> adapt(final Call<R> call) {
        return new LiveData<ApiResponse<R>>() {
            AtomicBoolean started = new AtomicBoolean(false);
            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, Response<R> response) {
                            if(response.isSuccessful()) {
                                postValue(new ApiResponse<>(response));
                            } else {
                                postValue(new ApiResponse<>(ApiException.httpError(response.raw().request().url().toString(), response)));
                            }
                        }

                        @Override
                        public void onFailure(Call<R> call, Throwable throwable) {
                            if (throwable instanceof IOException) {
                                postValue(new ApiResponse<R>(ApiException.networkError((IOException) throwable)));
                            } else {
                                postValue(new ApiResponse<R>(ApiException.unexpectedError(throwable)));
                            }
                        }
                    });
                }
            }
        };
    }
}
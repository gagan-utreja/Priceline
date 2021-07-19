package com.priceline.app.api.common;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {
    public enum Status {
        SUCCESS, ERROR, LOADING
    }

    private final Status status;
    private final T data;
    private final ApiException exception;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable ApiException exception) {
        this.status = status;
        this.data = data;
        this.exception = exception;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public ApiException getException() {
        return exception;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(ApiException exception, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, exception);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }
}
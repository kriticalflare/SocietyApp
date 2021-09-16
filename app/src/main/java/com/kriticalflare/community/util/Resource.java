package com.kriticalflare.community.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.kriticalflare.community.R;

public class Resource<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    @StringRes
    public final Integer message;
    @Nullable
    public final String apiMessage;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable Integer message, @Nullable String apiMessage) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.apiMessage = apiMessage;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null, null);
    }

    public static <T> Resource<T> error(@Nullable T data, String apiMessage) {
        return new Resource<>(Status.ERROR, data, R.string.api_error_message, apiMessage);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null, null);
    }

    public static <T> Resource<T> noNetwork(@Nullable T data) {
        return new Resource<>(Status.NO_NETWORK, data, R.string.network_issue_message, null);
    }

    public enum Status {
        SUCCESS, ERROR, LOADING, NO_NETWORK
    }
}
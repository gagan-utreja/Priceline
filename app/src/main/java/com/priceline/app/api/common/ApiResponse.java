package com.priceline.app.api.common;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;

public class ApiResponse<T> {

    public final int code;
    @Nullable
    public final T body;
    @Nullable
    public final Throwable error;


    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        this.error = error;
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            error = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                // try {
                message = getErrorModel(response).errorMessage;//response.errorBody().string();
//                } catch (IOException ignored) {
//                    Log.e("ERROR", "error while parsing response", ignored);
//                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            error = new IOException(message);
            body = null;
        }
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }


    public int getCode() {
        return code;
    }

    @Nullable
    public T getBody() {
        return body;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    public ApiErrorModel getErrorModel(Response<T> response) {

        if (response == null || response.errorBody() == null) {
            return new ApiErrorModel("unreachable");
        }
        try {
            String errorString = response.errorBody().string();
            int errorStatusCode = response.code();
            boolean isSuccessful = response.isSuccessful();
            String httpMessage = response.message();
            if (httpMessage == null)
                httpMessage = "";
            final String additionalErrorInfo = errorString + errorStatusCode + isSuccessful + httpMessage;

            Gson gson = new Gson();
            ApiErrorModel apiErrorModel = gson.fromJson(errorString, ApiErrorModel.class);
            apiErrorModel.prepareApiErrorMessage();
            Log.d("APIException ------- : ", apiErrorModel.errorMessage + " " + apiErrorModel.errorCode);
            if ((apiErrorModel.errorMessage == null && apiErrorModel.errorCode == null) || (apiErrorModel.errorMessage.length() <= 0 && apiErrorModel.errorCode.length() <= 0)) {
                return new ApiErrorModel("We are facing problem...We will be right back! \n" + additionalErrorInfo);
            }
            return apiErrorModel;
        } catch (Exception e1) {

            e1.printStackTrace();
            return new ApiErrorModel("unreachable");
        }
    }

}
package com.priceline.app.api.common;

/**
 * Created by Gagan
 */

import com.google.gson.Gson;
import com.priceline.app.utils.NetworkUtils;


import java.io.IOException;

import retrofit2.Response;


// This is RetrofitError converted to Retrofit 2
public class ApiException extends RuntimeException {
    private final String url;
    private final Response response;
    private final Kind kind;
    private ApiErrorModel errorModel;

    public ApiException(String message, String url, Response response, Kind kind, Throwable exception, boolean isNetworkError) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
        if (isNetworkError) {
            this.errorModel = new ApiErrorModel(message);
        }
    }


    public static ApiException httpError(String url, Response response) {
        String message = response.code() + " " + response.message();
        return new ApiException(message, url, response, Kind.HTTP, null, false);
    }

    public static ApiException networkError(IOException exception) {
        if (NetworkUtils.isNetworkAvailable()) {
            return new ApiException("Connection timeout! Please try again and check your internet connectivity.", null, null, Kind.NETWORK, exception, true);
        }
        return new ApiException("Internet connection appears to be offline, Please check your connection", null, null, Kind.NETWORK, exception, true);
    }

    public static ApiException unexpectedError(Throwable exception) {
        return new ApiException(exception.getMessage(), null, null, Kind.UNEXPECTED, exception, false);
    }

    /**
     * The request URL which produced the error.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Response object containing status code, headers, body, etc.
     */
    public Response getResponse() {
        return response;
    }

    /**
     * The event kind which triggered this error.
     */
    public Kind getKind() {
        return kind;
    }

    /**
     * HTTP response body converted to specified {@code type}. {@code null} if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified {@code type}.
     */
/*
    public <T> T getErrorBodyAs(Class<T> type, ResponseBody responseBody) throws IOException, RuntimeException {
        Gson gson = new Gson();
        ApiErrorModel errorModel = gson.fromJson(responseBody.toString(), type);
        Converter<ResponseBody, T> converter =  GsonConverterFactory.create().responseBodyConverter(type, new Annotation[0]);//MyApplication.getRestClient().getRetrofitInstance().responseBodyConverter(type, new Annotation[0]);
        try {
            return converter.convert(responseBody);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
*/
    public ApiErrorModel getErrorModel() {
        //TODO: Check for null if null Send a mock ApiErrorModel saying unexpected happen
        if (this.errorModel != null) {
            return errorModel;
        }
        if (response == null || response.errorBody() == null) {
            return new ApiErrorModel("Unable to connect. The app can not connect to the server. Please try again");
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
           // SmartLog.d("MDApiException ------- : ", apiErrorModel.errorMessage + " " + apiErrorModel.errorCode);
            if ((apiErrorModel.errorMessage == null && apiErrorModel.errorCode == null) || (apiErrorModel.errorMessage.length() <= 0 && apiErrorModel.errorCode.length() <= 0)) {
                //TODO: Report this incident somewhere
                return new ApiErrorModel("We are facing problem...We will be right back! \n" + additionalErrorInfo);
            }
            return apiErrorModel;
        } catch (Exception e1) {

            e1.printStackTrace();
            return new ApiErrorModel("Unable to connect. The app can not connect to the server");
        }
    }

    public String getErrorMessage() {
        if (this.getKind() == ApiException.Kind.HTTP || this.getKind() == ApiException.Kind.NETWORK) {
            ApiErrorModel apiErrorModel = this.getErrorModel();
            apiErrorModel.prepareApiErrorMessage();
            return apiErrorModel.getErrorMessage();

        } else {
            return "Unexpected Error";
        }
    }

    /**
     * Identifies the event kind which triggered
     */
    public enum Kind {
        /**
         * An {@link IOException} occurred while communicating to the server.
         */
        NETWORK,
        /**
         * A non-200 HTTP status code was received from the server.
         */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }
}
package com.priceline.app.utils;

import okhttp3.logging.HttpLoggingInterceptor;

public class Constants {

    public static final HttpLoggingInterceptor.Level HTTPLogLevel = HttpLoggingInterceptor.Level.BODY;

    // this should store in some encrypted way.
    public static final String API_KEY = "WhNegrUtwQoZabgLWXimimAVW3cEZhYg";

    // can use the firebase remote confid, so that can handle different build types
    public static String base_url= "https://api.nytimes.com/svc/books/v3/";
}

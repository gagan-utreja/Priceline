package com.priceline.app.service;


import androidx.lifecycle.LiveData;

import com.priceline.app.api.response.detailList.DetailListResponse;
import com.priceline.app.api.response.nameList.NameListResponse;
import com.priceline.app.api.common.ApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;


public interface UserRestService {

    @GET("lists/names.json")
    LiveData<ApiResponse<NameListResponse>> getNameList(@Query("api-key") String apiKey);

    @GET("lists.json")
    LiveData<ApiResponse<DetailListResponse>> getDetailsList(@Query("api-key") String apiKey, @Query("list") String listEncodeName);
}



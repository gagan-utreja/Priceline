package com.priceline.app.repository;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.priceline.app.api.common.ApiResponse;
import com.priceline.app.api.common.NetworkBoundResource;
import com.priceline.app.api.common.Resource;
import com.priceline.app.api.response.detailList.DetailListResponse;
import com.priceline.app.api.response.nameList.NameListResponse;
import com.priceline.app.service.UserRestService;
import com.priceline.app.utils.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Repository {

    final private UserRestService userRestService;
    final MutableLiveData<NameListResponse> nameListResponseMutableLiveData = new MutableLiveData<>();
    final MutableLiveData<DetailListResponse> detailListResponseMutableLiveData = new MutableLiveData<>();


    @Inject
    public Repository(UserRestService api) {
        this.userRestService = api;
    }

    /**
     * Get all Book Name details.
     *
     * @return NameListResponse
     */
    public LiveData<Resource<NameListResponse>> getNameList(Boolean refresh) {
        LiveData<Resource<NameListResponse>> liveDataOrders = new NetworkBoundResource<NameListResponse, NameListResponse>() {

            @Override
            protected void saveCallResult(@NonNull NameListResponse item) {
                nameListResponseMutableLiveData.postValue(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable NameListResponse data) {
                if (refresh)
                    return true;

                if (nameListResponseMutableLiveData.getValue() != null) {
                    return false;
                }
                return true;
            }

            @NonNull
            @Override
            protected LiveData<NameListResponse> loadFromDb() {
                nameListResponseMutableLiveData.setValue(nameListResponseMutableLiveData.getValue());
                return nameListResponseMutableLiveData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<NameListResponse>> createCall() {
                return userRestService.getNameList(Constants.API_KEY);
            }
        }.getAsLiveData();

        return liveDataOrders;
    }


    /**
     * Get all Book details list.
     *
     * @return DetailListResponse
     */
    public LiveData<Resource<DetailListResponse>> getDetailList(Boolean refresh, String listEncodeName) {
        LiveData<Resource<DetailListResponse>> liveDataOrders = new NetworkBoundResource<DetailListResponse, DetailListResponse>() {

            @Override
            protected void saveCallResult(@NonNull DetailListResponse item) {
                detailListResponseMutableLiveData.postValue(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable DetailListResponse data) {

                if (refresh)
                    return true;

                if (detailListResponseMutableLiveData.getValue() != null) {
                    return false;
                }
                return true;
            }

            @NonNull
            @Override
            protected LiveData<DetailListResponse> loadFromDb() {
                detailListResponseMutableLiveData.setValue(detailListResponseMutableLiveData.getValue());
                return detailListResponseMutableLiveData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<DetailListResponse>> createCall() {
                return userRestService.getDetailsList(Constants.API_KEY, listEncodeName);
            }
        }.getAsLiveData();

        return liveDataOrders;
    }


}
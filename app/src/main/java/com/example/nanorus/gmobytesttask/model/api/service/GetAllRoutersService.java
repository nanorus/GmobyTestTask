package com.example.nanorus.gmobytesttask.model.api.service;

import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

public interface GetAllRoutersService {

    @GET("trips")
    Single<RequestPojo> getAllRoutersRequestObservable(@Query("from_date") int from_date, @Query("to_date") int to_date);
}

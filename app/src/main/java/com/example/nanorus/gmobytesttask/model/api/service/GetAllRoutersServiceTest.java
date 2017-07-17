package com.example.nanorus.gmobytesttask.model.api.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetAllRoutersServiceTest {

    @GET("trips")
    Call<String> getAllRoutersRequestString(@Query("from_date") int from_date, @Query("to_date") int to_date);
}

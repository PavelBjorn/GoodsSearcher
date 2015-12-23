package com.fedor.pavel.goodssearcher.api;

import com.fedor.pavel.goodssearcher.constants.APIQueryConstants;
import com.fedor.pavel.goodssearcher.models.CategoryResponse;
import com.fedor.pavel.goodssearcher.models.GoodsResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;


public interface API {

    @GET(APIQueryConstants.API_CATEGORIES)
    void getCategories(@Query(APIQueryConstants.API_APP_ID_KEY) String apiKey, Callback<CategoryResponse> models);

    @GET(APIQueryConstants.API_GOODS)
    void getGoods(@QueryMap Map<String, String> params, Callback<GoodsResponse> models);

}

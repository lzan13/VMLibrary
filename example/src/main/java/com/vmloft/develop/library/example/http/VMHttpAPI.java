package com.vmloft.develop.library.example.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by lzan13 on 2017/6/13.
 * 请求接口
 */
public interface VMHttpAPI {

    @GET("test/get") Call<ResponseBody> testGetData();
}

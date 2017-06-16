package com.vmloft.develop.library.simple.http;

import com.vmloft.develop.library.tools.VMCallback;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by lzan13 on 2017/6/13.
 * http 请求管理者
 */
public class VMHttpManager {

    private static VMHttpManager instance = null;
    private VMHttpAPI httpAPI = null;
    private String baseUrl = "";

    /**
     * 私有构造方法，产生单例类实例，并做一些初始化操作
     */
    private VMHttpManager() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).build();
        httpAPI = retrofit.create(VMHttpAPI.class);
    }

    /**
     * 获取当前类实例
     */
    public static VMHttpManager getInstance() {
        if (instance == null) {
            instance = new VMHttpManager();
        }
        return instance;
    }

    public void testGet(VMCallback callback) {
        String result = null;
        Call<ResponseBody> call = httpAPI.testGetData();
        try {
            // retrofit 同步请求方法
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                result = response.body().toString();
                callback.onSuccess(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onError(-1, e.getMessage());
        }
    }
}

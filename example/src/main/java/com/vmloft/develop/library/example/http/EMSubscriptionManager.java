package com.vmloft.develop.library.example.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lzan13 on 2017/7/13.
 * Subscription account control manager
 */
public class EMSubscriptionManager {

    private final String TAG = this.getClass().getSimpleName();

    private final String HTTP_GET = "GET";
    private final String HTPP_POST = "POST";

    private static EMSubscriptionManager instance;

    private String baseUrl = "http://118.193.28.212:31060/web/pa/v1/1102170707012145/pa1-1/";
    private String accessToken = "6ccdc77f-70f7-45c7-ba6d-ddfd420df588";

    private HttpURLConnection connection;
    private URL connURL;

    private EMSubscriptionManager() {
    }

    public static EMSubscriptionManager getInstance() {
        if (instance == null) {
            instance = new EMSubscriptionManager();
        }
        return instance;
    }

    /**
     * \~chinese
     * 从服务器同步获取关注的订阅号列表
     *
     * @param pageNum 分页数，默认为 1
     * @param pageSize 分页大小，默认为20
     *
     * \~english
     * Get a list of subscription numbers from the server synchronization
     */
    public void fetchFollowSubscriptionAccountsFromServer(int pageNum, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("pagenum", pageNum);
        params.put("pagesize", pageSize);
        String result = httpGet("pas/" + "lz1", params);
    }

    /**
     * \~chinese
     * 从服务器异步获取关注的订阅号列表
     *
     * \~english
     * Obtain a list of subscription numbers from the server, asynchronously
     */
    public void asyncFetchFollowSubscriptionAccountsFromServer() {

    }

    /**
     * /~chinese
     * 封装 HttpURLConnection GET 请求
     *
     * @param api 请求接口
     * @param params 请求参数
     * @return 返回请求结果
     */
    private String httpGet(String api, Map<String, Object> params) {
        StringBuffer buffer = new StringBuffer();
        try {
            connURL = new URL(buildGetUrl(api, params));
            connection = (HttpURLConnection) connURL.openConnection();

            // set request headers
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");

            // set http request method
            connection.setRequestMethod(HTTP_GET);
            connection.setDoInput(true);
            // get method not need output data
            connection.setDoOutput(false);
            // connectTCPServer
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // read data
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                reader.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private void httpPost() {

    }

    /**
     * /~chinese
     * 拼接 URL 资源
     *
     * @param api api 接口地址
     * @param params 参数
     * @return 返回拼接后的请求地址
     */
    private String buildGetUrl(String api, Map<String, Object> params) {
        String url = baseUrl + api;
        if (!url.endsWith("?")) {
            url += "?";
        }
        int count = 0;
        Set<String> keys = params.keySet();
        for (String key : keys) {
            if (params.get(key) == null) {
                continue;
            }
            if (count == 0) {
                url += "&";
            }
            url += key + "=" + params.get(key);
            count++;
        }
        return url;
    }
}

package com.vmloft.develop.emrest;

import android.os.Bundle;
import android.view.View;
import com.vmloft.develop.emrest.network.VMHttpManager;
import com.vmloft.develop.library.tools.VMBaseActivity;
import com.vmloft.develop.library.tools.utils.VMFileUtil;
import com.vmloft.develop.library.tools.utils.VMLog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VMMainActivity extends VMBaseActivity {

    private JSONObject mObject = null;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_start_rest).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                readRestJson();
                startRestAPI();
            }
        });
    }

    private void startRestAPI() {
        String restUrl = mObject.optString("rest_url");
        String orgName = mObject.optString("org_name");
        String appName = mObject.optString("app_name");
        VMHttpManager.getInstance().setBaseUrl(restUrl + orgName + "/" + appName + "/");

        new Thread(new Runnable() {
            @Override public void run() {
                try {
                    testTokenAPI();
                    testUserAPI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void testTokenAPI() throws JSONException {
        JSONObject tokenObject = mObject.optJSONObject("token");
        String requestAPI = tokenObject.optString("api");
        String requestMethod = tokenObject.optString("method");
        JSONObject requestBody = tokenObject.optJSONObject("body");

        String result = VMHttpManager.getInstance().onHttpPost(requestAPI, requestBody);
        JSONObject resultObject = new JSONObject(result);
        if (!resultObject.has("error") && resultObject.has("access_token")) {
            VMHttpManager.getInstance().setAccessToken(resultObject.optString("access_token"));
        }
    }

    private void testUserAPI() throws JSONException {
        JSONArray userArray = mObject.optJSONArray("user");
        for (int i = 0; i < userArray.length(); i++) {
            JSONObject userObject = userArray.optJSONObject(i);
            String requestAPI = userObject.optString("api");
            String requestMethod = userObject.optString("method");
            Object requestBody = userObject.opt("body");
            String result = "";
            if (requestMethod.equals("GET")) {
                result = VMHttpManager.getInstance().onHttpGet(requestAPI);
            } else if (requestMethod.equals("POST")) {
                result = VMHttpManager.getInstance().onHttpPost(requestAPI, requestBody);
            } else if (requestMethod.equals("PUT")) {
                result = VMHttpManager.getInstance().onHttpPut(requestAPI, requestBody);
            } else if (requestMethod.equals("DELETE")) {
                result = VMHttpManager.getInstance().onHttpDelete(requestAPI);
            }

            JSONObject resultObject = new JSONObject(result);
        }
    }

    /**
     * 读取 json 配置文件
     */
    private void readRestJson() {
        File file = new File(VMFileUtil.getSDCard(), "em_rest.json");
        FileInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            outputStream = new ByteArrayOutputStream();
            byte[] butter = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(butter)) != -1) {
                outputStream.write(butter, 0, len);
            }
            String result = outputStream.toString();
            VMLog.d("Read file %s", result);
            mObject = new JSONObject(result);

            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

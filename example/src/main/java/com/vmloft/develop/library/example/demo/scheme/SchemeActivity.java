package com.vmloft.develop.library.example.demo.scheme;

import android.net.Uri;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.utils.VMLog;

import java.util.List;

/**
 * Create by lzan13 on 2020-01-02 20:46
 * 测试 Scheme 方式打开 app
 */
public class SchemeActivity extends AppActivity {
    @Override
    protected int layoutId() {
        return R.layout.activity_scheme;
    }

    @Override
    protected void initUI() {
        super.initUI();
    }

    @Override
    protected void initData() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            // 完整的url信息
            String url = uri.toString();
            VMLog.d("url: " + uri);
            // scheme部分
            String scheme = uri.getScheme();
            VMLog.d("scheme: " + scheme);
            // host部分
            String host = uri.getHost();
            VMLog.d("host: " + host);
            //port部分
            int port = uri.getPort();
            VMLog.d("port: " + port);
            // 访问路劲
            String path = uri.getPath();
            VMLog.d("path: " + path);
            // 参数
            List<String> pathSegments = uri.getPathSegments();
            VMLog.d("pathSegments: " + pathSegments.toString());
            // Query部分
            String query = uri.getQuery();
            VMLog.d("query: " + query);
            //获取指定参数值
            String goodsId = uri.getQueryParameter("id");
            VMLog.d("id: " + goodsId);
        }
    }
}

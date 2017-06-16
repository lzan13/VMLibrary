package com.vmloft.develop.library.simple.http;

import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMBaseActivity;
import com.vmloft.develop.library.tools.VMCallback;
import com.vmloft.develop.library.tools.utils.VMLog;

/**
 * Created by lzan13 on 2017/6/13.
 * 测试网络请求界面
 */
public class VMHttpActivity extends VMBaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);

        ButterKnife.bind(this);
    }

    @OnClick({ R.id.btn_test_get }) void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test_get:
                VMHttpManager.getInstance().testGet(new VMCallback() {
                    @Override public void onSuccess(String message) {
                        VMLog.i(message);
                    }

                    @Override public void onError(int code, String message) {
                        VMLog.i("onError %d %s", code, message);
                    }

                    @Override public void onPregress(int progress, String message) {

                    }
                });
                break;
        }
    }
}

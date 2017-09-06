package com.vmloft.develop.library.simple.jni;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.vmloft.develop.library.nativetools.utils.VMCrypto;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMActivity;

/**
 * Created by lzan13 on 2017/7/7.
 * 测试 jni 工具类
 */
public class VMJNIActivity extends VMActivity {

    @BindView(R.id.text_view) TextView textView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);

        ButterKnife.bind(activity);
    }

    @OnClick({ R.id.btn_test }) void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test:
                String str = VMCrypto.crypStr2SHA1("test");
                textView.setText(str);
                break;
        }
    }
}

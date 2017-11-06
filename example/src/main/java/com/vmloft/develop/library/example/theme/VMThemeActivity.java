package com.vmloft.develop.library.example.theme;

import android.os.Bundle;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.VMActivity;

/**
 * Created by lzan13 on 2017/4/7.
 * 测试控件样式主题界面
 */
public class VMThemeActivity extends VMActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        findViewById(R.id.btn_white_activated);
    }
}

package com.vmloft.develop.library.simple.theme;

import android.os.Bundle;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMBaseActivity;

/**
 * Created by lzan13 on 2017/4/7.
 * 测试控件样式主题界面
 */
public class VMThemeActivity extends VMBaseActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        findViewById(R.id.btn_white_activated).setActivated(true);
    }
}

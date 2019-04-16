package com.vmloft.develop.library.example.demo.custom;

import android.view.View;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.widget.VMTimerBtn;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lzan13 on 2017/4/1.
 * 测试录音控件
 */
public class CustomViewActivity extends AppActivity {

    @BindView(R.id.custom_btn_timer)
    VMTimerBtn mTimerBtn;

    @Override
    protected int loadView() {
        return R.layout.activity_view_custom;
    }

    @Override
    protected void init() {

    }

    @OnClick({R.id.custom_btn_timer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.custom_btn_timer:
                mTimerBtn.startTimer();
                break;
        }
    }
}

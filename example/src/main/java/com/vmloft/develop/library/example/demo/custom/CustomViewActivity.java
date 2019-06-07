package com.vmloft.develop.library.example.demo.custom;

import android.view.View;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.widget.VMTimerBtn;

import butterknife.BindView;
import butterknife.OnClick;
import com.vmloft.develop.library.tools.widget.record.VMRecordView;
import com.vmloft.develop.library.tools.widget.toast.VMToast;

/**
 * Created by lzan13 on 2017/4/1.
 * 测试录音控件
 */
public class CustomViewActivity extends AppActivity {

    @BindView(R.id.custom_btn_timer) VMTimerBtn mTimerBtn;
    @BindView(R.id.custom_record_view) VMRecordView mRecordView;

    @Override
    protected int layoutId() {
        return R.layout.activity_view_custom;
    }

    @Override
    protected void init() {
        mRecordView.setRecordListener(new VMRecordView.RecordListener() {
            @Override
            public void onStart() {
                VMToast.make(mActivity, "录音开始").done();
            }

            @Override
            public void onCancel() {
                VMToast.make(mActivity, "录音取消").error();
            }

            @Override
            public void onError(int code, String desc) {
                VMToast.make(mActivity, "录音失败 %d %s", code, desc).error();
            }

            @Override
            public void onComplete(String path, long time) {
                VMToast.make(mActivity, "录音完成 %s %d", path, time).done();
            }
        });
    }

    @OnClick({
        R.id.custom_btn_timer, R.id.btn_toast_1, R.id.btn_toast_2, R.id.btn_toast_3
    })
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.custom_btn_timer:
            mTimerBtn.startTimer();
            break;
        case R.id.btn_toast_1:
            VMToast.init(R.color.vm_black, R.drawable.vm_ic_camera, R.color.vm_amber);
            VMToast.make(mActivity, "测试自定义弹出 Toast 提醒功能，这是默认提醒样式！自定义颜色的").show();
            break;
        case R.id.btn_toast_2:
            VMToast.make(mActivity, "测试自定义弹出 Toast 提醒功能，这是完成提醒默认样式！绿色的").done();
            break;
        case R.id.btn_toast_3:
            VMToast.make(mActivity, "测试自定义弹出 Toast 提醒功能，这是错误提醒默认样式！红色的").error();
            break;
        }
    }
}

package com.vmloft.develop.library.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vmloft.develop.library.example.audio.VMAudioActivity;
import com.vmloft.develop.library.example.camera.VMCameraActivity;
import com.vmloft.develop.library.example.details.VMDetailsActivity;
import com.vmloft.develop.library.example.http.VMHttpActivity;
import com.vmloft.develop.library.example.jni.JniActivity;
import com.vmloft.develop.library.example.popup.VMPopupWindowActivity;
import com.vmloft.develop.library.example.record.ScreenRecordActivity;
import com.vmloft.develop.library.example.shell.ShellActivity;
import com.vmloft.develop.library.example.socket.VMSocketActivity;
import com.vmloft.develop.library.example.theme.VMThemeActivity;
import com.vmloft.develop.library.example.tools.SignatureActivity;
import com.vmloft.develop.library.example.webpage.WebPageActivity;
import com.vmloft.develop.library.example.widget.VMDotLineActivity;
import com.vmloft.develop.library.example.widget.VMRecordActivity;
import com.vmloft.develop.library.tools.VMActivity;
import com.vmloft.develop.library.tools.utils.VMLog;
import com.vmloft.develop.library.tools.widget.VMViewGroup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends VMActivity {

    @BindView(R.id.widget_toolbar) Toolbar toolbar;
    @BindView(R.id.view_group) VMViewGroup viewGroup;
    private TextView textView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(activity);

        toolbar.setTitle("MainActivity");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);

        String[] btnArray = {
            "Socket", "Dot Line", "Record", "Theme", "Camera", "Details", "Http", "Audio",
            "PopupWindow", "Signature", "Record Screen", "Web Page", "Shell", "Jni"
        };
        for (int i = 0; i < btnArray.length; i++) {
            Button btn = new Button(activity);
            btn.setText(btnArray[i]);
            btn.setId(100 + i);
            btn.setOnClickListener(viewListener);
            viewGroup.addView(btn);
        }
        testExecutor();
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
            case 100:
                intent.setClass(activity, VMSocketActivity.class);
                break;
            case 101:
                intent.setClass(activity, VMDotLineActivity.class);
                break;
            case 102:
                intent.setClass(activity, VMRecordActivity.class);
                break;
            case 103:
                intent.setClass(activity, VMThemeActivity.class);
                break;
            case 104:
                intent.setClass(activity, VMCameraActivity.class);
                break;
            case 105:
                intent.setClass(activity, VMDetailsActivity.class);
                break;
            case 106:
                intent.setClass(activity, VMHttpActivity.class);
                break;
            case 107:
                intent.setClass(activity, VMAudioActivity.class);
                break;
            case 108:
                intent.setClass(activity, VMPopupWindowActivity.class);
                break;
            case 109:
                intent.setClass(activity, SignatureActivity.class);
                break;
            case 110:
                intent.setClass(activity, ScreenRecordActivity.class);
                break;
            case 111:
                intent.setClass(activity, WebPageActivity.class);
                break;
            case 112:
                intent.setClass(activity, ShellActivity.class);
                break;
            case 113:
                intent.setClass(activity, JniActivity.class);
                break;
            }
            onStartActivity(activity, intent);
        }
    };

    private void testExecutor() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //executor.submit(new Runnable() {
        //    @Override public void run() {
        //        try {
        //            Thread.sleep(1000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //        VMLog.i("任务1");
        //    }
        //});
        //executor.submit(new Runnable() {
        //    @Override public void run() {
        //        try {
        //            Thread.sleep(1000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //        VMLog.i("任务2");
        //    }
        //});
        //executor.submit(new Runnable() {
        //    @Override public void run() {
        //        try {
        //            Thread.sleep(1000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //        VMLog.i("任务3");
        //        String str = textView.getText().toString();
        //        VMLog.i("任务3崩溃了");
        //    }
        //});
        //executor.submit(new Runnable() {
        //    @Override public void run() {
        //        try {
        //            Thread.sleep(1000);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //        VMLog.i("任务4");
        //    }
        //});
    }

    private void checkNumber(int num) {
        if ((num ^ 1) == 0) {
            VMLog.i("num: %d, 是2的 N 次方", num);
        } else {

        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }
}

package com.vmloft.develop.library.tools.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vmloft.develop.library.tools.utils.VMLog;

/**
 * Created by lzan13 on 2015/7/4.
 * 
 * Activity 的基类，做一些子类公共的工作
 */
public class VMActivity extends AppCompatActivity {

    protected String className = this.getClass().getSimpleName();

    protected VMActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VMLog.v("%s onCreate", className);
        mActivity = this;
        VMApp.putActivity(mActivity);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        VMLog.v("%s onRestart", className);
    }

    @Override
    protected void onStart() {
        super.onStart();
        VMLog.v("%s onStart", className);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VMLog.v("%s onResume", className);
    }

    @Override
    protected void onPause() {
        super.onPause();
        VMLog.v("%s onPause", className);
    }

    @Override
    protected void onStop() {
        super.onStop();
        VMLog.v("%s onStop", className);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VMLog.v("%s onDestroy", className);
        VMApp.removeActivity(mActivity);
        mActivity = null;
    }

    /**
     * 自定义 Activity 结束方法
     */
    protected void onFinish() {
        VMLog.v("%s onFinish", className);
        finish();
    }
}

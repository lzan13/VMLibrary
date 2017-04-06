package com.vmloft.develop.library.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.vmloft.develop.library.tools.utils.VMLog;

/**
 * Created by lzan13 on 2015/7/4.
 * Activity 的基类，做一些子类公共的工作
 */
public class VMBaseActivity extends AppCompatActivity {

    protected String className = this.getClass().getSimpleName();

    // 当前界面的上下文菜单对象
    protected VMBaseActivity activity;

    // 根布局
    private View rootView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VMLog.i("%s onCreate", className);
        activity = this;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 公用的 Activity 跳转方法
     * 基类定义并实现的方法，为了以后方便扩展
     *
     * @param activity 当前 Activity 对象
     * @param intent 界面跳转 Intent 实例对象
     */
    public void onStartActivity(Activity activity, Intent intent) {
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
        ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onStartActivity(Activity activity, Intent intent, View sharedElement) {
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElement,
                        sharedElement.getTransitionName());
        ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    }

    /**
     * 自定义 Activity 结束方法
     */
    protected void onFinish() {
        // 根据不同的系统版本选择不同的 finish 方法
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            activity.finish();
        } else {
            ActivityCompat.finishAfterTransition(activity);
        }
    }

    @Override protected void onRestart() {
        super.onRestart();
        VMLog.i("%s onRestart", className);
    }

    @Override protected void onStart() {
        super.onStart();
        VMLog.i("%s onStart", className);
    }

    @Override protected void onResume() {
        super.onResume();
        VMLog.i("%s onResume", className);
    }

    @Override protected void onPause() {
        super.onPause();
        VMLog.i("%s onPause", className);
    }

    @Override protected void onStop() {
        super.onStop();
        VMLog.i("%s onStop", className);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        activity = null;
        rootView = null;

        VMLog.i("%s onDestroy", className);
    }
}

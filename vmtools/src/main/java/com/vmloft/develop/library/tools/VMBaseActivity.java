package com.vmloft.develop.library.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
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

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VMLog.i("%s onCreate", className);
        activity = this;

        //activity.getWindow().setBackgroundDrawableResource(R.color.vm_transparent);
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

        VMLog.i("%s onDestroy", className);
    }

    /**
     * 封装公共的 Activity 跳转方法
     * 基类定义并实现的方法，为了以后方便扩展
     *
     * @param activity 当前 Activity 对象
     * @param intent 界面跳转 Intent 实例对象
     */
    public void onStartActivity(Activity activity, Intent intent) {
        Pair<View, String>[] pairs = VMTransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
        ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    }

    /**
     * 带有共享元素的 Activity 跳转方法
     *
     * @param activity 当前活动 Activity
     * @param intent 跳转意图
     * @param sharedElement 共享元素
     */
    public void onStartActivity(Activity activity, Intent intent, View sharedElement) {
        Pair<View, String>[] pairs = VMTransitionHelper.createSafeTransitionParticipants(this, true,
                new Pair<>(sharedElement, sharedElement.getTransitionName()));
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
        ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    }

    /**
     * 带有多个共享元素的 Activity 跳转方法
     *
     * @param activity 当前活动 Activity
     * @param intent 跳转意图
     * @param pairs 共享元素集合
     */
    public void onStartActivity(Activity activity, Intent intent, Pair<View, String>... pairs) {
        Pair<View, String>[] allPairs = VMTransitionHelper.createSafeTransitionParticipants(this, true, pairs);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, allPairs);
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
            activity.finishAfterTransition();
        }
    }
}

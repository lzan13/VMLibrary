package com.vmloft.develop.library.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.vmloft.develop.library.tools.utils.VMLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        VMLog.i("%s onDestroy", className);
        activity = null;
    }

    /**
     * 封装公共的 Activity 跳转方法
     * 基类定义并实现的方法，为了以后方便扩展
     *
     * @param activity 当前 Activity 对象
     * @param intent 界面跳转 Intent 实例对象
     */
    public void onStartActivity(Activity activity, Intent intent) {
        //Pair<View, String>[] pairs = createSafeTransitionParticipants(this, true);
        //ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
        //ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
        activity.startActivity(intent);
    }

    /**
     * TODO 因为5.x 过度动画会导致一些国产设备 UI 透明，暂时不用
     * 带有共享元素的 Activity 跳转方法
     *
     * @param activity 当前活动 Activity
     * @param intent 跳转意图
     * @param sharedElement 共享元素
     */
    //public void onStartActivity(Activity activity, Intent intent, View sharedElement) {
    //Pair<View, String>[] pairs = createSafeTransitionParticipants(this, true, new Pair<>(sharedElement, sharedElement.getTransitionName()));
    //ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);
    //ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    //activity.startActivity(intent);
    //}

    /**
     * 带有多个共享元素的 Activity 跳转方法
     *
     * @param activity 当前活动 Activity
     * @param intent 跳转意图
     * @param pairs 共享元素集合
     */
    //public void onStartActivity(Activity activity, Intent intent, Pair<View, String>... pairs) {
    //Pair<View, String>[] allPairs = createSafeTransitionParticipants(this, true, pairs);
    //ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, allPairs);
    //ActivityCompat.startActivity(activity, intent, optionsCompat.toBundle());
    //activity.startActivity(intent);
    //}

    /**
     * 创建界面切换共享元素数组
     */
    public static Pair<View, String>[] createSafeTransitionParticipants(@NonNull Activity activity,
            boolean includeStatusBar, @Nullable Pair... otherParticipants) {
        // Avoid system UI glitches as described here:
        // https://plus.google.com/+AlexLockwood/posts/RPtwZ5nNebb
        View decor = activity.getWindow().getDecorView();
        View statusBar = null;
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground);
        }
        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        // Create pair of transition participants.
        List<Pair> participants = new ArrayList<>(3);
        if (statusBar != null) {
            participants.add(new Pair<>(statusBar, statusBar.getTransitionName()));
        } else {
            participants.add(new Pair<>(navBar, navBar.getTransitionName()));
        }
        // only add transition participants if there's at least one none-null element
        if (otherParticipants != null && !(otherParticipants.length == 1
                && otherParticipants[0] == null)) {
            participants.addAll(Arrays.asList(otherParticipants));
        }
        return participants.toArray(new Pair[participants.size()]);
    }

    /**
     * 自定义 Activity 结束方法
     */
    protected void onFinish() {
        activity.finish();
    }
}

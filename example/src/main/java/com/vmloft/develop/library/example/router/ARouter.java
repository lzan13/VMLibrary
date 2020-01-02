package com.vmloft.develop.library.example.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.vmloft.develop.library.example.demo.ThreadActivity;
import com.vmloft.develop.library.example.demo.audio.MediaPlayActivity;
import com.vmloft.develop.library.example.demo.custom.DotLineActivity;
import com.vmloft.develop.library.example.demo.custom.CustomViewActivity;
import com.vmloft.develop.library.example.demo.details.DetailsActivity;
import com.vmloft.develop.library.example.demo.dialog.FloatMenuActivity;
import com.vmloft.develop.library.example.demo.image.ImageDiscernActivity;
import com.vmloft.develop.library.example.demo.indicator.IndicatorActivity;
import com.vmloft.develop.library.example.demo.picker.PickerActivity;
import com.vmloft.develop.library.example.demo.screen.RecordScreenActivity;
import com.vmloft.develop.library.example.demo.style.BtnStyleActivity;
import com.vmloft.develop.library.example.demo.web.WebActivity;
import com.vmloft.develop.library.example.tools.ToolsActivity;
import com.vmloft.develop.library.tools.router.VMRouter;

public class ARouter extends VMRouter {

    /**
     * 跳转工具界面
     */
    public static void goTools(Context context) {
        overlay(context, ToolsActivity.class);
    }

    /**
     * 跳转按钮样式界面
     */
    public static void goBtnStyle(Context context) {
        overlay(context, BtnStyleActivity.class);
    }

    /**
     * 跳转自定义描点控件
     */
    public static void goDotLine(Context context) {
        overlay(context, DotLineActivity.class);
    }

    /**
     * 跳转自定义查看详情控件
     */
    public static void goDetails(Context context) {
        overlay(context, DetailsActivity.class);
    }

    /**
     * 跳转自定义控件界面
     */
    public static void goCustomView(Context context) {
        overlay(context, CustomViewActivity.class);
    }

    /**
     * 跳转录制屏幕封装功能
     */
    public static void goRecordScreen(Context context) {
        overlay(context, RecordScreenActivity.class);
    }

    /**
     * 跳转播放音频
     */
    public static void goPlayAudio(Context context) {
        overlay(context, MediaPlayActivity.class);
    }

    /**
     * 跳转弹出对话框
     */
    public static void goPWDialog(Context context) {
        overlay(context, FloatMenuActivity.class);
    }

    /**
     * 跳转 JS 交互界面
     */
    public static void goWeb(Context context) {
        overlay(context, WebActivity.class);
    }

    /**
     * 跳转 JS 交互界面
     */
    public static void goIndicator(Context context) {
        overlay(context, IndicatorActivity.class);
    }

    /**
     * 图片识别
     */
    public static void goImageDiscern(Context context) {
        overlay(context, ImageDiscernActivity.class);
    }

    /**
     * 图片选择器
     */
    public static void goPicker(Context context) {
        overlay(context, PickerActivity.class);
    }

    /**
     * 测试线程
     */
    public static void goThread(Context context) {
        overlay(context, ThreadActivity.class);
    }

    /**
     * 通过 Url 打开页面
     */
    public static void goScheme(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vmlibrary://demo/with?id=1001"));
        overlay(context, intent);
    }
}

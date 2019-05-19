package com.vmloft.develop.library.tools.widget.toast;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMStr;

/**
 * Created by lzan13 on 2018/4/16.
 *
 * 自定义封装 Toast 显示类
 */
public class VMToast {

    // Toast 显示时长
    public static final int LONG = 5000;
    public static final int SHORT = 3000;

    private static VMToast instance;
    private static Activity mActivity;

    private static View mToastLayout;
    private static VMToastView mToastView;

    // 静态可设置参数
    private static String mMsg;
    private static int mDuration = SHORT;
    private static int mBGColor;
    private static int mIconRes;
    private static int mMsgColor;

    /**
     * 构造函数，上下文为activity
     */
    private VMToast() {}

    /**
     * 内部类实现单例模式
     */
    private static class InnerHolder {
        public static VMToast INSTANCE = new VMToast();
    }

    /**
     * 初始化弹出提示样式
     *
     * @param bgRes    背景颜色
     * @param iconRes  图标
     * @param msgColor 文本颜色
     */
    public static void init(int bgRes, int iconRes, int msgColor) {
        mBGColor = bgRes;
        mIconRes = iconRes;
        mMsgColor = msgColor;
    }

    /**
     * 根据字符串弹出提醒
     *
     * @param activity 需要展示提醒界面的 Activity 对象
     * @param msg      需要展示的消息
     */
    public static VMToast make(Activity activity, String msg) {
        mActivity = activity;
        mMsg = msg;
        initToast();
        return InnerHolder.INSTANCE;
    }

    /**
     * 根据资源 id 弹出提醒
     *
     * @param activity 需要展示提醒界面的 Activity 对象
     * @param resId    要展示消息资源 Id
     */
    public static VMToast make(Activity activity, int resId) {
        return make(activity, VMStr.byRes(resId));
    }

    /**
     * 根据资源 id 弹出 toast
     *
     * @param activity 需要展示提醒界面的 Activity 对象
     * @param resId    要展示提示资源 Id
     * @param duration 显示时长
     */
    public static VMToast make(Activity activity, int resId, int duration) {
        mDuration = duration;
        return make(activity, resId);
    }

    /**
     * 根据字符串弹出提醒
     *
     * @param activity 需要展示提醒界面的 Activity 对象
     * @param msg      要展示的提醒内容
     * @param duration 显示时长
     */
    public static VMToast make(Activity activity, String msg, int duration) {
        mDuration = duration;
        return make(activity, msg);
    }

    /**
     * 根据格式化字符串弹出提醒
     *
     * @param activity 需要展示提醒界面的 Activity 对象
     * @param msg      要展示的提醒内容
     * @param args     格式化参数
     * @return
     */
    public static VMToast make(Activity activity, String msg, Object... args) {
        return make(activity, String.format(msg, args));
    }

    /**
     * 显示前的初始化初始化提醒
     */
    private static void initToast() {
        if (mActivity != null) {
            mToastLayout = mActivity.findViewById(R.id.vm_toast_layout);
            if (mToastLayout == null) {
                mToastView = new VMToastView(mActivity);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mActivity.addContentView(mToastView, lp);
            } else {
                mToastView = (VMToastView) mToastLayout.getParent();
            }
            mToastView.setMsg(mMsg);
        }
    }

    /**
     * 设置背景资源
     */
    public VMToast setBGColor(int resId) {
        mBGColor = resId;
        return this;
    }

    /**
     * 设置图标资源
     */
    public VMToast setIcon(int resId) {
        mIconRes = resId;
        return this;
    }

    /**
     * 设置内容颜色
     */
    public VMToast setMsgColor(int resId) {
        mMsgColor = resId;
        return this;
    }

    /**
     * 显示常规操作的提示
     */
    public void show() {
        mToastView.setBGColor(mBGColor);
        mToastView.setIconRes(mIconRes);
        mToastView.setMsgColor(mMsgColor);
        mToastView.showToast(mDuration);
    }

    /**
     * 自定义完成操作的提示
     */
    public void done() {
        mToastView.setBGColor(R.color.vm_green);
        mToastView.setIconRes(R.drawable.vm_ic_emoticon);
        mToastView.setMsgColor(R.color.vm_white);
        mToastView.showToast(mDuration);
    }

    /**
     * 自定义错误操作的提示
     */
    public void error() {
        mToastView.setBGColor(R.color.vm_red);
        mToastView.setIconRes(R.drawable.vm_ic_emoticon_sad);
        mToastView.setMsgColor(R.color.vm_white);
        mToastView.showToast(mDuration);
    }

    /**
     * 是否在展示提醒
     */
    public static boolean isShow() {
        if (instance == null || mToastView == null) {
            return false;
        } else {
            return mToastView.isShow();
        }
    }
}

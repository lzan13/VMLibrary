package com.vmloft.develop.library.tools.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.VMTools;
import com.vmloft.develop.library.tools.utils.VMStr;

/**
 * Created by lzan13 on 2018/4/16.
 * 自定义封装 Toast 显示类
 */
public class VMToast {

    private Context mContext = VMTools.getContext();
    private static VMToast instance;
    private boolean isShow;

    private Handler handler = new Handler();

    private WindowManager windowManager;
    private WindowManager.LayoutParams toastParams;
    private View toastView;
    private ImageView iconView;
    private TextView textView;
    private static String toastMsg;


    private VMToast() {
        initParams();
        isShow = false;
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        toastView = inflater.inflate(R.layout.vm_widget_toast, null);
        iconView = toastView.findViewById(R.id.vm_img_toast_icon);
        textView = toastView.findViewById(R.id.vm_text_toast_text);
    }

    /**
     * 初始化 Toast 布局属性参数：位置，大小等
     */
    private void initParams() {
        toastParams = new WindowManager.LayoutParams();
        toastParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        toastParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        // Toast 的进入和退出动画效果
        toastParams.windowAnimations = R.style.VMToastAnim;

        toastParams.format = PixelFormat.TRANSLUCENT;
        toastParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        toastParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        toastParams.gravity = Gravity.LEFT | Gravity.TOP;
        toastParams.x = mContext.getResources().getDimensionPixelSize(R.dimen.vm_dimen_16);
        toastParams.y = mContext.getResources().getDimensionPixelSize(R.dimen.vm_dimen_72);

    }

    /**
     * 根据资源 id 弹出 toast
     */
    public static VMToast make(int resId) {
        if (instance == null) {
            instance = new VMToast();
        }
        toastMsg = VMStr.strByResId(resId);
        return instance;
    }

    /**
     * 根据字符串弹出 toast
     */
    public static VMToast make(String msg) {
        if (instance == null) {
            instance = new VMToast();
        }
        toastMsg = msg;
        return instance;
    }

    /**
     * 显示操作有错误提示 Toast
     */
    public void showError() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iconView.setImageResource(R.drawable.vm_ic_close_24dp);
            iconView.setColorFilter(ContextCompat.getColor(VMTools.getContext(), R.color.vm_red));
        } else {
            Drawable drawable = ContextCompat.getDrawable(VMTools.getContext(), R.drawable.vm_ic_close_24dp);
            Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
            wrapDrawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            DrawableCompat.setTint(drawable, ContextCompat.getColor(VMTools.getContext(), R.color.vm_red));
            iconView.setImageDrawable(wrapDrawable);
        }
        show(toastMsg);
    }

    /**
     * 显示操作正确执行完成的 oast
     */
    public void showDone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iconView.setImageResource(R.drawable.vm_ic_done_24dp);
            iconView.setColorFilter(ContextCompat.getColor(VMTools.getContext(), R.color.vm_green));
        } else {
            Drawable drawable = ContextCompat.getDrawable(VMTools.getContext(), R.drawable.vm_ic_done_24dp);
            Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
            wrapDrawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            DrawableCompat.setTint(drawable, ContextCompat.getColor(VMTools.getContext(), R.color.vm_green));
            iconView.setImageDrawable(wrapDrawable);
        }
        show(toastMsg);
    }

    /**
     * 公共的显示方法
     */
    private void show(final String msg) {
        if (!isShow) {
            isShow = true;
            textView.setText(msg);
            windowManager.addView(toastView, toastParams);
            handler.postDelayed(removeToastRunnable, 3000);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    removeCallbacks();
                    removeToast();
                    show(msg);
                }
            }, 2000);
        }
    }

    /**
     * 移除延迟任务
     */
    private void removeCallbacks() {
        handler.removeCallbacks(removeToastRunnable);
    }


    /**
     * 移除 Toast 任务
     */
    private Runnable removeToastRunnable = new Runnable() {
        @Override
        public void run() {
            removeToast();
        }
    };

    /**
     * 移除 Toast
     */
    private void removeToast() {
        if (toastView != null && toastView.isShown()) {
            windowManager.removeView(toastView);
        }
        isShow = false;
    }
}

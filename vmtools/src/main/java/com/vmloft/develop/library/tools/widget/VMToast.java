package com.vmloft.develop.library.tools.widget;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.VMTools;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMStr;

/**
 * Created by lzan13 on 2018/4/16.
 * 自定义封装 Toast 显示类
 */
public class VMToast {

    private static VMToast instance;
    private static String mToastMsg;
    private static int mToastDuration = Toast.LENGTH_SHORT;

    private Toast mToast;

    private View mView;
    private ImageView mIconView;
    private TextView mContentView;


    private VMToast() {
        LayoutInflater inflater = LayoutInflater.from(VMTools.getContext());
        mView = inflater.inflate(R.layout.vm_widget_toast, null);
        mIconView = mView.findViewById(R.id.vm_img_toast_icon);
        mContentView = mView.findViewById(R.id.vm_text_toast_content);

        mToast = new Toast(VMTools.getContext());
        int offset = VMDimen.dp2px(16);
        mToast.setGravity(Gravity.RIGHT | Gravity.TOP, 0, offset);
    }


    /**
     * 根据资源 id 弹出 toast
     *
     * @param resId    toast 要展示消息资源 Id
     * @param duration 显示时长
     */
    public static VMToast make(int resId, int duration) {
        mToastDuration = duration;
        return make(resId);
    }

    /**
     * 根据资源 id 弹出 toast
     *
     * @param resId toast 要展示消息资源 Id
     */
    public static VMToast make(int resId) {
        return make(VMStr.strByResId(resId));
    }

    /**
     * 根据字符串弹出 toast
     *
     * @param msg      toast 要展示的消息
     * @param duration 显示时长
     */
    public static VMToast make(String msg, int duration) {
        mToastDuration = duration;
        return make(msg);
    }

    /**
     * 根据字符串弹出 toast
     *
     * @param msg toast 要展示的消息
     */
    public static VMToast make(String msg) {
        if (instance == null) {
            instance = new VMToast();
        }
        mToastMsg = msg;
        return instance;
    }

    /**
     * 显示操作正确执行完成的 Toast
     */
    public void show() {
        mView.setBackgroundResource(R.drawable.vm_bg_toast_green);
        mIconView.setImageResource(R.drawable.vm_ic_emoticon);
        show(mToastMsg);
    }

    /**
     * 显示操作有错误提示 Toast
     */
    public void showError() {
        mView.setBackgroundResource(R.drawable.vm_bg_toast_red);
        mIconView.setImageResource(R.drawable.vm_ic_emoticon_sad);
        show(mToastMsg);
    }

    /**
     * 公共的显示方法
     */
    private void show(String msg) {
        mContentView.setText(msg);
        mToast.setDuration(mToastDuration);
        mToast.setView(mView);
        mToast.show();
    }

}

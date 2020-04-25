package com.vmloft.develop.library.tools.widget.toast

import android.app.Activity
import android.view.View
import android.widget.RelativeLayout.LayoutParams
import com.vmloft.develop.library.tools.R.color
import com.vmloft.develop.library.tools.R.drawable
import com.vmloft.develop.library.tools.R.id
import com.vmloft.develop.library.tools.utils.VMStr

/**
 * Created by lzan13 on 2018/4/16.
 *
 * 自定义封装 Toast 显示类
 */
class VMToast
/**
 * 构造函数，上下文为activity
 */
private constructor() {
    /**
     * 内部类实现单例模式
     */
    private object InnerHolder {
        var INSTANCE = VMToast()
    }

    /**
     * 设置背景资源
     */
    fun setBGColor(resId: Int): VMToast {
        mBGColor = resId
        return this
    }

    /**
     * 设置图标资源
     */
    fun setIcon(resId: Int): VMToast {
        mIconRes = resId
        return this
    }

    /**
     * 设置内容颜色
     */
    fun setMsgColor(resId: Int): VMToast {
        mMsgColor = resId
        return this
    }

    /**
     * 显示常规操作的提示
     */
    fun show() {
        mToastView!!.setBGColor(mBGColor)
        mToastView!!.setIconRes(mIconRes)
        mToastView!!.setMsgColor(mMsgColor)
        mToastView!!.showToast(mDuration)
    }

    /**
     * 自定义完成操作的提示
     */
    fun done() {
        mToastView!!.setBGColor(color.vm_green)
        mToastView!!.setIconRes(drawable.vm_ic_emotion)
        mToastView!!.setMsgColor(color.vm_white)
        mToastView!!.showToast(mDuration)
    }

    /**
     * 自定义错误操作的提示
     */
    fun error() {
        mToastView!!.setBGColor(color.vm_red)
        mToastView!!.setIconRes(drawable.vm_ic_emotion_sad)
        mToastView!!.setMsgColor(color.vm_white)
        mToastView!!.showToast(mDuration)
    }

    companion object {
        // Toast 显示时长
        const val LONG = 5000
        const val SHORT = 3000
        private val instance: VMToast? = null
        private var mActivity: Activity? = null
        private var mToastLayout: View? = null
        private var mToastView: VMToastView? = null

        // 静态可设置参数
        private var mMsg: String? = null
        private var mDuration = SHORT
        private var mBGColor = 0
        private var mIconRes = 0
        private var mMsgColor = 0

        /**
         * 初始化弹出提示样式
         *
         * @param bgRes    背景颜色
         * @param iconRes  图标
         * @param msgColor 文本颜色
         */
        fun init(bgRes: Int, iconRes: Int, msgColor: Int) {
            mBGColor = bgRes
            mIconRes = iconRes
            mMsgColor = msgColor
        }

        /**
         * 根据字符串弹出提醒
         *
         * @param activity 需要展示提醒界面的 Activity 对象
         * @param msg      需要展示的消息
         */
        fun make(activity: Activity?, msg: String?): VMToast {
            mActivity = activity
            mMsg = msg
            initToast()
            return InnerHolder.INSTANCE
        }

        /**
         * 根据资源 id 弹出提醒
         *
         * @param activity 需要展示提醒界面的 Activity 对象
         * @param resId    要展示消息资源 Id
         */
        fun make(activity: Activity?, resId: Int): VMToast {
            return make(activity, VMStr.byRes(resId))
        }

        /**
         * 根据资源 id 弹出 toast
         *
         * @param activity 需要展示提醒界面的 Activity 对象
         * @param resId    要展示提示资源 Id
         * @param duration 显示时长
         */
        fun make(activity: Activity?, resId: Int, duration: Int): VMToast {
            mDuration = duration
            return make(activity, resId)
        }

        /**
         * 根据字符串弹出提醒
         *
         * @param activity 需要展示提醒界面的 Activity 对象
         * @param msg      要展示的提醒内容
         * @param duration 显示时长
         */
        fun make(activity: Activity?, msg: String?, duration: Int): VMToast {
            mDuration = duration
            return make(activity, msg)
        }

        /**
         * 根据格式化字符串弹出提醒
         *
         * @param activity 需要展示提醒界面的 Activity 对象
         * @param msg      要展示的提醒内容
         * @param args     格式化参数
         * @return
         */
        fun make(activity: Activity?, msg: String?, vararg args: Any?): VMToast {
            return make(activity, String.format(msg!!, *args))
        }

        /**
         * 显示前的初始化初始化提醒
         */
        private fun initToast() {
            if (mActivity != null) {
                mToastLayout = mActivity!!.findViewById(id.vm_toast_layout)
                if (mToastLayout == null) {
                    mToastView = VMToastView(mActivity!!)
                    val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    mActivity!!.addContentView(mToastView, lp)
                } else {
                    mToastView = mToastLayout!!.parent as VMToastView
                }
                mToastView!!.setMsg(mMsg)
            }
        }

        /**
         * 是否在展示提醒
         */
        val isShow: Boolean
            get() = if (instance == null || mToastView == null) {
                false
            } else {
                mToastView!!.isShow
            }
    }
}
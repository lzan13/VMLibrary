package com.vmloft.develop.library.tools.widget

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.vmloft.develop.library.tools.R.styleable

/**
 * Created by lzan13 on 2017/11/30.
 *
 * 定时按钮
 */
class VMTimerBtn @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatButton(context, attrs, defStyleAttr) {

    private var mHandler: Handler? = null

    // 按钮默认文本
    private var mBtnText: String? = null

    // 倒计时文本 需为可格式化样式 例: 剩余(%d)
    private var mTimerText: String? = null

    // 倒计时时间， 默认5秒
    private var mMaxTime = 0
    private var mTimerTime = 0
    private var listener: TimerListener? = null

    init {
        handleAttrs(context, attrs)

        mBtnText = this.text.toString()
        mTimerTime = mMaxTime
        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                countDown()
            }
        }
    }

    /**
     * 获取资源属性
     *
     * @param context
     * @param attrs
     */
    private fun handleAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, styleable.VMTimerBtn)
        mTimerText = array.getString(styleable.VMTimerBtn_vm_timer_text)
        mMaxTime = array.getInt(styleable.VMTimerBtn_vm_timer_time, mMaxTime)
    }

    /**
     * 倒计时循环调用
     */
    private fun countDown() {
        if (mTimerTime > 0) {
            text = String.format(mTimerText!!, mTimerTime)
            mHandler?.sendEmptyMessageDelayed(0, 1000)
            mTimerTime--
        } else {
            isEnabled = true
            text = mBtnText
            mTimerTime = mMaxTime
            if (listener != null) {
                listener!!.onTimeOut()
            }
        }
    }

    /**
     * 设置最大时间 单位 秒
     *
     * @param time 时间
     */
    fun setTimerTime(time: Int) {
        mMaxTime = time
        mTimerTime = mMaxTime
    }

    /**
     * 开始倒计时
     */
    fun startTimer() {
        isEnabled = false
        countDown()
    }

    /**
     * 设置定时接口
     */
    fun setTimerListener(listener: TimerListener?) {
        this.listener = listener
    }

    /**
     * 定时接口
     */
    interface TimerListener {
        fun onTimeOut()
    }

}
package com.vmloft.develop.library.tools.widget.toast

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.R.layout
import com.vmloft.develop.library.tools.animator.VMAnimator
import com.vmloft.develop.library.tools.animator.VMAnimator.createAnimator
import com.vmloft.develop.library.tools.animator.VMAnimator.createOptions
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen

/**
 * Create by lzan13 on 2019/5/11 12:34
 *
 * 自定义弹出 toast view
 */
class VMToastView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    // Toast 背景控件
    private var mBGView: View? = null

    // Toast 图标控件
    private var mIconView: ImageView? = null

    // Toast 消息控件
    private var mMsgView: TextView? = null

    // 记录 Toast 是否正在展示
    var isShow = false
        private set
    private var mHeight = 0f
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h.toFloat()
    }

    /**
     * 初始化
     */
    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(layout.vm_widget_toast, this)
        mBGView = findViewById(R.id.vm_toast_layout)
        mIconView = findViewById(R.id.vm_toast_icon_iv)
        mMsgView = findViewById(R.id.vm_toast_msg_tv)
    }

    fun setIconRes(resId: Int) {
        if (resId == 0) {
            mIconView!!.visibility = View.GONE
        } else {
            mIconView!!.visibility = View.VISIBLE
            mIconView!!.setImageResource(resId)
        }
    }

    fun setMsg(msg: String?) {
        if (mMsgView != null) {
            mMsgView!!.text = msg
        }
    }

    fun setMsgColor(resId: Int) {
        if (resId == 0) {
            return
        }
        mIconView!!.setColorFilter(VMColor.byRes(resId))
        mMsgView!!.setTextColor(VMColor.byRes(resId))
    }

    fun setBGColor(resId: Int) {
        if (resId == 0) {
            return
        }
        mBGView!!.setBackgroundResource(resId)
    }

    /**
     * 显示提醒
     *
     * @param duration Toast 显示持续时间
     */
    fun showToast(duration: Int) {
        mHandler.removeMessages(TOAST_REMOVE)
        if (mHeight == 0f) {
            mHeight = VMDimen.dp2px(72).toFloat()
        }
        val options = createOptions(this, VMAnimator.TRANSY, -mHeight, 0.0f)
        createAnimator().play(options).addListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                isShow = true
            }

            override fun onAnimationEnd(animation: Animator) {
                val msg = mHandler.obtainMessage(TOAST_REMOVE)
                mHandler.sendMessageDelayed(msg, duration.toLong())
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        }).start(ANIM_DURATION.toLong())
    }

    /**
     * 移除提醒
     */
    private fun removeToast() {
        val viewGroup = this.parent as ViewGroup
        val options = createOptions(this, VMAnimator.TRANSY, 0.0f, -mHeight)
        createAnimator().play(options).addListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                isShow = false
                viewGroup.removeView(this@VMToastView)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        }).start(ANIM_DURATION.toLong())
    }

    /**
     * 自定义 Handler 实现定时移除提醒
     */
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                TOAST_REMOVE -> removeToast()
            }
        }
    }

    companion object {
        // 移除提醒
        private const val TOAST_REMOVE = 1001

        // 动画时间
        private const val ANIM_DURATION = 225
    }

    init {
        init(context)
    }
}
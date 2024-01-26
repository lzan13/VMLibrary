package com.vmloft.develop.library.tools.widget.tips

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
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen

/**
 * Create by lzan13 on 2019/5/11 12:34
 *
 * 描述：自定义顶部提示 view
 */
class VMTipsBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    // 顶部空间控件
    private var mSpaceView: View? = null

    // 背景控件
    private var mBGView: View? = null

    // 图标控件
    private var mIconView: ImageView? = null

    // 内容控件
    private var mMsgView: TextView? = null

    // 是否正在展示
    var isShow = false
        private set

    private var mHeight = 0f

    companion object {
        // 移除提醒
        private const val removeHint = 1001

        // 动画时间
        private const val animDuration = 225
    }

    /**
     * 初始化
     */
    init {
        LayoutInflater.from(context).inflate(layout.vm_widget_toast, this)
        mSpaceView = findViewById(R.id.vmSpaceView)
        mBGView = findViewById(R.id.vmContainerLL)
        mIconView = findViewById(R.id.vmIconIV)
        mMsgView = findViewById(R.id.vmMsgTV)

        mSpaceView?.layoutParams?.height = VMDimen.statusBarHeight
    }

    /**
     * 设置是否隐藏顶部空间
     */
    fun setHideTop(hideTop: Boolean) {
        mSpaceView?.visibility = if (hideTop) View.GONE else View.VISIBLE
    }

    /**
     * 显示提醒
     *
     * @param msg 显示内容
     * @param duration 显示时长，毫秒值
     * @param iconId 显示图标资源Id
     * @param bgId 显示背景资源 Id
     * @param colorId 显示文本颜色 Id
     */
    fun show(msg: String, duration: Long, iconId: Int, bgId: Int, colorId: Int) {
        mMsgView?.text = msg

        if (iconId != 0) {
            mIconView?.setImageResource(iconId)
        }
        mIconView?.visibility = if (iconId == 0) View.GONE else View.VISIBLE

        mBGView?.setBackgroundResource(if (bgId != 0) bgId else R.color.vm_toast_dark_color)
        mMsgView?.setTextColor(VMColor.byRes(if (colorId != 0) colorId else R.color.vm_toast_dark_color))

        mHandler.removeMessages(removeHint)
        if (mHeight == 0f) {
            mHeight = (VMDimen.dp2px(48) + VMDimen.statusBarHeight).toFloat()
        }
        val options = VMAnimator.AnimOptions(this, floatArrayOf(-mHeight, 0.0f), VMAnimator.transY, animDuration.toLong(), repeat = 0)
        VMAnimator.createAnimator().play(options).addListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                isShow = true
            }

            override fun onAnimationEnd(animation: Animator) {
                val msg = mHandler.obtainMessage(removeHint)
                mHandler.sendMessageDelayed(msg, duration)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        }).start()
    }

    /**
     * 移除提醒
     */
    private fun remove() {
        if (this.parent == null) return

        val viewGroup = this.parent as ViewGroup
        val options = VMAnimator.AnimOptions(this, floatArrayOf(0.0f, -mHeight), VMAnimator.transY, animDuration.toLong(), repeat = 0)
        VMAnimator.createAnimator().play(options).addListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                isShow = false
                viewGroup.removeView(this@VMTipsBar)
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        }).start()
    }

    /**
     * 自定义 Handler 实现定时移除提醒
     */
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                removeHint -> remove()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h.toFloat()
    }
}
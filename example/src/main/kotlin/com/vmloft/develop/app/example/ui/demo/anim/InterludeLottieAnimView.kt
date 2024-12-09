package com.vmloft.develop.app.example.ui.demo.anim

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout

import com.airbnb.lottie.LottieAnimationView

import com.airbnb.lottie.LottieDrawable

import com.vmloft.develop.app.example.databinding.WidgetDemoInterludeLottieAnimViewBinding
import com.vmloft.develop.library.tools.utils.logger.VMLog


/**
 * Author: lzan13
 * Created: 2024/11/14 15:53
 * Desc: 过场动画
 */
class InterludeLottieAnimView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val TAG = this.javaClass.simpleName

    // 过场动画当前状态
    private val interludeStatusBegin = 0
    private val interludeStatusLoop = 1
    private val interludeStatusOver = 2

    // 是否需要中间循环部分
    private var needLoopAnim = false
    private var interludeAnimStatus = interludeStatusBegin

    private var interludeLAV: LottieAnimationView? = null

    private var mBinding = WidgetDemoInterludeLottieAnimViewBinding.inflate(LayoutInflater.from(context), this, true)

    /**
     * 开始过场动画
     */
    fun startAnim(needLoop: Boolean = false) {
        VMLog.i("startAnim needLoop:$needLoop")
        interludeAnimStatus = interludeStatusBegin
        needLoopAnim = needLoop

        interludeLAV = LottieAnimationView(context)
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        interludeLAV?.layoutParams = lp
        interludeLAV?.imageAssetsFolder = "door/images"
        interludeLAV?.setAnimation("door/door.json")
        mBinding.animContainerLL.addView(interludeLAV)

        playBeginAnim()
    }

    /**
     * 结束过场动画
     */
    fun stopAnim() {
        VMLog.i("startAnim interludeAnimStatus:$interludeAnimStatus")
        interludeAnimStatus = interludeStatusOver
    }

    /**
     * 播放开始动画
     */
    private fun playBeginAnim() {
        this.visibility = View.VISIBLE
        interludeLAV?.setMinAndMaxFrame(0, 50)
        interludeLAV?.addAnimatorListener(object : CustomLottieAnimListener() {
            override fun onAnimationEnd(animation: Animator) {
                VMLog.i("playBeginAnim.onAnimationEnd")
                if (needLoopAnim && interludeAnimStatus != interludeStatusOver) {
                    interludeAnimStatus = interludeStatusLoop
                    playLoopAnim()
                } else {
                    playOverAnim()
                }
            }
        })
        interludeLAV?.setRepeatCount(0) // 是否循环
        interludeLAV?.playAnimation()
    }

    /**
     * 播放循环动画
     */
    private fun playLoopAnim() {
        interludeLAV?.imageAssetsFolder = "door/images"
        interludeLAV?.setAnimation("door/door.json")
        interludeLAV?.setMinAndMaxFrame(50, 150)
        interludeLAV?.addAnimatorListener(object : CustomLottieAnimListener() {
            override fun onAnimationEnd(animation: Animator) {
                VMLog.i("playLoopAnim.onAnimationEnd")
                playOverAnim()
            }

            override fun onAnimationCancel(animation: Animator) {
                VMLog.i("playLoopAnim.onAnimationCancel")
                playOverAnim()
            }

            override fun onAnimationRepeat(animation: Animator) {
                VMLog.i("playLoopAnim.onAnimationRepeat")
                if (interludeAnimStatus == interludeStatusOver) {
                    playOverAnim()
                }
            }
        })
        interludeLAV?.repeatCount = LottieDrawable.INFINITE //循环播放
        interludeLAV?.playAnimation()
    }

    /**
     * 播放结束动画
     */
    private fun playOverAnim() {
        interludeLAV?.imageAssetsFolder = "door/images"
        interludeLAV?.setAnimation("door/door.json")
        interludeLAV?.setMinAndMaxFrame(150, 211)
        interludeLAV?.addAnimatorListener(object : CustomLottieAnimListener() {
            override fun onAnimationEnd(animation: Animator) {
                VMLog.i("playOverAnim.onAnimationEnd")
                onAnimOver()
            }
        })
        interludeLAV?.setRepeatCount(0) //循环播放
        interludeLAV?.playAnimation()
    }

    /**
     * 过场动画结束
     */
    private fun onAnimOver() {
        this.visibility = View.GONE

        interludeLAV?.clearAnimation()
        mBinding.animContainerLL.removeView(interludeLAV)
        interludeLAV = null
    }

}
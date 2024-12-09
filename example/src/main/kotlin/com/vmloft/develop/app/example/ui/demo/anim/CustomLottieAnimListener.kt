package com.vmloft.develop.app.example.ui.demo.anim

import android.animation.Animator
import android.animation.Animator.AnimatorListener


/**
 * Author: lzan13
 * Created: 2024/11/14 16:20
 * Desc: Lottie 动画监听接口实现，方便字类少实现一些接口
 */
open class CustomLottieAnimListener : AnimatorListener {

    override fun onAnimationStart(animation: Animator) {
    }

    override fun onAnimationEnd(animation: Animator) {
    }

    override fun onAnimationCancel(animation: Animator) {
    }

    override fun onAnimationRepeat(animation: Animator) {
    }
}
package com.vmloft.develop.library.tools.animator

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorSet
import android.animation.AnimatorSet.Builder
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.view.View
import android.view.animation.LinearInterpolator
import java.util.ArrayList

/**
 * Create by lzan13 on 2019/6/5 15:43
 *
 * 自定义属性动画工具，方便简单实现动画
 */
object VMAnimator {
    // 重复模式
    const val RESTART = 1
    const val REVERSE = 2

    // 无限重复
    const val INFINITE = -1

    // 动画类型
    const val ALPHA = "Alpha"
    const val TRANSX = "TranslationX"
    const val TRANSY = "TranslationY"
    const val SCALEX = "ScaleX"
    const val SCALEY = "ScaleY"
    const val ROTATION = "Rotation"
    const val ROTATIONX = "RotationX"
    const val ROTATIONY = "RotationY"

    // 默认执行时间
    const val mDuration: Long = 1000

    // 创建默认的插值器 LinearInterpolator 前后减速，中间加速
    val mInterpolator: TimeInterpolator = LinearInterpolator()

    /**
     * 创建动画管理者
     */
    @JvmStatic
    fun createAnimator(): AnimatorSetWrap {
        return AnimatorSetWrap()
    }

    /**
     * 创建动画属性，
     */
    fun createOptions(target: Any, anim: String, vararg values: Float): Options {
        return createOptions(target, anim, mDuration, *values)
    }

    /**
     * 创建动画属性，
     */
    fun createOptions(target: Any, anim: String, duration: Long, vararg values: Float): Options {
        return createOptions(target, anim, duration, 0, *values)
    }

    /**
     * 创建动画属性，
     */
    fun createOptions(target: Any, anim: String, duration: Long, repeat: Int, vararg values: Float): Options {
        return Options(target, anim, mInterpolator, duration, repeat, RESTART, 0, values)
    }
    /**
     * 创建动画属性，
     */
    fun createOptions(target: Any, anim: String, duration: Long, repeat: Int, repeatMode:Int, vararg values: Float): Options {
        return Options(target, anim, mInterpolator, duration, repeat, repeatMode, 0, values)
    }

    /**
     * 属性动画组合包装类，对应的是 AnimatorSet 类
     *
     * 它对应的主要有这四个方法，play(开始)，before(在 XXX 之前)，with(与 XXX 同步)，after(在 XXX 之后)
     * 这四个方法里面全都是填入往后儿们的 Animator 类，但是先后执行顺序不一样，
     * 我们注意到他是先执行的 after，然后是 play 和 with 同时执行，最后执行的 before
     * 所以大家记住这个顺序，无论怎么写，都是这个执行顺序。
     */
    class AnimatorSetWrap() {
        /**
         * 获取AnimatorSet的实例
         */
        // 联合动画的动画容器
        private val animatorSet: AnimatorSet

        // 联合动画的动画构造器
        private var mAnimatorBuilder: Builder? = null

        // 判断 play 方法只允许执行一次的布尔值
        var isPlaying = false

        // 是否已经准备好动画
        var isReady = false

        // 顺序播放或者同时播放时存储动画的列表容器
        var mAnimatorList: MutableList<Animator>

        /**
         * 播放多个动画时方法，在其内部生成一个 Animator 并将该 Animator 加入到动画集合中等待播放
         *
         * @param options 动画参数
         */
        fun then(options: Options): AnimatorSetWrap {
            val animator = animator(options)
            mAnimatorList.add(animator)
            return this
        }

        /**
         * 播放单个动画方法，整个动画过程只能调用一次，并且一旦执行 play 方法将会清空动画集合
         *
         * @param options 动画参数
         */
        fun play(options: Options): AnimatorSetWrap {
            if (isPlaying) {
                throw RuntimeException("AnimatorSetWrap.play()方法只能调用一次")
            }
            val animator = animator(options)
            mAnimatorList.clear()
            mAnimatorBuilder = animatorSet.play(animator)
            return this
        }

        /**
         * 封装 AnimatorSet 的 before 方法
         *
         * @param options 动画参数
         */
        fun before(options: Options): AnimatorSetWrap {
            val animator = animator(options)
            mAnimatorBuilder = mAnimatorBuilder!!.before(animator)
            return this
        }

        /**
         * 封装 AnimatorSet 的 with 方法
         *
         * @param options 动画参数
         */
        fun with(options: Options): AnimatorSetWrap {
            val animator = animator(options)
            mAnimatorBuilder = mAnimatorBuilder!!.with(animator)
            return this
        }

        /**
         * 封装 AnimatorSet 的 after 方法
         *
         * @param options 动画参数
         */
        fun after(options: Options): AnimatorSetWrap {
            val animator = animator(options)
            mAnimatorBuilder = mAnimatorBuilder!!.after(animator)
            return this
        }

        /**
         * 统一生成一个动画对象
         *
         * @param options 动画参数
         * @return
         */
        @SuppressLint("ObjectAnimatorBinding")
        private fun animator(options: Options): ObjectAnimator {
            if (options.target == null) {
                throw RuntimeException("执行动画的目标不能为空")
            }
            isPlaying = true
            val animator = ObjectAnimator.ofFloat(options.target, options.anim, *options.values)
            animator.interpolator = options.interpolator
            animator.duration = options.duration
            animator.repeatCount = options.repeat
            animator.repeatMode = options.repeatMode
            animator.startDelay = options.delay.toLong()
            return animator
        }

        /**
         * 设置动画队列开始的延迟
         *
         * @param delay 延迟时间 毫秒值
         */
        fun after(delay: Long): AnimatorSetWrap {
            mAnimatorBuilder!!.after(delay)
            return this
        }

        /**
         * 开始执行动画，该动画操作主要用作执行 AnimatorSet 的组合动画，如果动画列表不为空，则执行逐一播放动画
         */
        fun start() {
            readyAnim(false)
            animatorSet.start()
        }

        /**
         * 指定动画时长播放动画，如果动画列表不为空，则执行逐一播放动画
         *
         * @param duration 动画时长
         */
        fun start(duration: Long) {
            readyAnim(false)
            animatorSet.duration = duration
            animatorSet.start()
        }

        /**
         * 开始执行动画，同时指定是否按照顺序执行
         */
        fun start(together: Boolean) {
            readyAnim(together)
            animatorSet.start()
        }

        /**
         * 在一定时长内运行完该组合动画
         *
         * @param duration 动画时长
         */
        fun start(together: Boolean, duration: Long) {
            readyAnim(together)
            animatorSet.duration = duration
            animatorSet.start()
        }

        /**
         * 延迟一定时长播放动画，如果动画列表不为空，则执行逐一播放动画
         *
         * @param delay 延迟时长
         */
        fun startDelay(delay: Long) {
            readyAnim(false)
            animatorSet.startDelay = delay
            animatorSet.start()
        }

        /**
         * 延迟一定时长播放动画
         *
         * @param delay 延迟时长
         */
        fun startDelay(together: Boolean, delay: Long) {
            readyAnim(together)
            animatorSet.startDelay = delay
            animatorSet.start()
        }

        /**
         * 准备动画，主要是初始化动画播放方式
         *
         * @param together 是否同时播放
         */
        private fun readyAnim(together: Boolean) {
            if (isReady || mAnimatorList.size <= 0) {
                return
            }
            isReady = true
            val set = AnimatorSet()
            if (together) {
                set.playTogether(mAnimatorList)
            } else {
                set.playSequentially(mAnimatorList)
            }
            mAnimatorBuilder!!.before(set)
        }

        /**
         * 暂停动画
         */
        fun resume() {
            animatorSet.resume()
        }

        /**
         * 暂停动画
         */
        fun pause() {
            animatorSet.pause()
        }

        /**
         * 取消动画
         */
        fun cancel() {
            animatorSet.cancel()
            mAnimatorList.clear()
        }

        /**
         * 添加动画监听
         */
        fun addListener(listener: AnimatorListener?): AnimatorSetWrap {
            animatorSet.addListener(listener)
            return this
        }

        /**
         * 移除动画监听
         */
        fun removeListener(listener: AnimatorListener?) {
            animatorSet.removeListener(listener)
        }

        /**
         * 取消全部AnimatorSet的监听
         */
        fun removeAllListeners() {
            animatorSet.removeAllListeners()
        }

        companion object {
            /**
             * 判断一个View是否在当前的屏幕中可见（肉眼真实可见）
             *
             * @return 返回true则可见
             */
            fun isVisibleOnScreen(view: View?): Boolean {
                return if (view == null) {
                    false
                } else view.windowVisibility == View.VISIBLE && view.visibility == View.VISIBLE && view.isShown
            }
        }

        /**
         * 私有构造，主要是负责
         * 1.初始化默认的插值器 mInterpolator
         * 2.初始化联合动画Set mAnimatorSet
         * 3.初始化顺序或同时播放动画容器 mAnimatorList
         */
        init {
            animatorSet = AnimatorSet()
            mAnimatorList = ArrayList(16)
        }
    }

    /**
     * 动画执行参数
     */
    data class Options(
        // 动画执行的目标
        var target: Any? = null,
        // 动画类型
        var anim: String = ALPHA,
        // 动画插值器
        var interpolator: TimeInterpolator?,
        // 动画时长
        var duration: Long = 0,
        // 动画重复次数
        var repeat: Int = 0,
        // 重复模式
        var repeatMode: Int = 0,
        // 动画开始延迟
        var delay: Int = 0,
        // 动画执行值
        var values: FloatArray
    )
}
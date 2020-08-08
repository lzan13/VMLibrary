package com.vmloft.develop.library.tools.widget.barrage

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMSystem
import com.vmloft.develop.library.tools.utils.logger.VMLog
import java.util.concurrent.LinkedBlockingQueue

/**
 * Create by lzan13 on 2020/6/3 17:14
 * 描述：弹幕控件
 */
class VMBarrageView<T> @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    // 默认最大展示 100 个
    private val defaultMaxSize: Int = 100

    // 弹幕动画默认耗时
    private val defaultDuration: Long = 5 * 1000

    // 弹幕之间最大间隔时间
    private val defaultMaxInterval: Long = 100

    // 是否激活中
    private var isActive: Boolean = false
    private var isPause: Boolean = false

    // 最大数量
    private var mMaxSize: Int = defaultMaxSize

    // 活动中的数量
    private var mActiveSize: Int = 0

    // 最后一个弹幕弹出时间
    private var mLastTime: Long = 0

    // View 构建器
    private lateinit var mCreator: VMViewCreator<T>

    // 数据队列
    private lateinit var mDataQueue: LinkedBlockingQueue<T>

    // View 队列，循环利用
    private lateinit var mViewQueue: LinkedBlockingQueue<View>

    // Anim 集合
    private var mAnimList: MutableList<ObjectAnimator> = mutableListOf()

    /**
     * 设置 View 构造器
     */
    fun setCreator(creator: VMViewCreator<T>): VMBarrageView<T> {
        if (isActive) {
            return this
        }
        mCreator = creator
        return this
    }

    /**
     * 设置最大数
     */
    fun setMaxSize(size: Int): VMBarrageView<T> {
        if (isActive) {
            return this
        }
        if (size <= defaultMaxSize) {
            mMaxSize = size
        }
        return this
    }

    /**
     * 开始构建
     */
    fun create(list: MutableList<T>?): VMBarrageView<T> {
        if (isActive) {
            return this
        }
        mDataQueue = LinkedBlockingQueue(mMaxSize * 5)
        if (list !== null) {
            mDataQueue.addAll(list)
        }
        mViewQueue = LinkedBlockingQueue(mMaxSize)
        return this
    }

    /**
     * 添加一个新的弹幕数据
     */
    fun addBarrage(bean: T) {
        if (isActive) {
            mDataQueue.add(bean)
        }
    }

    /**
     * 启动弹幕
     */
    fun start() {
        if (!isActive) {
            isActive = true
            startLoop()
        }
    }

    /**
     * 继续弹幕
     */
    fun resume() {
        isPause = false
        if (isActive) {
            for (anim in mAnimList) {
                anim.resume()
            }
        }
    }

    /**
     * 暂停弹幕
     */
    fun pause() {
        isPause = true
        if (isActive) {
            for (anim in mAnimList) {
                anim.pause()
            }
        }
    }

    /**
     * 停止弹幕
     */
    fun stop() {
        if (isActive) {
            isActive = false

            mDataQueue.clear()
            mViewQueue.clear()
            mAnimList.clear()

            removeAllViews()
        }
    }

    /**
     * 开启循环
     */
    private fun startLoop() {
        VMSystem.runTask(Runnable {
            while (isActive && !isPause) {
                val bean: T = mDataQueue.take()
                var view: View? = mViewQueue.poll()
                if (view === null) {
                    if (mActiveSize < mMaxSize) {
                        view = createView()
                        mViewQueue.put(view)
                    }
                    view = mViewQueue.take()
                }
                var randomTime = (Math.random() * defaultMaxInterval).toLong()
                val intervalTime = System.currentTimeMillis() - mLastTime
                if (intervalTime < randomTime) {
                    Thread.sleep(randomTime - intervalTime)
                }
                mLastTime = System.currentTimeMillis()
                VMSystem.runInUIThread(Runnable {
                    mActiveSize++
                    barrageAnim(view!!, bean)
                })
            }
        })
    }

    /**
     * 弹幕动画
     */
    private fun barrageAnim(view: View, bean: T) {
        // 将 View 加入容器
        addView(view)
        // 绑定数据
        mCreator.onBind(view, bean)

        val width = width.toFloat()
        // 这里高度加上 48dp 防止随机到最底部时把内容挤压
        val height = height.toFloat() - VMDimen.dp2px(48)

        // 随机设置位置
        val randomTop = (Math.random() * height).toInt()
        val params: LayoutParams = view.layoutParams as LayoutParams
        params.topMargin = randomTop
        view.layoutParams = params

        val randomDuration = (Math.random() * defaultDuration + defaultDuration).toInt()
        val anim = ObjectAnimator.ofFloat(view, "translationX", width, -width)
        anim.duration = randomDuration.toLong()
        anim.interpolator = LinearInterpolator()
        anim.addListener(object : AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                // 结束之后移除 view 并添加到缓存
                removeView(view)
                mViewQueue.put(view)
                mDataQueue.put(bean)
                mAnimList.remove(animation)
                mActiveSize--
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationStart(animation: Animator) {}
        });
        mAnimList.add(anim)
        anim.start()
    }

    /**
     * 创建弹幕
     */
    private fun createView(): View {
        return LayoutInflater.from(context)
            .inflate(mCreator.layoutId(), null)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        VMLog.d("onVisibilityChanged $visibility")
        if (visibility === 0) {
            // View 可见 继续
            resume()
        } else {
            // View 不可见 暂停
            pause()
        }
    }
}
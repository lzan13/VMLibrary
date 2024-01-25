package com.vmloft.develop.library.tools.voice.player

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMSystem
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderView


/**
 * Created by lzan13 on 2024/01/11
 * 描述：自定义声音波形控件，这里根据录音分贝数据显示
 */
class VMWaveformView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private var mWidth = 0
    private var mHeight = 0

    // 画笔
    private var mPaint: Paint

    // 波纹颜色
    private var lineBGColor = VMColor.byRes(R.color.vm_gray)
    private var lineFGColor = VMColor.byRes(R.color.vm_white)
    private var lineWidth = VMDimen.dp2px(2)
    private var lineSpace = VMDimen.dp2px(1)

    // 进度 0-100
    private var mCurrentProgress = 0f
    private var mMaxProgress = 100f

    // 当前进度宽度
    private var mCurrentProgressWidth = 0f

    // 进度回调
    private var mWaveformActionListener: WaveformActionListener? = null

    // 状态
    private var status = VMVoicePlayer.statusIdle

    // 是否再拖动中
    private var isDrag = false

    // 声音数据 Bean
    private var voiceBean: VMRecorderView.VoiceBean? = null

    /**
     * 手势处理
     */
    private val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            performClick()
            return true
        }

        override fun onLongPress(event: MotionEvent) {
            mWaveformActionListener?.onLongPress(event)
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            isDrag = true
            parent.requestDisallowInterceptTouchEvent(true)
            // 计算出拖动进度
            mCurrentProgress = if (e2.x < 0) {
                0f
            } else if (e2.x > mWidth) {
                mMaxProgress
            } else {
                e2.x * mMaxProgress / mWidth
            }
            // 对应到宽度
            mCurrentProgressWidth = if (mCurrentProgress > 0) {
                mCurrentProgress * mWidth / mMaxProgress
            } else {
                mWidth.toFloat()
            }

            invalidate()
            return true
        }

    })

    /**
     * 初始化控件
     */
    init {
        // 启用长按事件
        gestureDetector.setIsLongpressEnabled(true)
        // 获取控件属性
        handleAttrs(attrs)

        // 获得绘制文本的宽和高
        mPaint = Paint()
        mPaint.isAntiAlias = true // 设置抗锯齿
        mPaint.strokeWidth = lineWidth.toFloat()
        mPaint.style = Paint.Style.FILL
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
    }

    /**
     * 获取控件属性
     */
    private fun handleAttrs(attrs: AttributeSet?) {
        // 获取控件的属性值
        if (attrs == null) {
            return
        }
        val array = context.obtainStyledAttributes(attrs, R.styleable.VMVoiceView)

        lineBGColor = array.getColor(R.styleable.VMVoiceView_vm_line_bg_color, lineBGColor)
        lineFGColor = array.getColor(R.styleable.VMVoiceView_vm_line_fg_color, lineFGColor)
        lineWidth = array.getDimensionPixelOffset(R.styleable.VMVoiceView_vm_line_width, lineWidth)
        lineSpace = array.getDimensionPixelOffset(R.styleable.VMVoiceView_vm_line_space, lineSpace)

        array.recycle()
    }

    /**
     * 设置声音数据 Bean
     */
    fun setVoiceBean(bean: VMRecorderView.VoiceBean) {
        voiceBean = bean
        // 将时间设置为最大进度
        mMaxProgress = bean.duration.toFloat()

        if (bean.decibelList.isEmpty()) {
            // TODO 根据声音文件加载声音分贝波形数据
            loadDecibelList()
        }
        mWidth = (voiceBean!!.decibelList.size + 1) * (lineWidth + lineSpace)
        mCurrentProgressWidth = mWidth.toFloat()
    }

    fun updateVoiceBean(bean: VMRecorderView.VoiceBean) {
        voiceBean = bean
        // 将时间设置为最大进度
        mMaxProgress = bean.duration.toFloat()

        if (bean.decibelList.isEmpty()) {
            // TODO 根据声音文件加载声音分贝波形数据
            loadDecibelList()
        }
        mWidth = (bean.decibelList.size + 1) * (lineWidth + lineSpace)
        mCurrentProgressWidth = mWidth.toFloat()

        setMeasuredDimension(mWidth, mHeight)

        invalidate()
    }


    /**
     * 开始
     */
    fun start() {
        status = VMVoicePlayer.statusPlaying

        // 首次开始变化，先将进度宽度置为 0
        if (mCurrentProgress == 0f) {
            mCurrentProgressWidth = 0f
        }
        invalidate()
    }

    /**
     * 暂定
     */
    fun pause() {
        status = VMVoicePlayer.statusPause

        calculateProgressWidth()

        invalidate()
    }

    /**
     * 停止
     */
    fun stop() {
        status = VMVoicePlayer.statusIdle

        mCurrentProgress = 0f
        calculateProgressWidth()

        invalidate()
    }

    /**
     * 计算进度宽度
     */
    private fun calculateProgressWidth() {
        // 计算当前进度对应到宽度
        mCurrentProgressWidth = if (mCurrentProgress > 0) {
            mCurrentProgress * mWidth / mMaxProgress
        } else {
            mWidth.toFloat()
        }
    }

    /**
     * 更新进度
     */
    fun updateProgress(progress: Float) {
        if (!isDrag) {
            mCurrentProgress = progress
            if (mCurrentProgress == 0f) {
                mCurrentProgressWidth = mWidth.toFloat()
                postInvalidate()
            } else {
                // 开始动画更新进度
                startProgressAnim(progress)
            }
        }
    }

    /**
     * 设置最大进度
     */
    fun setMaxProgress(progress: Float) {
        mMaxProgress = progress
    }

    /**
     * 获取进度
     */
    fun getProgress(): Float {
        return mCurrentProgress
    }

    /**
     * 设置进度回调
     */
    fun setWaveformActionListener(listener: WaveformActionListener) {
        mWaveformActionListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 获取测量到的高度
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec))
        var childWidthSize = measuredWidth
        var childHeightSize = measuredHeight

        // 计算真实高度，并设置回去
        var realWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY)
        var realHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY)
        //设定宽高
        super.onMeasure(realWidthMeasureSpec, realHeightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (voiceBean == null) return

        // 绘制波形条纹
        drawWaveformBackground(canvas)

        // 绘制进度线
        drawProgressLine(canvas)
    }

    /**
     * 绘制波形进度背景
     */
    private fun drawWaveformBackground(canvas: Canvas) {
        // 绘制线条进度背景
        val lineBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        val lineCanvas = Canvas(lineBitmap)
        val linePaint = Paint()
        linePaint.color = lineBGColor
        linePaint.isAntiAlias = true // 设置抗锯齿
        linePaint.strokeWidth = lineWidth.toFloat()
        linePaint.style = Paint.Style.FILL
        linePaint.strokeJoin = Paint.Join.ROUND
        linePaint.strokeCap = Paint.Cap.ROUND
        voiceBean?.decibelList?.forEachIndexed { index, decibel ->
            val centerY = mHeight / 2f
            val decibelAmplitude = (decibel - 40)
            val startX = (index + 1) * (lineWidth + lineSpace).toFloat()
            val startY = centerY - decibelAmplitude
            val endX = startX
            val endY = centerY + decibelAmplitude
            lineCanvas.drawLine(startX, startY, endX, endY, linePaint)
        }
        canvas.drawBitmap(lineBitmap, 0f, 0f, mPaint)

        // 开启离屏缓冲
        val saveLayer = canvas.saveLayer(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), mPaint)

        canvas.drawBitmap(lineBitmap, 0f, 0f, mPaint)

        // 绘制线条进度前景
        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        mPaint.color = lineFGColor
        val bgBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        val bgCanvas = Canvas(bgBitmap)
        val bgPaint = Paint()
        bgPaint.color = lineFGColor
        bgPaint.isAntiAlias = true // 设置抗锯齿
        bgPaint.strokeWidth = lineWidth.toFloat()
        bgPaint.style = Paint.Style.FILL
        bgPaint.strokeJoin = Paint.Join.ROUND
        bgPaint.strokeCap = Paint.Cap.ROUND

        bgCanvas.drawRect(0f, 0f, mCurrentProgressWidth, mHeight.toFloat(), bgPaint)
        // 将背景绘制到 canvas
        canvas.drawBitmap(bgBitmap, 0f, 0f, mPaint)

        // 清楚 Xfermode
        mPaint.setXfermode(null)
        // 关闭离屏缓冲
        canvas.restoreToCount(saveLayer)
    }

    /**
     * 绘制进度线 TODO
     */
    private fun drawProgressLine(canvas: Canvas) {

    }

    /**
     * 进度动画
     */
    private fun startProgressAnim(progress: Float) {
        VMSystem.runInUIThread({
            // 现根据进度计算出需要的宽度
            val progressWidth = if (progress > 0) {
                progress * mWidth / mMaxProgress
            } else {
                mWidth.toFloat()
            }
            // 后续直接使用宽度了，这里直接赋值给当前进度
            mCurrentProgress = progress
            val mAnimator = ValueAnimator.ofFloat(mCurrentProgressWidth, progressWidth)
            mAnimator.duration = VMVoicePlayer.progressAnimDuration
            mAnimator.repeatCount = 0
            mAnimator.interpolator = LinearInterpolator()
            mAnimator.addUpdateListener { a: ValueAnimator ->
                if (status == VMVoicePlayer.statusPlaying && !isDrag) {
                    // 动画大小根据回调变化
                    mCurrentProgressWidth = a.animatedValue as Float

                    invalidate()
                }
            }
            mAnimator.start()
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when (action) {
            MotionEvent.ACTION_UP -> {
                isDrag = false
                // 拖动结束，回调当前进度
                mWaveformActionListener?.onProgressChange(mCurrentProgress)
            }
        }
        return gestureDetector.onTouchEvent(event)
    }

    /**
     * 根据声音文件加载分贝集合 TODO
     */
    private fun loadDecibelList() {}

    /**
     * 录音控件的回调接口，用于回调给调用者录音结果
     */
    interface WaveformActionListener {

        /**
         * 进度变化
         */
        fun onProgressChange(progress: Float)

        /**
         * 长按
         */
        fun onLongPress(event: MotionEvent) {}

    }
}
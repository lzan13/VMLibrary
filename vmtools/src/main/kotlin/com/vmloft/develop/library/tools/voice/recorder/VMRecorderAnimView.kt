package com.vmloft.develop.library.tools.voice.recorder

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

import com.vmloft.develop.library.tools.R
import com.vmloft.develop.library.tools.utils.VMColor
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMSystem


/**
 * Created by lzan13 on 2024/01/11
 * 描述：自定义录音波形控件，根据录音分贝变化
 */
open class VMRecorderAnimView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    protected var mWidth = 0
    protected var mHeight = 0

    // 画笔
    protected var mPaint: Paint

    protected var mBGColor = 0

    // 渐变色
    protected var mStartColor = 0xfafafa
    protected var mEndColor = 0xfafafa

    // 波形宽度
    protected var mLineWidth = VMDimen.dp2px(1)
    protected var mLineHeight = VMDimen.dp2px(1)

    // 波形线集合
    protected var lineList = mutableListOf<WaveformBean>()

    /**
     * 初始化控件
     */
    init {
        //禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        // 获取控件属性
        handleAttrs(attrs)

        // 画笔
        mPaint = Paint()
        // 设置抗锯齿
        mPaint.isAntiAlias = true
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

        mBGColor = array.getColor(R.styleable.VMVoiceView_vm_bg_color, mBGColor)

        mStartColor = array.getColor(R.styleable.VMVoiceView_vm_anim_start_color, mStartColor)
        mEndColor = array.getColor(R.styleable.VMVoiceView_vm_anim_end_color, mEndColor)

        mLineWidth = array.getDimensionPixelOffset(R.styleable.VMVoiceView_vm_anim_line_width, mLineWidth)
        mLineHeight = array.getDimensionPixelOffset(R.styleable.VMVoiceView_vm_anim_line_height, mLineHeight)
        array.recycle()
    }

    /**
     * 重写父类的 onSizeChanged 方法，检测控件宽高的变化
     *
     * @param w    控件当前宽
     * @param h    控件当前高
     * @param oldw 控件原来的宽
     * @param oldh 控件原来的高
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    /**
     * 更新分贝大小
     */
    open fun updateDecibel(decibel: Int) {
//        VMLog.i("updateDecibel $decibel")
        if (lineList.isEmpty()) {
            val spaceWidth = (mWidth - VMDimen.dp2px(10) * 2 - mLineWidth * 19) / 18
            for (i in 0..18) {
                val waveformBean = WaveformBean()
                waveformBean.width = mLineWidth.toFloat()
                waveformBean.height = mLineHeight.toFloat()
                waveformBean.centerX = VMDimen.dp2px(10) + i * spaceWidth + mLineWidth / 2f + i * mLineWidth
                waveformBean.centerY = mHeight / 2f
                when (i) {
                    4, 14 -> waveformBean.scale = 0.5f
                    5, 13 -> waveformBean.scale = 1f
                    6, 12 -> waveformBean.scale = 1.5f
                    7, 11 -> waveformBean.scale = 3f
                    8, 10 -> waveformBean.scale = 5f
                    9 -> waveformBean.scale = 8f
                    else -> waveformBean.scale = 0f
                }
                lineList.add(waveformBean)
            }
        }
        startLineAnim(decibel)
    }

    /**
     * 更新频谱数据
     */
    open fun updateFFTData(data: DoubleArray) {
//        VMLog.i("updateFSData $data")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制背景
        drawBackground(canvas)
        // 绘制波形线
        drawWaveformLine(canvas)
    }

    /**
     * 绘制背景
     */
    open fun drawBackground(canvas: Canvas) {
        if (mBGColor > 0) {
            canvas.drawColor(mBGColor)
        }
    }

    /**
     * 绘制波形线
     */
    open fun drawWaveformLine(canvas: Canvas) {
        // 开启离屏缓冲
        val saveLayer = canvas.saveLayer(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), mPaint)

        val bgBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        val bgCanvas = Canvas(bgBitmap)
        val bgPaint = Paint()
        // 绘制渐变背景
        val gradient = LinearGradient(0f, 0f, mWidth / 2.0f, 0f, mStartColor, mEndColor, Shader.TileMode.MIRROR)
        bgPaint.setShader(gradient)
        bgPaint.style = Paint.Style.FILL
        bgCanvas.drawRect(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), bgPaint)
        // 将背景绘制到 canvas
        canvas.drawBitmap(bgBitmap, 0f, 0f, mPaint)

        // 绘制线条
        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        mPaint.color = mStartColor

        val lineBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
        val lineCanvas = Canvas(lineBitmap)
        val linePaint = Paint()
        lineList.forEach { waveformBean ->
//            val animHeight = waveformBean.height + recordDecibel * waveformBean.scale
            val left = waveformBean.centerX - waveformBean.width / 2f
            val right = waveformBean.centerX + waveformBean.width / 2f
            val top = waveformBean.centerY - waveformBean.animHeight / 2f
            val bottom = waveformBean.centerY + waveformBean.animHeight / 2f
            val rectF = RectF(left, top, right, bottom)
            lineCanvas.drawRoundRect(rectF, mLineWidth.toFloat(), mLineWidth.toFloat(), linePaint)
        }
        canvas.drawBitmap(lineBitmap, 0f, 0f, mPaint)

        // 清除 Xfermode
        mPaint.setXfermode(null)
        // 关闭离屏缓冲
        canvas.restoreToCount(saveLayer)
    }

    /**
     * 波形平滑变换动画
     */
    private fun startLineAnim(decibel: Int) {
        VMSystem.runInUIThread({
            lineList.forEach {
                // 计算波形振幅最大高度
                val maxHeight = it.height + if (decibel > 45) (decibel - 45) * it.scale else decibel / 2 * it.scale
                val mAnimator = ValueAnimator.ofFloat(it.animHeight, maxHeight)
                mAnimator.duration = VMRecorderManager.sampleTime.toLong()
                mAnimator.repeatCount = 0
                mAnimator.interpolator = LinearInterpolator()
                mAnimator.addUpdateListener { a: ValueAnimator ->
                    // 动画大小根据回调变化，不能超过控件高度
                    val value = a.animatedValue as Float
                    it.animHeight = if (value >= mHeight) mHeight.toFloat() else value
                    invalidate()
                }
                mAnimator.start()
            }
        })
    }
}

/**
 * 波形控件属性类
 */
data class WaveformBean(
    // 中心点
    var centerX: Float = 0f,
    var centerY: Float = 0f,
    // 宽高
    var width: Float = VMDimen.dp2px(1).toFloat(),
    var height: Float = VMDimen.dp2px(1).toFloat(),
    var animHeight: Float = height,
    // 缩放倍数
    var scale: Float = 1f
) {}
package com.vmloft.develop.library.tools.recorder

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
 * 描述：自定义录音波形控件，根据音量变化
 */
class VMRecorderAnimView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private var mWidth = 0
    private var mHeight = 0

    // 画笔
    private var mPaint: Paint

    private var mBGColor = 0xfafafa

    // 渐变色
    private var mStartColor = 0xfafafa
    private var mEndColor = 0xfafafa

    // 波形宽度
    private var mBaseWidth = VMDimen.dp2px(5)
    private var mBaseHeight = VMDimen.dp2px(8)

    private var voiceDecibel = 1 // 声音分贝

    // 波形线集合
    private var lineList = mutableListOf<WaveformBean>()

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

        mBaseWidth = array.getDimensionPixelOffset(R.styleable.VMVoiceView_vm_anim_base_width, mBaseWidth)
        mBaseHeight = array.getDimensionPixelOffset(R.styleable.VMVoiceView_vm_anim_base_height, mBaseWidth)
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
        val spaceWidth = (mWidth - VMDimen.dp2px(10) * 2 - mBaseWidth * 19) / 18
        for (i in 0..18) {
            val waveformBean = WaveformBean()
            waveformBean.centerX = VMDimen.dp2px(10) + i * spaceWidth + mBaseWidth / 2 + i * mBaseWidth
            waveformBean.centerY = mHeight / 2
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

    /**
     * 更新分贝大小
     */
    fun updateDecibel(decibel: Int) {
//        VMLog.i("updateDecibel $decibel")
        startLineAnim(decibel)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        // 绘制背景
        drawBackground(canvas)
        // 绘制波形线
        drawLine(canvas)

    }

    /**
     * 绘制背景
     */
    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(mBGColor)
    }

    /**
     * 绘制波形线
     */
    private fun drawLine(canvas: Canvas) {
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
            lineCanvas.drawRoundRect(rectF, mBaseWidth.toFloat(), mBaseWidth.toFloat(), linePaint)
        }
        canvas.drawBitmap(lineBitmap, 0f, 0f, mPaint)

        // 清楚 Xfermode
        mPaint.setXfermode(null)
        // 关闭离屏缓冲
        canvas.restoreToCount(saveLayer)
    }

    /**
     * 绘制测试文本
     */
    private fun drawTest(canvas: Canvas) {

        // 左侧
        val paint = Paint()
        // 开启离屏缓冲
        val saveLayer = canvas.saveLayer(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), paint)

        // 绘制渐变背景
        val gradient1 = LinearGradient(0f, 0f, mWidth / 4.0f, 0f, mStartColor, mEndColor, Shader.TileMode.MIRROR)
        paint.setShader(gradient1)
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, mWidth / 2f, mHeight.toFloat(), paint)

        paint.color = VMColor.byRes(R.color.vm_red)
        paint.textSize = VMDimen.dp2px(14).toFloat()
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        val decibel = "current decibel: ${voiceDecibel}"
        val decibelWidth = VMDimen.getTextWidth(paint, decibel)
        val decibelHeight = VMDimen.getTextHeight(paint, decibel)

        val centerX1 = mWidth / 4 - decibelWidth / 2
        val centerY1 = mHeight / 2 - decibelHeight / 2
        canvas.drawText(decibel, centerX1, centerY1, paint)

        paint.setXfermode(null)

        canvas.restoreToCount(saveLayer)

        // 右侧
        val paint2 = Paint()

        // 开启离屏缓冲
        val saveLayer2 = canvas.saveLayer(0f, 0f, mWidth.toFloat(), mHeight.toFloat(), paint)

        paint2.textSize = VMDimen.dp2px(14).toFloat()
        // 绘制渐变背景
        val gradient2 = LinearGradient(0f, 0f, mWidth / 4.0f, 0f, mStartColor, mEndColor, Shader.TileMode.MIRROR)
        paint2.setShader(gradient2)
        paint2.style = Paint.Style.FILL
        canvas.drawRect(mWidth / 2f, 0f, mWidth.toFloat(), mHeight.toFloat(), paint2)

        paint2.color = VMColor.byRes(R.color.vm_blue)
        paint2.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_IN))
        val centerX2 = mWidth / 4 * 3 - decibelWidth / 2
        val centerY2 = mHeight / 2 - decibelHeight / 2
        canvas.drawText(decibel, centerX2, centerY2, paint2)

        paint2.setXfermode(null)

        canvas.restoreToCount(saveLayer2)

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
                mAnimator.duration = VMRecorderManager.sampleTime
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
    var centerX: Int = 0,
    var centerY: Int = 0,
    // 宽高
    var width: Float = VMDimen.dp2px(5).toFloat(),
    var height: Float = VMDimen.dp2px(8).toFloat(),
    var animHeight: Float = height,
    // 缩放倍数
    var scale: Float = 1f
) {}
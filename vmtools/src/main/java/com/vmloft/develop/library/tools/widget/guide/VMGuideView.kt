package com.vmloft.develop.library.tools.widget.guide

import android.widget.FrameLayout
import android.graphics.*
import android.text.Layout
import android.text.TextPaint
import android.view.ViewGroup
import android.text.StaticLayout
import android.view.MotionEvent
import android.view.View

import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.utils.VMDimen

/**
 * Create by lzan13 on 2022/4/23
 * 描述：自定义引导控件
 */
class VMGuideView(private val params: GuideParams) : View(params.activity) {

    private var paint: Paint = Paint()
    private var isNeedInit = true
    private var mCurrIndex = 0
    private var textPaint: TextPaint = TextPaint()

    private val space = VMDimen.dp2px(16)

    /**
     * 初始化
     */
    init {
        paint.isAntiAlias = true

        textPaint.color = Color.WHITE
        textPaint.textSize = VMDimen.sp2px(14)
        textPaint.isAntiAlias = true

        val lp = FrameLayout.LayoutParams(if (params.width == 0) ViewGroup.LayoutParams.MATCH_PARENT else params.width,
            if (params.height == 0) ViewGroup.LayoutParams.MATCH_PARENT else params.height)
        layoutParams = lp
        mCurrIndex = 0
    }

    /**
     * 初始化所有targetView的位置信息，在onDraw初始的原因是控件都已经测量完毕
     */
    private fun initRect() {
        if (!isNeedInit) return
        isNeedInit = false
        if (params.guideItems.size > 0) {
            for (viewParams in params.guideItems) {
                val locations = IntArray(2)
                viewParams.targetView.getLocationOnScreen(locations)
                val width = viewParams.targetView.measuredWidth
                val height = viewParams.targetView.measuredHeight
                viewParams.rect = Rect(locations[0], locations[1], locations[0] + width, locations[1] + height)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        initRect()

        val canvasWidth = canvas.width
        val canvasHeight = canvas.height

        //绘制背景
        val layerId = canvas.saveLayer(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        paint.color = params.bgColor
        canvas.drawRect(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat(), paint!!)
        if (params.guideItems.size <= 0) {
            canvas.restoreToCount(layerId)
            return
        }
        if (mCurrIndex >= params.guideItems.size) {
            canvas.restoreToCount(layerId)
            return
        }
        if (params.oneByOne) {
            //只绘制当前引导
            val item = params.guideItems[mCurrIndex]
            drawShape(item, canvas)
            if (item.guideRes == 0) {
                drawText(item, canvas, canvasWidth, canvasHeight)
            } else {
                drawGuide(item, canvas)
            }
        } else {
            //绘制所有引导
            for (item in params.guideItems) {
                drawShape(item, canvas)
                if (item.guideRes == 0) {
                    drawText(item, canvas, canvasWidth, canvasHeight)
                } else {
                    drawGuide(item, canvas)
                }
            }
        }
        canvas.restoreToCount(layerId)
    }

    /**
     * 绘制遮罩空白形状
     */
    private fun drawShape(item: GuideItem, canvas: Canvas) {
        if (item.shape == VMShape.guideShapeCircle) drawCircle(canvas, item.rect) else drawRect(canvas, item.rect)
    }

    /**
     * 位置会根据 targetView 位置自动调整
     * 没有设置 guideRes 是默认绘制提示文字
     *
     * @param item 当前引导图参数
     */
    private fun drawText(item: GuideItem, canvas: Canvas, canvasWidth: Int, canvasHeight: Int) {
        if (item.guideRes != 0) return
        val des = item.guideTips

        // 中心点坐标
        val centerX = item.rect.left + item.rect.width() / 2
        val centerY = item.rect.top + item.rect.height() / 2

        var x = if (centerX <= canvasWidth / 2) {
            centerX + item.offX
        } else {
            canvasWidth - centerX + item.rect.width() / 2 + item.offX
        }

        var column = 0
        val split = des.split("\n").toTypedArray()
        if (split.isNotEmpty()) column = split.size - 1

        var y = if (centerY <= canvasHeight / 2) {
            centerY + item.rect.height() / 2 + item.offY
        } else {
            centerY - item.rect.height() * 3 / 2 - textPaint.textSize * column + item.offY
        }

        val layout = StaticLayout(des, textPaint, canvasWidth - x - space,
            Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true)
        canvas.save()
        // 从 x,y 坐标开始画
        canvas.translate(x.toFloat(), y.toFloat())
        layout.draw(canvas)
        // 别忘了restore
        canvas.restore()
    }

    /**
     * 绘制引导图层和挖洞
     *
     * @param item 当前引导参数
     */
    private fun drawGuide(item: GuideItem, canvas: Canvas) {
        /**
         * targetView 所处位置矩形信息，用来确定绘制引导图片位置，虽然会根据 targetView 位置自动调整
         * 如果 targetView 左边空间较大引导图绘制在 targetView 左边，否则相反
         * 如果 targetView 上边空间较大引导图绘制在 targetView 上边，否则反之
         */
        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, item.guideRes) ?: return
        var len1 = 0
        var len2 = 0
//        if (p.shape == State.OVAL) {
//            len1 = (((p.rect.right - p.rect.left) * Math.sqrt(2.0) - (p.rect.right - p.rect.left)) / 2).toInt()
//            len2 = (((p.rect.bottom - p.rect.top) * Math.sqrt(2.0) - (p.rect.bottom - p.rect.top)) / 2).toInt()
//        } else
        if (item.shape == VMShape.guideShapeCircle) {
            val radius =
                Math.sqrt(((item.rect.right - item.rect.left) * (item.rect.right - item.rect.left) + (item.rect.bottom - item.rect.top) * (item.rect.bottom - item.rect.top)).toDouble()).toInt()
            len1 = (radius - (item.rect.right - item.rect.left)) / 2
            len2 = (radius - (item.rect.bottom - item.rect.top)) / 2
        }
        val x = if (item.rect.left > canvas.width - item.rect.right) {
            item.rect.left - len1 - bitmap.width + (item.rect.right - item.rect.left) / 2 + item.offX
        } else {
            item.rect.right + len2 - (item.rect.right - item.rect.left) / 2 + item.offX
        }
        val y = if (item.rect.top > canvas.height - item.rect.bottom) {
            item.rect.top - len2 - bitmap.height + item.offY
        } else {
            item.rect.bottom + len2 + item.offY
        }
        canvas.drawBitmap(bitmap, x.toFloat(), y.toFloat(), paint)
        bitmap.recycle()
    }

    /**
     * 挖椭圆洞
     *
     * @param rect ： targetView位置信息
     */
    private fun drawOval(canvas: Canvas, rect: Rect) {
        //挖洞
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        //算出targetView外接椭圆的短轴和长轴
        val len1 = (((rect.right - rect.left) * Math.sqrt(2.0) - (rect.right - rect.left)) / 2).toInt()
        val len2 = (((rect.bottom - rect.top) * Math.sqrt(2.0) - (rect.bottom - rect.top)) / 2).toInt()
        canvas.drawOval(RectF((rect.left - len1).toFloat(), (rect.top - len2).toFloat(), (rect.right + len1).toFloat(), (rect.bottom + len2).toFloat()), paint!!)
        paint.xfermode = null
    }

    /**
     * 挖圆形洞
     *
     * @param rect ： targetView位置信息
     */
    private fun drawCircle(canvas: Canvas, rect: Rect) {
        //挖洞
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        //绘制圆形，算出外接圆的半径
        canvas.drawCircle(
            ((rect.left + rect.right) / 2).toFloat(),
            ((rect.top + rect.bottom) / 2).toFloat(),
            (Math.sqrt(((rect.right - rect.left) * (rect.right - rect.left) + (rect.bottom - rect.top) * (rect.bottom - rect.top)).toDouble()).toInt() / 2).toFloat(),
            paint)
        paint.xfermode = null
    }

    /**
     * 挖矩形洞
     *
     * @param rect ： targetView位置信息
     */
    private fun drawRect(canvas: Canvas, rect: Rect) {
        //挖洞
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawRect(rect, paint)
        paint.xfermode = null
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        isNeedInit = true
    }

    /**
     * 重写事件分发
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        VMLog.i("touchTouchEvent: " + (MotionEvent.ACTION_DOWN == ev.action))
        //如果除了targetView也可以触发事件，点击任意区域都将进入下一个引导
        if (params.outsideTouchable) {
            if (MotionEvent.ACTION_DOWN == ev.action) onNext()
            return true
        }
        var touchable = false
        //如果是oneByOne只有点击当前targetView才可以触发事件，进入下一个引导
        if (params.oneByOne) {
            if (params.guideItems.size > 0 && mCurrIndex < params.guideItems.size) {
                val p = params.guideItems[mCurrIndex]
                if (ev.x > p.rect.left && ev.x < p.rect.right && ev.y > p.rect.top && ev.y < p.rect.bottom) {
                    touchable = true
                }
            }
        } else {
            //如果不是oneByOne点击所有targetView都可以触发事件，进入下一个引导
            if (params.guideItems.size > 0) {
                for (p in params.guideItems) {
                    if (ev.x > p.rect.left && ev.x < p.rect.right && ev.y > p.rect.top && ev.y < p.rect.bottom) {
                        touchable = true
                        break
                    }
                }
            }
        }
        if (MotionEvent.ACTION_DOWN == ev.action && touchable) onNext()
        return true
    }

    /**
     * 触发事件，下一步引导或者结束引导
     */
    private fun onNext() {
        if (!params.oneByOne) {
            onFinish()
        } else if (mCurrIndex < params.guideItems.size - 1) {
            mCurrIndex++
            invalidate()
            params.guideListener?.onNext(mCurrIndex)
        } else {
            onFinish()
        }
    }

    /**
     * 结束引导
     */
    private fun onFinish() {
        // 移除自身
        val rootView = this.parent as ViewGroup
        rootView.removeView(this)

        params.guideListener?.onFinish()
    }

    /**
     * 引导回调接口
     */
    interface GuideListener {
        // 进行下一个
        fun onNext(index: Int)

        // 引导结束
        fun onFinish()
    }
}
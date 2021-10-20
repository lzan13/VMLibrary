package com.vmloft.develop.library.example.ui.demo.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Message
import android.view.*
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.vmloft.develop.library.common.utils.show

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.common.SPManager
import com.vmloft.develop.library.tools.service.VMATypeFind
import com.vmloft.develop.library.example.ui.widget.TBLiveMsgETDialog
import com.vmloft.develop.library.tools.utils.logger.VMLog


/**
 * Create by lzan13 on 2021/8/13
 * 描述：
 */
@SuppressLint("StaticFieldLeak")
object TBLiveMsgManager {

    private lateinit var windowManager: WindowManager
    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var context: Context
    private lateinit var service: TBLiveMsgService

    private var floatView: View? = null

    private var isInit: Boolean = false

    private var isStart: Boolean = false

    fun init(context: Context) {
        if (isInit) {
            return
        }
        isInit = true
        this.context = context
        service = context as TBLiveMsgService
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    /**
     * 开始展示悬浮窗
     */
    fun setupFloatWindow() {
        if (floatView != null) {
            return
        }
        layoutParams = WindowManager.LayoutParams()
        // 位置为右侧顶部
        layoutParams.gravity = Gravity.START or Gravity.TOP
        // 设置宽高自适应
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

        // 设置悬浮窗透明
        layoutParams.format = PixelFormat.TRANSPARENT

        // 设置窗口类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }

        // 设置窗口标志类型，其中 FLAG_NOT_FOCUSABLE 是放置当前悬浮窗拦截点击事件，造成桌面控件不可操作
        // 会拦截输入框获取焦点
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL

        // 获取要现实的布局
        floatView = LayoutInflater.from(context).inflate(R.layout.widget_tb_live_msg_float_view, null)
        // 添加悬浮窗 View 到窗口
        windowManager.addView(floatView, layoutParams)

        // 当点击悬浮窗时，返回到通话界面
        floatView?.setOnClickListener {
            val intent = Intent(context, TBLiveMsgActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        //设置监听浮动窗口的触摸移动
        floatView?.setOnTouchListener(object : View.OnTouchListener {
            var result = false
            var x = 0f
            var y = 0f
            var startX = 0f
            var startY = 0f
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        result = false
                        x = event.x
                        y = event.y
                        startX = event.rawX
                        startY = event.rawY
                        VMLog.d("start x: $startX, y: $startY")
                    }
                    MotionEvent.ACTION_MOVE -> {
                        VMLog.d("move x: ${event.rawX}, y: ${event.rawY}")
                        // 当移动距离大于特定值时，表示是拖动悬浮窗，则不触发后边的点击监听
                        if (Math.abs(event.rawX - startX) > 20 || Math.abs(event.rawY - startY) > 20) {
                            result = true
                        }
                        // getRawX 获取触摸点相对于屏幕的坐标，getX 相对于当前悬浮窗坐标
                        // 根据当前触摸点 X 坐标计算悬浮窗 X 坐标，
                        layoutParams.x = (event.rawX - x).toInt()
                        // 根据当前触摸点 Y 坐标计算悬浮窗 Y 坐标，减25为状态栏的高度
                        layoutParams.y = (event.rawY - y - 25).toInt()
                        // 刷新悬浮窗
                        windowManager.updateViewLayout(floatView, layoutParams)
                    }
                    MotionEvent.ACTION_UP -> {
                    }
                }
                return result
            }
        })
        floatView?.findViewById<View>(R.id.tbLiveMsgStatusIV)?.setOnClickListener { autoMsgStatus() }
        floatView?.findViewById<View>(R.id.tbLiveMsgContentTV)?.setOnClickListener { showContentETDialog() }
    }

    /**
     * 停止悬浮窗
     */
    fun removeFloatView() {
        if (floatView != null) {
            windowManager.removeView(floatView)
            floatView = null
        }
    }

    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            nextAutoMsg()
        }
    }

    /**
     * 自动消息状态改变
     */
    private fun autoMsgStatus() {
        val statusIV = floatView?.findViewById<ImageView>(R.id.tbLiveMsgStatusIV)
        if (isStart) {
            isStart = false
            mHandler.removeMessages(0)
            statusIV?.setImageResource(R.drawable.ic_play)
        } else {
            isStart = true
            mHandler.sendEmptyMessage(0)
            statusIV?.setImageResource(R.drawable.ic_pause)
        }

    }

    private fun showContentETDialog() {
        val dialog = TBLiveMsgETDialog(context)
        val content = SPManager.get("tbLiveMsg", "content", "我来了😁~") as String
        val time = SPManager.get("tbLiveMsg", "time", "3000") as String
        dialog.setContent(content)
        dialog.setTime(time)
        dialog.setPositive(listener = {
            val content = dialog.getContent()
            val time = dialog.getTime()
            if (content.isNotEmpty() && time.isNotEmpty()) {
                val timeTV = floatView?.findViewById<TextView>(R.id.tbLiveMsgTimeTV)
                timeTV?.text = "间隔:$time"
                val contentTV = floatView?.findViewById<TextView>(R.id.tbLiveMsgContentTV)
                contentTV?.text = content
                SPManager.put("tbLiveMsg", "content", content)
                SPManager.put("tbLiveMsg", "time", time)
            } else {
                context.show("内容不能为空")
            }
        })

        // 设置窗口类型
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialog.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        } else {
            dialog.window?.setType(WindowManager.LayoutParams.TYPE_PHONE)
        }
        dialog.show()
    }

    private fun nextAutoMsg() {
        if (!isStart) return
        //        if (event.className == "com.taobao.taolive.room.TaoLiveVideoActivity") {
        VMLog.d("-lz-nextAutoMsg- ")
        val chatBtn = service.findFirst(VMATypeFind.newId("com.taobao.taobao", "taolive_chat_btn_text") as VMATypeFind<Any>) ?: return

        service.clickView(chatBtn)

        Thread.sleep(200)

        val msgET = service.findFirst(VMATypeFind.newId("com.taobao.taobao", "taolive_edit_text") as VMATypeFind<Any>)
        val content = SPManager.get("tbLiveMsg", "content", "1") as String
        service.editAction(msgET, content)

        Thread.sleep(200)

        val sendBtn = service.findFirst(VMATypeFind.newId("com.taobao.taobao", "taolive_edit_send") as VMATypeFind<Any>)
        service.clickView(sendBtn)

        val time = (SPManager.get("tbLiveMsg", "time", "3000") as String).toLong()
        // 睡眠 500 毫秒
        Thread.sleep(time - 400)
        // 发送下一条消息
        mHandler.sendEmptyMessage(0)
    }

    /**
     * 手势事件
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun motionAction() {
        val displayMetrics = context.resources.displayMetrics

        val path = Path()
        path.moveTo((displayMetrics.widthPixels / 2).toFloat(), (displayMetrics.heightPixels * 2 / 3).toFloat()) //从屏幕的2/3处开始滑动
        path.lineTo(10.0f, (displayMetrics.heightPixels * 2 / 3).toFloat())
        val sd = GestureDescription.StrokeDescription(path, 0, 500)
        service.dispatchGesture(GestureDescription.Builder().addStroke(sd).build(), object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
                VMLog.d("手势成功")
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
                VMLog.d("手势失败，请重启手机再试")
            }
        }, null)
    }

    /**
     * 点击操作
     */
    fun clickAction() {
        val ces: AccessibilityNodeInfo? = service.findFirst(VMATypeFind.newText("测试控件", true) as VMATypeFind<Any>)
        if (ces == null) {
            VMLog.d("找测试控件失败")
        } else {
            service.clickView(ces)
        }
    }

    /**
     * 长按操作
     */
    fun longClickAction() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            VMLog.e("7.0及以上才能使用手势")
            return
        }
        val ces: AccessibilityNodeInfo? = service.findFirst(VMATypeFind.newText("测试控件", true) as VMATypeFind<Any>)
        if (ces == null) {
            VMLog.e("找测试控件失败")
            return
        }

        //这里为了示范手势的效果
        // ces.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);//长按

        //这里为了示范手势的效果
        val absXY = Rect()
        ces.getBoundsInScreen(absXY)
        // HongBaoService.mService.dispatchGestureClick(absXY.left + (absXY.right - absXY.left) / 2, absXY.top + (absXY.bottom - absXY.top) / 2);//手势点击效果

        //手势长按效果
        //控件正中间
        service.dispatchGestureLongClick(absXY.left + (absXY.right - absXY.left) / 2, absXY.top + (absXY.bottom - absXY.top) / 2)
    }

    /**
     * 系统返回事件
     */
    fun backAction() {
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }

}
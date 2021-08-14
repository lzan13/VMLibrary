package com.vmloft.develop.library.example.ui.demo.accessibility

import android.view.accessibility.AccessibilityEvent

import com.vmloft.develop.library.tools.service.VMAutoService


/**
 * Create by lzan13 on 2021/8/12
 * 描述：淘宝直播消息辅助服务
 */
class TBLiveMsgService : VMAutoService() {

    /**
     * 辅助功能事件回调
     */
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        super.onAccessibilityEvent(event)

//        val biaoQingInfo = findFirst(AbstractTF.newContentDescription("表情", true) as AbstractTF<Any>)
//        if (biaoQingInfo != null) {
//            show("找到wx的表情图标") //第一次运行可能会吐不出来
//            VMLog.d("onAccessibilityEvent: 找到wx的表情图标") //可以查看日志
//            biaoQingInfo.recycle()
//        }
    }

    /**
     * 辅助服务被打断
     */
    override fun onInterrupt() {
        super.onInterrupt()
        TBLiveMsgManager.removeFloatView()
    }


    /**
     * 辅助功能启动
     */
    override fun onServiceConnected() {
        super.onServiceConnected()
        TBLiveMsgManager.init(this)
        TBLiveMsgManager.setupFloatWindow()
    }

    /**
     * 辅助服务结束
     */
    override fun onDestroy() {
        super.onDestroy()
        TBLiveMsgManager.removeFloatView()
    }
}
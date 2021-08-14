package com.vmloft.develop.library.tools.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi

import com.vmloft.develop.library.tools.utils.logger.VMLog

import java.security.InvalidParameterException


/**
 * Create by lzan13 on 2021/8/12
 * 描述：自动化操作辅助服务
 */
open class VMAutoService : AccessibilityService() {
    /**
     * 辅助功能事件回调
     */
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
//        VMLog.d("-lz-eventType- ${event.eventType}")
//        VMLog.d("-lz-className- ${event.className}")

    }

    /**
     * 辅助服务被打断
     */
    override fun onInterrupt() {
        VMLog.d("辅助功能被迫中断 (；′⌒`)")
    }


    /**
     * 辅助功能启动
     */
    override fun onServiceConnected() {
        super.onServiceConnected()
        VMLog.d("辅助功能锁定中 O(∩_∩)O~~")
    }

    /**
     * 辅助服务结束
     */
    override fun onDestroy() {
        super.onDestroy()
        VMLog.d("辅助功能已关闭 %>_<%")
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 公共方法
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 点击该控件
     *
     * @return true表示点击成功
     */
    fun clickView(nodeInfo: AccessibilityNodeInfo?): Boolean {
        if (nodeInfo != null) {
            if (nodeInfo.isClickable) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                return true
            } else {
                val parent = nodeInfo.parent
                if (parent != null) {
                    val b = clickView(parent)
                    parent.recycle()
                    if (b) return true
                }
            }
        }
        return false
    }

    /**
     * 输入内容
     */
    fun editAction(nodeInfo: AccessibilityNodeInfo?, content: String) {
        val arguments = Bundle()
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, content)
        nodeInfo?.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
    }

    /**
     * 查找第一个匹配的控件
     *
     * @param tfs 匹配条件，多个AbstractTF是&&的关系，如：
     * AbstractTF.newContentDescription("表情", true),AbstractTF.newClassName(AbstractTF.ST_IMAGEVIEW)
     * 表示描述内容是'表情'并且是imageview的控件
     */
    fun findFirst(vararg tfs: VMATypeFind<Any>): AccessibilityNodeInfo? {
        if (tfs.isEmpty()) throw InvalidParameterException("AbstractTF不允许传空")
        val rootInfo = rootInActiveWindow ?: return null
        var idTextTFCount = 0
        var idTextIndex = 0
        for (i in tfs.indices) {
            if (tfs[i] is VMATypeFind.IdTextTF) {
                idTextTFCount++
                idTextIndex = i
            }
        }
        return when (idTextTFCount) {
            0 -> {
                val returnInfo = findFirstRecursive(rootInfo, *tfs)
                rootInfo.recycle()
                returnInfo
            }
            1 -> if (tfs.size == 1) {
                val returnInfo2: AccessibilityNodeInfo? = (tfs[idTextIndex] as VMATypeFind.IdTextTF).findFirst(rootInfo)
                rootInfo.recycle()
                returnInfo2
            } else {
                var returnInfo3: AccessibilityNodeInfo? = null
                val listIdText: List<AccessibilityNodeInfo>? = (tfs[idTextIndex] as VMATypeFind.IdTextTF).findAll(rootInfo)
                listIdText?.let {
                    if (it.isNotEmpty()) {
                        for (info in listIdText) { //遍历找到匹配的
                            if (returnInfo3 == null) {
                                var isOk = true
                                for (tf in tfs) {
                                    if (!tf.checkOk(info)) {
                                        isOk = false
                                        break
                                    }
                                }
                                if (isOk) {
                                    returnInfo3 = info
                                } else {
                                    info.recycle()
                                }
                            } else {
                                info.recycle()
                            }
                        }
                        rootInfo.recycle()
                    }
                }
//                if (listIdText.isEmpty()) {
//                    break
//                }
                returnInfo3
            }
            else -> throw RuntimeException("由于时间有限，并且多了也没什么用，所以IdTF和TextTF只能有一个")
        }
        rootInfo.recycle()
        return null
    }

    /**
     * @param tfs 由于是递归循环，会忽略IdTF和TextTF
     */
    fun findFirstRecursive(parent: AccessibilityNodeInfo?, vararg tfs: VMATypeFind<Any>): AccessibilityNodeInfo? {
        if (parent == null) return null
        if (tfs.size == 0) throw InvalidParameterException("AbstractTF不允许传空")
        for (i in 0 until parent.childCount) {
            val child = parent.getChild(i) ?: continue
            var isOk = true
            for (tf in tfs) {
                if (!tf.checkOk(child)) {
                    isOk = false
                    break
                }
            }
            if (isOk) {
                return child
            } else {
                val childChild = findFirstRecursive(child, *tfs)
                child.recycle()
                if (childChild != null) {
                    return childChild
                }
            }
        }
        return null
    }

    /**
     * 查找全部匹配的控件
     *
     * @param tfs 匹配条件，多个AbstractTF是&&的关系，如：
     * AbstractTF.newContentDescription("表情", true),AbstractTF.newClassName(AbstractTF.ST_IMAGEVIEW)
     * 表示描述内容是'表情'并且是imageview的控件
     */
    fun findAll(vararg tfs: VMATypeFind<Any>): List<AccessibilityNodeInfo> {
        if (tfs.isEmpty()) throw InvalidParameterException("AbstractTF不允许传空")
        val list: ArrayList<AccessibilityNodeInfo> = ArrayList()
        val rootInfo = rootInActiveWindow ?: return list
        var idTextTFCount = 0
        var idTextIndex = 0
        for (i in tfs.indices) {
            if (tfs[i] is VMATypeFind.IdTextTF) {
                idTextTFCount++
                idTextIndex = i
            }
        }
        when (idTextTFCount) {
            0 -> findAllRecursive(list, rootInfo, tfs as VMATypeFind<Any>)
            1 -> {
                val listIdText: List<AccessibilityNodeInfo>? = (tfs[idTextIndex] as VMATypeFind.IdTextTF).findAll(rootInfo)
                listIdText?.let {
                    if (tfs.size == 1) {
                        list.addAll(listIdText)
                    } else {
                        for (info in listIdText) {
                            var isOk = true
                            for (tf in tfs) {
                                if (!tf.checkOk(info)) {
                                    isOk = false
                                    break
                                }
                            }
                            if (isOk) {
                                list.add(info)
                            } else {
                                info.recycle()
                            }
                        }
                    }
                }
            }
            else -> throw RuntimeException("由于时间有限，并且多了也没什么用，所以IdTF和TextTF只能有一个")
        }
        rootInfo.recycle()
        return list
    }

    /**
     * 目前好像只有外部输入设备才会调用（虚拟键盘没用）
     */
    override fun onKeyEvent(event: KeyEvent): Boolean {
        println("哈哈哈哈$event")
        return super.onKeyEvent(event)
    }

    /**
     * @param tfs 由于是递归循环，会忽略IdTF和TextTF
     */
    fun findAllRecursive(list: MutableList<AccessibilityNodeInfo>?, parent: AccessibilityNodeInfo?, vararg tfs: VMATypeFind<Any>) {
        if (parent == null || list == null) return
        if (tfs.isEmpty()) throw InvalidParameterException("AbstractTF不允许传空")
        for (i in 0 until parent.childCount) {
            val child = parent.getChild(i) ?: continue
            var isOk = true
            for (tf in tfs) {
                if (!tf.checkOk(child)) {
                    isOk = false
                    break
                }
            }
            if (isOk) {
                list.add(child)
            } else {
                findAllRecursive(list, child, *tfs)
                child.recycle()
            }
        }
    }

    /**
     * 立即发送移动的手势
     * 注意7.0以上的手机才有此方法，请确保运行在7.0手机上
     *
     * @param path  移动路径
     * @param mills 持续总时间
     */
    @RequiresApi(24)
    fun dispatchGestureMove(path: Path, mills: Long) {
        dispatchGesture(GestureDescription.Builder().addStroke(StrokeDescription(path, 0, mills)).build(), null, null)
    }

    /**
     * 点击指定位置
     * 注意7.0以上的手机才有此方法，请确保运行在7.0手机上
     */
    @RequiresApi(24)
    fun dispatchGestureClick(x: Int, y: Int) {
        val path = Path()
        path.moveTo(x.toFloat() - 1, y.toFloat() - 1)
        path.lineTo(x.toFloat() + 1, y.toFloat() + 1)
        dispatchGesture(GestureDescription.Builder().addStroke(StrokeDescription(path, 0, 100)).build(), null, null)
    }

    /**
     * 有些应用和谐了[.clickView]方法
     */
    @RequiresApi(24)
    fun dispatchGestureClick(info: AccessibilityNodeInfo) {
        val rect: Rect = VMATypeFind.mRecycleRect
        info.getBoundsInScreen(rect)
        dispatchGestureClick(rect.centerX(), rect.centerY())
    }

    /**
     * 长按指定位置
     * 注意7.0以上的手机才有此方法，请确保运行在7.0手机上
     */
    @RequiresApi(24)
    fun dispatchGestureLongClick(x: Int, y: Int) {
        val path = Path()
        path.moveTo(x.toFloat() - 1, y.toFloat() - 1)
        path.lineTo(x.toFloat(), y.toFloat() - 1)
        path.lineTo(x.toFloat(), y.toFloat())
        path.lineTo(x.toFloat() - 1, y.toFloat())
        dispatchGesture(GestureDescription.Builder().addStroke(StrokeDescription(path, 0, 1000)).build(), null, null)
    }

    /**
     * 由于太多,最好回收这些AccessibilityNodeInfo
     */
    fun recycleAccessibilityNodeInfo(listInfo: List<AccessibilityNodeInfo>) {
        if (listInfo.isEmpty()) return
        for (info in listInfo) {
            info.recycle()
        }
    }
}
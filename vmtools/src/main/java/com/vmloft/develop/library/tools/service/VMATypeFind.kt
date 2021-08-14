package com.vmloft.develop.library.tools.service

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import java.util.*

/**
 * Create by lzan13 2021/08/09
 * 描述：节点查找抽象类
 */
abstract class VMATypeFind<T>(protected val checkData: T, protected val isEquals: Boolean) {

    abstract fun checkOk(thisInfo: AccessibilityNodeInfo): Boolean

    companion object {
        // 创建方法
        var mRecycleRect = Rect()
//        const val ST_VIEW = "android.view.View"
//        const val ST_TEXTVIEW = "android.widget.TextView"
//        const val ST_IMAGEVIEW = "android.widget.ImageView"
//        const val ST_BUTTON = "android.widget.Button"
//        const val ST_IMAGEBUTTON = "android.widget.ImageButton"
//        const val ST_EDITTEXT = "android.widget.EditText"
//        const val ST_LISTVIEW = "android.widget.ListView"
//        const val ST_LINEARLAYOUT = "android.widget.LinearLayout"
//        const val ST_VIEWGROUP = "android.view.ViewGroup"
//        const val ST_SYSTEMUI = "com.android.systemui"

        /**
         * 找id，就是findAccessibilityNodeInfosByViewId方法
         * 和找text一样效率最高，如果能找到，尽量使用这个
         *
         * @param pageName 被查找项目的包名:com.xxx
         * @param idName   id值:xxxTV
         */
        fun newId(pageName: String, idName: String): IdTF {
            return newId("$pageName:id/$idName")
        }

        /**
         * @param idFullName id全称:com.xxx:id/xxxTV
         */
        fun newId(idFullName: String): IdTF {
            return IdTF(idFullName)
        }

        /**
         * 普通text，就是findAccessibilityNodeInfosByText方法
         * 和找id一样效率最高，如果能找到，尽量使用这个
         */
        fun newText(text: String, isEquals: Boolean = false): TextTF {
            return TextTF(text, isEquals)
        }

        /**
         * 类似uc浏览器，有text值但无法直接根据text来找到
         */
        fun newWebText(webText: String, isEquals: Boolean = false): WebTextTF {
            return WebTextTF(webText, isEquals)
        }

        /**
         * 找ContentDescription字段
         */
        fun newContentDescription(cd: String, isEquals: Boolean = false): ContentDescriptionTF {
            return ContentDescriptionTF(cd, isEquals)
        }

        /**
         * 找ClassName匹配
         */
        fun newClassName(className: String): ClassNameTF {
            return ClassNameTF(className, true)
        }

        fun newClassName(className: String, isEquals: Boolean = false): ClassNameTF {
            return ClassNameTF(className, isEquals)
        }

        /**
         * 在某个区域内的控件
         */
        fun newRect(rect: Rect): RectTF {
            return RectTF(rect)
        }
    }


    /**
     * 找id，就是findAccessibilityNodeInfosByViewId方法
     * 和找text一样效率最高，如果能找到，尽量使用这个
     */
    class IdTF(idFullName: String) : VMATypeFind<String>(idFullName, true), IdTextTF {
        override fun checkOk(thisInfo: AccessibilityNodeInfo): Boolean {
            return true //此处不需要实现
        }

        override fun findFirst(root: AccessibilityNodeInfo): AccessibilityNodeInfo? {
            val list = root.findAccessibilityNodeInfosByViewId(checkData)
            if (list.isEmpty()) {
                return null
            }
            for (i in 1 until list.size) { //其他的均回收
                list[i].recycle()
            }
            return list[0]
        }

        override fun findAll(root: AccessibilityNodeInfo): List<AccessibilityNodeInfo>? {
            return root.findAccessibilityNodeInfosByViewId(checkData)
        }
    }

    /**
     * 普通text，就是findAccessibilityNodeInfosByText方法
     * 和找id一样效率最高，如果能找到，尽量使用这个
     */
    class TextTF(text: String, isEquals: Boolean) : VMATypeFind<String>(text, isEquals), IdTextTF {
        override fun checkOk(thisInfo: AccessibilityNodeInfo): Boolean {
            return true //此处不需要实现
        }

        override fun findFirst(root: AccessibilityNodeInfo): AccessibilityNodeInfo? {
            val list = root.findAccessibilityNodeInfosByText(checkData)
            if (list.isEmpty()) {
                return null
            }
            return if (isEquals) {
                var returnInfo: AccessibilityNodeInfo? = null
                for (info in list) {
                    if (returnInfo == null && info.text != null && checkData == info.text.toString()) {
                        returnInfo = info
                    } else {
                        info.recycle()
                    }
                }
                returnInfo
            } else {
                list[0]
            }
        }

        override fun findAll(root: AccessibilityNodeInfo): List<AccessibilityNodeInfo>? {
            val list = root.findAccessibilityNodeInfosByText(checkData)
            if (list.isEmpty()) {
                return null
            }
            return if (isEquals) {
                val listNew = ArrayList<AccessibilityNodeInfo>()
                for (info in list) {
                    if (info.text != null && checkData == info.text.toString()) {
                        listNew.add(info)
                    } else {
                        info.recycle()
                    }
                }
                listNew
            } else {
                list
            }
        }
    }

    /**
     * 类似uc浏览器，有text值但无法直接根据text来找到
     */
    class WebTextTF(checkString: String, isEquals: Boolean) : VMATypeFind<String>(checkString, isEquals) {
        override fun checkOk(thisInfo: AccessibilityNodeInfo): Boolean {
            val text = thisInfo.text
            return if (isEquals) {
                text != null && text.toString() == checkData
            } else {
                text != null && text.toString().contains(checkData)
            }
        }
    }

    /**
     * 找ContentDescription字段
     */
    class ContentDescriptionTF(checkString: String, isEquals: Boolean) : VMATypeFind<String>(checkString, isEquals) {
        override fun checkOk(thisInfo: AccessibilityNodeInfo): Boolean {
            val text = thisInfo.contentDescription
            return if (isEquals) {
                text != null && text.toString() == checkData
            } else {
                text != null && text.toString().contains(checkData)
            }
        }
    }

    /**
     * 找ClassName匹配
     */
    class ClassNameTF(checkString: String, isEquals: Boolean) : VMATypeFind<String>(checkString, isEquals) {
        override fun checkOk(thisInfo: AccessibilityNodeInfo): Boolean {
            return if (isEquals) {
                thisInfo.className.toString() == checkData
            } else {
                thisInfo.className.toString().contains(checkData)
            }
        }
    }

    /**
     * 在某个区域内的控件
     */
    class RectTF(rect: Rect) : VMATypeFind<Rect>(rect, true) {
        override fun checkOk(thisInfo: AccessibilityNodeInfo): Boolean {
            thisInfo.getBoundsInScreen(mRecycleRect)
            return checkData.contains(mRecycleRect)
        }
    }

    interface IdTextTF {
        fun findFirst(root: AccessibilityNodeInfo): AccessibilityNodeInfo?
        fun findAll(root: AccessibilityNodeInfo): List<AccessibilityNodeInfo>?
    }
}
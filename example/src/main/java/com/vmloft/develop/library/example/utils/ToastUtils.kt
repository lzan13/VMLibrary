package com.vmloft.develop.library.example.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vmloft.develop.library.tools.utils.VMStr

import com.vmloft.develop.library.tools.widget.toast.VMToast

/**
 * Create by lzan13 on 2020/4/12 11:18
 * 描述：统一处理提醒
 */

/**
 * Activity 调用
 */
fun Activity.showBar(resId: Int, duration: Long = VMToast.durationShort) {
    VMToast.showBar(this, VMStr.byRes(resId), duration)
}

fun Activity.showBar(content: String, duration: Long = VMToast.durationShort) {
    VMToast.showBar(this, content, duration)
}

fun Activity.errorBar(resId: Int, duration: Long = VMToast.durationShort) {
    VMToast.errorBar(this, VMStr.byRes(resId), duration)
}

fun Activity.errorBar(content: String, duration: Long = VMToast.durationShort) {
    VMToast.errorBar(this, content, duration)
}


/**
 * Fragment 调用
 */
fun Fragment.showBar(resId: Int, duration: Long = VMToast.durationShort) {
    VMToast.showBar(activity!!, VMStr.byRes(resId), duration)
}

fun Fragment.showBar(content: String, duration: Long = VMToast.durationShort) {
    VMToast.showBar(activity!!, content, duration)
}

fun Fragment.errorBar(resId: Int, duration: Long = VMToast.durationShort) {
    VMToast.errorBar(activity!!, VMStr.byRes(resId), duration)
}

fun Fragment.errorBar(content: String, duration: Long = VMToast.durationShort) {
    VMToast.errorBar(activity!!, content, duration)
}

/**
 * 任意调用，这里是调用系统的 toast 提醒
 */
fun Context.show(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    VMToast.show(this, resId, duration)
}

fun Context.show(content: String, duration: Int = Toast.LENGTH_SHORT) {
    VMToast.show(this, content, duration)
}

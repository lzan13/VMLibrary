package com.vmloft.develop.library.example.utils

import android.app.Activity
import androidx.fragment.app.Fragment

import com.vmloft.develop.library.tools.widget.toast.VMToast

/**
 * Create by lzan13 on 2020/4/12 11:18
 * 描述：统一处理 Toast 提醒
 */
object ToastUtils {

    fun toast(activity: Activity, content: String, duration: Int = VMToast.LONG) {
        VMToast.make(activity, content, duration).done()
    }

    fun toastError(activity: Activity, content: String, duration: Int = VMToast.LONG) {
        VMToast.make(activity, content, duration).error()
    }
}

/**
 * Activity 调用
 */
fun Activity.toast(resId: Int, duration: Int = VMToast.LONG) {
    VMToast.make(this, resId, duration).done()
}

fun Activity.toast(content: String, duration: Int = VMToast.LONG) {
    VMToast.make(this, content, duration).done()
}

fun Activity.toastError(resId: Int, duration: Int = VMToast.LONG) {
    VMToast.make(this, resId, duration).error()
}

fun Activity.toastError(content: String, duration: Int = VMToast.LONG) {
    VMToast.make(this, content, duration).error()
}


/**
 * Fragment 调用
 */
fun Fragment.toast(resId: Int, duration: Int = VMToast.LONG) {
    VMToast.make(activity, resId, duration).done()
}

fun Fragment.toast(content: String, duration: Int = VMToast.LONG) {
    VMToast.make(activity, content, duration).done()
}

fun Fragment.toastError(resId: Int, duration: Int = VMToast.LONG) {
    VMToast.make(activity, resId, duration).error()
}

fun Fragment.toastError(content: String, duration: Int = VMToast.LONG) {
    VMToast.make(activity, content, duration).error()
}

//fun Context.toast(content: String, duration: Int = Toast.LENGTH_SHORT) {
//    Toast.makeText(this, content, duration).apply {
//        show()
//    }
//}
//
//fun Context.toast(@StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
//    toast(getString(id), duration)
//}
//
//fun Context.longToast(content: String) {
//    toast(content, Toast.LENGTH_LONG)
//}
//
//fun Context.longToast(@StringRes id: Int) {
//    toast(id, Toast.LENGTH_LONG)
//}
//
//fun Any.toast(context: Context, content: String, duration: Int = Toast.LENGTH_SHORT) {
//    context.toast(content, duration)
//}
//
//fun Any.toast(context: Context, @StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
//    context.toast(id, duration)
//}
//
//fun Any.longToast(context: Context, content: String) {
//    context.longToast(content)
//}
//
//fun Any.longToast(context: Context, @StringRes id: Int) {
//    context.longToast(id)
//}
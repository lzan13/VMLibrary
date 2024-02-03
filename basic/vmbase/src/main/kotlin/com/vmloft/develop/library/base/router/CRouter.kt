package com.vmloft.develop.library.base.router

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Size
import androidx.activity.result.ActivityResultLauncher

import com.didi.drouter.api.DRouter
import com.didi.drouter.api.Extend
import com.didi.drouter.router.RouterCallback.ActivityCallback

import com.vmloft.develop.library.tools.VMTools
import com.vmloft.develop.library.tools.utils.logger.VMLog
import java.io.Serializable
import java.lang.reflect.Type

/**
 * Create by lzan13 on 2020-02-24 21:57
 * 描述：针对路由注解统一收口
 */
object CRouter {
    // 通用传参 Key
    const val paramsWhat = "paramsWhat"
    const val paramsArg0 = "paramsArg0"
    const val paramsArg1 = "paramsArg1"
    const val paramsStr0 = "paramsStr0"
    const val paramsStr1 = "paramsStr1"
    const val paramsObj0 = "paramsObj0"
    const val paramsObj1 = "paramsObj1"
    const val paramsList = "paramsList"

    const val appMain = "/App/Main"

    // 定义页面路由，这里有有一点需要注意
    const val commonDebug = "/Common/Debug"
    const val commonWeb = "/Common/Web"

    const val imageDisplayMulti = "/Image/DisplayMulti"
    const val imageDisplaySingle = "/Image/DisplaySingle"

    const val qrScanQRCode = "/qr/ScanQRCode"
    const val qrScanQRCodeResCode = 1001

    fun init(app: Application) {
        DRouter.init(app)
    }

//    /**
//     * 可复用通用跳转方法，path 可像 url 那样在 ? 后拼接参数
//     * 形如：/home?key1=value1&key2=value2
//     * 落地页参数可以通过 intent 获取
//     */
//    fun go(path: String, callback: (Boolean) -> Unit = {}) {
//        DRouter.build(path).start(VMTools.context) { result -> // 可用来做降级
//            VMLog.i("router result ${result.isActivityStarted}")
//            callback.invoke(result.isActivityStarted)
//        }
//    }

    /**
     * 可复用通用跳转方法，可携带参数
     * path 可像 url 那样在 ? 后拼接参数
     * 形如：/home?key1=value1&key2=value2
     * 落地页参数可以通过 intent 获取
     */
    fun go(
        path: String,
        what: Int = 0,
        arg0: Int = 0,
        arg1: Int = 0,
        arg2: Boolean = false,
        str0: String? = null,
        str1: String? = null,
        obj0: Parcelable? = null,
        obj1: Parcelable? = null,
        list: ArrayList<Any>? = null,
        flags: Int = 0,
        callback: (Boolean) -> Unit = {}
    ) {
        val request = DRouter.build(path)

        request.putExtra(paramsWhat, what)
        request.putExtra(paramsArg0, arg0)
        request.putExtra(paramsArg1, arg1)

        str0?.let {
            request.putExtra(paramsStr0, str0)
        }

        str1?.let {
            request.putExtra(paramsStr1, str1)
        }

        obj0?.let {
            request.putExtra(paramsObj0, obj0)
        }

        obj1?.let {
            request.putExtra(paramsObj1, obj1)
        }

        list?.let {
            request.putExtra(paramsList, list)
        }
        // 设置 flags
        if (flags != 0) {
            request.putExtra(Extend.START_ACTIVITY_FLAGS, flags)
        }

        request.start(VMTools.context) { result ->
            VMLog.i("router result ${result.isActivityStarted}")
            // 可用来做降级
            callback.invoke(result.isActivityStarted)
        }
    }

    /**
     * 复用跳转，需要接收返回值
     */
    fun goResult(path: String, launcher: ActivityResultLauncher<Intent>? = null, callback: (Int, Intent?) -> Unit) {
        DRouter.build(path)
            .setActivityResultLauncher(launcher)
            .start(VMTools.context, object : ActivityCallback() {
                override fun onActivityResult(code: Int, data: Intent?) {
                    VMLog.i("router onActivityResult $code ${data?.extras?.size()}")
                    // 回调返回值
                    callback.invoke(code, data)
                }
            })
    }

    /**
     * 回到手机桌面
     */
    fun goHome() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        VMTools.context.startActivity(intent)
    }

    /**
     * 主界面
     * @param type 跳转类型 0-普通 1-清空登录信息
     */
    fun goMain(type: Int = 0) {
        DRouter.build(appMain)
            .putExtra(Extend.START_ACTIVITY_FLAGS, Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtra("type", type)
            .start()
    }

    /**
     * 打开 Web 页面
     */
    fun goWeb(url: String, system: Boolean = false) {
        if (system) {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            VMTools.context.startActivity(intent)
        } else {
            DRouter.build(commonWeb)
                .putExtra("url", url)
                .start()
        }
    }

    /**
     * 展示多图
     */
    fun goDisplayMulti(index: String, list: ArrayList<String>) {
        DRouter.build(imageDisplayMulti)
            .putExtra("index", index)
            .putExtra("pictureList", list)
            .start()
    }

    /**
     * 展示单图
     */
    fun goDisplaySingle(url: String) {
        DRouter.build(imageDisplaySingle)
            .putExtra("url", url)
            .start()
    }

    /**
     * 获取路由参数
     */
    fun optBoolean(intent: Intent, key: String): Boolean {
        if (intent.extras == null) return false
        return intent.extras!!.getBoolean(key, false)
    }

    fun optShort(intent: Intent, key: String): Short {
        if (intent.extras == null) return 0
        return intent.extras!!.getShort(key, 0)
    }

    fun optInt(intent: Intent, key: String): Int {
        if (intent.extras == null) return 0
        return intent.extras!!.getInt(key, 0)
    }

    fun optLong(intent: Intent, key: String): Long {
        if (intent.extras == null) return 0L
        return intent.extras!!.getLong(key, 0L)
    }

    fun optFloat(intent: Intent, key: String): Float {
        if (intent.extras == null) return 0f
        return intent.extras!!.getFloat(key, 0f)
    }

    fun optDouble(intent: Intent, key: String): Double {
        if (intent.extras == null) return 0.0
        return intent.extras!!.getDouble(key, 0.0)
    }

    fun optString(intent: Intent, key: String): String {
        if (intent.extras == null) return ""
        return intent.extras!!.getString(key, "")
    }

    fun optSize(intent: Intent, key: String): Size {
        if (intent.extras == null) return Size(0, 0)
        return intent.extras!!.getSize(key) ?: Size(0, 0)
    }

    fun <T> optParcelable(intent: Intent, key: String): T? {
        if (intent.extras == null) return null
        return intent.extras!!.getParcelable(key)
    }

    fun optSerializable(intent: Intent, key: String): Serializable? {
        if (intent.extras == null) return null
        return intent.extras!!.getSerializable(key)
    }

    fun optByteArray(intent: Intent, key: String): ByteArray {
        if (intent.extras == null) return byteArrayOf()
        return intent.extras!!.getByteArray(key) ?: byteArrayOf()
    }

    fun optIntArray(intent: Intent, key: String): IntArray {
        if (intent.extras == null) return intArrayOf()
        return intent.extras!!.getIntArray(key) ?: intArrayOf()
    }

    fun optLongArray(intent: Intent, key: String): LongArray {
        if (intent.extras == null) return longArrayOf()
        return intent.extras!!.getLongArray(key) ?: longArrayOf()
    }

    fun optFloatArray(intent: Intent, key: String): FloatArray {
        if (intent.extras == null) return floatArrayOf()
        return intent.extras!!.getFloatArray(key) ?: floatArrayOf()
    }

    fun optDoubleArray(intent: Intent, key: String): DoubleArray {
        if (intent.extras == null) return doubleArrayOf()
        return intent.extras!!.getDoubleArray(key) ?: doubleArrayOf()
    }

    fun optIntList(intent: Intent, key: String): ArrayList<Int> {
        if (intent.extras == null) return arrayListOf()
        return intent.extras!!.getIntegerArrayList(key) ?: arrayListOf()
    }

    fun optStringList(intent: Intent, key: String): ArrayList<String> {
        if (intent.extras == null) return arrayListOf<String>()
        return intent.extras!!.getStringArrayList(key) ?: arrayListOf<String>()
    }

    fun <T : Parcelable> optParcelableList(intent: Intent, key: String, clazz: Class<T>): ArrayList<T> {
        if (intent.extras == null) return arrayListOf<T>()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras!!.getParcelableArrayList(key, clazz) ?: arrayListOf<T>()
        } else {
            intent.extras!!.getParcelableArrayList<T>(key) ?: arrayListOf<T>()
        }
    }

    fun optBundle(intent: Intent, key: String): Bundle {
        if (intent.extras == null) return Bundle()
        return intent.extras!!.getBundle(key) ?: Bundle()
    }

}
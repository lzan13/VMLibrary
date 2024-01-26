package com.vmloft.develop.library.tools.permission

import android.annotation.SuppressLint
import android.os.Parcelable

import kotlinx.parcelize.Parcelize

/**
 * Create by lzan13 on 2019/04/26
 * 描述：权限数据传递实体类
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class VMPermissionBean(
    // 权限
    var permission: String = "",
    // 权限名称
    var name: String = "",
    // 权限理由
    var reason: String = "",
    // 资源 id
    var resId: Int = 0,
) : Parcelable {

}

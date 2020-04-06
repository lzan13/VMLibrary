package com.vmloft.develop.library.tools.permission

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

/**
 * Create by lzan13 on 2019/04/26
 *
 * 权限数据传递实体类
 */
class VMPermissionBean : Parcelable {
    // 权限
    var permission: String? = null

    // 资源 id
    var resId = 0

    // 权限名称
    var name: String? = null

    // 权限理由
    var reason: String? = null
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(permission)
        dest.writeInt(resId)
        dest.writeString(name)
        dest.writeString(reason)
    }

    /**
     * 空构造
     */
    constructor() {}

    /**
     * 自定义构造方法，直接传递参数
     *
     * @param permission 申请的权限
     */
    constructor(permission: String?) {
        this.permission = permission
    }

    /**
     * 自定义构造方法，直接传递参数
     *
     * @param permission 申请的权限
     * @param name       权限名称
     * @param reason     申请权限理由，用来在用户拒绝时展示给用户
     */
    constructor(permission: String?, name: String?, reason: String?) {
        this.permission = permission
        this.name = name
        this.reason = reason
    }

    /**
     * 自定义构造方法，直接传递参数
     *
     * @param permission 申请的权限
     * @param resId      权限图标
     * @param name       权限名称
     * @param reason     申请权限理由，用来在用户拒绝时展示给用户
     */
    constructor(permission: String?, resId: Int, name: String?, reason: String?) {
        this.permission = permission
        this.resId = resId
        this.name = name
        this.reason = reason
    }

    companion object {
        val CREATOR: Creator<VMPermissionBean?> = object : Creator<VMPermissionBean?> {
            override fun createFromParcel(parcel: Parcel): VMPermissionBean? {
                val bean = VMPermissionBean()
                bean.permission = parcel.readString()
                bean.resId = parcel.readInt()
                bean.name = parcel.readString()
                bean.reason = parcel.readString()
                return bean
            }

            override fun newArray(size: Int): Array<VMPermissionBean?> {
                return arrayOfNulls(size)
            }
        }
    }
}
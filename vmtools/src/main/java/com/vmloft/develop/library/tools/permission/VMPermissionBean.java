package com.vmloft.develop.library.tools.permission;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by lzan13 on 2019/04/26
 *
 * 权限数据传递实体类
 */
public class VMPermissionBean implements Parcelable {
    // 权限
    public String permission;
    // 资源 id
    public int resId;
    // 权限名称
    public String name;
    // 权限理由
    public String reason;

    public static final Creator<VMPermissionBean> CREATOR = new Creator<VMPermissionBean>() {
        @Override
        public VMPermissionBean createFromParcel(Parcel in) {
            VMPermissionBean p = new VMPermissionBean();
            p.permission = in.readString();
            p.resId = in.readInt();
            p.name = in.readString();
            p.reason = in.readString();
            return p;
        }

        @Override
        public VMPermissionBean[] newArray(int size) {
            return new VMPermissionBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(permission);
        dest.writeInt(resId);
        dest.writeString(name);
        dest.writeString(reason);
    }

    /**
     * 空构造
     */
    public VMPermissionBean() {

    }

    /**
     * 自定义构造方法，直接传递参数
     *
     * @param permission 申请的权限
     */
    public VMPermissionBean(String permission) {
        this.permission = permission;
    }

    /**
     * 自定义构造方法，直接传递参数
     *
     * @param permission 申请的权限
     * @param name       权限名称
     * @param reason     申请权限理由，用来在用户拒绝时展示给用户
     */
    public VMPermissionBean(String permission, String name, String reason) {
        this.permission = permission;
        this.name = name;
        this.reason = reason;
    }

    /**
     * 自定义构造方法，直接传递参数
     *
     * @param permission 申请的权限
     * @param resId      权限图标
     * @param name       权限名称
     * @param reason     申请权限理由，用来在用户拒绝时展示给用户
     */
    public VMPermissionBean(String permission, int resId, String name, String reason) {
        this.permission = permission;
        this.resId = resId;
        this.name = name;
        this.reason = reason;
    }
}

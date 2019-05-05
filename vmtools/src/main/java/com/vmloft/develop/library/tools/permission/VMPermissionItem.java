package com.vmloft.develop.library.tools.permission;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by lzan13 on 2019/04/26
 *
 * 权限数据传递实体类
 */
public class VMPermissionItem implements Parcelable {
    // 权限
    public String permission;
    // 权限名称
    public String permissionName;
    // 权限理由
    public String permissionReason;

    public static final Creator<VMPermissionItem> CREATOR = new Creator<VMPermissionItem>() {
        @Override
        public VMPermissionItem createFromParcel(Parcel in) {
            VMPermissionItem p = new VMPermissionItem();
            p.permission = in.readString();
            p.permissionName = in.readString();
            p.permissionReason = in.readString();
            return p;
        }

        @Override
        public VMPermissionItem[] newArray(int size) {
            return new VMPermissionItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(permission);
        dest.writeString(permissionName);
        dest.writeString(permissionReason);
    }
}

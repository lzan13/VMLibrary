package com.vmloft.develop.library.tools.picker.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Create by lzan13 on 2019/05/16 21:51
 *
 * 图片实体类，存储图片文件夹信息
 */
public class VMPictureBean implements Serializable, Parcelable {

    public String name;       //图片的名字
    public String path;       //图片的路径
    public long size;         //图片的大小
    public int width;         //图片的宽度
    public int height;        //图片的高度
    public String mimeType;   //图片的类型
    public long addTime;      //图片的创建时间

    /**
     * 图片的路径和创建时间相同就认为是同一张图片
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof VMPictureBean) {
            VMPictureBean bean = (VMPictureBean) o;
            return this.path.equalsIgnoreCase(bean.path) && this.addTime == bean.addTime;
        }

        return super.equals(o);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeLong(this.size);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.mimeType);
        dest.writeLong(this.addTime);
    }

    public VMPictureBean() {
    }

    protected VMPictureBean(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.size = in.readLong();
        this.width = in.readInt();
        this.height = in.readInt();
        this.mimeType = in.readString();
        this.addTime = in.readLong();
    }

    public static final Parcelable.Creator<VMPictureBean> CREATOR = new Parcelable.Creator<VMPictureBean>() {
        @Override
        public VMPictureBean createFromParcel(Parcel source) {
            return new VMPictureBean(source);
        }

        @Override
        public VMPictureBean[] newArray(int size) {
            return new VMPictureBean[size];
        }
    };
}

package com.vmloft.develop.library.tools.picker;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Create by lzan13 on 2019/05/16 21:51
 *
 * 定义图片加载接口，外部需要实现这个接口去加载图片，为的是减少对第三方库的依赖
 */
public interface ILoaderListener extends Serializable {
    /**
     * 通过上下文对象加载图片
     *
     * @param context   上下文对象
     * @param options   加载配置
     * @param imageView 目标控件
     */
    void load(Context context, Options options, ImageView imageView);

    ImageView createView(Context context);

    /**
     * 图片加载相关参数类
     */
    class Options {

        public Options(String url) {
            this.url = url;
        }

        // 图片地址
        public String url;

        // 是否为圆形
        public boolean isCircle;

        // 是否为圆角
        public boolean isRadius;
        // 圆角大小，需配合上个参数一起使用
        public int radiusSize;
        public int radiusLTSize; // 左上角
        public int radiusLBSize; // 左下角
        public int radiusRTSize; // 右上角
        public int radiusRBSize; // 右下角

        // 是否模糊
        public boolean isBlur;
    }
}

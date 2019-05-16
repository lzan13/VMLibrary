package com.vmloft.develop.library.tools.picker.loader;

import android.app.Activity;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Create by lzan13 on 2019/05/16 21:51
 *
 * 定义图片加载接口，外部需要实现这个接口去加载图片，为的是减少对第三方库的依赖
 */
public interface IPictureLoader extends Serializable {

    void displayImage(Activity activity, String path, ImageView imageView, int width, int height);

    void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height);

    void clearMemoryCache();
}

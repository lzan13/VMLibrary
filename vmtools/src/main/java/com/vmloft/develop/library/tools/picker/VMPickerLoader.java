package com.vmloft.develop.library.tools.picker;

import android.content.Context;
import android.widget.ImageView;

/**
 * Create by lzan13 on 2020-02-09 19:35
 * 图片加载抽象实现
 */
public abstract class VMPickerLoader implements ILoaderListener {

    @Override
    public abstract void load(Context context, Options options, ImageView imageView);

    @Override
    public ImageView createView(Context context) {
        return new ImageView(context);
    }
}

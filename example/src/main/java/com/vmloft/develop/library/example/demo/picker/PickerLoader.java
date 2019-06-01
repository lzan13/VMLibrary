package com.vmloft.develop.library.example.demo.picker;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vmloft.develop.library.tools.picker.IPictureLoader;


/**
 * Create by lzan13 on 2019/05/19 22:30
 *
 * 实现图片选择器加载图片接口
 */
public class PickerLoader implements IPictureLoader {

    /**
     * 通过上下文对象加载图片
     *
     * @param context   上下文对象
     * @param options   加载配置
     * @param imageView 目标控件
     */
    @Override
    public void load(Context context, Options options, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        if (options.isCircle) {
            requestOptions.circleCrop();
        } else if (options.isRadius) {
            requestOptions.transform(new MultiTransformation<>(new CenterCrop(),new RoundedCorners(options.radiusSize)));
        }
        GlideApp.with(context).load(options.url).apply(requestOptions).into(imageView);
    }
}

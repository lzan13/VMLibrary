package com.vmloft.develop.library.tools.picker.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vmloft.develop.library.tools.picker.ILoaderListener;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;

import java.util.List;

/**
 * Create by lzan13 on 2019/05/16 21:51
 *
 * 图片预览适配器
 */
public class VMPreviewPageAdapter extends PagerAdapter {

    private List<VMPictureBean> mDataList;
    private Activity mActivity;
    public OnPreviewClickListener listener;

    public VMPreviewPageAdapter(Activity activity, List<VMPictureBean> images) {
        this.mActivity = activity;
        this.mDataList = images;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = VMPicker.getInstance().getPictureLoader().createView(mActivity);

        VMPictureBean bean = mDataList.get(position);
        ILoaderListener.Options options = new ILoaderListener.Options(bean.path);
        VMPicker.getInstance().getPictureLoader().load(mActivity, options, imageView);

        //photoView.setOnPhotoTapListener((view, x, y) -> {
        //    if (listener != null) {
        //        listener.onPreviewClick(view, x, y);
        //    }
        //});
        imageView.setOnClickListener(v -> {
            listener.onPreviewClick(v);
        }); container.addView(imageView);
        return imageView;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * 设置预览点击监听接口
     */
    public void setPreviewClickListener(OnPreviewClickListener listener) {
        this.listener = listener;
    }

    /**
     * 定义预览点击监听接口
     */
    public interface OnPreviewClickListener {
        void onPreviewClick(View view);
    }
}

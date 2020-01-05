package com.vmloft.develop.library.tools.picker.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.vmloft.develop.library.tools.picker.IPictureLoader;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;

import com.vmloft.develop.library.tools.utils.VMDimen;
import java.util.ArrayList;
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
        PhotoView photoView = new PhotoView(mActivity);
        VMPictureBean bean = mDataList.get(position);
        IPictureLoader.Options options = new IPictureLoader.Options(bean.path);
        VMPicker.getInstance().getPictureLoader().load(mActivity, options, photoView);

        photoView.setOnPhotoTapListener((view, x, y) -> {
            if (listener != null) {
                listener.onPreviewClick(view, x, y);
            }
        });
        container.addView(photoView);
        return photoView;
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
        void onPreviewClick(View view, float x, float y);
    }
}

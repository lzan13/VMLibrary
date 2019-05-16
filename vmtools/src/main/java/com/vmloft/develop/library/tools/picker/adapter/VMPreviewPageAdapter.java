package com.vmloft.develop.library.tools.picker.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;

import com.vmloft.develop.library.tools.utils.VMDimen;
import java.util.ArrayList;

/**
 * Create by lzan13 on 2019/05/16 21:51
 *
 * 图片预览适配器
 */
public class VMPreviewPageAdapter extends PagerAdapter {

    private int screenWidth;
    private int screenHeight;
    private com.vmloft.develop.library.tools.picker.VMPicker VMPicker;
    private ArrayList<VMPictureBean> images = new ArrayList<>();
    private Activity mActivity;
    public PhotoViewClickListener listener;

    public VMPreviewPageAdapter(Activity activity, ArrayList<VMPictureBean> images) {
        this.mActivity = activity;
        this.images = images;

        screenWidth = VMDimen.getScreenSize().x;
        screenHeight =VMDimen.getScreenSize().x;
        VMPicker = VMPicker.getInstance();
    }

    public void setData(ArrayList<VMPictureBean> images) {
        this.images = images;
    }

    public void setPhotoViewClickListener(PhotoViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mActivity);
        VMPictureBean VMPictureBean = images.get(position);
        VMPicker.getPictureLoader()
            .displayImagePreview(mActivity, VMPictureBean.path, photoView, screenWidth, screenHeight);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (listener != null) {
                    listener.OnPhotoTapListener(view, x, y);
                }
            }
        });
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return images.size();
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

    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1);
    }
}

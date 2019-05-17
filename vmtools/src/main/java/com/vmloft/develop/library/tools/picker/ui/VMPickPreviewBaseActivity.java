package com.vmloft.develop.library.tools.picker.ui;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.picker.DataHolder;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.picker.adapter.VMPreviewPageAdapter;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.widget.VMViewPager;

import com.vmloft.develop.library.tools.utils.VMDimen;

import java.util.ArrayList;

/**
 * Create by lzan13 on 2019/05/17
 *
 * 图片预览基类
 */
public abstract class VMPickPreviewBaseActivity extends VMPickBaseActivity {

    protected ArrayList<VMPictureBean> mVMPictureBeans;      //跳转进ImagePreviewFragment的图片文件夹
    protected int mCurrentPosition = 0;              //跳转进ImagePreviewFragment时的序号，第几个图片
    protected TextView mTitleCount;                  //显示当前图片的位置  例如  5/31
    protected ArrayList<VMPictureBean> selectedImages;   //所有已经选中的图片
    protected View topBar;
    protected VMViewPager mViewPager;
    protected VMPreviewPageAdapter mAdapter;
    protected boolean isFromItems = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vm_activity_pick_preview);

        mCurrentPosition = getIntent().getIntExtra(VMPicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
        isFromItems = getIntent().getBooleanExtra(VMPicker.EXTRA_FROM_ITEMS, false);

        if (isFromItems) {
            // 据说这样会导致大量图片崩溃
            mVMPictureBeans = (ArrayList<VMPictureBean>) getIntent().getSerializableExtra(VMPicker.EXTRA_IMAGE_ITEMS);
        } else {
            // 下面采用弱引用会导致预览崩溃
            mVMPictureBeans = (ArrayList<VMPictureBean>) DataHolder.getInstance().retrieve(DataHolder.DH_CURRENT_IMAGE_FOLDER_ITEMS);
        }

        selectedImages = VMPicker.getInstance().getSelectedImages();

        //因为状态栏透明后，布局整体会上移，所以给头部加上状态栏的margin值，保证头部不会被覆盖
        topBar = findViewById(R.id.vm_pick_common_title_container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) topBar.getLayoutParams();
            params.topMargin = VMDimen.getStatusBarHeight();
            topBar.setLayoutParams(params);
        }
        topBar.findViewById(R.id.vm_common_ok_btn).setVisibility(View.GONE);
        topBar.findViewById(R.id.vm_common_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitleCount = (TextView) findViewById(R.id.vm_common_title_tv);

        mViewPager = (VMViewPager) findViewById(R.id.vm_pick_preview_viewpager);
        mAdapter = new VMPreviewPageAdapter(this, mVMPictureBeans);
        mAdapter.setPhotoViewClickListener(new VMPreviewPageAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1) {
                onImageSingleTap();
            }
        });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition, false);

        //初始化当前页面的状态
        mTitleCount.setText(getString(R.string.ip_preview_image_count, mCurrentPosition + 1, mVMPictureBeans
                .size()));
    }

    /**
     * 单击时，隐藏头和尾
     */
    public abstract void onImageSingleTap();

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        VMPicker.getInstance().restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        VMPicker.getInstance().saveInstanceState(outState);
    }
}
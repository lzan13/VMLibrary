package com.vmloft.develop.library.tools.picker.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;

import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.utils.VMFile;
import com.vmloft.develop.library.tools.utils.bitmap.VMBitmap;
import com.vmloft.develop.library.tools.widget.VMCropView;

import java.util.ArrayList;

/**
 * Create by lzan13 on 2019/05/16 20:37
 *
 * 修剪图片
 */
public class VMPickCropActivity extends VMPickBaseActivity implements VMCropView.OnBitmapSaveCompleteListener {

    private VMCropView mCropView;
    private Bitmap mBitmap;
    private boolean mIsSaveRectangle;
    private int mOutputX;
    private int mOutputY;
    private ArrayList<VMPictureBean> mVMPictureBeans;

    @Override
    protected int layoutId() {
        return R.layout.vm_activity_pick_crop;
    }

    @Override
    protected void initUI() {
        super.initUI();
        mCropView = findViewById(R.id.vm_pick_crop_iv);
        mCropView.setOnBitmapSaveCompleteListener(this);
    }

    @Override
    protected void initData() {
        getTopBar().setTitle(R.string.vm_pick_crop_picture);
        getTopBar().setIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                onFinish();
            }
        });
        getTopBar().setEndBtnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCropView.saveBitmapToFile(VMFile.getCacheFromSDCard(), mOutputX, mOutputY, mIsSaveRectangle);
            }
        });
        //获取需要的参数
        mOutputX = VMPicker.getInstance().getOutPutX();
        mOutputY = VMPicker.getInstance().getOutPutY();
        mIsSaveRectangle = VMPicker.getInstance().isSaveRectangle();
        mVMPictureBeans = VMPicker.getInstance().getSelectedPictures();
        String imagePath = mVMPictureBeans.get(0).path;

        mCropView.setFocusStyle(VMPicker.getInstance().getStyle());
        mCropView.setFocusWidth(VMPicker.getInstance().getFocusWidth());
        mCropView.setFocusHeight(VMPicker.getInstance().getFocusHeight());

        //缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels);
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(imagePath, options);
        //设置默认旋转角度
        mCropView.setImageBitmap(mCropView.rotate(mBitmap, VMBitmap.getBitmapDegree(imagePath)));
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = width / reqWidth;
            } else {
                inSampleSize = height / reqHeight;
            }
        }
        return inSampleSize;
    }

    @Override
    public void onBitmapSaveSuccess(VMPictureBean bean) {
        // 裁剪后替换掉返回数据的内容，但是不要改变全局中的选中数据
        mVMPictureBeans.remove(0);
        mVMPictureBeans.add(bean);

        Intent intent = new Intent();
        intent.putExtra(VMPicker.EXTRA_RESULT_ITEMS, mVMPictureBeans);
        setResult(VMPicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
        finish();
    }

    @Override
    public void onBitmapSaveError(VMPictureBean bean) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCropView.setOnBitmapSaveCompleteListener(null);
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}

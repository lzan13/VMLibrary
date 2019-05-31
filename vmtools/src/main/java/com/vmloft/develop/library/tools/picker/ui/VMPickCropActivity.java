package com.vmloft.develop.library.tools.picker.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;

import com.vmloft.develop.library.tools.base.VMConstant;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.utils.bitmap.VMBitmap;
import com.vmloft.develop.library.tools.widget.VMCropView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by lzan13 on 2019/05/16 20:37
 *
 * 修剪图片
 */
public class VMPickCropActivity extends VMPickBaseActivity implements VMCropView.OnBitmapSaveCompleteListener {

    private VMCropView mCropView;
    private Bitmap mBitmap;
    private boolean mIsSaveRectangle;
    private int mCropOutWidth;
    private int mCropOutHeight;
    private List<VMPictureBean> mPictureBeans;

    @Override
    protected int layoutId() {
        return R.layout.vm_activity_pick_crop;
    }

    @Override
    protected void initUI() {
        super.initUI();
        mCropView = findViewById(R.id.vm_pick_crop_iv);
        mCropView.setOnBitmapSaveCompleteListener(this);

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
                mCropView.saveBitmapToFile(VMPicker.getInstance().getCropCacheFolder(), mCropOutWidth, mCropOutHeight, mIsSaveRectangle);
            }
        });
    }

    @Override
    protected void initData() {
        mCropView.setFocusStyle(VMPicker.getInstance().getCropStyle());
        mCropView.setFocusWidth(VMPicker.getInstance().getCropFocusWidth());
        mCropView.setFocusHeight(VMPicker.getInstance().getCropFocusHeight());

        mCropOutWidth = VMPicker.getInstance().getCropOutWidth();
        mCropOutHeight = VMPicker.getInstance().getCropOutHeight();
        mIsSaveRectangle = VMPicker.getInstance().isSaveRectangle();
        mPictureBeans = VMPicker.getInstance().getSelectedPictures();

        String imagePath = mPictureBeans.get(0).path;
        // 缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels);
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(imagePath, options);
        // 设置默认旋转角度
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
        mPictureBeans.remove(0);
        mPictureBeans.add(bean);

        // 单选不需要裁剪，返回数据
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(VMConstant.KEY_PICK_RESULT_PICTURES, (ArrayList<? extends Parcelable>) mPictureBeans);
        setResult(VMConstant.VM_PICK_RESULT_CODE_PICTURES, intent);
        onFinish();
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

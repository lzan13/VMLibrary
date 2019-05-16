package com.vmloft.develop.library.tools.picker.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vmloft.develop.library.tools.picker.util.BitmapUtil;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.widget.VMCropView;

import java.util.ArrayList;

/**
 * Create by lzan13 on 2019/05/16 20:37
 *
 * 修剪图片
 */
public class VMPickCropActivity extends VMPickBaseActivity implements View.OnClickListener, VMCropView.OnBitmapSaveCompleteListener {

    private VMCropView mCropView;
    private Bitmap mBitmap;
    private boolean mIsSaveRectangle;
    private int mOutputX;
    private int mOutputY;
    private ArrayList<VMPictureBean> mVMPictureBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_crop);

        //初始化View
        findViewById(R.id.vm_common_back_btn).setOnClickListener(this);
        Button btn_ok = findViewById(R.id.vm_common_ok_btn);
        btn_ok.setText(getString(R.string.ip_complete));
        btn_ok.setOnClickListener(this);
        TextView tv_des = findViewById(R.id.vm_common_title_tv);
        tv_des.setText(getString(R.string.ip_photo_crop));
        mCropView = findViewById(R.id.vm_pick_crop_iv);
        mCropView.setOnBitmapSaveCompleteListener(this);

        //获取需要的参数
        mOutputX = VMPicker.getInstance().getOutPutX();
        mOutputY = VMPicker.getInstance().getOutPutY();
        mIsSaveRectangle = VMPicker.getInstance().isSaveRectangle();
        mVMPictureBeans = VMPicker.getInstance().getSelectedImages();
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
        //        mCropView.setImageBitmap(mBitmap);
        //设置默认旋转角度
        mCropView.setImageBitmap(mCropView.rotate(mBitmap, BitmapUtil.getBitmapDegree(imagePath)));

        //        mCropView.setImageURI(Uri.fromFile(new File(imagePath)));
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.vm_common_back_btn) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.vm_common_ok_btn) {
            mCropView.saveBitmapToFile(VMPicker.getInstance().getCropCacheFolder(this), mOutputX, mOutputY, mIsSaveRectangle);
        }
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

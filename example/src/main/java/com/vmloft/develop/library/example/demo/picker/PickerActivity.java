package com.vmloft.develop.library.example.demo.picker;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.base.VMConstant;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.widget.VMCropView;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.widget.VMViewGroup;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Create by lzan13 on 2019/05/19 20:20
 *
 * 测试图片选择器
 */
public class PickerActivity extends AppActivity{
    // 选择模式 单选 or 多选
    @BindView(R.id.picker_single_mode_rb) RadioButton mSingleModeRB;
    @BindView(R.id.picker_multi_mode_rb) RadioButton mMultiModeRB;

    // 是否开启裁剪
    @BindView(R.id.picker_crop_cb) CheckBox mCropCB;
    // 裁剪矩形 及 宽高
    @BindView(R.id.picker_crop_focus_rectangle_rb) RadioButton mCropRectangleRB;
    @BindView(R.id.picker_crop_focus_width_et) EditText mCropWidthET;
    @BindView(R.id.picker_crop_focus_height_et) EditText mCropHeightET;
    // 裁剪圆形 及 半径
    @BindView(R.id.picker_crop_focus_circle_rb) RadioButton mCropCircleRB;
    @BindView(R.id.picker_crop_focus_circle_et) EditText mCropSizeET;
    // 裁剪后输出宽高
    @BindView(R.id.picker_crop_out_width_et) EditText mCropOutWidthET;
    @BindView(R.id.picker_crop_out_height_et) EditText mCropOutHeightET;

    // 是否显示相机
    @BindView(R.id.picker_show_camera_cb) CheckBox mShowCameraCB;

    // 裁剪后是否保存矩形
    @BindView(R.id.picker_save_rectangle_cb) CheckBox mSaveRectangleCB;

    // 展示选择结果
    @BindView(R.id.picker_test_iv) ImageView mTestImgView;
    @BindView(R.id.picker_view_group) VMViewGroup mViewGroup;

    // 多选模式
    private boolean isMultiMode = false;
    // 是否显示相机
    private boolean isShowCamera = false;
    // 是否开启裁剪
    private boolean isCrop = true;
    // 裁剪焦点框的宽度
    private int mCropFocusWidth = 256;
    // 裁剪焦点框的高度
    private int mCropFocusHeight = 256;
    // 裁剪图片保存宽度
    private int mCropOutWidth = 720;
    // 裁剪图片保存高度
    private int mCropOutHeight = 720;
    private VMCropView.Style mCropStyle = VMCropView.Style.RECTANGLE;
    // 裁剪后是否保存矩形
    private boolean isSaveRectangle = true;


    // 实现加载图片接口
    private GlideIPictureLoader mPictureLoader;
    // 选择列表
    private ArrayList<VMPictureBean> mSelectPictures;
    // 展示选择图片高度
    private int height;

    /**
     * 加载布局
     */
    @Override
    protected int layoutId() {
        return R.layout.activity_picker;
    }

    /**
     * 初始化
     */
    @Override
    protected void init() {
        mSingleModeRB.setChecked(true);

        mCropCB.setChecked(true);
        mCropRectangleRB.setChecked(true);
        mCropWidthET.setText("256");
        mCropHeightET.setText("256");

        mCropSizeET.setText("128");
        mCropOutWidthET.setText("720");
        mCropOutHeightET.setText("720");

        mSaveRectangleCB.setChecked(true);

        mShowCameraCB.setChecked(true);

        mPictureLoader = new GlideIPictureLoader();

        height = VMDimen.dp2px(72);
    }

    @OnCheckedChanged({R.id.picker_crop_cb, R.id.picker_save_rectangle_cb,  R.id.picker_show_camera_cb })
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
        case R.id.picker_crop_cb:
            isCrop = isChecked;
            break;
        case R.id.picker_save_rectangle_cb:
            isSaveRectangle = isChecked;
            break;
        case R.id.picker_show_camera_cb:
            isShowCamera = isChecked;
            break;
        }
    }

    @OnClick({ R.id.picker_picture_btn })
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.picker_picture_btn:
            if (mSingleModeRB.isChecked()) {
                isMultiMode= false;
            } else if (mMultiModeRB.isChecked()) {
                isMultiMode= true;
            }
            if (mCropRectangleRB.isChecked()) {
                mCropStyle = VMCropView.Style.RECTANGLE;
                mCropFocusWidth = VMDimen.dp2px(Integer.valueOf(mCropWidthET.getText().toString()));
                mCropFocusHeight = VMDimen.dp2px(Integer.valueOf(mCropHeightET.getText().toString()));
            } else if (mCropCircleRB.isChecked()) {
                mCropFocusWidth = VMDimen.dp2px(Integer.valueOf(mCropSizeET.getText().toString())) * 2;
                mCropFocusHeight = mCropFocusWidth;
                mCropStyle = VMCropView.Style.CIRCLE;
            }
            mCropOutWidth = VMDimen.dp2px(Integer.valueOf(mCropOutWidthET.getText().toString()));
            mCropOutHeight = VMDimen.dp2px(Integer.valueOf(mCropOutHeightET.getText().toString()));

            VMPicker.getInstance().setMultiMode(isMultiMode)
                    .setPictureLoader(new GlideIPictureLoader())
                    .setCrop(isCrop)
                    .setCropFocusWidth(mCropFocusWidth)
                    .setCropFocusHeight(mCropFocusHeight)
                    .setCropOutWidth(mCropOutWidth)
                    .setCropOutHeight(mCropOutHeight)
                    .setCropStyle(mCropStyle)
                    .setSaveRectangle(isSaveRectangle)
                    .setSelectLimit(6)
                    .setShowCamera(isShowCamera)
                    .setSelectedPictures(mSelectPictures)
                    .startPicker(mActivity);
            break;
        }
    }

    /**
     * 接收返回值
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == VMConstant.VM_PICK_RESULT_CODE_PICTURES) {
            if (data != null && requestCode == VMConstant.VM_PICK_REQUEST_CODE) {
//                ArrayList<VMPictureBean> pictures = (ArrayList<VMPictureBean>) data.getSerializableExtra(VMPicker.EXTRA_RESULT_ITEMS);
                ArrayList<VMPictureBean> pictures = VMPicker.getInstance().getSelectedPictures();
                showPickImages(pictures);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 显示选择的图片
     */
    private void showPickImages(ArrayList<VMPictureBean> images) {
        mSelectPictures = images;
        mViewGroup.removeAllViews();
        for (int i = 0; i < mSelectPictures.size(); i++) {
            VMPictureBean item = mSelectPictures.get(i);
            int width = item.width * height / item.height;
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
            mPictureLoader.displayImage(mActivity, item.path, imageView, width, height);
            mViewGroup.addView(imageView, lp);
        }
    }
}

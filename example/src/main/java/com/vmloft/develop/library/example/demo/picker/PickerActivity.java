package com.vmloft.develop.library.example.demo.picker;

import android.content.Intent;
import android.util.TypedValue;
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
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.picker.bean.VMPictureBean;
import com.vmloft.develop.library.tools.picker.ui.VMPickGridActivity;
import com.vmloft.develop.library.tools.widget.VMCropView;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.widget.VMViewGroup;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class PickerActivity extends AppActivity{

    private VMPicker VMPicker;

    @BindView(R.id.rb_single_select) RadioButton rb_single_select;
    @BindView(R.id.rb_muti_select) RadioButton rb_muti_select;
    @BindView(R.id.rb_crop_square) RadioButton rb_crop_square;
    @BindView(R.id.rb_crop_circle) RadioButton rb_crop_circle;

    @BindView(R.id.et_crop_width) EditText et_crop_width;
    @BindView(R.id.et_crop_height) EditText et_crop_height;
    @BindView(R.id.et_crop_radius) EditText et_crop_radius;
    @BindView(R.id.et_outputx) EditText et_outputx;
    @BindView(R.id.et_outputy) EditText et_outputy;

    @BindView(R.id.pick_show_camera_cb) CheckBox mShowCameraCB;
    @BindView(R.id.pick_crop_cb) CheckBox mCropCB;
    @BindView(R.id.pick_save_rectangle_cb) CheckBox mSaveRectangleCB;
    @BindView(R.id.pick_test_iv) ImageView mTestImgView;

    @BindView(R.id.view_group) VMViewGroup mViewGroup;

    private GlideIPictureLoader imageLoader;
    // 选择列表
    private ArrayList<VMPictureBean> pickImages;
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
        VMPicker = VMPicker.getInstance();
        VMPicker.setPictureLoader(new GlideIPictureLoader());

        rb_muti_select.setChecked(true);
        rb_crop_square.setChecked(true);

        et_crop_width.setText("280");
        et_crop_height.setText("280");
        et_crop_radius.setText("140");
        et_outputx.setText("800");
        et_outputy.setText("800");

        VMPicker.setSelectLimit(9);

        mShowCameraCB.setChecked(true);
        mCropCB.setChecked(true);
        mSaveRectangleCB.setChecked(true);


        imageLoader = new GlideIPictureLoader();
        height = VMDimen.dp2px(72);
    }

    @OnCheckedChanged({ R.id.pick_show_camera_cb, R.id.pick_crop_cb, R.id.pick_save_rectangle_cb })
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
        case R.id.pick_show_camera_cb:
            VMPicker.setShowCamera(isChecked);
            break;
        case R.id.pick_crop_cb:
            VMPicker.setCrop(isChecked);
            break;
        case R.id.pick_save_rectangle_cb:
            VMPicker.setSaveRectangle(isChecked);
            break;
        }
    }

    @OnClick({ R.id.pick_picture_btn })
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.pick_picture_btn:
            VMPicker.setPictureLoader(new GlideIPictureLoader());

            if (rb_single_select.isChecked()) {
                VMPicker.setMultiMode(false);
            } else if (rb_muti_select.isChecked()) {
                VMPicker.setMultiMode(true);
            }
            if (rb_crop_square.isChecked()) {
                VMPicker.setStyle(VMCropView.Style.RECTANGLE);
                Integer width = Integer.valueOf(et_crop_width.getText().toString());
                Integer height = Integer.valueOf(et_crop_height.getText().toString());
                width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources()
                    .getDisplayMetrics());
                height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources()
                    .getDisplayMetrics());
                VMPicker.setFocusWidth(width);
                VMPicker.setFocusHeight(height);
            } else if (rb_crop_circle.isChecked()) {
                VMPicker.setStyle(VMCropView.Style.CIRCLE);
                Integer radius = Integer.valueOf(et_crop_radius.getText().toString());
                radius = VMDimen.dp2px(radius);
                VMPicker.setFocusWidth(radius * 2);
                VMPicker.setFocusHeight(radius * 2);
            }

            VMPicker.setOutPutX(Integer.valueOf(et_outputx.getText().toString()));
            VMPicker.setOutPutY(Integer.valueOf(et_outputy.getText().toString()));

            Intent intent = new Intent(this, VMPickGridActivity.class);
            intent.putExtra(VMPickGridActivity.EXTRAS_IMAGES, pickImages);
            //VMPicker.getInstance().setSelectedPictures(pictures);
            startActivityForResult(intent, 100);
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
        if (resultCode == VMPicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<VMPictureBean> images = (ArrayList<VMPictureBean>) data.getSerializableExtra(VMPicker.EXTRA_RESULT_ITEMS);
                showPickImages(images);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 显示选择的图片
     */
    private void showPickImages(ArrayList<VMPictureBean> images) {
        pickImages = images;
        mViewGroup.removeAllViews();
        for (int i = 0; i < pickImages.size(); i++) {
            VMPictureBean item = pickImages.get(i);
            int width = item.width * height / item.height;
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
            imageLoader.displayImage(mActivity, item.path, imageView, width, height);
            mViewGroup.addView(imageView, lp);
        }
    }
}

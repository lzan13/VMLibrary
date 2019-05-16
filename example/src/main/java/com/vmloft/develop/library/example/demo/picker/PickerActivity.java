package com.vmloft.develop.library.example.demo.picker;

import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vmloft.develop.library.picker.ImagePicker;
import com.vmloft.develop.library.picker.bean.ImageItem;
import com.vmloft.develop.library.picker.ui.ImageGridActivity;
import com.vmloft.develop.library.picker.view.CropImageView;
import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
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
public class PickerActivity extends AppActivity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private ImagePicker imagePicker;

    @BindView(R.id.rb_single_select)
    RadioButton rb_single_select;
    @BindView(R.id.rb_muti_select)
    RadioButton rb_muti_select;
    @BindView(R.id.rb_crop_square)
    RadioButton rb_crop_square;
    @BindView(R.id.rb_crop_circle)
    RadioButton rb_crop_circle;
    @BindView(R.id.tv_select_limit)
    TextView tv_select_limit;
    @BindView(R.id.et_crop_width)
    EditText et_crop_width;
    @BindView(R.id.et_crop_height)
    EditText et_crop_height;
    @BindView(R.id.et_crop_radius)
    EditText et_crop_radius;
    @BindView(R.id.et_outputx)
    EditText et_outputx;
    @BindView(R.id.et_outputy)
    EditText et_outputy;

    @BindView(R.id.cb_show_camera)
    CheckBox cb_show_camera;
    @BindView(R.id.cb_crop)
    CheckBox cb_crop;
    @BindView(R.id.cb_isSaveRectangle)
    CheckBox cb_isSaveRectangle;
    @BindView(R.id.btn_open_gallery)
    Button btn_open_gallery;
    @BindView(R.id.btn_wxDemo)
    Button btn_wxDemo;

    @BindView(R.id.view_group)
    VMViewGroup mViewGroup;

    private GlideImageLoader imageLoader;
    // 选择列表
    private ArrayList<ImageItem> pickImages;
    // 展示选择图片高度
    private int height;

    @Override
    protected int layoutId() {
        return R.layout.activity_picker;
    }

    @Override
    protected void init() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());

        rb_muti_select.setChecked(true);
        rb_crop_square.setChecked(true);

        et_crop_width.setText("280");
        et_crop_height.setText("280");
        et_crop_radius.setText("140");
        et_outputx.setText("800");
        et_outputy.setText("800");

        SeekBar sb_select_limit = findViewById(R.id.sb_select_limit);
        sb_select_limit.setMax(15);
        sb_select_limit.setOnSeekBarChangeListener(this);
        sb_select_limit.setProgress(9);

        cb_show_camera.setOnCheckedChangeListener(this);
        cb_show_camera.setChecked(true);
        cb_crop.setOnCheckedChangeListener(this);
        cb_crop.setChecked(true);
        cb_isSaveRectangle.setOnCheckedChangeListener(this);
        cb_isSaveRectangle.setChecked(true);

        btn_open_gallery.setOnClickListener(this);
        btn_wxDemo.setOnClickListener(this);

        imageLoader = new GlideImageLoader();

        height = VMDimen.dp2px(72);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_gallery:
                imagePicker.setImageLoader(new GlideImageLoader());

                if (rb_single_select.isChecked()) {
                    imagePicker.setMultiMode(false);
                } else if (rb_muti_select.isChecked()) {
                    imagePicker.setMultiMode(true);
                }
                if (rb_crop_square.isChecked()) {
                    imagePicker.setStyle(CropImageView.Style.RECTANGLE);
                    Integer width = Integer.valueOf(et_crop_width.getText().toString());
                    Integer height = Integer.valueOf(et_crop_height.getText().toString());
                    width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
                    height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
                    imagePicker.setFocusWidth(width);
                    imagePicker.setFocusHeight(height);
                } else if (rb_crop_circle.isChecked()) {
                    imagePicker.setStyle(CropImageView.Style.CIRCLE);
                    Integer radius = Integer.valueOf(et_crop_radius.getText().toString());
                    radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, getResources().getDisplayMetrics());
                    imagePicker.setFocusWidth(radius * 2);
                    imagePicker.setFocusHeight(radius * 2);
                }

                imagePicker.setOutPutX(Integer.valueOf(et_outputx.getText().toString()));
                imagePicker.setOutPutY(Integer.valueOf(et_outputy.getText().toString()));

                Intent intent = new Intent(this, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, pickImages);
                //ImagePicker.getInstance().setSelectedImages(images);
                startActivityForResult(intent, 100);
                break;
            case R.id.btn_wxDemo:
//                startActivity(new Intent(this, WxDemoActivity.class));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_show_camera:
                imagePicker.setShowCamera(isChecked);
                break;
            case R.id.cb_crop:
                imagePicker.setCrop(isChecked);
                break;
            case R.id.cb_isSaveRectangle:
                imagePicker.setSaveRectangle(isChecked);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tv_select_limit.setText(String.valueOf(progress));
        imagePicker.setSelectLimit(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
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
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                showPickImages(images);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 显示选择的图片
     */
    private void showPickImages(ArrayList<ImageItem> images) {
        pickImages = images;
        for (int i = 0; i < pickImages.size(); i++) {
            ImageItem item = pickImages.get(i);
            int width = item.width * height / item.height;
            ImageView imageView = new ImageView(activity);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
            imageLoader.displayImage(activity, item.path, imageView, width, height);
            mViewGroup.addView(imageView, lp);
        }

    }

}

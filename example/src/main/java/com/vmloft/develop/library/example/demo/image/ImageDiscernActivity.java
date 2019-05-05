package com.vmloft.develop.library.example.demo.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.tools.utils.VMLog;

import butterknife.BindView;

/**
 * Create by lzan13 on 2019/05/05
 *
 * 测试图片识别
 */
public class ImageDiscernActivity extends AppActivity {

    @BindView(R.id.id_img_1)
    ImageView oneView;
    @BindView(R.id.id_img_2)
    ImageView twoView;
    @BindView(R.id.id_img_3)
    ImageView destView;

    @Override
    protected int layoutId() {
        return R.layout.activity_image_discern;
    }

    @Override
    protected void init() {
        Bitmap oneBitmap = BitmapFactory.decodeFile("/sdcard/DCIM/Screenshots/verification_code_1.jpg");
//        Bitmap twoBitmap = BitmapFactory.decodeFile("/sdcard/DCIM/Screenshots/verification_code_2.jpg");
        Bitmap twoBitmap = BitmapFactory.decodeFile("/sdcard/DCIM/Screenshots/verification_code_3.jpg");
        if (oneBitmap == null || twoBitmap == null) {
            return;
        }
        oneView.setImageBitmap(oneBitmap);
        twoView.setImageBitmap(twoBitmap);
        String similarityPercent = ImageDsicern.similarityImage(oneBitmap, twoBitmap);
        VMLog.d("图片相似百分比: %s", similarityPercent);
    }
}

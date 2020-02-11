package com.vmloft.develop.library.example.demo.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
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

    @BindView(R.id.id_img_1) ImageView oneView;
    @BindView(R.id.id_img_2) ImageView twoView;
    @BindView(R.id.id_img_3) ImageView destView;

    @Override
    protected int layoutId() {
        return R.layout.activity_image_discern;
    }

    @Override
    protected void init() {
        Bitmap oneBitmap = BitmapFactory.decodeFile("/sdcard/DCIM/Screenshots/verification_code_1.png");
        Bitmap twoBitmap = BitmapFactory.decodeFile("/sdcard/DCIM/Screenshots/verification_code_2.png");
        //        Bitmap threeBitmap = BitmapFactory.decodeFile("/sdcard/DCIM/Screenshots/verification_code_2.png");
        if (oneBitmap == null || twoBitmap == null) {
            return;
        }
        // 剪切一下图片
        Rect mRect = new Rect(120, 800, 950, 1300);
        oneBitmap = Bitmap.createBitmap(oneBitmap, mRect.left, mRect.top, mRect.right - mRect.left, mRect.bottom - mRect.top);
        twoBitmap = Bitmap.createBitmap(twoBitmap, mRect.left, mRect.top, mRect.right - mRect.left, mRect.bottom - mRect.top);

        Point startPoint = new Point(0, 0);
        Point targetPoint = new Point(0, 0);

        Bitmap destBitmap = ImageDsicern.compareBitmap(oneBitmap, twoBitmap, startPoint, targetPoint);

        oneView.setImageBitmap(oneBitmap);
        twoView.setImageBitmap(twoBitmap);
        destView.setImageBitmap(destBitmap);

        VMLog.d("不同点坐标: start(%d, %d) - target(%d, %d)", startPoint.x, startPoint.y, targetPoint.x, targetPoint.y);
    }
}

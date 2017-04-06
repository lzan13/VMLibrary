package com.vmloft.develop.library.simple.widget;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMBaseActivity;
import com.vmloft.develop.library.tools.widget.VMDotLineView;

/**
 * Created by lzan13 on 2017/3/22.
 * 测试点线相连控件界面
 */
public class VMDotLineActivity extends VMBaseActivity {

    private Button testBtn;

    private VMDotLineView dotLineView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dot_line);

        dotLineView = (VMDotLineView) findViewById(R.id.view_dot_line);

        findViewById(R.id.btn_big_dipper).setOnClickListener(viewListener);
        findViewById(R.id.btn_square).setOnClickListener(viewListener);
        findViewById(R.id.btn_dot_color).setOnClickListener(viewListener);
        findViewById(R.id.btn_line_color).setOnClickListener(viewListener);

        dotLineView.addPoint(new Point(100, 100));
        dotLineView.addPoint(new Point(200, 200));
        dotLineView.addPoint(new Point(400, 400));
        dotLineView.addPoint(new Point(600, 600));
        dotLineView.addPoint(new Point(600, 300));
        dotLineView.addPoint(new Point(300, 600));
        dotLineView.addPoint(new Point(300, 700));
        dotLineView.addPoint(new Point(300, 400));

    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_big_dipper:
                    drawBigDipper();
                    break;
                case R.id.btn_square:
                    drawSquare();
                    break;
                case R.id.btn_dot_color:
                    dotLineView.setDotColor(0xddfe8729);
                    break;
                case R.id.btn_line_color:
                    dotLineView.setLineColor(0xddfe8729);
                    break;
            }
            dotLineView.refresh();
        }
    };

    /**
     * 绘制北斗七星
     */
    private void drawBigDipper() {
        dotLineView.clearPoints();
        dotLineView.setClosure( false);
        dotLineView.addPoint(new Point(100, 100));
        dotLineView.addPoint(new Point(300, 150));
        dotLineView.addPoint(new Point(390, 250));
        dotLineView.addPoint(new Point(500, 360));
        dotLineView.addPoint(new Point(495, 475));
        dotLineView.addPoint(new Point(710, 550));
        dotLineView.addPoint(new Point(790, 410));
    }

    /**
     * 绘制正方形
     */
    private void drawSquare() {
        dotLineView.clearPoints();
        dotLineView.setClosure(true);
        dotLineView.addPoint(new Point(200, 200));
        dotLineView.addPoint(new Point(600, 200));
        dotLineView.addPoint(new Point(600, 600));
        dotLineView.addPoint(new Point(200, 600));
    }
}

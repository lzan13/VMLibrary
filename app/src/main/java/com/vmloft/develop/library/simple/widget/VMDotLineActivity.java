package com.vmloft.develop.library.simple.widget;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.vmloft.develop.library.simple.R;
import com.vmloft.develop.library.tools.VMBaseActivity;

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

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dotLineView.setClosure(true);
                dotLineView.refresh();
            }
        });

        //dotLineView.addPoint(new Point(100, 100));
        //dotLineView.addPoint(new Point(300, 150));
        //dotLineView.addPoint(new Point(390, 250));
        //dotLineView.addPoint(new Point(500, 360));
        //dotLineView.addPoint(new Point(495, 475));
        //dotLineView.addPoint(new Point(710, 550));
        //dotLineView.addPoint(new Point(790, 410));

        //dotLineView.addPoint(new Point(100, 100));
        //dotLineView.addPoint(new Point(200, 200));
        //dotLineView.addPoint(new Point(400, 400));
        //dotLineView.addPoint(new Point(600, 600));
        //dotLineView.addPoint(new Point(600, 300));
        //dotLineView.addPoint(new Point(300, 600));
        //dotLineView.addPoint(new Point(300, 700));
        //dotLineView.addPoint(new Point(300, 400));


        dotLineView.addPoint(new Point(200, 200));
        dotLineView.addPoint(new Point(600, 200));
        dotLineView.addPoint(new Point(600, 600));
        dotLineView.addPoint(new Point(200, 600));


    }
}

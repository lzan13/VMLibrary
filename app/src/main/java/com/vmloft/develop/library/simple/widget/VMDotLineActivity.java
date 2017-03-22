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

        testBtn = (Button) findViewById(R.id.btn_test);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dotLineView.refresh();
            }
        });
        /**
         * A（315,400）B（400,325）C（460,310）D（545,300）E（580,434）F（690,310）G(670,235)
         */
        dotLineView = (VMDotLineView) findViewById(R.id.view_dot_line);
        dotLineView.addPoint(new Point(315, 400));
        dotLineView.addPoint(new Point(400, 324));
        dotLineView.addPoint(new Point(460, 310));
        dotLineView.addPoint(new Point(545, 300));
        dotLineView.addPoint(new Point(580, 434));
        dotLineView.addPoint(new Point(690, 310));
        dotLineView.addPoint(new Point(670, 235));

        //dotLineView.addPoint(new Point(100, 100));
        //dotLineView.addPoint(new Point(200, 200));
        //dotLineView.addPoint(new Point(400, 400));
        //dotLineView.addPoint(new Point(600, 600));

    }
}

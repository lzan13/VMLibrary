package com.vmloft.develop.library.simple;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.support.v7.widget.Toolbar;

import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.vmloft.develop.library.simple.camera.VMCameraActivity;
import com.vmloft.develop.library.simple.socket.VMSocketActivity;
import com.vmloft.develop.library.simple.theme.VMThemeActivity;
import com.vmloft.develop.library.simple.widget.details.VMDetailsActivity;
import com.vmloft.develop.library.simple.widget.VMRecordActivity;
import com.vmloft.develop.library.simple.widget.VMDotLineActivity;
import com.vmloft.develop.library.tools.VMBaseActivity;
import com.vmloft.develop.library.tools.widget.VMViewGroup;

public class VMMainActivity extends VMBaseActivity {

    @BindView(R.id.widget_toolbar) Toolbar toolbar;
    @BindView(R.id.view_group) VMViewGroup viewGroup;

    @Override protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(activity);

        toolbar.setTitle("MainActivity");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);

        String[] btnArray = {
                "Socket", "Dot Line", "Record", "Theme", "Camera", "Details"
        };
        for (int i = 0; i < btnArray.length; i++) {
            Button btn = new Button(new ContextThemeWrapper(activity, R.style.VMBtn_Red), null, 0);
            btn.setText(btnArray[i]);
            btn.setId(100 + i);
            btn.setOnClickListener(viewListener);
            viewGroup.addView(btn);
        }
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case 100:
                    intent.setClass(activity, VMSocketActivity.class);
                    break;
                case 101:
                    intent.setClass(activity, VMDotLineActivity.class);
                    break;
                case 102:
                    intent.setClass(activity, VMRecordActivity.class);
                    break;
                case 103:
                    intent.setClass(activity, VMThemeActivity.class);
                    break;
                case 104:
                    intent.setClass(activity, VMCameraActivity.class);
                    break;
                case 105:
                    intent.setClass(activity, VMDetailsActivity.class);
                    break;
            }
            onStartActivity(activity, intent);
        }
    };
}

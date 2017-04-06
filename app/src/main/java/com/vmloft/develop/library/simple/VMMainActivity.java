package com.vmloft.develop.library.simple;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;

import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.vmloft.develop.library.simple.socket.VMSocketActvity;
import com.vmloft.develop.library.simple.widget.RecordActivity;
import com.vmloft.develop.library.simple.widget.VMDotLineActivity;
import com.vmloft.develop.library.tools.VMBaseActivity;
import com.vmloft.develop.library.tools.widget.VMViewGroup;

public class VMMainActivity extends VMBaseActivity {

    @BindView(R.id.widget_toolbar) Toolbar toolbar;
    @BindView(R.id.view_group) VMViewGroup viewGroup;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        ButterKnife.bind(activity);

        toolbar.setTitle("MainActivity");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);

        String[] btnArray = {
                "Socket", "Dot Line View", "Record View"
        };
        for (int i = 0; i < btnArray.length; i++) {
            Button btn = new Button(activity);
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
                    intent.setClass(activity, VMSocketActvity.class);
                    break;
                case 101:
                    intent.setClass(activity, VMDotLineActivity.class);
                    break;
                case 102:
                    intent.setClass(activity, RecordActivity.class);
                    break;
            }
            startActivity(intent);
        }
    };
}

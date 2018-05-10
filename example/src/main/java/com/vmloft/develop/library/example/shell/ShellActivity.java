package com.vmloft.develop.library.example.shell;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.VMActivity;
import com.vmloft.develop.library.tools.utils.VMInput;
import com.vmloft.develop.library.tools.widget.VMViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lzan13 on 2018/5/2.
 */
public class ShellActivity extends VMActivity {

    @BindView(R.id.view_group) VMViewGroup viewGroup;
    private View rootView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);

        ButterKnife.bind(activity);

        RootShellCmd.initRootPermission();

        init();
    }

    private void init() {
        rootView = activity.getWindow().getDecorView();

        int[] ids = { 3, 4, 24, 25, 187, 210, 1000, 1001, 1002, 1003, 1004 };
        String[] btnArray = {
            "Home", "Back", "音量+", "音量－", "切换应用", "计算器", "触摸", "滑动", "输入文本", "鼠标点击", "长按拖动"
        };
        for (int i = 0; i < btnArray.length; i++) {
            Button btn = new Button(activity);
            btn.setText(btnArray[i]);
            btn.setId(ids[i]);
            btn.setOnClickListener(listener);
            viewGroup.addView(btn);
        }
    }

    private void down(int x, int y) {
        MotionEvent event =
            MotionEvent.obtain(SystemClock.uptimeMillis(), 1500,
                MotionEvent.ACTION_DOWN, x, y, 0);
        event.setAction(MotionEvent.ACTION_DOWN);
        event.setLocation(x, y);
        rootView.dispatchTouchEvent(event);
    }

    private void move(int x, int y) {
        MotionEvent event =
            MotionEvent.obtain(SystemClock.uptimeMillis(), 1500,
                MotionEvent.ACTION_MOVE, x, y, 0);
        event.setAction(MotionEvent.ACTION_MOVE);
        event.setLocation(x, y);
        rootView.dispatchTouchEvent(event);
    }

    private void up(int x, int y) {
        MotionEvent event =
            MotionEvent.obtain(SystemClock.uptimeMillis(), 1500,
                MotionEvent.ACTION_UP, x, y, 0);
        event.setAction(MotionEvent.ACTION_UP);
        event.setLocation(x, y);
        rootView.dispatchTouchEvent(event);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override public void onClick(View v) {
            switch (v.getId()) {
            case 3:
                RootShellCmd.simulateKeyEvent(3);
                break;
            case 4:
                RootShellCmd.simulateKeyEvent(4);
                break;
            case 24:
                RootShellCmd.simulateKeyEvent(24);
                break;
            case 25:
                RootShellCmd.simulateKeyEvent(25);
                break;
            case 187:
                RootShellCmd.simulateKeyEvent(187);
                break;
            case 210:
                RootShellCmd.simulateKeyEvent(210);
                break;
            case 1000:
                //RootShellCmd.simulateTap(540, 960);
                down(150, 580);
                //move(800, 1000);
                //move(150, 580);
                up(150, 580);
                break;
            case 1001:
                RootShellCmd.simulateSwipe(540, 960, 800, 1500);
                break;
            case 1002:
                RootShellCmd.simulateInputText("Hello shell!");
                break;
            case 1003:
                RootShellCmd.mouseClick();
                break;
            case 1004:
                RootShellCmd.simulateLong(540, 960, 300, 1200, 1500);
                break;
            }
        }
    };
}

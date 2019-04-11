package com.vmloft.develop.library.example;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;

import com.vmloft.develop.library.example.common.AppActivity;
import com.vmloft.develop.library.example.router.ARouter;
import com.vmloft.develop.library.tools.widget.VMViewGroup;

import butterknife.BindView;

public class MainActivity extends AppActivity {

    @BindView(R.id.view_group) VMViewGroup viewGroup;

    // 定义动态添加的控件起始 id
    private final int CLICK_START_ID = 100;

    @Override
    protected int loadView() {
        // 修改界面主题
        setTheme(R.style.AppTheme);
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        getToolbar().setTitle("VMLibrary");
        setSupportActionBar(getToolbar());

        String[] btnArray = {
            "工具", "描点控件", "详情控件", "录音控件", "录制屏幕", "声音播放", "按钮样式", "弹出窗口", "Web 功能", "指示器"
        };
        for (int i = 0; i < btnArray.length; i++) {
            Button btn = new Button(new ContextThemeWrapper(activity, R.style.VMBtn_Flat));
            btn.setText(btnArray[i]);
            btn.setId(CLICK_START_ID + i);
            btn.setOnClickListener(viewListener);
            viewGroup.addView(btn);
        }
    }

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case CLICK_START_ID + 0:
                ARouter.goTools(activity);
                break;
            case CLICK_START_ID + 1:
                ARouter.goDotLine(activity);
                break;
            case CLICK_START_ID + 2:
                ARouter.goDetails(activity);
                break;
            case CLICK_START_ID + 3:
                ARouter.goRecordAudio(activity);
                break;
            case CLICK_START_ID + 4:
                ARouter.goRecordScreen(activity);
                break;
            case CLICK_START_ID + 5:
                ARouter.goPlayAudio(activity);
                break;
            case CLICK_START_ID + 6:
                ARouter.goBtnStyle(activity);
                break;
            case CLICK_START_ID + 7:
                ARouter.goPWDialog(activity);
                break;
            case CLICK_START_ID + 8:
                ARouter.goWeb(activity);
                break;
            case CLICK_START_ID + 9:
                ARouter.goIndicator(activity);
                break;
            default:
                ARouter.goTools(activity);
                break;
            }
        }
    };

    /**
     * 重写返回按键会退到桌面，并不结束 app
     */
    @Override
    public void onBackPressed() {
        ARouter.goHome(activity);
    }
}

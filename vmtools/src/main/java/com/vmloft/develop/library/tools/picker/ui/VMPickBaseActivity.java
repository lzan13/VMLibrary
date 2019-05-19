package com.vmloft.develop.library.tools.picker.ui;

import android.os.Bundle;
import android.view.View;

import android.widget.RelativeLayout;
import com.vmloft.develop.library.tools.base.VMBActivity;
import com.vmloft.develop.library.tools.picker.VMPicker;
import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.widget.VMTopBar;

/**
 * 选择器基类
 */
public abstract class VMPickBaseActivity extends VMBActivity {

    // 统一的 TopBar
    protected VMTopBar mTopBar;

    @Override
    protected void initUI() {
        setupTopBar();
    }

    /**
     * 装载 TopBar
     */
    protected void setupTopBar() {
        mTopBar = findViewById(R.id.vm_common_top_bar);
        if (mTopBar != null) {
            // 设置状态栏透明主题时，布局整体会上移，所以给头部加上状态栏的 margin 值，保证头部不会被覆盖
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTopBar.getLayoutParams();
            params.topMargin = VMDimen.getStatusBarHeight();
            mTopBar.setLayoutParams(params);

            mTopBar.setIconListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 通用的获取 TopBar 方法
     */
    protected VMTopBar getTopBar() {
        return mTopBar;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        VMPicker.getInstance().restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        VMPicker.getInstance().saveInstanceState(outState);
    }
}

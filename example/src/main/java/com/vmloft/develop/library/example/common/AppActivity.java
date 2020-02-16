package com.vmloft.develop.library.example.common;

import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.base.VMBActivity;
import com.vmloft.develop.library.tools.utils.VMDimen;
import com.vmloft.develop.library.tools.utils.VMTheme;
import com.vmloft.develop.library.tools.widget.VMTopBar;

public abstract class AppActivity extends VMBActivity {

    // ButterKnife 注册对象
    private Unbinder unbinder;

    // 统一的 TopBar
    protected VMTopBar mTopBar;
    protected View mSpaceView;

    @Override
    protected void initUI() {
        unbinder = ButterKnife.bind(mActivity);

        VMTheme.setDarkStatusBar(mActivity, true);
        setupTobBar();
    }

    /**
     * 装载 TopBar
     */
    protected void setupTobBar() {
        mSpaceView = findViewById(R.id.common_top_space);
        if (mSpaceView != null) {
            // 设置状态栏透明主题时，布局整体会上移，所以给头部 View 设置 StatusBar 的高度
            mSpaceView.getLayoutParams().height = VMDimen.getStatusBarHeight();
        }
        mTopBar = findViewById(R.id.common_top_bar);
        if (mTopBar != null) {
            mTopBar.setIcon(R.drawable.ic_arrow_left);
            mTopBar.setIconListener(v -> {
                onBackPressed();
            });
        }
    }

    /**
     * 通用的获取 TopBar 方法
     */
    protected VMTopBar getTopBar() {
        return mTopBar;
    }

    /**
     * 设置图标
     */
    protected void setTopIcon(int resId) {
        if (mTopBar != null) {
            mTopBar.setIcon(resId);
        }
    }

    /**
     * 设置标题
     */
    protected void setTopTitle(String title) {
        if (mTopBar != null) {
            mTopBar.setTitle(title);
        }
    }

    /**
     * 设置子标题
     */
    protected void setTopSubtitle(String title) {
        if (mTopBar != null) {
            mTopBar.setSubtitle(title);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
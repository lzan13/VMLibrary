package com.vmloft.develop.library.example.common;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.View;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.base.VMActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.vmloft.develop.library.tools.utils.VMTheme;
import com.vmloft.develop.library.tools.widget.VMTopBar;

public abstract class AppActivity extends VMActivity {

    // ButterKnife 注册返回对象
    private Unbinder unbinder;
    // 统一的 TopBar
    protected VMTopBar mTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());

        VMTheme.setDarkStatusBar(mActivity, true);

        unbinder = ButterKnife.bind(mActivity);

        setupTobBar();

        init();
    }

    /**
     * 装载 TopBar
     */
    protected void setupTobBar() {
        mTopBar = findViewById(R.id.widget_top_bar);
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

    /**
     * 加载布局 id
     *
     * @return 返回布局 id
     */
    protected abstract int layoutId();

    /**
     * 初始化
     */
    protected abstract void init();
}
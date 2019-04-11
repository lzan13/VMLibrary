package com.vmloft.develop.library.example.common;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.base.VMActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class AppActivity extends VMActivity {

    // ButterKnife 注册返回对象
    private Unbinder unbinder;
    // 统一的 Toolbar
    @BindView(R.id.widget_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(loadView());

        unbinder = ButterKnife.bind(activity);

        init();
    }

    /**
     * 通用的获取 Toolbar 方法
     */
    protected Toolbar getToolbar() {
        return mToolbar;
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
    protected abstract int loadView();

    /**
     * 初始化
     */
    protected abstract void init();
}

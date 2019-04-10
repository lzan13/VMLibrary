package com.vmloft.develop.library.example.common;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.vmloft.develop.library.example.R;
import com.vmloft.develop.library.tools.VMActivity;

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

    protected Toolbar getToolbar() {
        return mToolbar;
    }

    protected abstract int loadView();

    protected abstract void init();
}

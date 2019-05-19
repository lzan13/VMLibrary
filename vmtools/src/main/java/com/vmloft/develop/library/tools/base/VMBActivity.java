package com.vmloft.develop.library.tools.base;

import android.os.Bundle;

/**
 * Create by lzan13 on 2019/05/19 17:57
 *
 * 自定义一个通用的基类
 */
public abstract class VMBActivity extends VMActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());

        initUI();

        initData();
    }

    /**
     * 加载布局 id
     *
     * @return 返回布局 id
     */
    protected abstract int layoutId();

    /**
     * 初始化 UI
     */
    protected abstract void initUI();

    /**
     * 初始化数据
     */
    protected abstract void initData();
}
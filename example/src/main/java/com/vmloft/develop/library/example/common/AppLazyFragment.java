package com.vmloft.develop.library.example.common;

import com.vmloft.develop.library.tools.base.VMLazyFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Create by lzan13 on 2019/04/11
 * 定义项目 Fragment 基类
 */
public abstract class AppLazyFragment extends VMLazyFragment {

    // ButterKnife 注册对象
    private Unbinder unbinder;

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, getView());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
package com.vmloft.develop.library.tools.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vmloft.develop.library.tools.R;
import com.vmloft.develop.library.tools.utils.VMLog;

/**
 * Create by lzan13 on 2019/04/11
 *
 * 自定义懒加载实现 Fragment 基类
 * 需注意: TODO 懒加载功能只能结合 ViewPager 才有效，其他清空不能使用
 */
public abstract class VMLazyFragment extends VMFragment {

    // 是否已经初始化
    protected boolean isInit = false;
    // 是否已经加载
    protected boolean isLoaded = false;
    // 是否显示
    protected boolean isVisible = false;

    // 懒加载内容容器
    protected ViewGroup mLazyContainer;
    // 懒加载进度提示蒙层
    protected View mLazyLoadingView;

    /**
     * 正常初始化 Fragmnet 方法，等价于 onCreate()
     */
    @Override
    protected void init() {
        initView();

        isInit = true;
        lazyLoad();
    }

    /**
     * 在懒加载实现是，这里布局加载了预定义的布局，默认布局有一个加载进度的蒙层，当 initView 执行完成后取消蒙层显示
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vm_fragment_lazy_layout, container, false);

        mLazyContainer = view.findViewById(R.id.lazy_container);
        mLazyLoadingView = view.findViewById(R.id.lazy_loading);
        if (!isNeedLoading()) {
            mLazyLoadingView.setVisibility(View.GONE);
        }
        LayoutInflater.from(mContext).inflate(layoutId(), mLazyContainer);

        return view;
    }

    /**
     * 判断当前 Fragment 是否显示做数据加载判断，此方法只能用在和 ViewPager 一起使用
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        VMLog.d("setUserVisibleHint: %s, %b", className, isVisibleToUser);
        // 保存当前 Fragment 显示状态
        isVisible = isVisibleToUser;
        // 触发懒加载
        lazyLoad();
    }

    /**
     * 懒加载数据处理，内部判断是否真正的去加载
     */
    private void lazyLoad() {
        // 只是打印输出当前状态
        if (isLoaded) {
            VMLog.d("不是第一次加载数据 isVisible: %b, %s", isVisible, className);
        } else {
            VMLog.d("第一次加载数据 isVisible: %b, %s", isVisible, className);
        }
        // 这里确定要不要执行数据加载
        if (isLoaded || !isVisible || !isInit) {
            return;
        }
        // 满足条件，调用数据加载
        initData();

        // 加载完成，隐藏懒加载进度
        isLoaded = true;
        // 判断是否需要加载进度 View
        if (isNeedLoading()) {
            mLazyLoadingView.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化界面控件，将 Fragment 变量和 View 建立起映射关系
     */
    protected abstract void initView();

    /**
     * 加载数据，这里已经实现了懒加载
     */
    protected abstract void initData();

    /**
     * 是否需要 loading
     */
    protected boolean isNeedLoading() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoaded = false;
    }
}

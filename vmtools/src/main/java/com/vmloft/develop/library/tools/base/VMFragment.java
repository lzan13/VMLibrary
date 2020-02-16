package com.vmloft.develop.library.tools.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.vmloft.develop.library.tools.utils.VMLog;

/**
 * Created by lzan13 on 2016/7/6.
 * Fragment的基类，进行简单的封装，ViewPager 结合 Fragment 实现数据懒加载
 */
public abstract class VMFragment extends Fragment {

    protected String className = this.getClass().getSimpleName();

    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        VMLog.v("onAttach: %s", className);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VMLog.v("onCreate: %s", className);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        VMLog.v("onCreateView: %s", className);
        View view = inflater.inflate(layoutId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        VMLog.v("onViewCreated: %s", className);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        VMLog.v("onActivityCreated: %s", className);

        initUI();

        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        VMLog.v("onStart: %s", className);
    }

    @Override
    public void onResume() {
        super.onResume();
        VMLog.v("onResume: %s", className);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VMLog.v("onActivityResult: %s", className);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        VMLog.v("onHiddenChanged: %s", className);
    }

    @Override
    public void onPause() {
        super.onPause();
        VMLog.v("onPause: %s", className);
    }

    @Override
    public void onStop() {
        super.onStop();
        VMLog.v("onStop: %s", className);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        VMLog.v("onDestroyView: %s", className);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        VMLog.v("onDetach: %s", className);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VMLog.v("onDestroy: %s", className);
    }

    /**
     * 加载 Fragment 界面布局
     *
     * @return 返回布局 id
     */
    protected abstract int layoutId();

    /**
     * 初始化 Fragment 界面 UI
     */
    protected abstract void initUI();

    /**
     * 加载数据
     */
    protected abstract void initData();

    /**
     * 定义 Fragmnet 回调接口，方便 Activity 与 Fragment 通讯
     */
    public interface FragmentListener {
        void onAction(int action, Object obj);
    }
}

package com.vmloft.develop.library.tools.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        VMLog.d("onAttach: %s", className);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VMLog.d("onCreate: %s", className);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        VMLog.d("onCreateView: %s", className);
        View view = inflater.inflate(layoutId(), container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        VMLog.d("onActivityCreated: %s", className);

        init();
    }

    //@Override
    //public void onStart() {
    //    super.onStart();
    //    VMLog.d("onStart: %s", className);
    //}
    //
    //@Override
    //public void onResume() {
    //    super.onResume();
    //    VMLog.d("onResume: %s", className);
    //}
    //
    //@Override
    //public void onPause() {
    //    super.onPause();
    //    VMLog.d("onPause: %s", className);
    //}
    //
    //@Override
    //public void onStop() {
    //    super.onStop();
    //    VMLog.d("onStop: %s", className);
    //}
    //
    //@Override
    //public void onDestroyView() {
    //    super.onDestroyView();
    //    VMLog.d("onDestroyView: %s", className);
    //}
    //
    //@Override
    //public void onDetach() {
    //    super.onDetach();
    //    VMLog.d("onDetach: %s", className);
    //}
    //
    //@Override
    //public void onDestroy() {
    //    super.onDestroy();
    //    VMLog.d("onDestroy: %s", className);
    //}


    /**
     * 加载 Fragment 界面布局
     *
     * @return 返回布局 id
     */
    protected abstract int layoutId();

    /**
     * 初始化 Fragment 界面
     */
    protected abstract void init();

    /**
     * 定义 Fragmnet 回调接口，方便 Activity 与 Fragment 通讯
     */
    public interface FragmentListener {
        void onAction(int action, Object obj);
    }
}

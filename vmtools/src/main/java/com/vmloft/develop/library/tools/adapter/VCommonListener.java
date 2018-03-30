package com.vmloft.develop.library.tools.adapter;

/**
 * Created by lzan13 on 2017/12/4.
 * 通用的 RecyclerView 回调接口
 */
public interface VCommonListener {
    /**
     * 回调方法
     *
     * @param action 动作
     * @param object 参数
     */
    void onItemAction(int action, Object object);
}

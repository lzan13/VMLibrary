package com.vmloft.develop.library.tools.base;

/**
 * Created by lzan13 on 2017/6/13.
 * 自定义请求回调
 */
public interface VMCallback<T> {

    /**
     * 成功的回调
     *
     * @param t 成功回调内容，可空
     */
    void onSuccess(T t);

    /**
     * 失败的回调
     *
     * @param code 失败错误码
     * @param desc 失败描述
     */
    void onError(int code, String desc);

    /**
     * 当前进度回调
     *
     * @param progress 当前进度百分比
     * @param desc     当前进度描述
     */
    void onProgress(int progress, String desc);
}

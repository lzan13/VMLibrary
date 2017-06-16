package com.vmloft.develop.library.tools;

/**
 * Created by lzan13 on 2017/6/13.
 * 自定义请求回调
 */
public interface VMCallback {

    /**
     * 成功的回调
     *
     * @param message 成功回调内容
     */
    void onSuccess(String message);

    /**
     * 失败的回调
     *
     * @param code 失败错误码
     * @param message 失败描述
     */
    void onError(int code, String message);

    /**
     * 当前进度回调
     *
     * @param progress 当前进度百分比
     * @param message 当前进度描述
     */
    void onPregress(int progress, String message);
}

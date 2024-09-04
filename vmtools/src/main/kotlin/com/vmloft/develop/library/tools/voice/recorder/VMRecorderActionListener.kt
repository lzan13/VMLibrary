package com.vmloft.develop.library.tools.voice.recorder

import com.vmloft.develop.library.tools.voice.bean.VMVoiceBean

/**
 * 录音控件的回调接口，用于回调给调用者录音结果
 */
abstract class VMRecorderActionListener {
    /**
     * 录音开始，默认空实现，有需要可重写
     */
    open fun onStart() {}

    /**
     * 录音取消，默认空实现，有需要可重写
     */
    open fun onCancel() {}

    /**
     * 录音成功
     * @param bean 录音数据 bean
     */
    abstract fun onComplete(bean: VMVoiceBean)

    /**
     * 录音分贝
     */
    open fun onDecibel(decibel: Int) {}

    /**
     * 录音分贝
     */
    open fun onRecordFFTData(data: DoubleArray) {}

    /**
     * 录音错误
     *
     * @param code 错误码
     * @param desc 错误描述
     */
    abstract fun onError(code: Int, desc: String)

}

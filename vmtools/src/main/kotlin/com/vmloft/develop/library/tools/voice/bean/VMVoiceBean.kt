package com.vmloft.develop.library.tools.voice.bean
    /**
     * 声音数据 bean
     */
    data class VMVoiceBean(
        // 录音分贝集合 按照 10次/s 采样，展示时可自己适当抽样
        var decibelList: MutableList<Int> = mutableListOf(),
        // 录音时长
        var duration: Int = 0,
        // 录音文件路径
        var path: String = "",
    )
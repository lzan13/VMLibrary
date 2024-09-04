package com.vmloft.develop.library.tools.voice.bean

import com.vmloft.develop.library.tools.utils.VMDimen

/**
 * 波形控件属性类
 */
data class VMWaveformBean(
    // 中心点
    var centerX: Float = 0f,
    var centerY: Float = 0f,
    // 宽高
    var width: Float = VMDimen.dp2px(1).toFloat(),
    var height: Float = VMDimen.dp2px(1).toFloat(),
    var animHeight: Float = height,
    // 缩放倍数
    var scale: Float = 1f
) {}
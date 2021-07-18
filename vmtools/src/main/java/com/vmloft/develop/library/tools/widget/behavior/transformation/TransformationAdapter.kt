package com.vmloft.develop.library.tools.widget.behavior.transformation

import android.view.View

import com.vmloft.develop.library.tools.widget.behavior.VMHeaderLayout

open class TransformationAdapter<in V : View> : Transformation<V> {

    override fun onStateMinHeight(child: V, parent: VMHeaderLayout, unConsumedDy: Int) {
    }

    override fun onStateNormalProcess(child: V, parent: VMHeaderLayout, percent: Float, dy: Int) {
    }

    override fun onStateMaxHeight(child: V, parent: VMHeaderLayout, unConsumedDy: Int) {
    }

    override fun onStateExtendProcess(child: V, parent: VMHeaderLayout, percent: Float, dy: Int) {
    }

    override fun onStateExtendMaxEnd(child: V, parent: VMHeaderLayout, unConsumedDy: Int) {
    }
}
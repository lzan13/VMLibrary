package com.vmloft.develop.library.tools.widget.behavior.transformation

import android.view.View

import com.vmloft.develop.library.tools.widget.behavior.VMHeaderLayout

class AlphaTransformation : TransformationAdapter<View>() {

    override fun onStateMinHeight(child: View, parent: VMHeaderLayout, unConsumedDy: Int) {
        child.alpha = 0.0f
        super.onStateMinHeight(child, parent, unConsumedDy)
    }

    override fun onStateNormalProcess(child: View, parent: VMHeaderLayout, percent: Float, dy: Int) {
        child.alpha = percent
    }

    override fun onStateMaxHeight(child: View, parent: VMHeaderLayout, unConsumedDy: Int) {
        child.alpha = 1.0f
        super.onStateMaxHeight(child, parent, unConsumedDy)
    }

}
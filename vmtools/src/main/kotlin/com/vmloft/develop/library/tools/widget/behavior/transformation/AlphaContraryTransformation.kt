package com.vmloft.develop.library.tools.widget.behavior.transformation

import android.view.View
import com.vmloft.develop.library.tools.widget.behavior.VMHeaderLayout

class AlphaContraryTransformation : TransformationAdapter<View>() {

    override fun onStateMinHeight(child: View, parent: VMHeaderLayout, unConsumedDy: Int) {
        child.alpha = 1.0f
    }

    override fun onStateNormalProcess(child: View, parent: VMHeaderLayout, percent: Float, dy: Int) {
        child.alpha = 1.0f - percent
    }

    override fun onStateMaxHeight(child: View, parent: VMHeaderLayout, unConsumedDy: Int) {
        child.alpha = 0.0f
    }

}
package com.vmloft.develop.library.tools.widget.behavior.transformation

import android.view.View
import com.vmloft.develop.library.tools.widget.behavior.VMHeaderLayout

class ScrollTransformation : TransformationAdapter<View>() {

    override fun onStateMinHeight(child: View, parent: VMHeaderLayout, unConsumedDy: Int) {
        parent.offsetChild(child, unConsumedDy)
    }

    override fun onStateNormalProcess(child: View, parent: VMHeaderLayout, percent: Float, dy: Int) {
        parent.offsetChild(child, dy)
    }

    override fun onStateExtendProcess(child: View, parent: VMHeaderLayout, percent: Float, dy: Int) {
        parent.offsetChild(child, dy)
    }

    override fun onStateExtendMaxEnd(child: View, parent: VMHeaderLayout, unConsumedDy: Int) {
        parent.offsetChild(child, unConsumedDy)
    }

    override fun onStateMaxHeight(child: View, parent: VMHeaderLayout, unConsumedDy: Int) {
        parent.offsetChild(child, unConsumedDy)
    }

}
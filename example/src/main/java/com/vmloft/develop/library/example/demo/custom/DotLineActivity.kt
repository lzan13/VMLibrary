package com.vmloft.develop.library.example.demo.custom

import android.graphics.Point
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R.id
import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.AppActivity

import kotlinx.android.synthetic.main.activity_dot_line.dipperBtn
import kotlinx.android.synthetic.main.activity_dot_line.dotColorBtn
import kotlinx.android.synthetic.main.activity_dot_line.dotLineView
import kotlinx.android.synthetic.main.activity_dot_line.lineColorBtn
import kotlinx.android.synthetic.main.activity_dot_line.squareBtn

/**
 * Created by lzan13 on 2017/3/22.
 * 测试点线相连控件界面
 */
@Route(path = "/VMLoft/DotLine")
class DotLineActivity : AppActivity() {

    override fun layoutId(): Int = layout.activity_dot_line

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义描点控件")

        dotLineView.addPoint(Point(100, 100))
        dotLineView.addPoint(Point(200, 200))
        dotLineView.addPoint(Point(400, 400))
        dotLineView.addPoint(Point(600, 600))
        dotLineView.addPoint(Point(600, 300))
        dotLineView.addPoint(Point(300, 600))
        dotLineView.addPoint(Point(300, 700))
        dotLineView.addPoint(Point(300, 400))

        dipperBtn.setOnClickListener { view -> onClick(view) }
        squareBtn.setOnClickListener { view -> onClick(view) }
        dotColorBtn.setOnClickListener { view -> onClick(view) }
        lineColorBtn.setOnClickListener { view -> onClick(view) }
    }

    override fun initData() {}

    private fun onClick(v: View) {
        when (v.id) {
            id.dipperBtn -> drawBigDipper()
            id.squareBtn -> drawSquare()
            id.dotColorBtn -> dotLineView.setDotColor(-0x220178d7)
            id.lineColorBtn -> dotLineView.setLineColor(-0x220178d7)
        }
        dotLineView.refresh()
    }

    /**
     * 绘制北斗七星
     */
    private fun drawBigDipper() {
        dotLineView.clearPoints()
        dotLineView.setClosure(false)
        dotLineView.addPoint(Point(100, 100))
        dotLineView.addPoint(Point(300, 150))
        dotLineView.addPoint(Point(390, 250))
        dotLineView.addPoint(Point(500, 360))
        dotLineView.addPoint(Point(495, 475))
        dotLineView.addPoint(Point(710, 550))
        dotLineView.addPoint(Point(790, 410))
    }

    /**
     * 绘制正方形
     */
    private fun drawSquare() {
        dotLineView.clearPoints()
        dotLineView.setClosure(true)
        dotLineView.addPoint(Point(200, 200))
        dotLineView.addPoint(Point(600, 200))
        dotLineView.addPoint(Point(600, 600))
        dotLineView.addPoint(Point(200, 600))
    }
}
package com.vmloft.develop.library.example.demo.barrage

import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.AppActivity
import com.vmloft.develop.library.tools.widget.barrage.VMBarrageView
import com.vmloft.develop.library.tools.widget.barrage.VMViewCreator
import kotlinx.android.synthetic.main.activity_barrage.*

//import kotlinx.android.synthetic.main.activity_barrage.*


/**
 * Created by lzan13 on 2020/5/18.
 * 描述：测试通知提醒界面
 */
@Route(path = "/VMLoft/Barrage")
class BarrageActivity : AppActivity() {

    private val mDataList: MutableList<BarrageBean> = mutableListOf()

    override fun layoutId(): Int {
        return R.layout.activity_barrage
    }

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义弹幕")
        val barrageView = findViewById<VMBarrageView<BarrageBean>>(R.id.barrageView)
        barrageStartBtn.setOnClickListener {
            barrageView.setCreator(ViewCreator())
                    .setMaxSize(50)
                    .create(mDataList)
                    .start()
        }

        barrageStopBtn.setOnClickListener {
            barrageView.stop()
        }

        barrageSendBtn.setOnClickListener {
            barrageView.addBarrage(BarrageBean("测试弹幕 ~ ~ "))
        }

    }

    override fun initData() {
        mDataList.add(BarrageBean("测试弹幕 0"))
        mDataList.add(BarrageBean("测试弹幕测试弹幕 1"))
        mDataList.add(BarrageBean("测试弹幕 2"))
        mDataList.add(BarrageBean("测试弹幕 3"))
        mDataList.add(BarrageBean("测试弹幕测试弹幕 4"))
        mDataList.add(BarrageBean("测试弹幕 5"))
        mDataList.add(BarrageBean("测试弹幕 6"))
        mDataList.add(BarrageBean("测试弹幕测试弹幕测试弹幕 7"))
        mDataList.add(BarrageBean("测试弹幕 8"))
        mDataList.add(BarrageBean("测试弹幕测试弹幕测试弹幕测试弹幕 9"))


    }


    class ViewCreator : VMViewCreator<BarrageBean> {
        override fun layoutId(): Int = R.layout.item_barrage_view

        override fun onBind(view: View, bean: BarrageBean) {
            val barrageItemTV = view.findViewById<TextView>(R.id.barrageItemTV)
            barrageItemTV.text = bean.content
        }

    }

    override fun onDestroy() {
        barrageView.stop()
        super.onDestroy()
    }
}
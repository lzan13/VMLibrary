package com.vmloft.develop.app.example.ui.demo.custom

import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.base.BActivity

import com.vmloft.develop.app.example.R
import com.vmloft.develop.app.example.databinding.ActivityDemoViewBarrageBinding
import com.vmloft.develop.app.example.router.AppRouter
import com.vmloft.develop.library.tools.animator.VMAnimator
import com.vmloft.develop.library.tools.widget.barrage.VMBarrageView
import com.vmloft.develop.library.tools.widget.barrage.VMViewCreator



/**
 * Created by lzan13 on 2020/5/18.
 * 描述：测试自定义弹幕控件界面
 */
@Route(path = AppRouter.appBarrage)
class BarrageViewActivity : BActivity<ActivityDemoViewBarrageBinding>() {
    private var mAnimatorWrap: VMAnimator.AnimatorSetWrap? = null

    private val mDataList: MutableList<BarrageBean> = mutableListOf()

    override fun initVB() = ActivityDemoViewBarrageBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义弹幕")
        val barrageView = findViewById<VMBarrageView<BarrageBean>>(R.id.barrageView)
        mBinding.barrageStartBtn.setOnClickListener {
            barrageView.setCreator(ViewCreator())
                .setMaxSize(50)
                .create(mDataList)
                .start()
        }

        mBinding.barrageResumeBtn.setOnClickListener {
            barrageView.resume()
        }
        mBinding.barragePauseBtn.setOnClickListener {
            barrageView.pause()
        }
        mBinding.barrageStopBtn.setOnClickListener {
            barrageView.stop()
        }
        mBinding.barrageSendBtn.setOnClickListener {
            barrageView.addBarrage(BarrageBean("测试手动新增弹幕 ~ ~ "))
        }
        mBinding.barrageResetBtn.setOnClickListener {
            mDataList.clear()
            mDataList.add(BarrageBean("测试重置弹幕 0"))
            mDataList.add(BarrageBean("测试重置弹幕测试弹幕 1"))
            mDataList.add(BarrageBean("测试重置弹幕 2"))
            mDataList.add(BarrageBean("测试重置弹幕 3"))
            mDataList.add(BarrageBean("测试重置弹幕测试弹幕 4"))
            mDataList.add(BarrageBean("测试重置弹幕 5"))
            mDataList.add(BarrageBean("测试重置弹幕 6"))
            mDataList.add(BarrageBean("测试重置弹幕测试弹幕测试弹幕 7"))
            mDataList.add(BarrageBean("测试重置弹幕 8"))
            mDataList.add(BarrageBean("测试重置弹幕测试弹幕测试弹幕测试弹幕 9"))
            barrageView.stop()
            barrageView.setCreator(ViewCreator())
                .setMaxSize(50)
                .create(mDataList)
                .start()
        }

        mBinding.animStartBtn.setOnClickListener { startAnim() }
        mBinding.animPauseBtn.setOnClickListener { pauseAnim() }
        mBinding.animResumeBtn.setOnClickListener { resumeAnim() }
        mBinding.animStopBtn.setOnClickListener { stopAnim() }

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

    /**
     * 开始动画
     */
    private fun startAnim() {
        val scaleXOptions = VMAnimator.AnimOptions(mBinding.animView, floatArrayOf(0f, 20f), VMAnimator.scaleX, 2500, repeatMode = 1)
        val scaleYOptions = VMAnimator.AnimOptions(mBinding.animView, floatArrayOf(0f, 20f), VMAnimator.scaleY, 2500, repeatMode = 1)
        val alphaOptions = VMAnimator.AnimOptions(mBinding.animView, floatArrayOf(1.0f, 0.0f), VMAnimator.alpha, 2500, repeatMode = 1)
        mAnimatorWrap = VMAnimator.createAnimator().play(scaleXOptions).with(scaleYOptions).with(alphaOptions)
        mAnimatorWrap?.start(delay = 100)
    }

    /**
     * 暂停动画
     */
    private fun pauseAnim() {
        mAnimatorWrap?.pause()
    }

    /**
     * 继续动画
     */
    private fun resumeAnim() {
        mAnimatorWrap?.resume()
    }

    /**
     * 停止动画
     */
    private fun stopAnim() {
        mAnimatorWrap?.cancel()
        mAnimatorWrap = null
    }

    /**
     * 弹幕创造者
     */
    class ViewCreator : VMViewCreator<BarrageBean> {
        override fun layoutId(): Int = R.layout.item_barrage_view

        override fun onBind(view: View, bean: BarrageBean) {
            val barrageItemTV = view.findViewById<TextView>(R.id.barrageItemTV)
            barrageItemTV.text = bean.content
        }

    }

    override fun onDestroy() {
        mBinding.barrageView.stop()
        super.onDestroy()
    }
}
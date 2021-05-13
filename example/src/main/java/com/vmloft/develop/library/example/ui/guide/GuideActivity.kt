package com.vmloft.develop.library.example.ui.guide

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.common.base.BaseActivity
import com.vmloft.develop.library.example.common.SPManager
import com.vmloft.develop.library.example.router.AppRouter

import com.vmloft.develop.library.tools.adapter.VMFragmentPagerAdapter

import kotlinx.android.synthetic.main.activity_guide.*


/**
 * Create by lzan13 on 2020/05/02 15:56
 * 描述：引导界面
 */
@Route(path = "/VMLoft/Guide")
class GuideActivity : BaseActivity() {

    private var mCurrentIndex = 0
    private var mFragmentList: MutableList<Fragment> = mutableListOf()
    private var mAdapter: VMFragmentPagerAdapter? = null


    override fun layoutId(): Int = R.layout.activity_guide

    override fun initUI() {
        super.initUI()

        guidePrevBtn.setOnClickListener {
            mCurrentIndex -= 1
            guideViewPager.setCurrentItem(mCurrentIndex, true)
        }
        guideNextBtn.setOnClickListener {
            mCurrentIndex += 1
            guideViewPager.setCurrentItem(mCurrentIndex, true)
        }
        guideFinishBtn.setOnClickListener {
            SPManager.instance.setGuideHide()
            AppRouter.goMain()
            finish()
        }
    }

    override fun initData() {
        mFragmentList.add(GuideFragment.newInstance(R.drawable.img_baymax, R.string.guide_title_0, R.string.guide_intro_0))
        mFragmentList.add(GuideFragment.newInstance(R.drawable.img_baymax, R.string.guide_title_1, R.string.guide_intro_1))
        mFragmentList.add(GuideFragment.newInstance(R.drawable.img_baymax, R.string.guide_title_2, R.string.guide_intro_2))
        mAdapter = VMFragmentPagerAdapter(supportFragmentManager, mFragmentList)
        guideViewPager.offscreenPageLimit = mFragmentList.size - 1
        guideViewPager.adapter = mAdapter
        guideIndicatorView.setViewPager(guideViewPager)
        /**
         * 通过监听 ViewPager 页面滑动渐变调整 ViewPager 的背景
         */
        guideViewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                mCurrentIndex = position
                guidePrevBtn.visibility = if (position == 0) View.GONE else View.VISIBLE
                guideNextBtn.visibility = if (position == mFragmentList.size - 1) View.GONE else View.VISIBLE
                guideFinishBtn.visibility = if (position == mFragmentList.size - 1) View.VISIBLE else View.GONE
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

}
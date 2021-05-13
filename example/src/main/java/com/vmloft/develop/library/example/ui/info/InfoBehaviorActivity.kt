package com.vmloft.develop.library.example.ui.info

import android.graphics.Color
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.ui.main.mine.TabFragment
import com.vmloft.develop.library.example.ui.main.mine.TabFragmentPagerAdapter
import com.vmloft.develop.library.tools.widget.behavior.VMBehaviorFrameLayout
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMTheme

import kotlinx.android.synthetic.main.activity_info_behavior.*
import kotlinx.android.synthetic.main.widget_common_top_bar.*


/**
 * Create by lzan13 on 2020/06/07 21:06
 * 描述：设置
 */
@Route(path = AppRouter.appInfoBehavior)
class InfoBehaviorActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_info_behavior

    override fun initUI() {
        super.initUI()
        VMTheme.setDarkStatusBar(this, false)

        setTopTitleColor(R.color.app_title_display)

        initBehavior()
    }

    override fun initData() {
    }


    private fun initBehavior() {
        mineBehaviorLayout.setStickHeaderHeight(VMDimen.dp2px(96) + VMDimen.statusBarHeight)
        mineBehaviorLayout.setHeaderScrollListener(object : VMBehaviorFrameLayout.SimpleHeaderScrollListener() {
            override fun onScroll(dy: Int, percent: Float) {
                mineCoverIV.translationY = dy / 2f

                setTopTitle(if (percent > 0.6) "个人信息" else "")

                commonTopLL.setBackgroundColor(Color.argb((percent * 255).toInt(), 42, 42, 42))
            }
        })
        mineBehaviorLayout.setMaxHeaderHeight(VMDimen.dp2px(360))

        val titles = arrayOf("最新", "热门", "我的")
        val fragments: ArrayList<Fragment> = ArrayList()
        for (i in titles.indices) {
            fragments.add(TabFragment())
            tabLayout.addTab(tabLayout.newTab())
        }
        tabLayout.setupWithViewPager(viewPager, false)
        viewPager.adapter = TabFragmentPagerAdapter(supportFragmentManager, fragments)
        for (i in titles.indices) {
            tabLayout.getTabAt(i)?.text = titles[i]
        }
    }
}
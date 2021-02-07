package com.vmloft.develop.library.example.ui.main.mine


import android.graphics.Color
import androidx.fragment.app.Fragment
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseFragment
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.utils.showBar
import com.vmloft.develop.library.tools.utils.behavior.VMBehaviorLinearLayout
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.utils.VMTheme

import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.widget_common_top_bar.*


/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：我的
 */
class MineFragment : BaseFragment() {

    private var drawOver = false
    override fun layoutId() = R.layout.fragment_mine

    override fun initUI() {
        super.initUI()
        VMTheme.setDarkStatusBar(activity!!, false)

        mineInfoCL.setOnClickListener {
            AppRouter.go(AppRouter.appInfoBehavior)
        }
        initBehavior()

    }

    override fun initData() {

    }

    private fun initBehavior() {
        mineBehaviorLayout.setStickHeaderHeight(VMDimen.dp2px(96) + VMDimen.statusBarHeight)
        mineBehaviorLayout.setHeaderScrollListener(object : VMBehaviorLinearLayout.SimpleHeaderScrollListener() {
            override fun onScroll(dy: Int, percent: Float) {
                setTopTitle(if (percent > 0.6) "个人信息" else "")

                commonTopLL.setBackgroundColor(Color.argb((percent * 255).toInt(), 42, 42, 42))
            }
        })
        mineBehaviorLayout.setHeaderBackground(mineCoverIV)
        mineBehaviorLayout.setMaxHeaderHeight(VMDimen.dp2px(360))

        mineAvatarIV.setOnClickListener {
            if (drawOver) {
                drawOver = false
                mineBehaviorLayout.setNeedDragOver(drawOver)
            } else {
                drawOver = true
                mineBehaviorLayout.setNeedDragOver(drawOver)
            }
        }
        mineNameTV.setOnClickListener { showBar("点击名字") }

        val titles = arrayOf("最新", "热门", "我的")
        val fragments: ArrayList<Fragment> = ArrayList()
        for (i in titles.indices) {
            fragments.add(TabFragment())
            tabLayout.addTab(tabLayout.newTab())
        }
        tabLayout.setupWithViewPager(viewPager, false)
        viewPager.adapter = TabFragmentPagerAdapter(childFragmentManager, fragments)
        for (i in titles.indices) {
            tabLayout.getTabAt(i)?.text = titles[i]
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        activity?.let { activity ->
            if (hidden) {
                VMTheme.setDarkStatusBar(activity, true)
            } else {
                VMTheme.setDarkStatusBar(activity, false)
            }
        }
    }
}
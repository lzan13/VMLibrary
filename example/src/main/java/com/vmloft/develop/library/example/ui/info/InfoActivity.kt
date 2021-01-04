package com.vmloft.develop.library.example.ui.info

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.ui.main.mine.TabFragment
import com.vmloft.develop.library.example.ui.main.mine.TabFragmentPagerAdapter
import com.vmloft.develop.library.example.utils.toast
import com.vmloft.develop.library.example.widget.BehaviorFrameLayout
import com.vmloft.develop.library.tools.utils.VMDimen

import kotlinx.android.synthetic.main.activity_info.*


/**
 * Create by lzan13 on 2020/06/07 21:06
 * 描述：设置
 */
@Route(path = AppRouter.appInfo)
class InfoActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_info

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings)

        initBehavior()
    }

    override fun initData() {
    }


    private fun initBehavior() {

        mineBehaviorLayout.setHeaderScrollListener(object : BehaviorFrameLayout.SimpleHeaderScrollListener() {
            override fun onScroll(dy: Int, fraction: Float) {
                mineCoverIV.translationY = dy / 2f
            }
        })
        mineBehaviorLayout.setMaxHeaderHeight(VMDimen.dp2px(360))

        mineAvatarIV.setOnClickListener { toast("点击头像") }
        mineNameTV.setOnClickListener { toast("点击名字") }

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
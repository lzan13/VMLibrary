package com.vmloft.develop.library.example.ui.user

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.alibaba.android.arouter.facade.annotation.Route

import com.google.android.material.tabs.TabLayoutMediator

import com.vmloft.develop.library.common.base.BaseActivity
import com.vmloft.develop.library.common.utils.CUtils
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.common.CommonFragment

import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Create by lzan13 on 2021/01/20 22:56
 * 描述：用户信息界面
 */
@Route(path = AppRouter.appUserInfo)
class UserInfoActivity : BaseActivity() {

    private val fragmentList = arrayListOf<Fragment>()
    private lateinit var publishFragment: CommonFragment
    private lateinit var likeFragment: CommonFragment

    private val titles = listOf("发布", "喜欢")

    override fun layoutId() = R.layout.activity_user_info

    override fun initUI() {
        super.initUI()
        CUtils.setDarkMode(this, false)

        setTopTitleColor(R.color.app_title_display)

    }

    override fun initData() {
        initFragmentList()
        initViewPager()
    }

    /**
     * 初始化 Fragment 集合
     */
    private fun initFragmentList() {
        publishFragment = CommonFragment.newInstance(titles[0])
        likeFragment = CommonFragment.newInstance(titles[1])

        fragmentList.run {
            add(publishFragment)
            add(likeFragment)
        }
    }

    /**
     * 初始化 ViewPager
     */
    private fun initViewPager() {
        viewPager.offscreenPageLimit = titles.size - 1
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int) = fragmentList[position]

            override fun getItemCount() = fragmentList.size
        }
        // 将 TabLayout 与 ViewPager 进行绑定
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}
package com.vmloft.develop.library.example.ui.user

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.tabs.TabLayoutMediator

import com.vmloft.develop.library.common.base.BaseActivity
import com.vmloft.develop.library.common.image.IMGLoader
import com.vmloft.develop.library.common.router.CRouter
import com.vmloft.develop.library.common.utils.CUtils
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.ui.main.mine.TabFragment
import com.vmloft.develop.library.tools.utils.VMDimen
import com.vmloft.develop.library.tools.widget.behavior.VMBehaviorFrameLayout

import kotlinx.android.synthetic.main.activity_user_info.*


/**
 * Create by lzan13 on 2021/01/20 22:56
 * 描述：用户信息界面
 */
@Route(path = AppRouter.appUserInfo)
class UserInfoActivity : BaseActivity() {

    private val fragmentList = arrayListOf<Fragment>()
    private lateinit var minePublishFragment: TabFragment
    private lateinit var mineLikeFragment: TabFragment

    private val titles = listOf("发布", "喜欢")

    override fun layoutId() = R.layout.activity_user_info

    override fun initUI() {
        super.initUI()
        CUtils.setDarkMode(this, false)

        setTopTitleColor(R.color.app_title_display)

        initBehavior()

//        mineCoverIV.setOnClickListener { CRouter.goDisplaySingle(user.cover) }
//        mineAvatarIV.setOnClickListener { CRouter.goDisplaySingle(user.avatar) }
        mineFansLL.setOnClickListener { }
        mineFollowLL.setOnClickListener { }
        mineLikeLL.setOnClickListener { }

//        mineEditInfoTV.setOnClickListener { CRouter.go(AppRouter.appPersonalInfo) }
//        mineSettingsBtn.setOnClickListener { CRouter.go(AppRouter.appSettings) }

    }

    override fun initData() {
        ARouter.getInstance().inject(this)

        initFragmentList()
        initViewPager()

        bindInfo()
    }

    /**
     * 初始化 Fragment 集合
     */
    private fun initFragmentList() {
        minePublishFragment = TabFragment.newInstance("测试 tab 1")
        mineLikeFragment = TabFragment.newInstance("测试 tab 2")

        fragmentList.run {
            add(minePublishFragment)
            add(mineLikeFragment)
        }
    }

    private fun initBehavior() {
        mineBehaviorLayout.setStickHeaderHeight(VMDimen.dp2px(48) + VMDimen.dp2px(36) + VMDimen.statusBarHeight)
        mineBehaviorLayout.setHeaderScrollListener(object : VMBehaviorFrameLayout.SimpleHeaderScrollListener() {
            override fun onScroll(dy: Int, percent: Float) {
                mineCoverIV.translationY = dy / 2f

                setTopTitle(if (percent > 0.6) "小透明" else "")

                setTopBGColor(Color.argb((percent * 255).toInt(), 42, 42, 42))
            }
        })
        mineBehaviorLayout.setMaxHeaderHeight(VMDimen.dp2px(512))

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
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
//            tab.customView = createTabView(position)
//            if (position == 0) {
//                tab.customView?.isSelected = true
//            }
        }.attach()
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                setTabState(tab, true)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//                setTabState(tab, false)
//            }
//        })
    }
//
//    /**
//     * 设置 Tab 选中状态
//     */
//    private fun setTabState(tab: TabLayout.Tab, selected: Boolean) {
//        tab.customView?.isSelected = selected
//        var tabTV: TextView? = tab.customView?.findViewById(R.id.customTabTV)
//        if (selected) {
//            tabTV?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
//            tabTV?.setTextColor(VMColor.byRes(R.color.app_tab_selected))
//            tabTV?.setTypeface(Typeface.DEFAULT_BOLD)
//        } else {
//            tabTV?.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
//            tabTV?.setTextColor(VMColor.byRes(R.color.app_tab))
//        }
//    }
//
//    /**
//     * 创建自定义 TabView
//     */
//    private fun createTabView(position: Int): View {
//        var customTabView: View = View.inflate(activity, R.layout.widget_tab_view, null)
//        var titleTV: TextView = customTabView.findViewById(R.id.customTabTV)
//        titleTV.text = titles[position]
//        return customTabView
//    }

    /**
     * 绑定用户数据
     */
    private fun bindInfo() {
//        val cover: String = user.cover
//        IMGLoader.loadCover(mineCoverIV, cover)
//
//        val avatar: String = user.avatar
//        IMGLoader.loadAvatar(mineAvatarIV, avatar)
//
//        when (user.gender) {
//            1 -> mineGenderIV.setImageResource(R.drawable.ic_gender_man)
//            0 -> mineGenderIV.setImageResource(R.drawable.ic_gender_woman)
//            else -> mineGenderIV.setImageResource(R.drawable.ic_gender_man)
//        }
//
//        mineNameTV.text = user.nickname
//        mineAddressTV.text = user.address
//        mineSignatureTV.text = user.signature
//
//        mineLikeTV.text = user.likeCount.toString()
//        mineFollowTV.text = user.followCount.toString()
//        mineFansTV.text = user.fansCount.toString()
    }

}
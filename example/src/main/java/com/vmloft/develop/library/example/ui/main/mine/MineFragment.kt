package com.vmloft.develop.library.example.ui.main.mine


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.google.android.material.tabs.TabLayoutMediator
import com.vmloft.develop.library.base.BFragment

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.base.router.CRouter
import com.vmloft.develop.library.base.utils.CUtils
import com.vmloft.develop.library.example.common.CommonFragment
import com.vmloft.develop.library.example.databinding.FragmentMineBinding
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.tools.utils.VMDimen


/**
 * Create by lzan13 on 2020/05/02 11:54
 * 描述：我的
 */
class MineFragment : BFragment<FragmentMineBinding>() {

    private val fragmentList = arrayListOf<Fragment>()
    private lateinit var publishFragment: CommonFragment
    private lateinit var secretFragment: CommonFragment
    private lateinit var likeFragment: CommonFragment

    private val titles = listOf("发布", "私密", "喜欢")

    override fun initVB(inflater: LayoutInflater, parent: ViewGroup?) = FragmentMineBinding.inflate(inflater, parent, false)

    override fun initUI() {
        super.initUI()
        CUtils.setDarkMode(requireActivity(), false)
        mBinding.tabTopSpaceView.layoutParams.height = VMDimen.dp2px(48) + VMDimen.statusBarHeight

        setTopTitleColor(R.color.app_title_display)
        setTopTitle("小透明")

//            mineAvatarIV.setOnClickListener { CRouter.goDisplaySingle(mUser.avatar) }
//            mineFansLL.setOnClickListener { }
//            mineFollowLL.setOnClickListener { }
//            mineLikeLL.setOnClickListener { }
//
//            mineEditInfoTV.setOnClickListener { CRouter.go(AppRouter.appPersonalInfo) }
        mBinding.mineEditInfoTV.setOnClickListener { CRouter.go(AppRouter.appUserInfo) }
        mBinding.mineSettingsBtn.setOnClickListener { CRouter.go(AppRouter.appSettings) }
//
//            LDEventBus.observe(this, Constants.userInfoEvent, User::class.java, {
//                mUser = it
//                bindInfo()
//            })
    }

    override fun initData() {
        initFragmentList()
        initViewPager()

        bindInfo()
    }

    /**
     * 初始化 Fragment 集合
     */
    private fun initFragmentList() {
        publishFragment = CommonFragment.newInstance(titles[0])
        secretFragment = CommonFragment.newInstance(titles[1])
        likeFragment = CommonFragment.newInstance(titles[2])

        fragmentList.run {
            add(publishFragment)
            add(secretFragment)
            add(likeFragment)
        }
    }

    /**
     * 初始化 ViewPager
     */
    private fun initViewPager() {
        mBinding.viewPager.offscreenPageLimit = titles.size - 1
        mBinding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int) = fragmentList[position]

            override fun getItemCount() = fragmentList.size
        }
        // 将 TabLayout 与 ViewPager 进行绑定
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        activity?.let {
            if (hidden) {
                CUtils.setDarkMode(it, true)
            } else {
                CUtils.setDarkMode(it, false)
            }
        }
    }

    /**
     * 绑定用户数据
     */
    private fun bindInfo() {
        mBinding.mineNameTV.text = "测试昵称"

//            val cover: String = mUser.cover
//            IMGLoader.loadCover(mineCoverIV, cover)
//
//            val avatar: String = mUser.avatar
//            IMGLoader.loadAvatar(mineAvatarIV, avatar)
//
//            when (mUser.gender) {
//                1 -> mineGenderIV.setImageResource(R.drawable.ic_gender_man)
//                0 -> mineGenderIV.setImageResource(R.drawable.ic_gender_woman)
//                else -> mineGenderIV.setImageResource(R.drawable.ic_gender_man)
//            }
//
//            mineNameTV.text = if (mUser.nickname.isNullOrEmpty()) VMStr.byRes(R.string.info_nickname_default) else mUser.nickname
//            mineAddressTV.text = if (mUser.address.isNullOrEmpty()) VMStr.byRes(R.string.info_address_default) else mUser.address
//            mineSignatureTV.text = if (mUser.signature.isNullOrEmpty()) VMStr.byRes(R.string.info_signature_default) else mUser.signature
//
//            mineLikeTV.text = mUser.likeCount.toString()
//            mineFollowTV.text = mUser.followCount.toString()
//            mineFansTV.text = mUser.fansCount.toString()
    }


}
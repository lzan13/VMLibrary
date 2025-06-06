package com.vmloft.develop.app.example.ui.demo.anim

import com.didi.drouter.annotation.Router

import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.app.example.databinding.ActivityDemoAnimBinding
import com.vmloft.develop.app.example.router.AppRouter

/**
 * Created by lzan13 on 2017/4/7.
 * 描述：测试 Lottie 动画
 */
@Router(path = AppRouter.appAnim)
class AnimActivity : BActivity<ActivityDemoAnimBinding>() {

    override fun initVB() = ActivityDemoAnimBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("Lottie 动画")
    }

    override fun initData() {
        binding.startAnimBtn.setOnClickListener {
            binding.interludeAV.startAnim(false)
        }
        binding.startLoopAnimBtn.setOnClickListener {
            binding.interludeAV.startAnim(true)
        }
        binding.stopAnimBtn.setOnClickListener {
            binding.interludeAV.stopAnim()
        }
    }

    private fun bindLottieData() {
//        for (fileName in lottieAnimList) {
//            val lottieView = LayoutInflater.from(this)
//                .inflate(R.layout.lottie_anim_item, null)
//            val lottieAnimView = lottieView.findViewById<LottieAnimationView>(R.id.lottieAnimView)
//            lottieAnimView.imageAssetsFolder = "lottie/images/${fileName}"
//            // 设置动画资源
//            lottieAnimView.setAnimation("lottie/json/${fileName}.json")
//            // 设置重复次数
//            lottieAnimView.repeatCount = LottieDrawable.INFINITE
//            // 开始播放
//            lottieAnimView.playAnimation()
//
//            // 设置名称
//            val lottieAnimName = lottieView.findViewById<TextView>(R.id.lottieAnimName)
//            lottieAnimName.text = fileName
//
//            val margin = VMDimen.dp2px(8)
//            val lp: ViewGroup.MarginLayoutParams = ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT)
//            lp.setMargins(margin)
//            mBinding.viewGroup.addView(lottieView, lp)
//        }

    }

}
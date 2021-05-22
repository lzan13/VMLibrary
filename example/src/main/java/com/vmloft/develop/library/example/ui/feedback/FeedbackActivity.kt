package com.vmloft.develop.library.example.ui.feedback

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.common.base.BaseActivity
import com.vmloft.develop.library.example.router.AppRouter


/**
 * Create by lzan13 on 2020/6/17 17:10
 * 描述：问题反馈
 */
@Route(path = AppRouter.appFeedback)
class FeedbackActivity : BaseActivity() {

    @Autowired
    lateinit var url: String


    override fun layoutId(): Int = R.layout.activity_feedback

    override fun initUI() {
        super.initUI()
        setTopTitle(R.string.settings_feedback)
    }

    override fun initData() {

    }

}

package com.vmloft.develop.library.common.web

import android.webkit.WebView
import android.widget.LinearLayout

import com.didi.drouter.annotation.Router

import com.just.agentweb.AgentWeb
import com.just.agentweb.WebChromeClient

import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.library.base.router.CRouter
import com.vmloft.develop.library.common.databinding.ActivityWebBinding
import com.vmloft.develop.library.tools.utils.VMColor

/**
 * Create by lzan13 on 2020/05/02 15:56
 * 描述：Web 界面
 */
@Router(path = CRouter.commonWeb)
class WebActivity : BActivity<ActivityWebBinding>() {

    lateinit var url: String

    private lateinit var mAgentWeb: AgentWeb

    override fun initUI() {
        super.initUI()
        setTopTitle(com.vmloft.develop.library.tools.R.string.vm_loading)

        initWebView()
    }

    override fun initVB() = ActivityWebBinding.inflate(layoutInflater)

    override fun initData() {
        url = CRouter.optString(intent,"url")

        mAgentWeb.urlLoader.loadUrl(url)
    }

    private fun initWebView() {
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding.webContainer, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator(VMColor.byRes(com.vmloft.develop.library.base.R.color.app_main), 1)
            .setWebChromeClient(chromeClient)
            .createAgentWeb()
            .ready()
            .go("")
        mAgentWeb.webCreator.webParentLayout.setBackgroundResource(com.vmloft.develop.library.base.R.color.app_bg)
    }

    /**
     * WebView 回调
     */
    private val chromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onReceivedTitle(view: WebView, title: String) {
            setTopTitle(title)
        }
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}
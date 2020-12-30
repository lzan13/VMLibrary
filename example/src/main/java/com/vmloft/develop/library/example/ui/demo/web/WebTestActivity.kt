package com.vmloft.develop.library.example.ui.demo.web

import android.view.View
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout.LayoutParams
import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.router.AppRouter

import kotlinx.android.synthetic.main.activity_web_page.webContainerLL

/**
 * Created by lzan13 on 2018/4/28.
 * WebView 测试界面
 */
@Route(path = AppRouter.appWebTest)
class WebTestActivity : BaseActivity() {
    private var webView: WebView? = null
    override fun layoutId(): Int {
        return layout.activity_web_page
    }

    override fun initUI() {
        super.initUI()
        setTopTitle("加载 Web 页面")

        webView = WebView(mActivity)
        webView?.settings?.javaScriptEnabled = true
        webView?.isVerticalScrollBarEnabled = false
        webView?.isHorizontalScrollBarEnabled = false
        webView?.webViewClient = viewClient
        webView?.webChromeClient = chromeClient
        webContainerLL.addView(webView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        webView?.loadUrl("file:///android_asset/index.html")
    }

    override fun initData() {}

    /**
     * java 调用 js 无参方法
     *
     * @param jsMethod js 方法名
     */
    fun callJS(view: View) {
        var method = "javaCallJs"
        val callJavascript = "javascript:$method()"
        webView?.loadUrl(callJavascript)
    }

    /**
     * java 调用 js 带有参数的方法
     */
    fun callJSArgs(view: View) {
        var method = "javaCallJsArgs"
        var params = "Hi Title 2\n======\n\n 你好啊2"
        val callJavascript = "javascript:$method('$params')"
        webView?.loadUrl(callJavascript)
    }

    private val viewClient: WebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {}
        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            super.onReceivedError(view, request, error)
        }
    }
    private val chromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
            return super.onJsAlert(view, url, message, result)
        }
    }
}
package com.vmloft.develop.library.example.demo.scheme

import com.alibaba.android.arouter.facade.annotation.Route
import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.tools.utils.logger.VMLog

/**
 * Create by lzan13 on 2020-01-02 20:46
 * 测试 Scheme 方式打开 app
 */
@Route(path = "/VMLoft/Scheme")
class SchemeActivity : BaseActivity() {

    override fun layoutId(): Int = layout.activity_scheme
    override fun initUI() {
        super.initUI()
        setTopTitle("Url 跳转")
    }

    override fun initData() {
        val uri = intent.data
        if (uri != null) {
            // 完整的url信息
            val url = uri.toString()
            VMLog.d("url: $uri")
            // scheme部分
            val scheme = uri.scheme
            VMLog.d("scheme: $scheme")
            // host部分
            val host = uri.host
            VMLog.d("host: $host")
            //port部分
            val port = uri.port
            VMLog.d("port: $port")
            // 访问路劲
            val path = uri.path
            VMLog.d("path: $path")
            // 参数
            val pathSegments = uri.pathSegments
            VMLog.d("pathSegments: $pathSegments")
            // Query部分
            val query = uri.query
            VMLog.d("query: $query")
            //获取指定参数值
            val goodsId = uri.getQueryParameter("id")
            VMLog.d("id: $goodsId")
        }
    }
}
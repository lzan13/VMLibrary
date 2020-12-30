package com.vmloft.develop.library.example.ui.demo.thread

import android.os.Handler
import android.os.Message
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.example.R.layout
import com.vmloft.develop.library.example.base.BaseActivity
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.tools.utils.logger.VMLog

/**
 * Create by lzan13 on 2019-11-15 13:15
 */
@Route(path = AppRouter.appThread)
class ThreadActivity : BaseActivity() {
    override fun layoutId(): Int = layout.activity_thread

    override fun initUI() {
        super.initUI()
        setTopTitle("验证线程操作")
    }

    override fun initData() {}

    /**
     * 模拟启动异步任务
     */
    fun startTask(view: View) {
        mHandler.removeCallbacks(runnable)
        startRunnable()
    }

    private fun startRunnable() {
        mHandler.postDelayed(runnable, 2000)
    }

    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
        }
    }
    var runnable = Runnable {
        VMLog.d("验证线程停止执行情况 - 0 - start -")
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        // 中间标识执行过程
        VMLog.d("验证线程停止执行情况 - 1 - run -")
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        VMLog.d("验证线程停止执行情况 - 2 - end -")

        //要做的事情
        startRunnable()
    }
}
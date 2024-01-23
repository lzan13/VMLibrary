package com.vmloft.develop.library.example.ui.demo.custom

import android.view.MotionEvent
import android.view.View

import com.alibaba.android.arouter.facade.annotation.Route

import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.library.base.widget.CommonDialog
import com.vmloft.develop.library.example.R
import com.vmloft.develop.library.example.databinding.ActivityDemoViewCustomBinding
import com.vmloft.develop.library.example.router.AppRouter
import com.vmloft.develop.library.example.utils.darkBar
import com.vmloft.develop.library.base.utils.showBar
import com.vmloft.develop.library.example.utils.errorBar
import com.vmloft.develop.library.base.utils.show
import com.vmloft.develop.library.tools.utils.VMUtils
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.recorder.VMRecorderView.RecordActionListener
import com.vmloft.develop.library.tools.widget.tips.VMTips
import com.vmloft.develop.library.tools.recorder.VMRecorderView
import com.vmloft.develop.library.tools.recorder.VMWaveformView
import java.util.Timer
import java.util.TimerTask

/**
 * Created by lzan13 on 2017/4/1.
 * 描述：测试录音控件
 */
@Route(path = AppRouter.appCustomView)
class CustomViewActivity : BActivity<ActivityDemoViewCustomBinding>() {

    override fun initVB() = ActivityDemoViewCustomBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义控件演示")

        initVoiceView()
    }

    override fun initData() {}

    fun startTimer(view: View) {
        mBinding.customTimerBtn.startTimer()
    }

    fun tips1(view: View) {
        VMTips.showBar(mActivity, "测试自定义图标提示条", VMTips.durationLong, R.drawable.emoji_dog)
        startVoice()
    }

    fun tips2(view: View) {
        showBar("测试自定义默认提示条")
        mBinding.voiceWaveformViewLeft.updateProgress(VMUtils.random(100).toFloat())
    }

    fun tips3(view: View) {
        darkBar("测试自定义暗色提示条")
        stopVoice()
    }

    fun tips4(view: View) {
        errorBar("测试自定义错误提示条，这是错误提醒默认样式！红色的")
    }

    fun tips5(view: View) {
        this.show("测试系统提示")
    }

    fun showDialog(view: View) {
        mDialog = CommonDialog(this)
        (mDialog as CommonDialog).let { dialog ->
            dialog.setTitle("测试对话框标题")
            dialog.setContent("测试对话框内容测试对话框内容测试对话框内容测试对话框内容")
            dialog.setNegative("消极按钮") {
                this.darkBar("点击了 消极按钮")
            }
            dialog.setPositive("积极按钮") {
                this.showBar("点击了 积极按钮")
            }
            dialog.show()
        }
    }

    var timer: Timer? = null
    var mCurrentProgress = 0f

    private fun startVoice() {
        mBinding.voiceWaveformViewLeft.start()

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if (mCurrentProgress >= 100) {
                    stopVoice()
                }
                mCurrentProgress += 5
                mBinding.voiceWaveformViewLeft.updateProgress(mCurrentProgress)
            }
        }, 100, 1000)
    }

    private fun stopVoice() {
        mBinding.voiceWaveformViewLeft.stop()

        timer?.cancel()
        timer = null
    }

    private fun initVoiceView() {
        // 测试波形控件
        val voiceBean = VMRecorderView.VoiceBean()
        voiceBean.duration = 25402
        voiceBean.decibelList.addAll(
            arrayListOf(
                1,
                55,
                55,
                52,
                53,
                55,
                58,
                58,
                61,
                57,
                61,
                54,
                70,
                66,
                71,
                71,
                66,
                66,
                65,
                52,
                54,
                59,
                59,
                54,
                57,
                56,
                58,
                72,
                71,
                54,
                67,
                70,
                74,
                56,
                70,
                70,
                52,
                69,
                71,
                75,
                71,
                72,
                61,
                57,
                56,
                54,
                54,
                68,
                61,
                62,
                54,
                66,
                67,
                72,
                72,
                53,
                58,
                57,
                52,
                72,
                69,
                66,
                54,
                68,
                51,
                74,
                52,
                72,
                73,
                50,
                62,
                67,
                72,
                64,
                66,
                73,
                74,
                75,
                63,
                72,
                56,
                54,
                70,
                61,
                59,
                54,
                54,
                57,
                52,
                51,
                63,
                69,
                69,
                64,
                69,
                56,
                68,
                63,
                55,
                72,
                50,
                61,
                71,
                55,
                56,
                70,
                55,
                53,
                64,
                68,
                59,
                65,
                75,
                74,
                57,
                62,
                56,
                59,
                61,
                58,
                61,
                54,
                53,
                52,
                57,
                54
            )
        )
        voiceBean.path = "/storage/emulated/0/Android/data/com.vmloft.develop.library.example/files/voice/VMVoice_20240120_141655694.amr"
        mBinding.voiceWaveformViewLeft.setVoiceBean(voiceBean)
        mBinding.voiceWaveformViewLeft.setOnClickListener {
            VMLog.i("voiceWaveformViewLeft.setOnClickListener")
        }
        mBinding.voiceWaveformViewLeft.setWaveformActionListener(object : VMWaveformView.WaveformActionListener {
            override fun onProgressChange(progress: Float) {
                VMLog.i("voiceWaveformViewLeft.onProgressChange $progress")
                mCurrentProgress = progress
            }

            override fun onLongPress(event: MotionEvent) {
                VMLog.i("voiceWaveformViewLeft.onLongPress $event")
            }
        })
        // 测试录音控件
        mBinding.voiceRecordView.setRecordActionListener(object : RecordActionListener() {
            override fun onStart() {
                showBar("录音开始")
            }

            override fun onCancel() {
                errorBar("录音取消")
            }

            override fun onComplete(bean: VMRecorderView.VoiceBean) {
                showBar("录音完成 ${bean.duration}-${bean.path}")
                // 测试控件波形及播放进度效果
                mBinding.voiceWaveformViewLeft.updateVoiceBean(bean)
            }

            override fun onDecibel(decibel: Int) {
                // TODO 录音声音分贝
            }

            override fun onError(code: Int, desc: String) {
                errorBar("录音失败 $code $desc")
            }

        })
        // 设置录音联动动画控件
        mBinding.voiceRecordView.setRecordAnimView(mBinding.voiceRecordAnimView)
    }

}
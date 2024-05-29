package com.vmloft.develop.app.example.ui.demo.custom

import android.view.MotionEvent
import android.view.View

import com.didi.drouter.annotation.Router

import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.app.example.router.AppRouter
import com.vmloft.develop.library.base.utils.showBar
import com.vmloft.develop.app.example.utils.errorBar
import com.vmloft.develop.app.example.databinding.ActivityDemoViewRecorderBinding
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderView.RecordActionListener
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderView
import com.vmloft.develop.library.tools.voice.player.VMVoicePlayer
import com.vmloft.develop.library.tools.voice.player.VMWaveformView

/**
 * Created by lzan13 on 2017/4/1.
 * 描述：测试录音控件
 */
@Router(path = AppRouter.appCustomRecorderView)
class RecorderViewActivity : BActivity<ActivityDemoViewRecorderBinding>() {

    lateinit var voiceBean: VMRecorderView.VoiceBean

    override fun initVB() = ActivityDemoViewRecorderBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义录音及播放")

        initVoiceView()
    }

    override fun initData() {}


    /**
     * 语音播放
     */
    fun start(view: View) {
        // 开始播放
        VMVoicePlayer.start(voiceBean.path)
    }

    fun pause(view: View) {
        VMVoicePlayer.pause()
    }

    fun resume(view: View) {
        VMVoicePlayer.resume()
    }

    fun stop(view: View) {
        VMVoicePlayer.stop()
        mBinding.voiceWaveformView.stop()
    }

    private fun initVoiceView() {
        mBinding.voiceLL.setOnClickListener { VMVoicePlayer.start(voiceBean.path) }
        VMVoicePlayer.setOnPlayActionListener(object : VMVoicePlayer.IOnPlayActionListener {
            override fun onStart() {
                mBinding.voiceWaveformView.start()
            }

            override fun onPause() {
                mBinding.voiceWaveformView.pause()
            }

            override fun onComplete() {
                mBinding.voiceWaveformView.stop()
            }

            override fun onProgressChange(progress: Float) {
                mBinding.voiceWaveformView.updateProgress(progress)
            }
        })

        // 测试波形控件
        voiceBean = VMRecorderView.VoiceBean()
        voiceBean.duration = 27401
        voiceBean.decibelList.addAll(
            arrayListOf(
                50,
                58,
                55,
                59,
                57,
                54,
                56,
                54,
                54,
                59,
                57,
                57,
                55,
                60,
                59,
                62,
                66,
                63,
                59,
                66,
                63,
                63,
                59,
                56,
                63,
                62,
                62,
                58,
                60,
                58,
                60,
                60,
                59,
                57,
                55,
                51,
                56,
                58,
                55,
                58,
                60,
                59,
                57,
                52,
                55,
                55,
                59,
                59,
                59,
                56,
                54,
                59,
                54,
                54,
                53,
                52,
                53,
                61,
                58,
                60,
                56,
                56,
                56,
                59,
                58,
                56,
                54,
                57,
                52,
                54,
                57,
                60,
                58,
                55,
                54,
                53,
                58,
                61,
                62,
                54,
                57,
                61,
                58,
                58,
                57,
                74,
                83,
                82,
                76,
                70,
                80,
                69,
                80,
                73,
                79,
                73,
                74,
                75,
                58,
                73,
                70,
                72,
                72,
                73,
                73,
                61,
                72,
                56,
                73,
                73,
                70,
                74,
                65,
                56,
                55,
                54,
                61,
                62,
                60,
                56,
                52,
                74,
                59,
                72,
                57,
                62,
                69,
                79,
                75,
                74,
                64,
                60,
                78,
                71,
                55,
                52
            )
        )
        voiceBean.path = "/storage/emulated/0/Android/data/com.vmloft.develop.library.example/files/voice/VMVoice_20240125_110054910.mp3"

        mBinding.voiceWaveformView.setVoiceBean(voiceBean)
        mBinding.voiceWaveformView.setOnClickListener {
            VMLog.i("voiceWaveformView.setOnClickListener")
        }
        mBinding.voiceWaveformView.setWaveformProgressListener(object : VMWaveformView.WaveformProgressListener {
            override fun onProgressChange(progress: Float) {
                VMLog.i("voiceWaveformView.onProgressChange $progress")
                VMVoicePlayer.updateProgress(progress)
            }
        })
        mBinding.voiceWaveformView.setWaveformClickListener(object:VMWaveformView.WaveformClickListener{
            override fun onLongClick(event: MotionEvent) {
                VMLog.i("voiceWaveformView.onLongClick $event")
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
                voiceBean = bean
                // 测试控件波形及播放进度效果
                mBinding.voiceWaveformView.updateVoiceBean(bean)
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
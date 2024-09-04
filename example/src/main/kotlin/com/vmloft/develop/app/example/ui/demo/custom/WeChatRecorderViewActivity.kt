package com.vmloft.develop.app.example.ui.demo.custom

import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.didi.drouter.annotation.Router
import com.effective.android.panel.PanelSwitchHelper
import com.effective.android.panel.interfaces.ContentScrollMeasurer
import com.effective.android.panel.interfaces.listener.OnEditFocusChangeListener
import com.effective.android.panel.interfaces.listener.OnKeyboardStateListener
import com.effective.android.panel.interfaces.listener.OnPanelChangeListener
import com.effective.android.panel.view.panel.IPanelView
import com.effective.android.panel.view.panel.PanelView

import com.vmloft.develop.app.example.R
import com.vmloft.develop.library.base.BActivity
import com.vmloft.develop.app.example.router.AppRouter
import com.vmloft.develop.app.example.databinding.ActivityDemoViewRecorderWechatBinding
import com.vmloft.develop.library.tools.utils.VMView
import com.vmloft.develop.library.tools.utils.logger.VMLog
import com.vmloft.develop.library.tools.voice.bean.VMVoiceBean
import com.vmloft.develop.library.tools.voice.recorder.VMRecorderActionListener
import com.vmloft.develop.library.tools.widget.tips.VMTips

/**
 * Created by lzan13 on 2017/4/1.
 * 描述：测试仿微信录音控件
 */
@Router(path = AppRouter.appCustomRecorderViewWeChat)
class WeChatRecorderViewActivity : BActivity<ActivityDemoViewRecorderWechatBinding>() {

    private var isVoiceRecord = false
    private var cotnent: String = ""

    // 面板切换帮助工具
    private var panelSwitchHelper: PanelSwitchHelper? = null

    override fun initVB() = ActivityDemoViewRecorderWechatBinding.inflate(layoutInflater)

    override fun initUI() {
        super.initUI()
        setTopTitle("自定义仿微信录音")

        binding.voiceIV.setOnClickListener {
            changeVoiceRecordViewState()
        }

        initVoiceView()
        initInputET()
    }

    override fun initData() {}

    override fun onResume() {
        super.onResume()
        initPanelSwitchHelper()
    }

    private fun initVoiceView() {
        binding.recorderLayout.setRecorderWaveformLayout(binding.recorderWaveformLayout)
        binding.recorderLayout.setRecorderActionListener(object : VMRecorderActionListener() {
            override fun onStart() {
                binding.recorderMaskView.visibility = View.VISIBLE
                binding.inputCL.visibility = View.GONE
                VMTips.showBar(this@WeChatRecorderViewActivity, "录音开始")
            }

            override fun onCancel() {
                binding.recorderMaskView.visibility = View.GONE
                binding.inputCL.visibility = View.VISIBLE
                VMTips.showBar(this@WeChatRecorderViewActivity, "录音取消")
            }

            override fun onComplete(bean: VMVoiceBean) {
                binding.recorderMaskView.visibility = View.GONE
                binding.inputCL.visibility = View.VISIBLE
                VMTips.showBar(this@WeChatRecorderViewActivity, "录音完成")
            }

            override fun onDecibel(decibel: Int) {
            }

            override fun onRecordFFTData(data: DoubleArray) {
            }


            override fun onError(code: Int, desc: String) {
                binding.recorderMaskView.visibility = View.GONE
                binding.inputCL.visibility = View.VISIBLE
                VMTips.showBar(this@WeChatRecorderViewActivity, "录音出错 $code, $desc")
            }
        })
    }

    private fun initInputET() {
        // 输入框内容改变
        binding.messageET.doAfterTextChanged { editable ->
            cotnent = editable.toString()
            checkMessageET()
        }
    }

    private fun initPanelSwitchHelper() {
        if (panelSwitchHelper != null) return
        panelSwitchHelper = PanelSwitchHelper.Builder(this).setWindowInsetsRootView(binding.root)
            .addKeyboardStateListener(object : OnKeyboardStateListener {
                override fun onKeyboardChange(visible: Boolean, height: Int) {
                    VMLog.i("WeChatRecorderViewActivity.Keyboard state $visible, $height")
                }
            }).addEditTextFocusChangeListener(object : OnEditFocusChangeListener {
                override fun onFocusChange(view: View?, hasFocus: Boolean) {
                    VMLog.i("WeChatRecorderViewActivity.EditText focus:$hasFocus")
                }
            }).addPanelChangeListener(object : OnPanelChangeListener {
                override fun onKeyboard() {
                    VMLog.i("WeChatRecorderViewActivity.onKeyboard")
                    onHidePanel()
                }

                override fun onNone() {
                    VMLog.i("WeChatRecorderViewActivity.onNone")
                    onHidePanel()
                }

                override fun onPanel(panel: IPanelView?) {
                    VMLog.i("WeChatRecorderViewActivity.onPanel")
                    onShowPanel(panel!!)
                }

                override fun onPanelSizeChange(
                    panel: IPanelView?,
                    portrait: Boolean,
                    oldWidth: Int,
                    oldHeight: Int,
                    width: Int,
                    height: Int,
                ) {
                    VMLog.i("WeChatRecorderViewActivity.onPanelSizeChange $portrait oldW:$oldWidth-newW:$width oldH:$oldHeight-newH:$height")
                }
            })
//            .addPanelHeightMeasurer(object : PanelHeightMeasurer {
//                override fun getPanelTriggerId() = R.id.emotionIV
//                override fun getTargetPanelDefaultHeight() = VMDimen.dp2px(288)
//                override fun synchronizeKeyboardHeight() = false
//            }).addPanelHeightMeasurer(object : PanelHeightMeasurer {
//                override fun getPanelTriggerId() = R.id.moreIV
//                override fun getTargetPanelDefaultHeight() = VMDimen.dp2px(256)
//                override fun synchronizeKeyboardHeight() = false
//            })
            // 测量应该滚动部分高度
            .addContentScrollMeasurer(object : ContentScrollMeasurer {
                override fun getScrollDistance(defaultDistance: Int) = defaultDistance

                override fun getScrollViewId() = R.id.recyclerView
            })
            // 日志开关
            .logTrack(false)
            .build()
    }

    /**
     * 改变语音录制布局状态
     */
    private fun changeVoiceRecordViewState() {
        // 语音面板变化要把其他面板都隐藏
        panelSwitchHelper?.resetState()

        if (isVoiceRecord) {
            VMView.showKeyboard(this, binding.messageET)
            hideVoiceRecordPanel()
        } else {
            VMView.hideKeyboard(this, binding.messageET)
            showVoiceRecordPanel()
        }
    }

    private fun checkMessageET() {
        binding.sendTV.visibility = if (cotnent.isNullOrEmpty() || isVoiceRecord) View.GONE else View.VISIBLE
        binding.moreIV.visibility = if (cotnent.isNullOrEmpty() || isVoiceRecord) View.VISIBLE else View.GONE
    }

    /**
     * 面板隐藏回调
     */
    private fun onHidePanel() {
//        hideVoiceRecordPanel()
        hideEmotionPanel()
        hideMorePanel()
    }

    /**
     * 面板显示回调
     */
    private fun onShowPanel(view: IPanelView) {
        if (view !is PanelView) return
        hideVoiceRecordPanel()
        when (view.id) {
            R.id.emotionPanelView -> showEmotionPanel()
            R.id.morePanelView -> showMorePanel()
        }
    }

    /**
     * 显示语音录制面板
     */
    private fun showVoiceRecordPanel() {
        isVoiceRecord = true
        // 录音状态
        binding.voiceIV.setImageResource(R.drawable.ic_im_chat_input_keyboard)
        binding.messageET.visibility = View.GONE
        binding.touchTV.visibility = View.VISIBLE
        binding.recorderLayout.visibility = View.VISIBLE

        checkMessageET()
    }

    /**
     * 隐藏语音录制面板
     */
    private fun hideVoiceRecordPanel() {
        isVoiceRecord = false
        // 输入框状态
        binding.voiceIV.setImageResource(R.drawable.ic_im_chat_input_voice)
        binding.messageET.visibility = View.VISIBLE
        binding.touchTV.visibility = View.GONE
        binding.recorderLayout.visibility = View.GONE

        checkMessageET()
    }

    private fun showEmotionPanel() {
        binding.voiceIV.setImageResource(R.drawable.ic_im_chat_input_voice)
        binding.emotionIV.setImageResource(R.drawable.ic_im_chat_input_keyboard)
        binding.moreIV.setImageResource(R.drawable.ic_im_chat_input_more)
    }

    private fun hideEmotionPanel() {
        binding.emotionIV.setImageResource(R.drawable.ic_im_chat_input_emotion)

    }

    private fun showMorePanel() {
        binding.voiceIV.setImageResource(R.drawable.ic_im_chat_input_voice)
        binding.emotionIV.setImageResource(R.drawable.ic_im_chat_input_emotion)
        binding.moreIV.setImageResource(R.drawable.ic_im_chat_input_keyboard)
    }

    private fun hideMorePanel() {
        binding.moreIV.setImageResource(R.drawable.ic_im_chat_input_more)
    }

    /**
     * 拦截返回按键
     */
    fun hookOnBackPressed(): Boolean {
        return panelSwitchHelper != null && panelSwitchHelper?.hookSystemBackByPanelSwitcher() ?: false
    }


    override fun onBackPressed() {
        if (hookOnBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()

        releasePanelHelper()
    }

    /**
     * 释放面板帮助类
     */
    private fun releasePanelHelper() {
        if (panelSwitchHelper != null) {
            binding.panelRootContainer.recycle()
            panelSwitchHelper = null
        }
    }
}
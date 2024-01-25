VMTools
=======
[![](https://jitpack.io/v/com.github.lzan13/VMLibrary.svg)](https://jitpack.io/#com.github.lzan13/VMLibrary)
[![](https://img.shields.io/badge/blog-%E7%A9%BF%E8%A3%A4%E8%A1%A9%E9%97%AF%E5%A4%A9%E4%B8%8B-blue.svg)](https://blog.melove.net)
[![](https://img.shields.io/badge/github-lzan13-blue.svg)](https://github.com/lzan13)
[![](https://img.shields.io/badge/gitee-lzan13-red.svg)](https://gitee.com/lzan13)

封装自己开发中常用的一些工具类，以及自定义控件等，方便以后开发项目的使用，以及分享给其他开发者直接使用


### #使用
**引用库**

现已将库发布到 JitPack.io，需要使用的同学可以直接通过远程方式引用
```
// 项目根目录 build.gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

...

// 项目 module build.gradle
dependencies {
    // 依赖最新版本 lastView 看最上边标签显示版本
    implementation 'com.github.lzan13:VMLibrary:lastVersion'
}
```

**初始化**

在 Application 进行初始化
```kotlin
fun onCreate(){
    VMTools.init(context)
}
```

**愉快的调用**

库中的工具类都是静态方法，可以直接调用:
```kotlin
// dp 尺寸转 px
val width = VMDimen.dp2px(4)
// 判断一个字符串是否是邮箱格式
val isEmail = VMReg.isEmail(email)
// ...
// 等等，具体可以看下边的说明，自己去发掘吧
```

### 二次开发
- 二次开发直接 clone 源码修改即可
- lame 库问题，
  - 当前 lame 版本 3.100 https://lame.sourceforge.io/
  - lame 源码，为了减小包体积，这里未提交 lame 相关源码，如需自己编译，下载 lame 源码，复制 libmp3lame 文件夹至 jni 下
  - 本库只编译了 arm64-v8a 架构 so，如需其他架构，需要自己下载源码编译，修改 jni/Application.mk APP_ABI值对应架构就行

### 更新日志
[Update](./UPDATE.md)

### #具体接口
主要实现了工具类以及自定义控件的封装，一起一些其他平常使用的代码封装

- [adapter](src/main/java/com/vmloft/develop/library/tools/adapter) 适配器简单封装
  - [VMViewPager2Adapter](src/main/java/com/vmloft/develop/library/tools/adapter/VMViewPager2Adapter) ViewPager2 适配器
  - [VMViewPagerAdapter](src/main/java/com/vmloft/develop/library/tools/adapter/VMViewPagerAdapter) ViewPager 适配器
- [animator](src/main/java/com/vmloft/develop/library/tools/animator) 对属性动画进行简单封装

- [base](src/main/java/com/vmloft/develop/library/tools/base) 当前库所定义的一些基类
  - [VMBDialog](src/main/java/com/vmloft/develop/library/tools/base/VMBDialog.java) 对话框基类，封装简单对话框方法，UI 自定义

- [permission](src/main/java/com/vmloft/develop/library/tools/permission) 6.X以上权限申请工具

- [recorder](src/main/java/com/vmloft/develop/library/tools/voice) 录音机控件
  - [VMAudioRecorder](src/main/java/com/vmloft/develop/library/tools/voice/VMAudioRecorder) AudioRecord 实现 借助 lame 实现录制 mp3 文件
  - [VMEncoder](src/main/java/com/vmloft/develop/library/tools/voice/VMEncoder)  借助 lame 进行 pcm 到 mp3 转换
  - [VMLame](src/main/java/com/vmloft/develop/library/tools/voice/VMLame) VMLame 调用 lame 接口的本地方法类
  - [VMMediaRecorder](src/main/java/com/vmloft/develop/library/tools/voice/VMMediaRecorder) MediaRecorder 实现 录制 amr 文件
  - [VMRecorderAnimView](src/main/java/com/vmloft/develop/library/tools/voice/VMRecorderAnimView) 录音控件动画类
  - [VMRecorderEngine](src/main/java/com/vmloft/develop/library/tools/voice/VMRecorderEngine) 录音引擎接口 VMAudioRecorder 和 VMMediaRecorder 都实现此接口
  - [VMRecorderManager](src/main/java/com/vmloft/develop/library/tools/voice/VMRecorderManager) 录音管理类
  - [VMRecorderView](src/main/java/com/vmloft/develop/library/tools/voice/VMRecorderView) 录音触摸类
  - [VMWaveformView](src/main/java/com/vmloft/develop/library/tools/voice/VMWaveformView) 录音播放波形，一般用来im消息展示

- [service](src/main/java/com/vmloft/develop/library/tools/service) 服务封装
  - [VMATypeFind](src/main/java/com/vmloft/develop/library/tools/service/VMATypeFind) 节点查找抽象类
  - [VMAutoService](src/main/java/com/vmloft/develop/library/tools/service/VMAutoService) 自动化操作辅助服务


- [utils](src/main/java/com/vmloft/develop/library/tools/utils) 自定义工具类部分
  - [bitmap](src/main/java/com/vmloft/develop/library/tools/utils/bitmap) 图片处理
  - [logger](src/main/java/com/vmloft/develop/library/tools/utils/logger) 日志
  - [VMColor](src/main/java/com/vmloft/develop/library/tools/utils/VMColor.java) 颜色
  - [VMDate](src/main/java/com/vmloft/develop/library/tools/utils/VMDate.java) 时间
  - [VMDimen](src/main/java/com/vmloft/develop/library/tools/utils/VMDimen.java) 尺寸
  - [VMFile](src/main/java/com/vmloft/develop/library/tools/utils/VMFile.java) 文件
  - [VMNetwork](src/main/java/com/vmloft/develop/library/tools/utils/VMNetwork.java) 网络信息获取
  - [VMReg](src/main/java/com/vmloft/develop/library/tools/utils/VMReg.java) 正则
  - [VMSP](src/main/java/com/vmloft/develop/library/tools/utils/VMSP.java) SharedPreference 工具
  - [VMStr](src/main/java/com/vmloft/develop/library/tools/utils/VMStr.java) 字符串操作
  - [VMSystem](src/main/java/com/vmloft/develop/library/tools/utils/VMSystem.java) 系统相关
  - [VMTheme](src/main/java/com/vmloft/develop/library/tools/utils/VMTheme.java) 主题
  - [VMUtils](src/main/java/com/vmloft/develop/library/tools/utils/VMUtils.java) 工具
  - [VMView](src/main/java/com/vmloft/develop/library/tools/utils/VMView.java) 视图相关

- [widget](src/main/java/com/vmloft/develop/library/tools/widget) 自定义控件部分
  - [barrage](src/main/java/com/vmloft/develop/library/tools/widget/barrage) 弹幕控件
  - [behavior](src/main/java/com/vmloft/develop/library/tools/widget/behavior) 自定义伸缩头部
  - [dialog](src/main/java/com/vmloft/develop/library/tools/widget/dialog) 自定义对话框
    - [VMBDialog](src/main/java/com/vmloft/develop/library/tools/widget/dialog/VMBDialog) 自定义对话框基类
    - [VMDefaultDialog](src/main/java/com/vmloft/develop/library/tools/widget/dialog/VMDefaultDialog) 自定义对话框默认实现
  - [draw](src/main/java/com/vmloft/develop/library/tools/widget/draw) 自定义绘制View
  - [guide](src/main/java/com/vmloft/develop/library/tools/widget/guide) 自定义遮罩引导
  - [indicator](src/main/java/com/vmloft/develop/library/tools/widget/indicator) 指示器控件
  - [layout](src/main/java/com/vmloft/develop/library/tools/widget/layout) 布局控件
    - [VMExpandableLayout](src/main/java/com/vmloft/develop/library/tools/widget/layout/VMExpandableLayout) 伸缩布局
    - [VMRatioLayout](src/main/java/com/vmloft/develop/library/tools/widget/layout/VMRatioLayout) 方形布局
    - [VMTableLayoutManager](src/main/java/com/vmloft/develop/library/tools/widget/layout/VMTableLayoutManager) 表格布局
  - [loading](src/main/java/com/vmloft/develop/library/tools/widget/loading) 加载控件
  - [tips](src/main/java/com/vmloft/develop/library/tools/widget/tips) Tips 提醒
  - [VMExpandableLayout](src/main/java/com/vmloft/develop/library/tools/widget/VMExpandableLayout.java) 可伸缩布局
  - [VMFloatMenu](src/main/java/com/vmloft/develop/library/tools/widget/VMFloatMenu.java) 悬浮菜单
  - [VMKeyboardController](src/main/java/com/vmloft/develop/library/tools/widget/VMKeyboardController.java) 输入面板控制器
  - [VMLineView](src/main/java/com/vmloft/develop/library/tools/widget/VMLineView.java) 单行选项控件
  - [VMRatioLayout](src/main/java/com/vmloft/develop/library/tools/widget/VMRatioLayout.java) 固定宽高比 Layout
  - [VMTimerBtn](src/main/java/com/vmloft/develop/library/tools/widget/VMTimerBtn.java) 定时按钮
  - [VMTopBar](src/main/java/com/vmloft/develop/library/tools/widget/VMTopBar.java) 顶部 TopBar
  - [VMViewGroup](src/main/java/com/vmloft/develop/library/tools/widget/VMViewGroup.java) 自动换行 ViewGroup
  - [VMWaveView](src/main/java/com/vmloft/develop/library/tools/widget/VMWaveView.java) 波形图控件

- [VMTools](src/main/java/com/vmloft/develop/library/tools/VMTools.java) 工具初始化类

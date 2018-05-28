VMTools
=======

[![Download](https://api.bintray.com/packages/lzan13/VMLibrary/vmtools/images/download.svg)](https://bintray.com/lzan13/VMLibrary/vmtools/_latestVersion)
[![](https://img.shields.io/badge/author-lzan13-green.svg)](https://github.com/lzan13)
[![](https://img.shields.io/badge/weibo-@lzan13-red.svg)](http://weibo.com/lzan13)


封装自己开发中常用的一些工具类，以及自定义控件等，方便以后开发项目的使用，以及分享给其他开发者直接使用


### #使用方法
现已将库发布到 JCenter 仓库，需要使用的同学可以直接通过远程方式引用
```
dependencies {
    ...
    implementation 'com.vmloft.library:vmtools:0.0.1'
}
```

### #实现功能
主要实现了工具类以及自定义控件的封装，一起一些其他平常使用的代码封装

- [adapter](src/main/java/com/vmloft/develop/library/tools/adapter) RecyclerView.Adapter 简单封装

- [router](src/main/java/com/vmloft/develop/library/tools/router) 项目跳转路由
    - [VMParams](src/main/java/com/vmloft/develop/library/tools/utils/VMParams.java) 路由跳转传参
    - [VMRouter](src/main/java/com/vmloft/develop/library/tools/utils/VMRouter.java) 路由基类，业务层继承实现自己业务跳转

- [utils](src/main/java/com/vmloft/develop/library/tools/utils) 自定义工具类部分
    - [bitmap](src/main/java/com/vmloft/develop/library/tools/utils/bitmap) 图片处理
    - [VMCrypto](src/main/java/com/vmloft/develop/library/tools/utils/VMCrypto.java) 加密解密
    - [VMDate](src/main/java/com/vmloft/develop/library/tools/utils/VMDate.java) 时间
    - [VMDimen](src/main/java/com/vmloft/develop/library/tools/utils/VMDimen.java) 尺寸
    - [VMFile](src/main/java/com/vmloft/develop/library/tools/utils/VMFile.java) 文件
    - [VMLanguage](src/main/java/com/vmloft/develop/library/tools/utils/VMLanguage.java) 语言切换
    - [VMLog](src/main/java/com/vmloft/develop/library/tools/utils/VMLog.java) 日志
    - [VMNetwork](src/main/java/com/vmloft/develop/library/tools/utils/VMNetwork.java) 网络
    - [VMReg](src/main/java/com/vmloft/develop/library/tools/utils/VMReg.java) 正则
    - [VMSP](src/main/java/com/vmloft/develop/library/tools/utils/VMSP.java) SharedPreference
    - [VMStr](src/main/java/com/vmloft/develop/library/tools/utils/VMStr.java) 字符串操作
    - [VMTheme](src/main/java/com/vmloft/develop/library/tools/utils/VMTheme.java) 主题切换

- [widget](src/main/java/com/vmloft/develop/library/tools/widget) 自定义控件部分
    - [VMAuthCodeBtn](src/main/java/com/vmloft/develop/library/tools/widget/VMAuthCodeBtn.java) 验证码按钮
    - [VMCamera2Preview](src/main/java/com/vmloft/develop/library/tools/widget/VMCamera2Preview.java) Camera2 预览控件
    - [VMCameraPreview](src/main/java/com/vmloft/develop/library/tools/widget/VMCamera2Preview.java) Camera 预览控件
    - [VMDetailsView](src/main/java/com/vmloft/develop/library/tools/widget/VMDetailsView.java) 显示更多
    - [VMDotLineView](src/main/java/com/vmloft/develop/library/tools/widget/VMDotLineView.java) 描点连线
    - [VMDrawView](src/main/java/com/vmloft/develop/library/tools/widget/VMDrawView.java) 可绘制 View
    - [VMExpandableLayout](src/main/java/com/vmloft/develop/library/tools/widget/VMExpandableLayout.java) 可伸缩布局
    - [VMImageView](src/main/java/com/vmloft/develop/library/tools/widget/VMImageView.java) 圆角图片
    - [VMRecordView](src/main/java/com/vmloft/develop/library/tools/widget/VMRecordView.java) 录音控件
    - [VMTimerBtn](src/main/java/com/vmloft/develop/library/tools/widget/VMTimerBtn.java) 定时按钮
    - [VMToast](src/main/java/com/vmloft/develop/library/tools/widget/VMToast.java) 全局 Toast 提醒
    - [VMViewGroup](src/main/java/com/vmloft/develop/library/tools/widget/VMViewGroup.java) ViewGroup
    - [VMWaveformView](src/main/java/com/vmloft/develop/library/tools/widget/VMWaveformView.java) 波形图
    
- [VMActivity](src/main/java/com/vmloft/develop/library/tools/VMActivity.java) Activity 基类
- [VMApp](src/main/java/com/vmloft/develop/library/tools/VMApp.java) Application 基类
- [VMCallback](src/main/java/com/vmloft/develop/library/tools/VMCallback.java) 通用回调，可继承重载
- [VMFragment](src/main/java/com/vmloft/develop/library/tools/VMFragment.java) Fragment 基类 实现数据懒加载
- [VMTools](src/main/java/com/vmloft/develop/library/tools/VMTools.java) 入口类
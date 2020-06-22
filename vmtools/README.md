VMTools
=======

[![Download](https://api.bintray.com/packages/lzan13/VMLoft/vmtoolsx/images/download.svg)](https://bintray.com/lzan13/VMLoft/vmtoolsx/_latestVersion)
[![](https://img.shields.io/badge/author-lzan13-green.svg)](https://github.com/lzan13)
[![](https://img.shields.io/badge/weibo-@lzan13-red.svg)](http://weibo.com/lzan13)
[![](https://img.shields.io/badge/blog-%E7%A9%BF%E8%A3%A4%E8%A1%A9%E9%97%AF%E5%A4%A9%E4%B8%8B-blue.svg)](https://blog.melove.net)


封装自己开发中常用的一些工具类，以及自定义控件等，方便以后开发项目的使用，以及分享给其他开发者直接使用


### #使用
**引用库**

现已将库发布到 JCenter 仓库，需要使用的同学可以直接通过远程方式引用，最新版 1.0.0 为 androidx 版本，
如果项目还未迁移到 androidx 需使用 0.10.5 版本
```
dependencies {
    ...
    implementation 'com.vmloft.library:vmtools:1.1.3'
}
```

**初始化**

在 Application 进行初始化，如果直接继承自 VMApp，可以忽略这一步
```java
public void onCreate(){
    ...
    VMTools.init(context)
}
```

**愉快的调用**

库中的工具类都是静态方法，可以直接地哦啊用，Example:
```java
// dp 尺寸转 px
int width = VMDimen.dp2px(4);
// 判断一个字符串是否是邮箱格式
booelan isEmail = VMReg.isEmail(email);
// ...等等，具体可以看下边的说明，自己去发掘吧
```

### #具体接口
主要实现了工具类以及自定义控件的封装，一起一些其他平常使用的代码封装

- [adapter](src/main/java/com/vmloft/develop/library/tools/adapter) RecyclerView.Adapter 简单封装
- [animator](src/main/java/com/vmloft/develop/library/tools/animator) 对属性动画进行简单封装

- [base](src/main/java/com/vmloft/develop/library/tools/base) 当前库所定义的一些基类
    - [VMActivity](src/main/java/com/vmloft/develop/library/tools/base/VMBActivity.java) Activity 基类
    - [VMFragment](src/main/java/com/vmloft/develop/library/tools/base/VMBFragment.java) Fragment 基类

- [permission](src/main/java/com/vmloft/develop/library/tools/permission) 6.X以上权限申请工具

- [utils](src/main/java/com/vmloft/develop/library/tools/utils) 自定义工具类部分
    - [bitmap](src/main/java/com/vmloft/develop/library/tools/utils/bitmap) 图片处理
    - [VMLog](src/main/java/com/vmloft/develop/library/tools/utils/logger) 日志
    - [VMDate](src/main/java/com/vmloft/develop/library/tools/utils/VMColor.java) 颜色
    - [VMDate](src/main/java/com/vmloft/develop/library/tools/utils/VMDate.java) 时间
    - [VMDimen](src/main/java/com/vmloft/develop/library/tools/utils/VMDimen.java) 尺寸
    - [VMFile](src/main/java/com/vmloft/develop/library/tools/utils/VMFile.java) 文件
    - [VMNetwork](src/main/java/com/vmloft/develop/library/tools/utils/VMNetwork.java) 网络
    - [VMReg](src/main/java/com/vmloft/develop/library/tools/utils/VMReg.java) 正则
    - [VMSPUtil](src/main/java/com/vmloft/develop/library/tools/utils/VMSPUtil.java) SharedPreference 工具
    - [VMStr](src/main/java/com/vmloft/develop/library/tools/utils/VMStr.java) 字符串操作
    - [VMStr](src/main/java/com/vmloft/develop/library/tools/utils/VMSystem.java) 系统相关
    - [VMTheme](src/main/java/com/vmloft/develop/library/tools/utils/VMTheme.java) 主题
    - [VMTheme](src/main/java/com/vmloft/develop/library/tools/utils/VMUtils.java) 工具

- [widget](src/main/java/com/vmloft/develop/library/tools/widget) 自定义控件部分
    - [VMBarrageView](src/main/java/com/vmloft/develop/library/tools/widget/barrage/VMBarrageView.java) 弹幕控件
    - [VMIndicatorView](src/main/java/com/vmloft/develop/library/tools/widget/indicator/VMIndicatorView.java) 指示器控件
    - [VMRecordView](src/main/java/com/vmloft/develop/library/tools/widget/record/VMRecordView.java) 录音控件
    - [VMToast](src/main/java/com/vmloft/develop/library/tools/widget/toast) 全局 Toast 提醒
    - [VMExpandableLayout](src/main/java/com/vmloft/develop/library/tools/widget/VMExpandableLayout.java) 可伸缩布局
    - [VMFloatMenu](src/main/java/com/vmloft/develop/library/tools/widget/VMFloatMenu.java) 悬浮菜单
    - [VMImageView](src/main/java/com/vmloft/develop/library/tools/widget/VMLineView.java) 单行选项控件
    - [VMSquareLayout](src/main/java/com/vmloft/develop/library/tools/widget/VMRatioLayout.java) 正方形控件
    - [VMTimerBtn](src/main/java/com/vmloft/develop/library/tools/widget/VMTimerBtn.java) 定时按钮
    - [VMTimerBtn](src/main/java/com/vmloft/develop/library/tools/widget/VMTopBar.java) 顶部 TopBar
    - [VMViewGroup](src/main/java/com/vmloft/develop/library/tools/widget/VMViewGroup.java) 自动换行 ViewGroup

- [VMTools](src/main/java/com/vmloft/develop/library/tools/VMTools.java) 工具初始化类

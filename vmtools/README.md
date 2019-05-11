VMTools
=======

[![Download](https://api.bintray.com/packages/lzan13/VMLibrary/vmtools/images/download.svg)](https://bintray.com/lzan13/VMLibrary/vmtools/_latestVersion)
[![](https://img.shields.io/badge/author-lzan13-green.svg)](https://github.com/lzan13)
[![](https://img.shields.io/badge/weibo-@lzan13-red.svg)](http://weibo.com/lzan13)
[![](https://img.shields.io/badge/blog-@%E7%A9%BF%E8%A3%A4%E8%A1%A9%E9%97%AF%E5%A4%A9%E4%B8%8B-blue.svg)](http://blog.melove.net)


封装自己开发中常用的一些工具类，以及自定义控件等，方便以后开发项目的使用，以及分享给其他开发者直接使用


### #使用
**引用库**

现已将库发布到 JCenter 仓库，需要使用的同学可以直接通过远程方式引用
```
dependencies {
    ...
    implementation 'com.vmloft.library:vmtools:0.5.1'
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

- [base](src/main/java/com/vmloft/develop/library/tools/base) 当前库所定义的一些基类
    - [VMActivity](src/main/java/com/vmloft/develop/library/tools/base/VMActivity.java) Activity 基类
    - [VMApp](src/main/java/com/vmloft/develop/library/tools/base/VMApp.java) Application 基类
    - [VMCallback](src/main/java/com/vmloft/develop/library/tools/base/VMCallback.java) 通用回调，可继承重载
    - [VMFragment](src/main/java/com/vmloft/develop/library/tools/base/VMFragment.java) Fragment 基类 实现数据懒加载
    - [VMLazyFragment](src/main/java/com/vmloft/develop/library/tools/base/VMFragment.java) Fragment 基类 实现数据懒加载
    
- [permission](src/main/java/com/vmloft/develop/library/tools/permission) 6.X以上权限申请工具

- [router](src/main/java/com/vmloft/develop/library/tools/router) 项目跳转路由
    - [VMParams](src/main/java/com/vmloft/develop/library/tools/utils/VMParams.java) 路由跳转传参
    - [VMRouter](src/main/java/com/vmloft/develop/library/tools/utils/VMRouter.java) 路由基类，业务层继承实现自己业务跳转

- [utils](src/main/java/com/vmloft/develop/library/tools/utils) 自定义工具类部分
    - [bitmap](src/main/java/com/vmloft/develop/library/tools/utils/bitmap) 图片处理
    - [VMCrypto](src/main/java/com/vmloft/develop/library/tools/utils/VMCrypto.java) 加密解密
    - [VMDate](src/main/java/com/vmloft/develop/library/tools/utils/VMDate.java) 时间
    - [VMDimen](src/main/java/com/vmloft/develop/library/tools/utils/VMDimen.java) 尺寸
    - [VMEditor](src/main/java/com/vmloft/develop/library/tools/utils/VMEditor.java) 针对 EditText 封装撤销和回复功能
    - [VMFile](src/main/java/com/vmloft/develop/library/tools/utils/VMFile.java) 文件
    - [VMLanguage](src/main/java/com/vmloft/develop/library/tools/utils/VMLanguage.java) 语言切换
    - [VMLog](src/main/java/com/vmloft/develop/library/tools/utils/VMLog.java) 日志
    - [VMNetwork](src/main/java/com/vmloft/develop/library/tools/utils/VMNetwork.java) 网络
    - [VMReg](src/main/java/com/vmloft/develop/library/tools/utils/VMReg.java) 正则
    - [VMSPUtil](src/main/java/com/vmloft/develop/library/tools/utils/VMSPUtil.java) SharedPreference 工具
    - [VMStr](src/main/java/com/vmloft/develop/library/tools/utils/VMStr.java) 字符串操作
    - [VMStr](src/main/java/com/vmloft/develop/library/tools/utils/VMSystem.java) 系统相关
    - [VMTheme](src/main/java/com/vmloft/develop/library/tools/utils/VMTheme.java) 主题

- [widget](src/main/java/com/vmloft/develop/library/tools/widget) 自定义控件部分
    - [VMIndicatorView](src/main/java/com/vmloft/develop/library/tools/widget/indicator/VMIndicatorView.java) 指示器控件
    - [VMToast](src/main/java/com/vmloft/develop/library/tools/widget/toast/VMToast.java) 全局 Toast 提醒
    - [VMAuthCodeBtn](src/main/java/com/vmloft/develop/library/tools/widget/VMAuthCodeBtn.java) 验证码按钮
    - [VMDetailsView](src/main/java/com/vmloft/develop/library/tools/widget/VMDetailsView.java) 显示更多
    - [VMDotLineView](src/main/java/com/vmloft/develop/library/tools/widget/VMDotLineView.java) 描点连线
    - [VMDrawView](src/main/java/com/vmloft/develop/library/tools/widget/VMDrawView.java) 可绘制 View
    - [VMEditView](src/main/java/com/vmloft/develop/library/tools/widget/VMEditView.java) 输入控件
    - [VMExpandableLayout](src/main/java/com/vmloft/develop/library/tools/widget/VMExpandableLayout.java) 可伸缩布局
    - [VMImageView](src/main/java/com/vmloft/develop/library/tools/widget/VMImageView.java) 圆角图片
    - [VMRecordView](src/main/java/com/vmloft/develop/library/tools/widget/VMRecordView.java) 录音控件
    - [VMTimerBtn](src/main/java/com/vmloft/develop/library/tools/widget/VMTimerBtn.java) 定时按钮
    - [VMViewGroup](src/main/java/com/vmloft/develop/library/tools/widget/VMViewGroup.java) ViewGroup
    - [VMWaveformView](src/main/java/com/vmloft/develop/library/tools/widget/VMWaveformView.java) 波形图
    
- [VMTools](src/main/java/com/vmloft/develop/library/tools/VMTools.java) 工具初始化类

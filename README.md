VMLibrary
=========

[![](https://jitpack.io/v/com.github.lzan13/VMLibrary.svg)](https://jitpack.io/#com.github.lzan13/VMLibrary)
[![](https://img.shields.io/badge/blog-%E7%A9%BF%E8%A3%A4%E8%A1%A9%E9%97%AF%E5%A4%A9%E4%B8%8B-blue.svg)](https://blog.melove.net)
[![](https://img.shields.io/badge/github-lzan13-blue.svg)](https://github.com/lzan13)
[![](https://img.shields.io/badge/gitee-lzan13-red.svg)](https://gitee.com/lzan13)

封装自己开发中常用的一些工具类以及自定义控件等，这个项目包含了多个`module`，现在已经将 `vmtools`发布到`JitPack`，有需要的同学可以通过远程引用的方式使用


### #介绍
本来是作为一个自己项目管理第三方依赖用的，后来慢慢地积累了一些常用的工具类以及自定义控件，所以有想法把他放出去，
可以让其他有需要的同学以及使用，我这边呢会慢慢完善这个库的功能，如果有好想法，好的工具类也可以提到这里来

>PS:由于国内访问过慢，仓库代码在`gitee`上同样有一份 [VMLibrary](https://gitee.com/lzan13/VMLibrary)

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

### #详细
- [example](./example) 示例`Module`主要都是一些自己测试代码
- [vmtools](./vmtools) 自己封装的工具类库，包含常用工具类以及一些自定义控件等


### #LICENSE
[MIT License Copyright (c) 2021 lzan13](./LICENSE)

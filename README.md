VMLibrary
=========

[![](https://jitpack.io/v/com.gitee.lzan13/VMLibrary.svg)](https://jitpack.io/#com.gitee.lzan13/VMLibrary)
[![](https://img.shields.io/badge/blog-%E7%A9%BF%E8%A3%A4%E8%A1%A9%E9%97%AF%E5%A4%A9%E4%B8%8B-blue.svg)](https://blog.melove.net)
[![](https://img.shields.io/badge/github-lzan13-blue.svg)](https://github.com/lzan13)
[![](https://img.shields.io/badge/gitee-lzan13-red.svg)](https://gitee.com/lzan13)

封装自己开发中常用的一些工具类以及自定义控件等，这个项目包含了多个`module`，现在已经将 `vmtools`发布到`JitPack`，
有需要的同学可以通过远程引用的方式使用


### #介绍
本来是作为一个自己项目管理第三方依赖用的，后来慢慢地积累了一些常用的工具类以及自定义控件，所以有想法把他放出去，
可以让其他有需要的同学以及使用，我这边呢会慢慢完善这个库的功能，如果有好想法，好的工具类也可以提到这里来

>PS:由于国内访问过慢，仓库代码在`gitee`上同样有一份 [VMLibrary](https://gitee.com/lzan13/VMLibrary)

### #引用
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
    implementation 'com.gitee.lzan13:VMLibrary:lastVersion'
}
```

### #详细
- [example](./example) 示例`Module`主要都是一些自己测试代码
- [vmtools](./vmtools) 自己封装的工具类库，包含常用工具类以及一些自定义控件等


### #LICENSE
[MIT License Copyright (c) 2021 lzan13](./LICENSE)

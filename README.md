VMLibrary
=========
[![](https://img.shields.io/badge/author-lzan13-green.svg)](https://github.com/lzan13)
[![](https://img.shields.io/badge/weibo-@lzan13-red.svg)](http://weibo.com/lzan13)

### vmtools
[![Download](https://api.bintray.com/packages/lzan13/VMLibrary/vmtools/images/download.svg)](https://bintray.com/lzan13/VMLibrary/vmtools/_latestVersion)
详细说明移步[vmtools](./vmtools)

--------

封装自己开发中常用的一些工具类以及自定义控件等，这个项目包含了多个 module，现在已经将 `vmtools`发布到`JCenter`仓库，
有需要的同学可以通过远程引用的方式使用


### #说明
本来是作为一个自己项目管理第三方依赖用的，后来慢慢地积累了一些常用的工具类以及自定义控件，所以有想法把他放出去，
可以让其他有需要的同学以及使用，我这边呢会慢慢完善这个库的功能，如果有好想法，好的工具类也可以提到这里来

### #使用

```gradle
// 项目依赖
dependencies {
    ...
    implementation 'com.vmloft.library:vmtools:{版本号}'
}
```

### #详细描述
当前库管理包含了两个库，一个是`APP`端的，另一个是`TV`端，因为`TV`好多处理都和`APP`端不同，没弄在一起
下面是各个库的详细介绍，可以点击进去查看:

- 【[APP 端工具库 VMTools](./vmtools/README.md)】

- 【[TV 端工具库 VMTVTools](./vmtvtools/README.md)】
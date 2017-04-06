VMLibraryManager
================
各种扩展库管理项目，其中包含自己引用的其他的第三方库，以及自己创建的一些工具类库，
方便自己在开发项目中对一些工具类的使用，以及度其他第三方库的使用；

欢迎大家`star`or`fork`

### #说明
此管理项目中包含的一些库都是开源的，有些是官方提供的，有些是其他大牛或者自己封装的，后期会慢慢完善，有需要朋友也可以下载使用

### #使用方法
将本项目`clone`到自己的工作空间，可以和自己的项目放在同一目录，然后在`settings.gradle`加入如下代码:
（其中 library name 改成对应的名字）
```gradle
include ':library name'
project(':library name').projectDir = new File('../VMLibraryManager/module name')
```
然后在项目的主`module`项目依赖中引入这个扩展库:
```gradle
// 项目依赖
dependencies {
    // 引入项目 libs 目录下的各种第三方库，后边可以不用再单独添加
    compile fileTree(include: ['*.jar'], dir: 'libs')
    ...
    // 引入自己封装的工具库 GitHub: https://github.com/lzan13/VMLibraryManager
    compile project(':library name')
}
```

### #详细描述
当前库管理包含了两个库，一个是`APP`端的，另一个是`TV`端，因为`TV`好多处理都和`APP`端不同，没弄在一起
下面是各个库的详细介绍，可以点击进去查看:

- 【[APP 端工具库 VMTools](./vmtools/README.md)】

- 【[TV 端工具库 VMTVTools](./vmtvtools/README.md)】
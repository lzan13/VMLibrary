VMTVTools
=========
针对移动`APP`项目封装一些自己常用的工具类，以及一些自定义的控件等；同时管理一些自己项目中常用的第三方库，
这样可以方便只需要引用这一个库，其他的库都通过此库进行管理


### #使用方法
将本项目`clone`到自己的工作空间，和自己的项目放在同一目录，然后在`settings.gradle`加入如下代码:
```gradle
include ':vmtvtools'
project(':vmtvtools').projectDir = new File('../VMLibraryManager/vmtvtools')
```
然后在项目的主`module`项目依赖中引入这个扩展库:
```gradle
// 项目依赖
dependencies {
    // 引入项目 libs 目录下的各种第三方库，后边可以不用再单独添加
    compile fileTree(include: ['*.jar'], dir: 'libs')
    ...
    // 引入自己封装的工具库 GitHub: https://github.com/lzan13/VMLibraryManager/vmtvtools
    compile project(':vmtvtools')
}
```

### #实现功能
封装了一些自己常用的工具类

- [utils package](src/main/java/com/vmloft/develop/library/tools/tv/utils/) 自定义工具类部分
    - [VMDimenUtil](src/main/java/com/vmloft/develop/library/tools/tv/utils/VMDimenUtil.java) 尺寸
    - [VMLog](src/main/java/com/vmloft/develop/library/tools/utils/tv/VMLog.java) 日志
    - [VMSPUtil](src/main/java/com/vmloft/develop/library/tools/tv/utils/VMSPUtil.java) SharedPreference
    
- [widget package](src/main/java/com/vmloft/develop/library/tools/tv/widget) 自定义控件部分
    - [VMFocusLayout](src/main/java/com/vmloft/develop/library/tools/tv/widget/VMFocusLayout.java) 焦点控件
    
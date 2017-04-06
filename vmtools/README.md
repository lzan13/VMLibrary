VMTools
=======
针对移动`APP`项目封装一些自己常用的工具类，以及一些自定义的控件等；同时管理一些自己项目中常用的第三方库，
这样可以方便只需要引用这一个库，其他的库都通过此库进行管理


### #使用方法
将本项目`clone`到自己的工作空间，和自己的项目放在同一目录，然后在`settings.gradle`加入如下代码:
```gradle
include ':vmtools'
project(':vmtools').projectDir = new File('../VMLibraryManager/vmtools')
```
然后在项目的主`module`项目依赖中引入这个扩展库:
```gradle
// 项目依赖
dependencies {
    // 引入项目 libs 目录下的各种第三方库，后边可以不用再单独添加
    compile fileTree(include: ['*.jar'], dir: 'libs')
    ...
    // 引入自己封装的工具库 GitHub: https://github.com/lzan13/VMLibraryManager
    compile project(':vmtools')
}
```

### #实现功能
封装了一些自己常用的工具类

- [utils package](src/main/java/com/vmloft/develop/library/tools/utils) 自定义工具类部分
    - [VMBitmapUtil](src/main/java/com/vmloft/develop/library/tools/utils/VMBitmapUtil.java) 图片
    - [VMCryptoUtil](src/main/java/com/vmloft/develop/library/tools/utils/VMCryptoUtil.java) 加密解密
    - [VMDateUtil](src/main/java/com/vmloft/develop/library/tools/utils/VMDateUtil.java) 时间
    - [VMDimenUtil](src/main/java/com/vmloft/develop/library/tools/utils/VMDimenUtil.java) 尺寸
    - [VMFileUtil](src/main/java/com/vmloft/develop/library/tools/utils/VMFileUtil.java) 文件
    - [VMLog](src/main/java/com/vmloft/develop/library/tools/utils/VMLog.java) 日志
    - [VMNetUtil](src/main/java/com/vmloft/develop/library/tools/utils/VMNetUtil.java) 网络
    - [VMSPUtil](src/main/java/com/vmloft/develop/library/tools/utils/VMSPUtil.java) SharedPreference
    - [VMStrUtil](src/main/java/com/vmloft/develop/library/tools/utils/VMStrUtil.java) String
    
- [widget package](src/main/java/com/vmloft/develop/library/tools/widget) 自定义控件部分
    - [VMDotLineView](src/main/java/com/vmloft/develop/library/tools/widget/VMDotLineView.java) 描点连线
    - [VMImageView](src/main/java/com/vmloft/develop/library/tools/widget/VMImageView.java) 圆角图片
    - [VMRecordView](src/main/java/com/vmloft/develop/library/tools/widget/VMRecordView.java) 录音
    - [VMViewGroup](src/main/java/com/vmloft/develop/library/tools/widget/VMViewGroup.java) ViewGroup
    - [VMWaveformView](src/main/java/com/vmloft/develop/library/tools/widget/VMWaveformView.java) 波形图
    
    
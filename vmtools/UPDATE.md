更新日志
=======

### 2025/05/08 V1.9.1
- 优化日志输出，支持外层包装制定堆栈偏移，方便输出日志真实堆栈

### 2025/04/09 V1.9.0
- 优化 VMFloatMenu 控件，支持指定弹出方向，支持设置背静资源，设置阴影透明度

### 2024/05/23 V1.8.9
- 录音波形动画新增频谱展示，使用傅里叶变换进行转换

### 2024/03/06 V1.8.8
- 优化语音播放与录制

### 2024/03/05 V1.8.7
- 优化语音播放，添加波形播放竖线绘制

### 2024/01/31 V1.8.3
- 修改录音按下动画，添加倒计时提示

### 2024/01/29 V1.8.2
- 修改录音按下动画持续时间，调整波形控件事件处理

### 2022/02/21 V1.8.1
- 对语音录制加上分贝最大值限制
- 迁移项目导kts，同时使用 Composing builds 自定义插件管理依赖

### 2022/02/21 V1.7.9
- 使用 Media3 ExoPlayer 实现语音播放，和波形控件联动

### 2022/02/21 V1.7.7
- 使用 Media3 ExoPlayer 实现语音播放，和波形控件联动

### 2022/02/21 V1.7.6
- 使用 lame + AudioRecord 实现录制 mp3 格式语音文件

### 2022/02/21 V1.7.3
- 实现语音波形控件 

### 2022/02/21 V1.7.2
- 重写录音控件，录制和动画实现分体式设计

### 2022/02/21 V1.6.9
- 优化录音控件权限申请

### 2022/02/21 V1.6.8
- 解决权限申请弹窗，在某些情况下关闭后，还留有一个透明activity的问题

### 2022/02/21 V1.6.7
- 波形控件居中显示

### 2022/02/21 V1.6.6
- 新增VMWaveView控件

### 2022/02/21 V1.6.5
- 新增VMView工具类，将外部对输入法的操作及相关工具方法移到这里边
- 新增VMKeyboardController类，实现平滑切换输入法与扩展面板
- 优化录音工具类，优化动画显示

### 2022/02/21 V1.6.3
- 细节调整，包括时间格式化，正则匹配校验

### 2022/02/21 V1.6.2
- 添加遮罩引导控件

### 2022/02/21 V1.6.1
- 调整vmlog等级判断方式，优化弹幕控件

### 2022/02/21 V1.6.0
- 优化自定义VMLoadingView 控件，解决预览不生效问题

### 2022/02/21 V1.5.9
- 拆分 VMCommon 为多个小功能 Library，方便按需引入
- 调整基础样式

### 2022/02/21 V1.5.6
- 添加自定义 VMDrawView，支持设置颜色 宽度

### 2022/02/21 V1.5.5
- 优化时间工具类方法，vmtools库加上英文支持

### 2022/02/21 V1.5.4
- VMBDialog 支持设置显示位置并定义动画

### 2022/01/23 V1.5.3
- VMFloatMenu 支持自定义布局

### 2021/10/17 V1.5.2
- 更新 VMLineView 布局
- 优化自定义对话框 VMBDialog 实现

### 2021/10/17 V1.5.1
- 更新 VMLineView 自定义功能更强
- 迁移 DataBinding 到 ViewBinding

### 2021/08/14 V1.5.0
- 对 VMHeaderLayout 添加更新布局方法

### 2021/08/14 V1.4.9
- 更新打包配置

### 2021/08/14 V1.4.8
- VMHeaderLayout 问题修复
- 修改发布配置以及 Gradle 插件添加方式
- 修改 jitpack 打包时 java jdk 版本指定为 java 11

### 2021/07/20 V1.4.7
- 配置 mavenCenteral 代理地址

### 2021/07/20 V1.4.6
- 调整基础库，添加协议与政策弹窗

### 2021/07/20 V1.4.6
- 升级打包工具版本到 30.0.3，添加辅助功能服务，demo 层添加自动发送淘宝直播消息功能

### 2021/07/04 V1.4.4
- 修复压缩图片是计算旋转角度为 0 处理崩溃
- VMHeaderLayout 加上更新内容布局方法

### 2021/07/04 V1.4.3
- 处理 bitmap 时，将有旋转的图片角度调整回来
- 修改顶部联动控件实现

### 2021/07/04 V1.4.2
- 修复 toMD5 方法错误
- 修复获取后缀名报错

### 2021/07/04 V1.4.1
- 调整工具类一些方法，添加 md5 sha1 加密等方法
- 修改 bitmap 一些方法
- 修改 file 工具类方法

### 2021/07/04 V1.4.0
- 调整图片压缩，支持 PNG 格式保留透明度
- 优化动画工具类，减少方法数

### 2021/07/04 V1.3.9
- 添加一个统一管理以来的 module，方便后续项目直接使用，同时把当前项目的 example 作为一个模板项目
- 动画创建添加重复模式参数
- 调整文件路径获取方式，为了兼容 Android 10

### 2021/05/13 V1.3.4
- JCenter 关闭，修改项目发布到 JitPack.io，添加对话框基类，修改权限申请提示对话框

### 2021/04/19 V1.3.3
- 调整弹出提醒，UI 微调

### 2021/01/05 V1.3.2
- 调整 VMLineView 布局

### 2020/01/05 V1.3.1
- 添加一个自定义控件，方便实现个人信息头像滚动联动，调整悬浮菜单显示位置

### 2020/01/05 V1.3.0
- 完成图片保存到系统文件夹的高低版本适配

### 2020/01/05 V1.2.9
- 修复压缩图片地址转换错误问题

### 2020/01/05 V1.2.8
- 适配 Android10 图片压缩保存等处理

### 2020/01/05 V1.2.7
- 调整按钮主题样式，可以跟随主题改变，修改 VMFloatMenu 支持图标和设置背景，微调 VMLineView UI

### 2020/01/05 V1.2.6
- 调整 VMLineView 样式，新增分割线类型

### 2020/01/05 V1.2.5
- 调整按钮主题样式

### 2020/01/05 V1.2.4
- 优化 SharedPreferences 工具类，example一些操作正式化

### 2020/01/05 V1.2.3
- 修改 VMTopBar endBtn 样式设置

### 2020/01/05 V1.2.2
- 修改 VMTopBar 居中实现，修改 VMLineView 分割线实现

### 2020/01/05 V1.2.1
- 调整弹幕控件

### 2020/01/05 V1.2.0
- 添加自定义加载控件

### 2020/01/05 V1.1.9
- 修改颜色资源，demo 添加图标资源

### 2020/01/05 V1.1.8
- 修改输入框暗色样式以及状态栏适配

### 2020/01/05 V1.1.7
- 完善黑暗模式的适配

### 2020/01/05 V1.1.6
- 修改设置黑暗模式方式

### 2020/01/05 V1.1.4
- 添加弹幕控件

### 2020/01/05 V1.1.1
- 去掉针对 java 的兼容完全迁移到 Kotlin

### 2020/01/05 V1.1.0
- 迁移部分功能到 Kotlin，移除自定义图片选择器部分等功能

### 2020/01/05 V1.0.1
- 优化自定义正方形布局，改为 VMRatioLayout 比例布局

### 2020/01/05 V1.0.0
- 正式发布 1.0.0，同时迁移到 androidx，如果项目没有迁移 androidx 请使用 0.10.5 版本

### 2020/01/05 V0.10.5
- VMFloatMenu 支持定义字体颜色

### 2020/01/05 V0.10.4
- 修复异步执行任务错误

### 2020/01/05 V0.10.3
- 修改加载图片预览图可以外部实现创建接口

### 2020/01/05 V0.10.2
- 修复 VMToast 显示，调整正则匹配，

### 2020/01/05 V0.10.1
- 修改图片选择器按钮颜色

### 2020/01/05 V0.10.0
- 调整标题居中

### 2020/01/05 V0.9.22
- 设置取消居中不生效的问题

### 2020/01/05 V0.9.21
- 更新 VMTopBar 设置，支持居中标题

### 2020/01/05 V0.9.19
- 自定义 VMTopBar 与 VMLineView 添加自定义控件方法

### 2020/01/05 V0.9.18
- 设置 VMRouter 参数支持 Serializable

### 2020/01/05 V0.9.15
- 设置返回参数 resultCode 为 RESULT_OK

### 2020/01/05 V0.9.14
- 调整图片选择器，选择结果返回请求码

### 2020/01/03 V0.9.13
- 路由获取参数添加一个通过 intent 获取的方法

### 2020/01/03 V0.9.12
- 对 VMTopBar 进行调整，添加设置右侧按钮样式方法

### 2019/07/23 V0.9.11
- 优化表情雨控件

### 2019/07/23 V0.9.10
- 修改递归删除文件夹只删除文件的问题

### 2019/07/23 V0.9.8
- 修改路由跳转传递参数 flags 无效问题

### 2019/07/23 V0.9.7
- 解决图片选择器多选状态下拍照不剪切返回崩溃的 bug

### 2019/07/23 V0.9.6
- 修改跳转路由一些方法，不影响外部使用
- 调整路由参数为 Parcelable 方便通用

### 2019/07/23 V0.9.4
- 优化 VMTopBar 以及 VMLineView 控件，可自定义文本样式

### 2019/07/23 V0.9.3
- 优化 VMAdapter 集合实现 HeaderView FooterView MoreView EmptyView 功能

### 2019/07/17 V0.9.2
- 实现保存日志文件以及压缩日志
- 实现表情雨彩蛋控件 VMEmojiRainView

### 2019/07/15 V0.9.1
- 更新日志输出
- 优化 VMLineView 可设置 title 颜色

### 2019/06/17 V0.9.0
- 解决 OPPO 上 getLongVersionCode 崩溃的问题

### 2019/06/17 V0.8.8
- 解决 Android8.0 固定竖屏的 bug

### 2019/06/14 V0.8.7
- 修改设置黑色状态栏，兼容 MIUI Flyme 系统
- 修改颜色资源，使的自定义控件支持夜间模式

### 2019/06/12 V0.8.5
- 自定义正方形控件可设置按照宽高取值

### 2019/06/10 V0.8.4
- 优化录音控件，加入监听音量动画

### 2019/06/9 V0.8.3
- 添加自定义动画工具类 VMAnimator
- 重新实现自定义录音控件 VMRecordView

### 2019/06/8 V0.8.2
- 自定义输入框添加一些方法

### 2019/06/1 V0.8.1
- 修改图片加载接口，添加 Options 参数配置

### 2019/06/1 V0.8.0
- 添加阴影文字主题样式

### 2019/05/31 V0.7.9
- 解决二次进入选择器默认选中问题

### 2019/05/30 V0.7.8
- 添加 VMSquareLayout 正方形布局控件

### 2019/05/29 V0.7.6
- 自定义悬浮菜单 VMFloatMenu

### 2019/05/28 V0.7.5
- 解决因为图片本身带有旋转信息时获取图片宽高不准问题

### 2019/05/27 V0.7.3
- VMPicker 添加通过 Fragment 打开方式

### 2019/05/26 V0.7.2
- VMAdapter 添加单参数构造方法

### 2019/05/23 V0.7.1
- 解决选择图片多次刷新问题
- 示例部分实现选择器图片圆角
- 需改默认情况下裁剪框大小

### 2019/05/20 V0.6.6
- 修改加载图片回调接口方法名

### 2019/05/20 V0.6.2
- 完善图片选择器在有虚拟按键设备上的兼容性

### 2019/05/19 V0.6.1
- 修改 VMColor 获取 color 方法名
- 修改 VMStr 几个方法名
- 添加 VMTopBar 自定义控件
- 添加 VMPicker 图片选择器工具

### 2019/05/15 V0.5.8
- VMTheme 增加设置状态栏黑色文字方法

### 2019/05/15 V0.5.7
- 修改 VMLineView 默认点击效果

### 2019/05/13 V0.5.5
- 实现自定义 VMLineView 控件

### 2019/05/12 V0.5.3
- 调整权限申请弹出框

### 2019/05/08 V0.5.1
- 更更新自定义 VMToast 实现

### 2019/05/08 V0.5.0
这次算是一个较大的更新，添加了一个新的功能
- 实现 6.X 权限申请封装 
- 路由跳转添加可接受返回的方式

### 2019/04/19 V0.3.1
- 添加自定义输入控件
- 修改自定义 Toast 的实现，

### 2019/04/16 V0.3.0
- 修改自定义回调接口`VMCallback`返回数据为泛型

### 2019/04/11 V0.2.8
- 修改默认主题导航样式，默认不设置半透明

### 2019/04/11 V0.2.6
- 创建基类包，把所有基类放入包中
- 修改`Fragment`懒加载实现，新增`VMLazyFragment`类
- 懒加载 Fragment 添加是否需要加载进度 View 配置方法，同时添加一个默认的简单 FragmentPagerAdapter 适配器
- 添加指示器自定义控件 (VMIndicatorView)[src/main/java/com/vmloft/develop/library/tools/widget/indicator/VMIndicatorView.java]


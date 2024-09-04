#######################################################
### 三方框架
#######################################################

### 地址日期选择器
-keep class com.aigestudio.wheelpicker.** { *;}

### AgentWeb 库混淆配置
-keep class com.just.agentweb.** {*;}
-dontwarn com.just.agentweb.**

### LiveEventBus 混淆配置
-dontwarn com.jeremyliao.liveeventbus.**
-keep class com.jeremyliao.liveeventbus.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.arch.core.** { *; }

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/lzan13/develop/android/android-sdk/ToolsActivity/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

### 自身项目混淆配置
-keep class com.vmloft.develop.library.example.**{*;}
-keep public class com.vmloft.develop.library.example.R$*{
public static final int *;
}
# 设置生成平台 all:全部 armeabi-v7a:arm32位 arm64-v8a:arm64位 x86:虚拟机常用 x86-64
APP_ABI := arm64-v8a
APP_MODULES := vmlame
APP_CFLAGS += -DSTDC_HEADERS
APP_PLATFORM := android-21

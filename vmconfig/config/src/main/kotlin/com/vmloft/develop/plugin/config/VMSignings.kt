package com.vmloft.develop.plugin.config

/**
 * Created by lzan13 on 2024/01/25
 * 描述：签名配置
 * 签名文件由 AndroidStudio 生成，然后使用 keytool 命令转换格式
 * keytool -importkeystore -srckeystore ./vmloft.debug.jks -destkeystore ./vmloft.debug.keystore -deststoretype pkcs12
 * 签名配置，这里是默认添加的 debug 签名，方便打包测试，发布时需生成自己的签名文件，记得将签名文件复制到项目中
 */
object VMSignings {
    const val keyAlias = "vmloft"
    const val keyPassword = "123456"
    const val storeFile = "../vmloft.debug.jks"
    const val storePassword = "123456"
}
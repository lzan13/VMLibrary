//
// Created by lz liu on 2018/5/7.
//

#include <jni.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>

#include "VMLog.h"
// use std namespace
using namespace std;

// 全局属性
jstring globalStr;
extern "C" {

int compare(const int *a, const int *b) {
    return (*a) - (*b);
}

/**
 * 传递 java 数组到 jni
 * @param env
 * @param type
 * @param array_
 */
JNIEXPORT void JNICALL Java_com_vmloft_develop_library_ntools_VMTestJni_giveArray(JNIEnv *env, jclass type, jintArray array_) {
    // jintArray -> jint 指针 -> C int 数组 转换
    jint *array = env->GetIntArrayElements(array_, NULL);
    // 获取数组长度
    int len = env->GetArrayLength(array_);
    // 排序
    qsort(array, len, sizeof(jint), (int (*)(const void *, const void *)) compare);
    // 释放 C 数组
    env->ReleaseIntArrayElements(array_, array, 0);
}

/**
 * 获取 jni 数组
 * @param env
 * @param type
 * @return
 */
JNIEXPORT jintArray JNICALL Java_com_vmloft_develop_library_ntools_VMTestJni_getArray(JNIEnv *env, jclass type) {
    int len = 10;
    // 创建指定大小的数组
    jintArray jintArr = env->NewIntArray(len);
    // 根据数组对象获取到指针
    jint *arr = env->GetIntArrayElements(jintArr, NULL);
    for (int i = 0; i < len; i++) {
        arr[i] = i;
    }
    // 释放 C 数组
    env->ReleaseIntArrayElements(jintArr, arr, 0);
    return jintArr;
}


JNIEXPORT jstring JNICALL Java_com_vmloft_develop_library_ntools_VMTestJni_sayHelloJni(JNIEnv *env, jclass type) {
    // 根据 类名通过 FindClass 获取类定义，
    // 也可以通过 GetObjectClass 通过 obj 对应的类定义
    // 静态 native 方法第二个参数本身就是类定义，所以可以直接使用
    jclass cls = env->FindClass("com/vmloft/develop/library/ntools/VMTestJni");
    // 获取调用 java 方法的定义，这里获取构造方法，方便下边创建对象
    jmethodID constructor = env->GetMethodID(cls, "<init>", "()V");
    // 实例化一个对象
    jobject obj = env->NewObject(cls, constructor);

    jmethodID sayHello = env->GetMethodID(cls, "sayHello", "()Ljava/lang/String;");
    jstring hello_ = (jstring) env->CallObjectMethod(obj, sayHello);

    jmethodID sayHelloStatic = env->GetStaticMethodID(cls, "sayHelloStatic", "()Ljava/lang/String;");
    jstring helloStatic_ = (jstring) env->CallStaticObjectMethod(cls, sayHelloStatic);

    const char *hello = env->GetStringUTFChars(hello_, false);
    const char *helloStatic = env->GetStringUTFChars(helloStatic_, false);

    LOGI("Jni 调用 Java 方法: %s, %s", hello, helloStatic);

    char *helloStr = "这是 JNI ";
    //    strcat(helloStr, hello);
    //    strcat(helloStr, helloStatic);

    return env->NewStringUTF(helloStr);
}

JNIEXPORT void JNICALL Java_com_vmloft_develop_library_ntools_VMTestJni_say(JNIEnv *env, jobject instance) {
    jclass cls = env->FindClass("com/vmloft/develop/library/ntools/VMTestJni");
    jfieldID ageFid = env->GetFieldID(cls, "age", "I");
    jint age = env->GetIntField(instance, ageFid);

    jfieldID nameFid = env->GetFieldID(cls, "name", "Ljava/lang/String;");
    jstring name_ = (jstring) env->GetObjectField(instance, nameFid);

    jfieldID sayFid = env->GetStaticFieldID(cls, "say", "Ljava/lang/String;");
    jstring say_ = (jstring) env->GetStaticObjectField(cls, sayFid);

    const char *name = env->GetStringUTFChars(name_, false);
    const char *say = env->GetStringUTFChars(say_, false);

    LOGI("获取到的属性值 age:%d, name:%s, say:%s", age, name, say);

    age = 27;
    env->SetIntField(instance, ageFid, age);
    char hello[50] = "";
    strcat(hello, say);
    strcat(hello, name);
    jstring hello_ = env->NewStringUTF(hello);

    env->SetStaticObjectField(cls, sayFid, hello_);
    LOGI("修改后的属性值 age:%d, name:%s, say:%s", age, name, hello);

}

/**
 * 传递字符串数组，返回字符串
 * @param env
 * @param instance
 * @param strs
 * @return
 */
JNIEXPORT jstring JNICALL
Java_com_vmloft_develop_library_ntools_VMTestJni_getString(JNIEnv *env, jobject instance, jobjectArray strArr) {
    jsize size = env->GetArrayLength(strArr);
    string sss;
    for (int i = 0; i < size; i++) {
        jstring obj = (jstring) env->GetObjectArrayElement(strArr, i);
        string tmp(env->GetStringUTFChars(obj, false));
        sss += tmp;
        LOGI("解析 java 数组得到的数据：%s", tmp.c_str());
    }
    LOGI("解析 java 数组得到的数据：%s", sss.c_str());
    return env->NewStringUTF(sss.c_str());
}

/**
 * 获取字符串数组
 */
JNIEXPORT jobjectArray JNICALL
Java_com_vmloft_develop_library_ntools_VMTestJni_getStringArray(JNIEnv *env, jobject instance) {
    jstring str;
    jsize len = 5;
    jobjectArray arr = env->NewObjectArray(len, env->FindClass("java/lang/String"), 0);
    const char *cArr[] = {"Hello ", "world!", "This ", "is ", "Jni!"};
    for (int i = 0; i < len; i++) {
        str = env->NewStringUTF(cArr[i]);
        env->SetObjectArrayElement(arr, i, str);
    }
    return arr;

}

JNIEXPORT void JNICALL Java_com_vmloft_develop_library_ntools_VMTestJni_createGlobalRef(JNIEnv *env, jobject instance) {
    jstring obj = env->NewStringUTF("这是全局的属性!");
    globalStr = (jstring) env->NewGlobalRef(obj);
}

JNIEXPORT jstring JNICALL Java_com_vmloft_develop_library_ntools_VMTestJni_getGlobalRef(JNIEnv *env, jobject instance) {
    return globalStr;
}

JNIEXPORT void JNICALL Java_com_vmloft_develop_library_ntools_VMTestJni_deleteGlobalRef(JNIEnv *env, jobject instance) {
    env->DeleteGlobalRef(globalStr);
}
}
#include <jni.h>
#include <string>
#include <iostream>

// use std namespace
using namespace std;

extern "C"

JNIEXPORT jstring JNICALL Java_com_vmloft_develop_library_nativetools_utils_VMCrypto_crypStr2SHA1
        (JNIEnv *env, jclass, jstring) {
    string hello = "crypString2SHA1";
    char sayHello[] = {'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd', '\0'};
    cout << sayHello << endl;
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring JNICALL Java_com_vmloft_develop_library_nativetools_utils_VMCrypto_crypStr2MD5
        (JNIEnv *env, jclass, jstring) {
    string hello = "crypString2MD5";
    return env->NewStringUTF(hello.c_str());
}

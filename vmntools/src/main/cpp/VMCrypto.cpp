#include <jni.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <iostream>


// use std namespace
using namespace std;

extern "C" {

JNIEXPORT jstring JNICALL Java_com_vmloft_develop_library_ntools_utils_VMCrypto_crypStr2SHA1(JNIEnv *env, jstring string_) {
    const char *string = "crypString2SHA1";
    char sayHello[] = {'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd', '\0'};
    cout << sayHello << endl;
    return env->NewStringUTF(string);
}

JNIEXPORT jstring JNICALL Java_com_vmloft_develop_library_ntools_utils_VMCrypto_crypStr2MD5(JNIEnv *env, jstring string_) {
    const char *string = "crypString2MD5";
    char sayHello[] = {'H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd', '\0'};
    cout << sayHello << endl;
    return env->NewStringUTF(string);
}

}
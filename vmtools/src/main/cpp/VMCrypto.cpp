#include <jni.h>
#include <string>

#include "VMCrypto.h"

extern "C"

JNIEXPORT jstring JNICALL Java_com_vmloft_develop_library_tools_utils_VMCrypto_crypStr2SHA1
        (JNIEnv *env, jclass, jstring) {
    std::string hello = "crypString2SHA1";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring JNICALL Java_com_vmloft_develop_library_tools_utils_VMCrypto_crypStr2MD5
        (JNIEnv *env, jclass, jstring) {
    std::string hello = "crypString2MD5";
    return env->NewStringUTF(hello.c_str());
}

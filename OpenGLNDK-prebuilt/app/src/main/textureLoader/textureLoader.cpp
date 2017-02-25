//
// Created by ARF on 17/11/2016.
//


#include <jni.h>
#include <android/log.h>
#include <string>


// Android log function wrappers
static const char* kTAG = "textureLoader.cpp";

#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, kTAG, __VA_ARGS__))


JavaVM  *javaVM;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {

    javaVM = vm;
    return  JNI_VERSION_1_6;
}



int LoadTexture(std::string filename) {

    JNIEnv* env;

    if ((javaVM)->GetEnv((void**)&env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR; // JNI version not supported.
    }

    jclass clz = env->FindClass("com/lighthouse3d/android/openglndk_prebuilt/TextureLoader");

    jmethodID theFunc = (env)->GetStaticMethodID(clz,
                              "CreateTextureFromAssets", "(Ljava/lang/String;)I");
    if (!theFunc) {
        LOGE("Failed to retrieve getBuildVersion() methodID @ line %d",
             __LINE__);
        return JNI_ERR;
    }
    jstring javaMsg = env->NewStringUTF(filename.c_str());
    jint textureID = env->CallStaticIntMethod(clz, theFunc, javaMsg);
    env->DeleteLocalRef(javaMsg);

    return textureID;
}


int LoadCubeMapTexture(std::string px, std::string nx, std::string py, std::string ny,
                        std::string pz, std::string nz) {

    JNIEnv* env;

    if ((javaVM)->GetEnv((void**)&env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR; // JNI version not supported.
    }

    jclass clz = env->FindClass("com/lighthouse3d/android/openglndk_prebuilt/TextureLoader");

    jmethodID theFunc = (env)->GetStaticMethodID(clz,
                                                 "CreateCubeMapTextureFromAssets", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I");
    if (!theFunc) {
        LOGE("Failed to retrieve getBuildVersion() methodID @ line %d",
             __LINE__);
        return JNI_ERR;
    }
    jstring jmPX = env->NewStringUTF(px.c_str());
    jstring jmNX = env->NewStringUTF(nx.c_str());
    jstring jmPY = env->NewStringUTF(py.c_str());
    jstring jmNY = env->NewStringUTF(ny.c_str());
    jstring jmPZ = env->NewStringUTF(pz.c_str());
    jstring jmNZ = env->NewStringUTF(nz.c_str());
    jint textureID = env->CallStaticIntMethod(clz, theFunc,
                                              jmPX, jmNX, jmPY, jmNY, jmPZ, jmNZ);
    env->DeleteLocalRef(jmPX);
    env->DeleteLocalRef(jmNX);
    env->DeleteLocalRef(jmPY);
    env->DeleteLocalRef(jmNY);
    env->DeleteLocalRef(jmPZ);
    env->DeleteLocalRef(jmNZ);

    return textureID;
}

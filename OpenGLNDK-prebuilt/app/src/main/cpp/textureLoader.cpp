//
// Created by ARF on 17/11/2016.
//

#include <jni.h>
#include <pthread.h>
#include <string>
#include <android/log.h>

// Android log function wrappers
static const char* kTAG = "hello-jniCallback";
#define LOGI(...) \
  ((void)__android_log_print(ANDROID_LOG_INFO, kTAG, __VA_ARGS__))
#define LOGW(...) \
  ((void)__android_log_print(ANDROID_LOG_WARN, kTAG, __VA_ARGS__))
#define LOGE(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, kTAG, __VA_ARGS__))


typedef struct tick_context {
    JavaVM  *javaVM;
    jclass   jniHelperClz;
    jobject  jniHelperObj;
    jclass   mainActivityClz;
    jobject  mainActivityObj;
    pthread_mutex_t  lock;
    int      done;
} TickContext;

TickContext g_ctx;
jmethodID theFunc;
JNIEnv* env;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {
    memset(&g_ctx, 0, sizeof(g_ctx));

    g_ctx.javaVM = vm;
    if ((*vm)->GetEnv(vm, (void**)&env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR; // JNI version not supported.
    }

    jclass  clz = (*env)->FindClass(env,
                                    "com/example/arf/cpptest/TextureLoader");
    g_ctx.jniHelperClz = (*env)->NewGlobalRef(env, clz);

    jmethodID  jniHelperCtor = (*env)->GetMethodID(env, g_ctx.jniHelperClz,
                                                   "<init>", "()V");
    jobject    handler = (*env)->NewObject(env, g_ctx.jniHelperClz,
                                           jniHelperCtor);
    g_ctx.jniHelperObj = (*env)->NewGlobalRef(env, handler);

    g_ctx.done = 0;
    g_ctx.mainActivityObj = NULL;

    theFunc = (*env)->GetStaticMethodID(
            env, g_ctx.jniHelperClz,
            "CreateTextureFromAssets", "()Ljava/lang/String;");
    if (!theFunc) {
        LOGE("Failed to retrieve getBuildVersion() methodID @ line %d",
             __LINE__);
        return;
    }


    return  JNI_VERSION_1_6;
}



int LoadTexture(std::string filename) {

    jstring javaMsg = (*env)->NewStringUTF(env, filename.c_str());
    jint textureID = (*env)->CallIntMethod(env, g_ctx.jniHelperObj, theFunc, javaMsg);
    (*env)->DeleteLocalRef(env, javaMsg);

    return textureID;
}

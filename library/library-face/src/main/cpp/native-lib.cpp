//
// Created by 罗东华 on 2021/12/27.
// 动态注册方法
//

#include "jni.h"

extern bool find_face_init(JNIEnv *, jobject, jobject);

extern jobject find_face_detect(JNIEnv *, jobject, jobject);

static const JNINativeMethod jniNativeMethodFindFace[] = {
        {"init",   "(Landroid/content/res/AssetManager;)Z",       (bool *) find_face_init},
        {"detect", "(Landroid/graphics/Bitmap;)Ljava/util/List;", (jobject *) find_face_detect}
};

extern void check_face_init(JNIEnv *, jobject, jobject);

extern jfloatArray check_face_getFeature(JNIEnv *, jobject, jbyteArray, jint, jint, jintArray);

extern jfloat check_face_getResult(JNIEnv *env, jobject, jfloatArray, jfloatArray);

static const JNINativeMethod jniNativeMethodCheckFace[] = {
        {"init",       "(Landroid/content/res/AssetManager;)V", (void *) check_face_init},
        {"getFeature", "([BII[I)[F",                            (jfloatArray *) check_face_getFeature},
        {"getResult",  "([F[F)F",                               (jfloat *) check_face_getResult}
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *) {

    JNIEnv *env = nullptr;
    int result = vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6);

    if (result != JNI_OK) {
        return -1;
    }

    jclass clazzFindFace = env->FindClass("com/fm/library/face/FaceSdk$FindFace");
    jclass clazzCheckFace = env->FindClass("com/fm/library/face/FaceSdk$CheckFace");

    env->RegisterNatives(
            clazzFindFace,
            jniNativeMethodFindFace,
            sizeof(jniNativeMethodFindFace) / sizeof(JNINativeMethod)
    );

    env->RegisterNatives(
            clazzCheckFace,
            jniNativeMethodCheckFace,
            sizeof(jniNativeMethodCheckFace) / sizeof(JNINativeMethod)
    );

    return JNI_VERSION_1_6;
}
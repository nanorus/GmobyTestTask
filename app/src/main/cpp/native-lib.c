#include <jni.h>

JNIEXPORT jint JNICALL
Java_com_example_nanorus_gmobytesttask_view_profile_ProfileActivity_calculateSum(JNIEnv *env,
                                                                                 jobject thiz,
                                                                                 jint first,
                                                                                 jint second) {
    return first + second;
}


#include <jni.h>
#include <stdio.h>
#include "JNIDemoJava.h"

JNIEXPORT void JNICALL Java_jnidemojava_Main_nativePrint
        (JNIEnv *env, jobject obj,jstring jstr)
{


    const char* str = env->GetStringUTFChars(jstr,0);
env->ReleaseStringUTFChars(str,jstr,0);

    printf("\nHello World from C\n");

}
//
// Created by ARF on 17/11/2016.
//

#ifndef CPPTEST_TEXTURELOADER_H
#define CPPTEST_TEXTURELOADER_H


#include <string>
#include <jni.h>

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved);
int LoadTexture(std::string filename);
int LoadCubeMapTexture(std::string px, std::string nx, std::string py, std::string ny,
                       std::string pz, std::string nz);

#endif //CPPTEST_TEXTURELOADER_H

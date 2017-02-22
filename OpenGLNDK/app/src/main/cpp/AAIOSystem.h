//
// Created by ARF on 14/11/2016.
//

#ifndef CPPTEST_AAIOSYSTEM_H
#define CPPTEST_AAIOSYSTEM_H


#include <assimp/IOSystem.hpp>
#include "AAIOStream.h"
#include <android/asset_manager.h>

class AAIOSystem: public Assimp::IOSystem {

private:
    AAssetManager *m_Manager;
    AAIOStream *m_Stream;
    std::string m_LastPath;
    std::string getPath(const std::string &fn);

public:
    AAIOSystem(AAssetManager* aManager);

    Assimp::IOStream *Open(const char* file, const char* mode = "rb") override;
    void Close(Assimp::IOStream *) override;
    bool ComparePaths (const char*, const char*) const override;
    bool Exists (const char* file) const override;
    char getOsSeparator () const override;

};


#endif //CPPTEST_AAIOSYSTEM_H

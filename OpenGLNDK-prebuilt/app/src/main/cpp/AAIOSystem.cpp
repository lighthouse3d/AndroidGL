//
// Created by ARF on 14/11/2016.
//

#include "AAIOSystem.h"


AAIOSystem::AAIOSystem(AAssetManager *aManager):
        m_Manager(aManager), m_LastPath(""){

}


Assimp::IOStream *
AAIOSystem::Open(const char* file, const char* mode) {

    AAsset* asset = AAssetManager_open(m_Manager, file, AASSET_MODE_UNKNOWN);

    if (asset == NULL)
    {
        // Workaround for issue https://github.com/assimp/assimp/issues/641
        // Look for the file in the directory of the previously loaded file
        std::string file2 = m_LastPath + "/" + file;
        asset = AAssetManager_open(m_Manager, file2.c_str(), AASSET_MODE_UNKNOWN);
        if (asset == NULL)
            throw "Failed opening asset file"; // replace with proper exception class
    }
    m_LastPath = getPath(file);
    m_Stream = new AAIOStream(asset);
    return m_Stream;
}


void
AAIOSystem::Close(Assimp::IOStream *) {

    delete m_Stream;
}


bool
AAIOSystem::ComparePaths (const char* a, const char* b) const {

    return strcmp(a,b) == 0;
}


bool
AAIOSystem::Exists (const char* file) const {

    AAsset* asset = AAssetManager_open(m_Manager, file, AASSET_MODE_UNKNOWN);

    if (asset != NULL) {
        AAsset_close(asset);
        return true;
    }
    else
        return false;
}


char
AAIOSystem::getOsSeparator () const {

    return '/';
}


std::string
AAIOSystem::getPath(const std::string &fn) {

    size_t found = fn.find_last_of("/\\");

    if (found == fn.npos)
        return(""); // clone string
    else
        return(fn.substr(0,found));
}

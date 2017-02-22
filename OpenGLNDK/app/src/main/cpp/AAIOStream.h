//
// Created by ARF on 14/11/2016.
//

#ifndef CPPTEST_AAIOSTREAM_H
#define CPPTEST_AAIOSTREAM_H


#include <assimp/IOStream.hpp>
#include <android/asset_manager.h>

#include <assimp/types.h>

class AAIOStream: public Assimp::IOStream {

public:
    AAIOStream(AAsset *asset);
    ~AAIOStream();

    std::size_t FileSize () const override;
    void Flush() override;
    std::size_t Read(void* buf, std::size_t size, std::size_t count) override ;
    aiReturn Seek (std::size_t offset, aiOrigin origin) override;
    std::size_t Tell () const override;
    std::size_t Write (const void*, std::size_t, std::size_t) override;

private:
    AAsset *m_Asset;

    int to_whence (aiOrigin origin);
};


#endif //CPPTEST_AAIOSTREAM_H

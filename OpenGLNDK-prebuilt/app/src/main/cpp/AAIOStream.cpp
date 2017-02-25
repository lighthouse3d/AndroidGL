//
// Created by ARF on 14/11/2016.
//

#include "AAIOStream.h"

AAIOStream::AAIOStream(AAsset *asset): m_Asset(asset) {

}


AAIOStream::~AAIOStream() {

    AAsset_close(m_Asset);
}


std::size_t
AAIOStream::FileSize() const {

    return (size_t)AAsset_getLength64(m_Asset);
}

void
AAIOStream::Flush() {

    throw "AAIOStream::Flush() is unsupported";
}


std::size_t
AAIOStream::Read(void* buf, std::size_t size, std::size_t count) {

    return (size_t)AAsset_read(m_Asset, buf, size*count);
}


aiReturn
AAIOStream::Seek (std::size_t offset, aiOrigin origin) {

    AAsset_seek64(m_Asset, offset, to_whence(origin));
    return aiReturn_SUCCESS;
}


std::size_t
AAIOStream::Tell () const {

    return (size_t)(AAsset_getLength64(m_Asset) - AAsset_getRemainingLength64(m_Asset));
}


std::size_t
AAIOStream::Write (const void*, std::size_t, std::size_t) {

    throw "AAIOStream::Write() is unsupported";
}


int AAIOStream::to_whence (aiOrigin origin) {

    if (origin == aiOrigin_SET) return SEEK_SET;
    if (origin == aiOrigin_CUR) return SEEK_CUR;
    if (origin == aiOrigin_END) return SEEK_END;
    throw "AAIOStream to_whence: invalid aiOrigin";
}
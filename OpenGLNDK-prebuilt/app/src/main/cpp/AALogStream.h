//
// Created by ARF on 14/11/2016.
//

#ifndef CPPTEST_AALOGSTREAM_H
#define CPPTEST_AALOGSTREAM_H

#include <cstdio>
#include <assimp/LogStream.hpp>
#include <android/log.h>

class AALogStream :
        public Assimp::LogStream
{
public:
    // Constructor
    AALogStream()
    {
        // empty
    }

    // Destructor
    ~AALogStream()
    {
        // empty
    }
    // Write womethink using your own functionality
    void write(const char* message)
    {
        __android_log_print(ANDROID_LOG_DEBUG, "Assimp Test", "%s\n", message);
    }
};

#endif //CPPTEST_AALOGSTREAM_H

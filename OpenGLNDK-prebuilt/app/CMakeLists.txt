# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)

set(LOCAL_ARM_MODE arm)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds it for you.
# Gradle automatically packages shared libraries with your APK.

include_directories (
    src/main/vsl
    src/main/textureLoader
    src/main/assimp
    src/main/assimp/assimp/include/assimp
    src/main/assimp/include/assimp/import
    src/main/assimp/contrib/ConvertUTF/
    src/main/assimp/contrib/openddlparser/include/
    src/main/assimp/contrib/rapidjson/include
    src/main/assimp/contrib/clipper/
    src/main/assimp/contrib/unzip
    src/main/assimp/contrib/poly2tri/poly2tri
    src/main/assimp/contrib/poly2tri/poly2tri/common/
    src/main/assimp/contrib/poly2tri/poly2tri/sweep
    src/main/assimp/contrib/irrXML
    src/main/assimp/contrib/zlib)


add_library( # Sets the name of the library.
    native-lib

    # Sets the library as a shared library.
    SHARED

    # Provides a relative path to your source file(s).
    # Associated headers in the same location as their source
    # file are automatically included.
    src/main/cpp/native-lib.cpp)


add_library(assimpAI
    SHARED
    src/main/cpp/AAIOSystem.cpp
    src/main/cpp/AAIOStream.cpp)


add_library(vsl
    SHARED
    src/main/vsl/vsFontLib.cpp
	src/main/vsl/vsLogLib.cpp
	src/main/vsl/vsMathLib.cpp
	src/main/vsl/vsModelLib.cpp
	src/main/vsl/vsResourceLib.cpp
	src/main/vsl/vsShaderLib.cpp
	src/main/vsl/vsGeometryLib.cpp
	src/main/vsl/vsSurfRevLib.cpp
	src/main/vsl/vslibs.h
	src/main/vsl/vslConfig.h)


add_library(textureLoader
    SHARED
    src/main/textureLoader/textureLoader.cpp)



# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log)

find_library(android-lib
              android)

find_library(GLES-lib
              GLESv3)


add_library(assimp-lib
            SHARED
            IMPORTED)
            
set_target_properties( # Specifies the target library.
                       assimp-lib

                       # Specifies the parameter you want to define.
                       PROPERTIES IMPORTED_LOCATION

                       # Provides the path to the library you want to import.
                       ${CMAKE_SOURCE_DIR}/../libs/${ANDROID_ABI}/libassimp.so )

include_directories(${CMAKE_SOURCE_DIR}/../includes)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       assimpAI

                       assimp-lib
                       ${android-lib})

target_link_libraries( # Specifies the target library.
                       textureLoader

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       ${android-lib})

target_link_libraries( # Specifies the target library.
                       native-lib

                       assimpAI
                       vsl
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       ${android-lib})

target_link_libraries( # Specifies the target library.
                       vsl

                       assimp-lib
                       textureLoader
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       ${android-lib}
                       ${GLES-lib})

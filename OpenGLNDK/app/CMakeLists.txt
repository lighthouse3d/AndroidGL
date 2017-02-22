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

add_library(assimp
    SHARED
    src/main/assimp/assimp/3DSConverter.cpp
    src/main/assimp/assimp/3DSExporter.cpp
    src/main/assimp/assimp/3DSLoader.cpp
    src/main/assimp/assimp/ACLoader.cpp
    src/main/assimp/assimp/ASELoader.cpp
    src/main/assimp/assimp/ASEParser.cpp
    src/main/assimp/assimp/AssbinExporter.cpp
    src/main/assimp/assimp/AssbinLoader.cpp
    src/main/assimp/assimp/Assimp.cpp
    src/main/assimp/assimp/AssimpCExport.cpp
    src/main/assimp/assimp/AssxmlExporter.cpp
    src/main/assimp/assimp/B3DImporter.cpp
    src/main/assimp/assimp/BaseImporter.cpp
    src/main/assimp/assimp/BaseProcess.cpp
    src/main/assimp/assimp/Bitmap.cpp
    src/main/assimp/assimp/BlenderBMesh.cpp
    src/main/assimp/assimp/BlenderDNA.cpp
    src/main/assimp/assimp/BlenderLoader.cpp
    src/main/assimp/assimp/BlenderModifier.cpp
    src/main/assimp/assimp/BlenderScene.cpp
    src/main/assimp/assimp/BlenderTessellator.cpp
    src/main/assimp/assimp/BVHLoader.cpp
    src/main/assimp/assimp/CalcTangentsProcess.cpp
    src/main/assimp/assimp/COBLoader.cpp
    src/main/assimp/assimp/ColladaExporter.cpp
    src/main/assimp/assimp/ColladaLoader.cpp
    src/main/assimp/assimp/ColladaParser.cpp
    src/main/assimp/assimp/ComputeUVMappingProcess.cpp
    src/main/assimp/assimp/ConvertToLHProcess.cpp
    src/main/assimp/assimp/CSMLoader.cpp
    src/main/assimp/assimp/D3MFImporter.cpp
    src/main/assimp/assimp/D3MFOpcPackage.cpp
    src/main/assimp/assimp/DeboneProcess.cpp
    src/main/assimp/assimp/DefaultIOStream.cpp
    src/main/assimp/assimp/DefaultIOSystem.cpp
    src/main/assimp/assimp/DefaultLogger.cpp
    src/main/assimp/assimp/DXFLoader.cpp
    src/main/assimp/assimp/Exporter.cpp
    src/main/assimp/assimp/FBXAnimation.cpp
    src/main/assimp/assimp/FBXBinaryTokenizer.cpp
    src/main/assimp/assimp/FBXConverter.cpp
    src/main/assimp/assimp/FBXDeformer.cpp
    src/main/assimp/assimp/FBXDocument.cpp
    src/main/assimp/assimp/FBXDocumentUtil.cpp
    src/main/assimp/assimp/FBXImporter.cpp
    src/main/assimp/assimp/FBXMaterial.cpp
    src/main/assimp/assimp/FBXMeshGeometry.cpp
    src/main/assimp/assimp/FBXModel.cpp
    src/main/assimp/assimp/FBXNodeAttribute.cpp
    src/main/assimp/assimp/FBXParser.cpp
    src/main/assimp/assimp/FBXProperties.cpp
    src/main/assimp/assimp/FBXTokenizer.cpp
    src/main/assimp/assimp/FBXUtil.cpp
    src/main/assimp/assimp/FindDegenerates.cpp
    src/main/assimp/assimp/FindInstancesProcess.cpp
    src/main/assimp/assimp/FindInvalidDataProcess.cpp
    src/main/assimp/assimp/FixNormalsStep.cpp
    src/main/assimp/assimp/GenFaceNormalsProcess.cpp
    src/main/assimp/assimp/GenVertexNormalsProcess.cpp
    src/main/assimp/assimp/glTFExporter.cpp
    src/main/assimp/assimp/glTFImporter.cpp
    src/main/assimp/assimp/HMPLoader.cpp
    src/main/assimp/assimp/IFCBoolean.cpp
    src/main/assimp/assimp/IFCCurve.cpp
    src/main/assimp/assimp/IFCGeometry.cpp
    src/main/assimp/assimp/IFCLoader.cpp
    src/main/assimp/assimp/IFCMaterial.cpp
    src/main/assimp/assimp/IFCOpenings.cpp
    src/main/assimp/assimp/IFCProfile.cpp
    src/main/assimp/assimp/IFCReaderGen.cpp
    src/main/assimp/assimp/IFCUtil.cpp
    src/main/assimp/assimp/Importer.cpp
    src/main/assimp/assimp/ImporterRegistry.cpp
    src/main/assimp/assimp/ImproveCacheLocality.cpp
    src/main/assimp/assimp/IRRLoader.cpp
    src/main/assimp/assimp/IRRMeshLoader.cpp
    src/main/assimp/assimp/IRRShared.cpp
    src/main/assimp/assimp/JoinVerticesProcess.cpp
    src/main/assimp/assimp/LimitBoneWeightsProcess.cpp
    src/main/assimp/assimp/LWOAnimation.cpp
    src/main/assimp/assimp/LWOBLoader.cpp
    src/main/assimp/assimp/LWOLoader.cpp
    src/main/assimp/assimp/LWOMaterial.cpp
    src/main/assimp/assimp/LWSLoader.cpp
    src/main/assimp/assimp/MakeVerboseFormat.cpp
    src/main/assimp/assimp/MaterialSystem.cpp
    src/main/assimp/assimp/MD2Loader.cpp
    src/main/assimp/assimp/MD3Loader.cpp
    src/main/assimp/assimp/MD5Loader.cpp
    src/main/assimp/assimp/MD5Parser.cpp
    src/main/assimp/assimp/MDCLoader.cpp
    src/main/assimp/assimp/MDLLoader.cpp
    src/main/assimp/assimp/MDLMaterialLoader.cpp
    src/main/assimp/assimp/MS3DLoader.cpp
    src/main/assimp/assimp/NDOLoader.cpp
    src/main/assimp/assimp/NFFLoader.cpp
    src/main/assimp/assimp/ObjExporter.cpp
    src/main/assimp/assimp/ObjFileImporter.cpp
    src/main/assimp/assimp/ObjFileMtlImporter.cpp
    src/main/assimp/assimp/ObjFileParser.cpp
    src/main/assimp/assimp/OFFLoader.cpp
    src/main/assimp/assimp/OgreBinarySerializer.cpp
    src/main/assimp/assimp/OgreImporter.cpp
    src/main/assimp/assimp/OgreMaterial.cpp
    src/main/assimp/assimp/OgreStructs.cpp
    src/main/assimp/assimp/OgreXmlSerializer.cpp
    src/main/assimp/assimp/OpenGEXExporter.cpp
    src/main/assimp/assimp/OpenGEXImporter.cpp
    src/main/assimp/assimp/OptimizeGraph.cpp
    src/main/assimp/assimp/OptimizeMeshes.cpp
    src/main/assimp/assimp/PlyExporter.cpp
    src/main/assimp/assimp/PlyLoader.cpp
    src/main/assimp/assimp/PlyParser.cpp
    src/main/assimp/assimp/PostStepRegistry.cpp
    src/main/assimp/assimp/PretransformVertices.cpp
    src/main/assimp/assimp/ProcessHelper.cpp
    src/main/assimp/assimp/Q3BSPFileImporter.cpp
    src/main/assimp/assimp/Q3BSPFileParser.cpp
    src/main/assimp/assimp/Q3BSPZipArchive.cpp
    src/main/assimp/assimp/Q3DLoader.cpp
    src/main/assimp/assimp/RawLoader.cpp
    src/main/assimp/assimp/RemoveComments.cpp
    src/main/assimp/assimp/RemoveRedundantMaterials.cpp
    src/main/assimp/assimp/RemoveVCProcess.cpp
    src/main/assimp/assimp/SceneCombiner.cpp
    src/main/assimp/assimp/ScenePreprocessor.cpp
    src/main/assimp/assimp/SGSpatialSort.cpp
    src/main/assimp/assimp/SIBImporter.cpp
    src/main/assimp/assimp/SkeletonMeshBuilder.cpp
    src/main/assimp/assimp/SMDLoader.cpp
    src/main/assimp/assimp/SortByPTypeProcess.cpp
    src/main/assimp/assimp/SpatialSort.cpp
    src/main/assimp/assimp/SplitByBoneCountProcess.cpp
    src/main/assimp/assimp/SplitLargeMeshes.cpp
    src/main/assimp/assimp/StandardShapes.cpp
    src/main/assimp/assimp/StepExporter.cpp
    src/main/assimp/assimp/STEPFileEncoding.cpp
    src/main/assimp/assimp/STEPFileReader.cpp
    src/main/assimp/assimp/STLExporter.cpp
    src/main/assimp/assimp/STLLoader.cpp
    src/main/assimp/assimp/Subdivision.cpp
    src/main/assimp/assimp/TargetAnimation.cpp
    src/main/assimp/assimp/TerragenLoader.cpp
    src/main/assimp/assimp/TextureTransform.cpp
    src/main/assimp/assimp/TriangulateProcess.cpp
    src/main/assimp/assimp/UnrealLoader.cpp
    src/main/assimp/assimp/ValidateDataStructure.cpp
    src/main/assimp/assimp/Version.cpp
    src/main/assimp/assimp/VertexTriangleAdjacency.cpp
    src/main/assimp/assimp/XFileExporter.cpp
    src/main/assimp/assimp/XFileImporter.cpp
    src/main/assimp/assimp/XFileParser.cpp
    src/main/assimp/assimp/XGLLoader.cpp

    src/main/assimp/contrib/ConvertUTF/ConvertUTF.c

    src/main/assimp/contrib/openddlparser/code/DDLNode.cpp
    src/main/assimp/contrib/openddlparser/code/OpenDDLCommon.cpp
    src/main/assimp/contrib/openddlparser/code/OpenDDLExport.cpp
    src/main/assimp/contrib/openddlparser/code/OpenDDLParser.cpp
    src/main/assimp/contrib/openddlparser/code/Value.cpp

    src/main/assimp/contrib/clipper/clipper.cpp

    src/main/assimp/contrib/unzip/ioapi.c
    src/main/assimp/contrib/unzip/unzip.c

    src/main/assimp/contrib/poly2tri/poly2tri/common/shapes.cc
    src/main/assimp/contrib/poly2tri/poly2tri/sweep/advancing_front.cc
    src/main/assimp/contrib/poly2tri/poly2tri/sweep/cdt.cc
    src/main/assimp/contrib/poly2tri/poly2tri/sweep/sweep.cc
    src/main/assimp/contrib/poly2tri/poly2tri/sweep/sweep_context.cc

    src/main/assimp/contrib/zlib/adler32.c
    src/main/assimp/contrib/zlib/compress.c
    src/main/assimp/contrib/zlib/crc32.c
    src/main/assimp/contrib/zlib/deflate.c
    src/main/assimp/contrib/zlib/gzclose.c
    src/main/assimp/contrib/zlib/gzlib.c
    src/main/assimp/contrib/zlib/gzread.c
    src/main/assimp/contrib/zlib/gzwrite.c
    src/main/assimp/contrib/zlib/inflate.c
    src/main/assimp/contrib/zlib/infback.c
    src/main/assimp/contrib/zlib/inftrees.c
    src/main/assimp/contrib/zlib/inffast.c
    src/main/assimp/contrib/zlib/trees.c
    src/main/assimp/contrib/zlib/uncompr.c
    src/main/assimp/contrib/zlib/zutil.c

    src/main/assimp/contrib/irrXML/irrXML.cpp)


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


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       assimpAI

                       assimp
                       ${android-lib})

target_link_libraries( # Specifies the target library.
                       textureLoader

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       ${android-lib})

target_link_libraries( # Specifies the target library.
                       native-lib

                       assimp
                       assimpAI
                       vsl
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       ${android-lib})

target_link_libraries( # Specifies the target library.
                       vsl

                       assimp
                       textureLoader
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       ${android-lib}
                       ${GLES-lib})

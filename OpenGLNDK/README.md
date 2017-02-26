#OpenGL with NDK 

An OpenGL® ES 3.0 app in C++ using [VSL](http://www.lighthouse3d.com/very-simple-libs/). [Assimp](http://www.assimp.org/) is used to load models. Textures are loaded using JNI.

The starting point for this app was the code from hello-gl2, hello-libs, and gles3jni, available in [googlesamples/android-ndk](https://developer.android.com/training/graphics/opengl/index.html). The added/changed features are: 
* arcball to control camera
* texture loading
* model loading with Assimp
* using VSL to speed up demo development

Move a finger to rotate the box.

![screenshot](openglndk.png)

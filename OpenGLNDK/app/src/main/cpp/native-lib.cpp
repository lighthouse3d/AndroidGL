#include <jni.h>
#include <android/log.h>

#include <string>
#include <assimp/Importer.hpp>
#include "AAIOSystem.h"
#include "AALogStream.h"
#include <android/asset_manager_jni.h>
#include <assimp/scene.h>
#include <assimp/DefaultLogger.hpp>
#include <vslibs.h>


static const char* kTAG = "native-lib.cpp";

#define LOGD(...) \
  ((void)__android_log_print(ANDROID_LOG_DEBUG, kTAG, __VA_ARGS__))


static VSMathLib *vsml;
static VSShaderLib shader, cmShader;
static VSModelLib model, plane0, plane1, plane2, plane3, plane4, plane5;
static VSGrid modelGrid;
static VSAxis modelAxis;
static VSSurfRevLib surfRev, pawn;
static AAssetManager *mgr;

static float startX;
static float startY;
static float currX;
static float currY;
static bool rotating;

static const float SCALE_FACTOR = 0.001f;
static float currDist, startDist, prevDist, prevScale = 1.0f;
static bool scaling;

static int m_Width;
static int m_Height;


void getArcBallVec(float x, float y, float *v) {

    v[0] = x * 2.0f / m_Width - 1;
    v[1] = -(y * 2.0f / m_Height - 1);
    v[2] = 0.0f;

    float op = v[0] * v[0] + v[1] * v[1];
    if (op < 1.0)
        v[2] = (float)sqrt(1.0f - op);
    else
        vsml->normalize(v);
}


void computeAngleAndAxisOfRotation(float *va, float *vb, float *angle, float *axis) {

    *angle = acosf(fminf(1.0f, VSMathLib::dotProduct(va, vb)));
    vsml->crossProduct(va, vb, axis);
    axis[3] = 0;

}


void computeRotationMatrix() {

    if (currX != startX || currY != startY) {
        float va[3], vb[3], axisCamCoord[4], axisWorldCoord[4];
        float angle;
        getArcBallVec(currX, currY, vb);
        getArcBallVec(startX, startY, va);
        computeAngleAndAxisOfRotation(va, vb, &angle, axisCamCoord);

        // cam space to world space (pre trackball rotation)
        vsml->loadIdentity(VSMathLib::AUX0);
        vsml->multMatrix(VSMathLib::AUX0, vsml->get(VSMathLib::VIEW));
        vsml->multMatrix(VSMathLib::AUX0, vsml->get(VSMathLib::AUX1));
        vsml->invert(VSMathLib::AUX0);
        vsml->multMatrixPoint(VSMathLib::AUX0, axisCamCoord, axisWorldCoord);
        vsml->loadIdentity(VSMathLib::AUX0);

        vsml->rotate(VSMathLib::AUX1, angle * 180 / 3.1415f, axisWorldCoord[0], axisWorldCoord[1],
                     axisWorldCoord[2]);
        vsml->loadMatrix(VSMathLib::AUX2, vsml->get(VSMathLib::AUX0));
        vsml->multMatrix(VSMathLib::AUX2, vsml->get(VSMathLib::AUX1));
        startX = currX;
        startY = currY;
    }
}


void computeScaleMatrix() {

    if (currDist != prevDist) {
        float scale = (currDist - startDist) * SCALE_FACTOR;
        if (prevScale + scale >= 0.1) {
            vsml->loadIdentity(VSMathLib::AUX3);
            vsml->scale(VSMathLib::AUX3, prevScale + scale, prevScale + scale, prevScale + scale);
        }
        prevDist = currDist;

    }
}


GLuint setupShaders() {

    // Shader for models

    shader.init();
    shader.loadShader(VSShaderLib::VERTEX_SHADER, "shaders/color.vert");
    shader.loadShader(VSShaderLib::FRAGMENT_SHADER, "shaders/color.frag");

    // set semantics for the shader variables
    //shader.setProgramOutput(0,"outputF");
    shader.setVertexAttribName(VSShaderLib::VERTEX_COORD_ATTRIB, "position");
    shader.setVertexAttribName(VSShaderLib::TEXTURE_COORD_ATTRIB, "texCoord");

    shader.prepareProgram();

#ifdef __ANDROID_API__
    __android_log_print(ANDROID_LOG_DEBUG, "InfoLog", "\n%s", shader.getAllInfoLogs().c_str());
#else
    printf("InfoLog\n%s\n\n", shader.getAllInfoLogs().c_str());
#endif

     // add sampler uniforms
    shader.setUniform("texUnit", 0);

    cmShader.init();
    cmShader.loadShader(VSShaderLib::VERTEX_SHADER, "shaders/cubemap1.vert");
    cmShader.loadShader(VSShaderLib::FRAGMENT_SHADER, "shaders/cubemap1.frag");

    cmShader.setVertexAttribName(VSShaderLib::VERTEX_COORD_ATTRIB, "position");
    cmShader.setVertexAttribName(VSShaderLib::NORMAL_ATTRIB, "normal");

    cmShader.prepareProgram();

    cmShader.setUniform("texUnit",0);

#ifdef __ANDROID_API__
    __android_log_print(ANDROID_LOG_DEBUG, "InfoLog", "\n%s", cmShader.getAllInfoLogs().c_str());
#else
    printf("InfoLog\n%s\n\n", shader.getAllInfoLogs().c_str());
#endif

    return(1);
}


void initVSL() {

    VSResourceLib::setMaterialBlockName("Material");

//	Init VSML
    vsml = VSMathLib::getInstance();
    vsml->setUniformBlockName("Matrices");
    vsml->setUniformName(VSMathLib::PROJ_VIEW_MODEL, "m_pvm");
    vsml->setUniformName(VSMathLib::NORMAL_MODEL, "m_normalM");
    vsml->setUniformName(VSMathLib::MODEL, "m_model");
    vsml->setUniformName(VSMathLib::NORMAL, "m_normal");
    vsml->setUniformName(VSMathLib::VIEW_MODEL, "m_vm");

    vsml->loadIdentity(VSMathLib::AUX1);
    vsml->loadIdentity(VSMathLib::AUX2);
    vsml->loadIdentity(VSMathLib::AUX3);

}

extern "C"
void
Java_com_lighthouse3d_android_openglndk_GLES3JNILib_init(JNIEnv* env, jobject obj, jobject assetManager) {

    Assimp::Importer *imp = new Assimp::Importer();
    mgr = AAssetManager_fromJava(env, assetManager);
    AAIOSystem* ioSystem = new AAIOSystem(mgr);
    imp->SetIOHandler(ioSystem);
#ifdef __VSL_MODEL_LOADING__
    VSModelLib::SetImporter(imp);
#endif
    VSShaderLib::SetAssetManager(mgr);

    setupShaders();
    LOGD("Shaders ready");
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_CULL_FACE);

    glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

    initVSL();
    LOGD("VSL init done");

#ifdef __VSL_MODEL_LOADING__
    model.load("teapot.obj");
    LOGD("Teapot loaded");
 /*   model.addCubeMapTexture(0,
                               "CM/CloudyHills_posx_512.png",
                               "CM/CloudyHills_negx_512.png",
                               "CM/CloudyHills_posy_512.png",
                               "CM/CloudyHills_negy_512.png",
                               "CM/CloudyHills_posz_512.png",
                               "CM/CloudyHills_negz_512.png");
*/
    model.addCubeMapTexture(0,
                            "CM/posx.jpg",
                            "CM/negx.jpg",
                            "CM/posy.jpg",
                            "CM/negy.jpg",
                            "CM/posz.jpg",
                            "CM/negz.jpg");

    LOGD("cubemap loaded");
    plane0.load("plane.obj");
    plane1.addMeshes(plane0);
    plane2.addMeshes(plane0);
    plane3.addMeshes(plane0);
    plane4.addMeshes(plane0);
    plane5.addMeshes(plane0);
/*
    plane0.addTexture(0, "CM/CloudyHills_negy_512.png");
    plane1.addTexture(0, "CM/CloudyHills_negz_512.png");
    plane2.addTexture(0, "CM/CloudyHills_posz_512.png");
    plane3.addTexture(0, "CM/CloudyHills_posy_512.png");
    plane4.addTexture(0, "CM/CloudyHills_posx_512.png");
    plane5.addTexture(0, "CM/CloudyHills_negx_512.png");
*/
    plane0.addTexture(0, "CM/negy.jpg");
    plane1.addTexture(0, "CM/negz.jpg");
    plane2.addTexture(0, "CM/posz.jpg");
    plane3.addTexture(0, "CM/posy.jpg");
    plane4.addTexture(0, "CM/posx.jpg");
    plane5.addTexture(0, "CM/negx.jpg");
#endif
    modelGrid.set(VSGrid::Y, 5, 10);
    float gray[4] = {0.5f, 0.5f, 0.5f, 1.0f};
    modelGrid.setColor(VSResourceLib::EMISSIVE, gray);
    modelAxis.set(3.0f, 0.025f);

    surfRev.createTorus(1,2,16,16);
    pawn.createPawn();
    LOGD("Init done");
}


extern "C"
void
Java_com_lighthouse3d_android_openglndk_GLES3JNILib_resize(JNIEnv* env, jobject obj, jint width, jint height) {

    m_Width = width;
    m_Height = height;
    float ratio = (width * 1.0f)/height;
    glViewport(0,0, width, height);

    vsml->loadIdentity(VSMathLib::PROJECTION);
    vsml->perspective(60.0f, ratio, 0.1f, 100.0f);
}


extern "C"
void
Java_com_lighthouse3d_android_openglndk_GLES3JNILib_step(JNIEnv* env, jobject obj) {

    float lightDir[4] = {-1.0f, 1.0f, 1.0f, 0.0f};
    float lightDirEyeCoord[4];

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    vsml->loadIdentity(VSMathLib::VIEW);
    vsml->loadIdentity(VSMathLib::MODEL);
    // set camera
    vsml->lookAt(0, 0, 5, 0,0,0, 0,1,0);
    if (rotating) {
        computeRotationMatrix();
    }
    if (scaling) {
        computeScaleMatrix();
    }
    vsml->multMatrix(VSMathLib::VIEW, vsml->get(VSMathLib::AUX2));
    vsml->multMatrix(VSMathLib::VIEW, vsml->get(VSMathLib::AUX3));

    float c[3];
    vsml->getCameraPosition(c);
    cmShader.setUniform("camPosWorld", c);


    // render stuff
#ifdef __VSL_MODEL_LOADING__
    // use regular shader
    glUseProgram(shader.getProgramIndex());

    // render the six planes of the skybox
      vsml->pushMatrix(VSMathLib::MODEL);
      vsml->translate(0.0, -4.0, 0.0);
      vsml->scale(4.0, 4.0, 4.0);
      plane0.render();

      vsml->popMatrix(VSMathLib::MODEL);
      vsml->pushMatrix(VSMathLib::MODEL);
      vsml->translate(0.0, 4.0, 0.0);
      vsml->scale(4.0, 4.0, 4.0);
      vsml->rotate(180, 1.0, 0.0, 0.0);
      plane3.render();
      vsml->popMatrix(VSMathLib::MODEL);

      vsml->pushMatrix(VSMathLib::MODEL);
      vsml->translate(0.0, 0.0, -4.0);
      vsml->scale(4.0, 4.0, 4.0);
      vsml->rotate(90, 1.0, 0.0, 0.0);
      vsml->rotate(180, 0.0, 1.0, 0.0);
      plane1.render();
      vsml->popMatrix(VSMathLib::MODEL);

      vsml->pushMatrix(VSMathLib::MODEL);
      vsml->translate(0.0, 0.0, 4.0);
      vsml->scale(4.0, 4.0, 4.0);
      vsml->rotate(-90, 1.0, 0.0, 0.0);
      plane2.render();
      vsml->popMatrix(VSMathLib::MODEL);

      vsml->pushMatrix(VSMathLib::MODEL);
      vsml->translate(4.0, 0.0, 0.0);
      vsml->scale(4.0, 4.0, 4.0);
      vsml->rotate(90, 0.0, 0.0, 1.0);
      vsml->rotate(90, 0.0, 1.0, 0.0);
      plane4.render();
      vsml->popMatrix(VSMathLib::MODEL);

      vsml->pushMatrix(VSMathLib::MODEL);
      vsml->translate(-4.0, 0.0, 0.0);
      vsml->scale(4.0, 4.0, 4.0);
      vsml->rotate(-90, 0.0, 0.0, 1.0);
      vsml->rotate(-90, 0.0, 1.0, 0.0);
      plane5.render();
      vsml->popMatrix(VSMathLib::MODEL);

      // use the cubemap shader
      glUseProgram(cmShader.getProgramIndex());
      // render model
      model.render();

#endif
 }


extern "C"
void
Java_com_lighthouse3d_android_openglndk_GLES3JNILib_startRotating(JNIEnv* env, jobject obj, float x, float y) {

    currX = x;
    currY = y;
    startX = x;
    startY = y;
    rotating = true;
}


extern "C"
void
Java_com_lighthouse3d_android_openglndk_GLES3JNILib_stopRotating(JNIEnv* env, jobject obj) {

    rotating = false;
}


extern "C"
void
Java_com_lighthouse3d_android_openglndk_GLES3JNILib_updateRotation(JNIEnv* env, jobject obj, float newX, float newY) {

    currX = newX;
    currY = newY;
}


extern "C"
void
Java_com_lighthouse3d_android_openglndk_GLES3JNILib_startScaling(JNIEnv* env, jobject obj, float dist) {

    currDist = dist;
    startDist = dist;
    prevDist = dist;
    scaling = true;
}


extern "C"
void
Java_com_lighthouse3d_android_openglndk_GLES3JNILib_stopScaling(JNIEnv* env, jobject obj) {

    LOGD("stopScaling");
    scaling = false;
    prevScale += (currDist - startDist) * SCALE_FACTOR;
    if (prevScale < 0.1f)
        prevScale = 0.1f;
    startDist = currDist;
}


extern "C"
void
Java_com_lighthouse3d_android_openglndk_GLES3JNILib_updateScale(JNIEnv* env, jobject obj, float dist) {

    currDist = dist;

}


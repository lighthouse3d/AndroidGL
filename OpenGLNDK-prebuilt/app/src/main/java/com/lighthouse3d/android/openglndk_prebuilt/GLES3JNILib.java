package com.lighthouse3d.android.openglndk_prebuilt;

import android.content.res.AssetManager;

/**
 * Created by ARF on 16/11/2016.
 */

public class GLES3JNILib {

    static {
        System.loadLibrary("native-lib");
    }

    public static native void init(AssetManager c);
    public static native void resize(int width, int height);
    public static native void step();

    public static native void startRotating(float x, float y);
    public static native void updateRotation(float x, float y);
    public static native void stopRotating();

    public static native void startScaling(float dist);
    public static native void updateScale(float dist);
    public static native void stopScaling();
}

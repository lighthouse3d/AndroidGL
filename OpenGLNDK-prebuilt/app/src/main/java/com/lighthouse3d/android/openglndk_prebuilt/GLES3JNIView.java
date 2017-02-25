package com.lighthouse3d.android.openglndk_prebuilt;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ARF on 16/11/2016.
 */

public class GLES3JNIView extends GLSurfaceView {

    private static final String TAG = "GLES3JNIView";
    private int m_FingersDown = 0;

    public GLES3JNIView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Context m_Context;
        MyRenderer m_Renderer;

        m_Context = context;
        // Pick an EGLConfig with RGB8 color, 16-bit depth, no stencil,
        // supporting OpenGL ES 2.0 or later backwards-compatible versions.
        //setEGLConfigChooser(8, 8, 8, 0, 16, 0);
        setEGLContextClientVersion(3);
        m_Renderer = new MyRenderer(m_Context);
        setRenderer(m_Renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        int aux = e.getPointerCount();
        if (aux > 2) {
            Log.e(TAG, "Max two fingers");
        }

        // a finger was lifted, proceed with rotation
        if (aux == 1 && m_FingersDown == 2) {
            GLES3JNILib.startRotating(x, y);
            GLES3JNILib.stopScaling();
        }
        // another finger is touching, proceed with scale
        else if (aux == 2 && m_FingersDown == 1) {
            GLES3JNILib.startScaling(fingerSeparation(e));
            GLES3JNILib.stopRotating();
        }

        m_FingersDown = aux;

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (m_FingersDown == 1)
                    GLES3JNILib.startRotating(x,y);
                else {
                    GLES3JNILib.startScaling(fingerSeparation(e));
                }
                Log.d(TAG, "Action down" + e.getPointerCount());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d(TAG, "Action pointer down");
                break;

            case MotionEvent.ACTION_UP:
                GLES3JNILib.stopRotating();
                GLES3JNILib.stopScaling();
                Log.d(TAG, "Action up");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d(TAG, "Action pointer up");
                break;
            case MotionEvent.ACTION_MOVE:
                if (m_FingersDown == 1)
                    GLES3JNILib.updateRotation(x,y);
                else
                    GLES3JNILib.updateScale(fingerSeparation(e));

                Log.d(TAG, "Action move" + e.getPointerCount());
                break;
        }
        return true;
    }


    private float fingerSeparation(MotionEvent e) {

        float x0 = e.getX(0); float y0 = e.getY(0);
        float x1 = e.getX(1); float y1 = e.getY(1);

        return (float)Math.sqrt((x0-x1)*(x0-x1) + (y0-y1)*(y0-y1));
    }


    private static class MyRenderer implements GLSurfaceView.Renderer  {

        private Context m_Context;

        private MyRenderer(Context context) {
            super();
            m_Context = context;
            TextureLoader.SetContext(m_Context);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES3JNILib.step();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES3JNILib.resize(width, height);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES3JNILib.init(m_Context.getAssets());
        }

    }
}

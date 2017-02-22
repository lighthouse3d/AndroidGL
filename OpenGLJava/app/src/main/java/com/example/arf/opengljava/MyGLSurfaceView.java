package com.example.arf.opengljava;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ARF on 25/10/2016.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer m_Renderer;

    public MyGLSurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);

        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(3);
        m_Renderer = new MyGLRenderer(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(m_Renderer);
        // setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, we are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                m_Renderer.startRotating(x,y);
                break;
            case MotionEvent.ACTION_UP:
                m_Renderer.stopRotating();
                break;
            case MotionEvent.ACTION_MOVE:
                m_Renderer.updateRotation(x,y);
                break;
        }
        return true;
    }
}

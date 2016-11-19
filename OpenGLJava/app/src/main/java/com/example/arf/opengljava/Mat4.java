package com.example.arf.opengljava;

import android.opengl.Matrix;

/**
 * Created by ARF on 05/11/2016.
 */

public class Mat4 {

    private float[] m_Data;

    public Mat4() {

        m_Data = new float[16];

        for (int i = 1; i < 15; ++i) {

            m_Data[i] = 0.0f;
        }
        m_Data[0] = 1.0f;
        m_Data[5] = 1.0f;
        m_Data[10]= 1.0f;
        m_Data[15]= 1.0f;
    }


    public float [] getMat() {

        return m_Data;
    }


    public void translate(float x, float y, float z) {

        Matrix.translateM(m_Data, 0, x,y,z);
    }


    public void scale(float x) {

        Matrix.scaleM(m_Data, 0, x ,x, x);
    }


    public void scale(float x, float y, float z) {

        Matrix.scaleM(m_Data, 0, x ,y, z);
    }
}

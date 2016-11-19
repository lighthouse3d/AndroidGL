package com.example.arf.opengljava;

/**
 * Created by ARF on 04/11/2016.
 */

public class Mat3 {

    private float[] m_Data;

    public Mat3(float m44[]) {

        m_Data = new float[9];

        m_Data[0] = m44[0];
        m_Data[1] = m44[1];
        m_Data[2] = m44[2];

        m_Data[3] = m44[4];
        m_Data[4] = m44[5];
        m_Data[5] = m44[6];

        m_Data[6] = m44[8];
        m_Data[7] = m44[9];
        m_Data[8] = m44[10];
    }

    public void inverseTranspose() {

        float a00, a01, a02, a10, a11, a12, a20, a21, a22;
        a00 = m_Data[0]; a01 = m_Data[1]; a02 = m_Data[2];
        a10 = m_Data[3]; a11 = m_Data[4]; a12 = m_Data[5];
        a20 = m_Data[6]; a21 = m_Data[7]; a22 = m_Data[8];

        float b01 = a11 * a22 - a12 * a21;
        float b11 = a12 * a20 - a22 * a10;
        float b21 = a21 * a10 - a11 * a20;

        float d = a00*b01 + a01*b11 + a02*b21;
        if (d == 0.0f) { return ; }
        float id = 1/d;

        m_Data[0] = b01*id;
        m_Data[1] = b11*id;
        m_Data[2] = b21*id;

        m_Data[3] = (-a22*a01 + a02*a21)*id;
        m_Data[4] = (a22*a00 - a02*a20)*id;
        m_Data[5] = (-a21*a00 + a01*a20)*id;

        m_Data[6] = (a12*a01 - a02*a11)*id;
        m_Data[7] = (-a12*a00 + a02*a10)*id;
        m_Data[8] = (a11*a00 - a01*a10)*id;
    }

    public float[] getMat() {

        return m_Data;
    }
}

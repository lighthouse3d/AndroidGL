package com.example.arf.opengljava;

import android.graphics.Bitmap;
import android.opengl.GLES30;
import android.opengl.GLUtils;

/**
 * Created by ARF on 01/11/2016.
 */

public class Material {

    private float[] m_Diffuse;
    private float[] m_Specular;
    private float[] m_Ambient;
    private float m_Shininess;
    private String m_TexFilename;
    private int[] m_TexID;


    public Material() {

        m_Diffuse = new float[]{0.8f, 0.8f, 0.8f, 1.0f};
        m_Ambient = new float[]{0.2f, 0.2f, 0.2f, 1.0f};
        m_Specular = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        m_Shininess = 128.0f;
        m_TexFilename = "";
        m_TexID = new int[]{0};
    }


    private void copyColor(float[] in, float [] out) {

        out[0] = in [0]; out[1] = in[1]; out[2] = in[2]; out[3] = in[3];
    }


    public float[] getDiffuse() {

        return m_Diffuse;
    }


    public float[] getSpecular() {

        return m_Specular;
    }


    public float[] getAmbient() {

        return m_Ambient;
    }


    public float getShininess() {

        return m_Shininess;
    }


    public int getTexID() {

        return m_TexID[0];
    }


    public String getTexFilename() {

        return m_TexFilename;
    }


    public void setDiffuse(float[] diffuse) {

        copyColor(diffuse,m_Diffuse);
    }


    public void setSpecular(float[] specular) {

        copyColor(specular, m_Specular);
    }


    public void setAmbient(float[] ambient) {

        copyColor(ambient, m_Ambient);
    }


    public void setShininess(float shininess) {

        m_Shininess = shininess;
    }


    public void setTexFilename(String texFilename) {

        m_TexFilename = texFilename;
        Bitmap b = IOHelper.LoadBitmapFromAssets(texFilename);

        GLES30.glGenTextures(1, m_TexID, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, m_TexID[0]);

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, b, 0);
        b.recycle();
    }

    public void bindTexture(int unit) {

        if (m_TexID[0] != 0) {

            GLES30.glActiveTexture(unit);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, m_TexID[0]);
        }
    }


    public void unbindTexture() {

        if (m_TexID[0] != 0) {
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        }
    }
}

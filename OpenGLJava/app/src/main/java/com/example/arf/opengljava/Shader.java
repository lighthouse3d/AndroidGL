package com.example.arf.opengljava;

import android.opengl.GLES30;
import android.util.Log;

import java.io.IOException;

/**
 * Created by ARF on 02/11/2016.
 */

public class Shader {

    private int m_vsID;
    private int m_fsID;
    private int m_pID;

    private int m_positionLoc;
    private int m_normalLoc;
    private int m_texCoordLoc;

    private int m_uniPVMLoc;
    private int m_uniNormalLoc;
    private int m_uniViewModelLoc;
    private int m_uniViewLoc;

    private int m_uniSampler2DLoc;
    private int m_uniVec4DiffuseLoc;
    private int m_uniVec4SpecularLoc;
    private int m_uniFloatShininessLoc;


    public Shader(String vs, String fs) {

        String vsSource = "";
        String fsSource = "";

        try {
            vsSource = IOHelper.LoadShaderFileFromAssets(vs);
            fsSource = IOHelper.LoadShaderFileFromAssets(fs);
        }
        catch(IOException e) {
            Log.d("Shader", e.getMessage());
        }
        Log.d("Shader", vsSource);

        m_vsID = GLES30.glCreateShader(GLES30.GL_VERTEX_SHADER);
        GLES30.glShaderSource(m_vsID, vsSource);
        GLES30.glCompileShader(m_vsID);

        m_fsID = GLES30.glCreateShader(GLES30.GL_FRAGMENT_SHADER);
        GLES30.glShaderSource(m_fsID, fsSource);
        GLES30.glCompileShader(m_fsID);

        m_pID = GLES30.glCreateProgram();
        GLES30.glAttachShader(m_pID, m_vsID);
        GLES30.glAttachShader(m_pID, m_fsID);

        GLES30.glBindAttribLocation(m_pID, 0, "aVec4Position");
        GLES30.glBindAttribLocation(m_pID, 1, "aVec3Normal");
        GLES30.glBindAttribLocation(m_pID, 2, "aVec2TexCoord");

        GLES30.glLinkProgram(m_pID);

        m_uniPVMLoc = GLES30.glGetUniformLocation(m_pID, "uMat4PVM");
        m_uniNormalLoc = GLES30.glGetUniformLocation(m_pID, "uMat3Normal");
        m_uniViewModelLoc = GLES30.glGetUniformLocation(m_pID, "uMat4ViewModel");
        m_uniViewLoc = GLES30.glGetUniformLocation(m_pID, "uMat4View");
        m_uniSampler2DLoc = GLES30.glGetUniformLocation(m_pID, "uSampler2D");

        m_uniVec4DiffuseLoc = GLES30.glGetUniformLocation(m_pID, "uVec4Diffuse");
        m_uniVec4SpecularLoc = GLES30.glGetUniformLocation(m_pID, "uVec4Specular");
        m_uniFloatShininessLoc = GLES30.glGetUniformLocation(m_pID, "uFloatShininess");

        int[] status = new int[2];
        GLES30.glGetIntegerv(GLES30.GL_MAJOR_VERSION, status, 0);
        GLES30.glGetIntegerv(GLES30.GL_MINOR_VERSION, status, 1);
        Log.d("Shader", "GLES version "  + String.valueOf(status[0]) + "." + String.valueOf(status[1]));
        GLES30.glGetIntegerv(GLES30.GL_SHADING_LANGUAGE_VERSION, status, 0);
        Log.d("Shader", "Shader version "  + String.valueOf(status[0]));
        GLES30.glGetProgramiv(m_pID, GLES30.GL_LINK_STATUS, status, 0);
        if (status[0] == 0) {
            Log.d("Shader", "Program InfoLog: " + GLES30.glGetProgramInfoLog(m_pID));
        }
        Log.d("Shader", "Link Status: " + String.valueOf(status[0]));
        GLES30.glGetShaderiv(m_vsID, GLES30.GL_COMPILE_STATUS, status, 0);
        Log.d("Shader", "Vertex Shader Compile Status: " + String.valueOf(status[0]));
        if (status[0] == 0) {
            Log.d("Shader", GLES30.glGetShaderInfoLog(m_vsID));
        }
        GLES30.glGetShaderiv(m_fsID, GLES30.GL_COMPILE_STATUS, status, 0);
        Log.d("Shader", "Fragment Shader Compile Status: " + String.valueOf(status[0]));
        if (status[0] == 0) {
            Log.d("Shader", GLES30.glGetShaderInfoLog(m_fsID));
        }
    }


    public void useProgram() {

        GLES30.glUseProgram(m_pID);
    }


    public void setMatrices(float[] pvm, float[] vm, float [] v, float[] normal) {

        GLES30.glUniformMatrix4fv(m_uniPVMLoc, 1, false, pvm, 0);
        GLES30.glUniformMatrix4fv(m_uniViewModelLoc, 1, false, vm, 0);
        GLES30.glUniformMatrix4fv(m_uniViewLoc, 1, false, v, 0);
        GLES30.glUniformMatrix3fv(m_uniNormalLoc, 1, false, normal, 0);
    }


    public void setMaterial(Material m, int unit) {

        GLES30.glUniform4fv(m_uniVec4DiffuseLoc, 1, m.getDiffuse(), 0 );
        GLES30.glUniform4fv(m_uniVec4SpecularLoc, 1, m.getSpecular(), 0 );
        GLES30.glUniform1f(m_uniFloatShininessLoc, m.getShininess());
        GLES30.glUniform1i(m_uniSampler2DLoc, unit);
        GLES30.glActiveTexture(unit);
        m.bindTexture(unit);
    }


    public void restoreMaterial() {

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
    }


    public int getProgramID() {

        return m_pID;
    }


    public int getTexCoordLoc() {

        return m_texCoordLoc;
    }


    public int getNormalLoc() {

        return m_normalLoc;
    }


    public int getPositionLoc() {

        return m_positionLoc;
    }


    public int getUniPVMLoc() {

        return m_uniPVMLoc;
    }


    public int getUniNormalLoc() {

        return m_uniNormalLoc;
    }


    public int getUniViewModelLoc() {

        return m_uniViewModelLoc;
    }
}

package com.example.arf.opengljava;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES30;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by ARF on 01/11/2016.
 */

public class Model {

    private FloatBuffer[] m_Positions, m_Normals, m_TexCoords;
    private IntBuffer[] m_Indices;
    private int[] m_VAO;
    private Material[] m_Material;
    private Mat4 m_ModelMatrix;
    static private Context m_Context;



    static void SetContext(Context cont) {
        m_Context = cont;
    }


    public Model() {

    }

    public boolean modelFromJSON(String filename)

    {
        try {
            String jsonString = IOHelper.LoadJSONFromAssets(filename);
            JSONObject jsonobject = new JSONObject(jsonString);
            JSONArray jarray = (JSONArray) jsonobject.getJSONArray("model");

            int count = jarray.length();

            m_VAO = new int[count];
            m_Positions = new FloatBuffer[count];
            m_Normals = new FloatBuffer[count];
            m_TexCoords = new FloatBuffer[count];
            m_Indices = new IntBuffer[count];
            m_Material = new Material[count];

            int error;
            error = GLES30.glGetError();
            GLES30.glGenVertexArrays(count, m_VAO, 0);
            error = GLES30.glGetError();

            // iterate over models
            for (int i = 0; i < count; i++) {

                GLES30.glBindVertexArray(m_VAO[i]);
                error = GLES30.glGetError();
                Log.d("Model", "build indices " + String.valueOf(error));

                int[] arrays = {0,0,0,0};
                JSONObject jb = (JSONObject) jarray.get(i);

                if (jb.has("indices")) {
                    GLES30.glGenBuffers(1, arrays, 0);
                    error = GLES30.glGetError();
                    JSONArray indicesJS = jb.getJSONArray("indices");
                    m_Indices[i] = IntBuffer.allocate(indicesJS.length());
                    m_Indices[i].position(0);
                    for (int k = 0; k < indicesJS.length(); ++k) {
                        m_Indices[i].put(indicesJS.getInt(k));
                    }
                    GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, arrays[0]);
                    error = GLES30.glGetError();
                    m_Indices[i].position(0);
                    GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, m_Indices[i].capacity() * 4, m_Indices[i], GLES30.GL_STATIC_DRAW);
                    error = GLES30.glGetError();

                }

                if (jb.has("position")) {
                    GLES30.glGenBuffers(1, arrays, 1);

                    JSONArray positionsJS = jb.getJSONArray("position");
                    m_Positions[i] = FloatBuffer.allocate(positionsJS.length());
                    m_Positions[i].position(0);
                    for (int k = 0; k < positionsJS.length(); ++k) {
                        m_Positions[i].put((float)positionsJS.getDouble(k));
                    }
                    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, arrays[1]);
                    m_Positions[i].position(0);
                    GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, m_Positions[i].capacity() * 4, m_Positions[i], GLES30.GL_STATIC_DRAW);
                    GLES30.glEnableVertexAttribArray(0);
                    GLES30.glVertexAttribPointer(0, 4, GLES30.GL_FLOAT, false, 0, 0);

                    TextView tv = (TextView)((Activity)m_Context).findViewById(R.id.textView);
                    tv.setText("Model has " + String.valueOf(positionsJS.length()/4) + " vertices");
                }
                else // position must exist
                    return false;

                if (jb.has("normal")) {
                    GLES30.glGenBuffers(1, arrays, 2);
                    JSONArray normalsJS = jb.getJSONArray("normal");
                    m_Normals[i] = FloatBuffer.allocate(normalsJS.length());
                    m_Normals[i].position(0);
                    for (int k = 0; k < normalsJS.length(); ++k) {
                        m_Normals[i].put((float)normalsJS.getDouble(k));
                    }
                    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, arrays[2]);
                    m_Normals[i].position(0);
                    GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, m_Normals[i].capacity() * 4, m_Normals[i], GLES30.GL_STATIC_DRAW);
                    GLES30.glEnableVertexAttribArray(1);
                    GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 0, 0);
                }

                if (jb.has("texCoord")) {
                    GLES30.glGenBuffers(1, arrays, 3);
                    JSONArray texCoordsJS = jb.getJSONArray("texCoord");
                    m_TexCoords[i] = FloatBuffer.allocate(texCoordsJS.length());
                    m_TexCoords[i].position(0);
                    for (int k = 0; k < texCoordsJS.length(); ++k) {
                        m_TexCoords[i].put((float)texCoordsJS.getDouble(k));
                    }
                    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, arrays[3]);
                    m_TexCoords[i].position(0);
                    GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, m_TexCoords[i].capacity() * 4, m_TexCoords[i], GLES30.GL_STATIC_DRAW);
                    GLES30.glEnableVertexAttribArray(2);
                    GLES30.glVertexAttribPointer(2, 2, GLES30.GL_FLOAT, false, 0, 0);
                }

                if (jb.has("material")) {
                    m_Material[i] = new Material();
                    float[] color = new float[4];
                    JSONObject jmat = (JSONObject) jb.get("material");

                    if (jmat.has("diffuse")) {
                        JSONArray jprop = jmat.getJSONArray("diffuse");
                        for (int j = 0; j < 4; ++j) {
                            color[j] = (float) jprop.getDouble(j);
                        }
                        m_Material[i].setDiffuse(color);
                    }
                    if (jmat.has("ambient")) {
                        JSONArray jprop = jmat.getJSONArray("ambient");
                        for (int j = 0; j < 4; ++j) {
                            color[j] = (float) jprop.getDouble(j);
                        }
                        m_Material[i].setAmbient(color);
                    }
                    if (jmat.has("specular")) {
                        JSONArray jprop = jmat.getJSONArray("specular");
                        for (int j = 0; j < 4; ++j) {
                            color[j] = (float) jprop.getDouble(j);
                        }
                        m_Material[i].setSpecular(color);
                    }
                    if (jmat.has("shininess")) {
                        m_Material[i].setShininess((float)jmat.getDouble("shininess"));
                    }

                    if (jmat.has("texture")) {
                        String texFile = jmat.getString("texture");
                        m_Material[i].setTexFilename((texFile));

                    }
                }
                error = GLES30.glGetError();
                Log.d("Model", "Loading " + String.valueOf(error));
            }
            m_ModelMatrix = new Mat4();
            if(jsonobject.has("boundingBox")) {
                JSONArray jprop = (JSONArray) jsonobject.getJSONArray("boundingBox");
                float[] min = new float[3];
                float[] max = new float[3];

                for (int j = 0; j < 3; ++j) {
                    min[j] = (float) jprop.getDouble(j);
                    max[j] = (float) jprop.getDouble(j + 3);
                }
                float tmpX = 1 / Math.abs(max[0] - min[0]);
                float tmpY = 1 / Math.abs(max[1] - min[1]);
                float tmpZ = 1 / Math.abs(max[2] - min[2]);
                float tmp = tmpX < tmpY ? tmpY : tmpX;
                tmp = tmp < tmpZ ? tmpZ * 2 : tmp * 2;
                m_ModelMatrix.scale(tmp);
                m_ModelMatrix.translate(-(max[0] + min[0]) * 0.5f,
                        -(max[1] + min[1]) * 0.5f,
                        -(max[2] + min[2]) * 0.5f);
            }

        } catch (JSONException e) {
            Log.d("Model", "Loading failed" + e.getMessage());
            return false;

        }
        return true;
    }


    public int[] getVAOs() {
        return m_VAO;
    }


    public IntBuffer[] getIndices() {
        return m_Indices;
    }


    public Material getMaterial(int i) {

        if (i < m_Material.length) {
            return m_Material[i];
        }
        else {
            return m_Material[0];
        }
    }


    public Mat4 getModelMatrix() {

        return m_ModelMatrix;
    }

} // end class Model

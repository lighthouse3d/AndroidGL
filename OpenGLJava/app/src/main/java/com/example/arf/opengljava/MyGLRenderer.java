package com.example.arf.opengljava;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ARF on 25/10/2016.
 */


public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] m_PVMMatrix = new float[16];
    private final float[] m_ProjectionMatrix = new float[16];
    private final float[] m_ViewModelMatrix = new float[16];
    private final float[] m_ViewMatrix = new float[16];
    private final float[] m_RotationMatrix = new float[16];
    private Context m_Context;


    private String m_VertShaderFile = new String();
    private String m_FragShaderFile = new String();
    private String m_ModelFile = new String();

    private Model m_Model = new Model();
    private Shader m_Shader;

    private int m_Width;
    private int m_Height;

    private float m_PrevX;
    private float m_PrevY;
    private float m_NewX;
    private float m_NewY;
    private boolean m_Rotating;


    public MyGLRenderer(Context context) {
        super();

        m_Context = context;
        IOHelper.SetContext(m_Context);
        Model.SetContext(context);
        Matrix.setIdentityM(m_RotationMatrix, 0);

        m_Model = new Model();
    }


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES30.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);

        loadAssetsAndShaders("cube2.json", "shaders/phong.vert", "shaders/phong.frag");
    }


    @Override
    public void onDrawFrame(GL10 unused) {

        // Draw background color
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(m_ViewMatrix, 0, 0, 0, 5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        if (m_Rotating)
            rotate();
        // Calculate the projection and view transformation
        Matrix.multiplyMM(m_PVMMatrix, 0, m_ProjectionMatrix, 0, m_ViewMatrix, 0);


        // Create a rotation for the triangle

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.

        Matrix.multiplyMM(m_PVMMatrix, 0, m_PVMMatrix, 0, m_RotationMatrix, 0);
        Mat4 m = m_Model.getModelMatrix();
        float[] model = m.getMat();
        Matrix.multiplyMM(m_PVMMatrix, 0, m_PVMMatrix, 0, model, 0);
        Matrix.multiplyMM(m_ViewModelMatrix, 0, m_ViewMatrix, 0, m_RotationMatrix, 0);
        Matrix.multiplyMM(m_ViewModelMatrix, 0, m_ViewMatrix, 0, model, 0);
        Mat3 normal = new Mat3(m_ViewModelMatrix);
        normal.inverseTranspose();
        float[] normalMat = normal.getMat();

        m_Shader.useProgram();
        m_Shader.setMatrices(m_PVMMatrix, m_ViewModelMatrix, m_ViewMatrix, normalMat);

        int[] vaos = m_Model.getVAOs();
        IntBuffer[]  indices = m_Model.getIndices();
        for (int i = 0; i < vaos.length; ++i) {
            m_Shader.setMaterial(m_Model.getMaterial(i), 0);
            GLES30.glBindVertexArray(vaos[i]);
            GLES30.glDrawElements(GLES30.GL_TRIANGLES, indices[i].capacity(), GLES30.GL_UNSIGNED_INT, 0);
            m_Shader.restoreMaterial();
        }
    }


    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES30.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        m_Width = width;
        m_Height = height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.perspectiveM(m_ProjectionMatrix, 0, 60, ratio, 1, 100);
     }


    public void loadAssetsAndShaders(String model, String vertex, String fragment) {

        m_ModelFile = model;
        m_Model.modelFromJSON(model);

        m_FragShaderFile = fragment;
        m_VertShaderFile = vertex;
        m_Shader = new Shader("shaders/phong.vert", "shaders/phong.frag");
    }


    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }


    private void normalize(float v[]) {

        float l = (float)Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        if (l > 0.0) {
            v[0] /= l;
            v[1] /= l;
            v[2] /= l;
        }
    }


    private float dot(float a[], float b[]) {

        return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
    }


    private void cross(float a[], float b[], float res[]) {

        res[0] = a[1] * b[2] - a[2] * b[1];
        res[1] = a[2] * b[0] - a[0] * b[2];
        res[2] = a[0] * b[1] - a[1] * b[0];
    }


    private void getArcBallVec(float x, float y, float v[]) {

        v[0] = x * 2.0f / m_Width - 1;
        v[1] = -(y * 2.0f / m_Height - 1);
        v[2] = 0.0f;

        float op = v[0] * v[0] + v[1] * v[1];
        if (op < 1.0)
            v[2] = (float)Math.sqrt(1 - op);
        else
            normalize(v);
    }


    public void rotate() {

        if (m_NewX != m_PrevX || m_NewY != m_PrevY) {
            float[] va, vb, axisCamCoord, axisWorldCoord;
            float[] aux = new float[16];
            float[] aux2 = new float[16];
            va = new float[3]; vb = new float[3];
            axisCamCoord = new float[4]; axisWorldCoord = new float[4];

            getArcBallVec(m_PrevX, m_PrevY, va);
            getArcBallVec(m_NewX, m_NewY, vb);

            float angleRad = (float)Math.acos(Math.min(1.0, dot(va,vb)));
            float angleDeg = (float)Math.toDegrees(angleRad);
            cross(va,vb, axisCamCoord);
            axisCamCoord[3] = 0.0f;
            Matrix.multiplyMM(aux, 0, m_ViewMatrix, 0, m_RotationMatrix, 0);
            Matrix.invertM(aux2, 0, aux, 0);
            Matrix.multiplyMV(axisWorldCoord, 0, aux2, 0, axisCamCoord, 0);
            Matrix.rotateM(m_RotationMatrix, 0, angleDeg, axisWorldCoord[0], axisWorldCoord[1], axisWorldCoord[2]);
            m_PrevX = m_NewX;
            m_PrevY = m_NewY;
        }
    }


    public void startRotating(float x, float y) {

        m_PrevX = x;
        m_PrevY = y;
        m_NewX = x;
        m_NewY = y;
        m_Rotating = true;
    }


    public void updateRotation(float newX, float newY) {

        m_NewX = newX;
        m_NewY = newY;
    }


    public void stopRotating() {

        m_Rotating = false;
    }

}

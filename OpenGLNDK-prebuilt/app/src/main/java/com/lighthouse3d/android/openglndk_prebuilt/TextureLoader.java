package com.lighthouse3d.android.openglndk_prebuilt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ARF on 17/11/2016.
 */

public class TextureLoader {

    private static Context IOHContext;

    private TextureLoader() {
    }


    public static void SetContext(Context context) {

        IOHContext = context;
    }

    private static Bitmap CreateBitmapFromAssets(String filename) {

        Bitmap bitmap = Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888);
        InputStream istr = null;

        try {
            istr = IOHContext.getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(istr);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //Always clear and close
            try {
                if (istr != null) {
                    istr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }



    public static int CreateTextureFromAssets(String filename) {

       // Bitmap bitmap = Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_8888);
        Bitmap bitmap = CreateBitmapFromAssets(filename);
 /*       InputStream istr = null;

        try {
            istr = IOHContext.getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(istr);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //Always clear and close
            try {
                if (istr != null) {
                    istr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/
        int m_TexID[];
        m_TexID = new int[1];

        GLES30.glGenTextures(1, m_TexID, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, m_TexID[0]);

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
        return m_TexID[0];
    }


    public static int CreateCubeMapTextureFromAssets(String posX, String negX,
                                                     String posY, String negY,
                                                     String posZ, String negZ) {

        int m_TexID[];
        m_TexID = new int[1];

        Bitmap bitmapPosX = CreateBitmapFromAssets(posX);
        Bitmap bitmapNegX = CreateBitmapFromAssets(negX);
        Bitmap bitmapPosY = CreateBitmapFromAssets(posY);
        Bitmap bitmapNegY = CreateBitmapFromAssets(negY);
        Bitmap bitmapPosZ = CreateBitmapFromAssets(posZ);
        Bitmap bitmapNegZ = CreateBitmapFromAssets(negZ);

        GLES30.glGenTextures(1, m_TexID, 0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, m_TexID[0]);

        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_S,  GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_T,  GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_CUBE_MAP, GLES30.GL_TEXTURE_WRAP_R,  GLES30.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, bitmapPosX, 0);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, bitmapNegX, 0);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, bitmapPosY, 0);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, bitmapNegY, 0);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, bitmapPosZ, 0);
        GLUtils.texImage2D(GLES30.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, bitmapNegZ, 0);

        GLES30.glBindTexture(GLES30.GL_TEXTURE_CUBE_MAP, 0);

        bitmapPosX.recycle();
        bitmapNegX.recycle();
        bitmapPosY.recycle();
        bitmapNegY.recycle();
        bitmapPosZ.recycle();
        bitmapNegZ.recycle();

        return m_TexID[0];
    }

}

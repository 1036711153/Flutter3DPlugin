package com.taobao.flutter_3d_plugin.gl.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class GL3DUtils {

    public static void openAlphablend() {
        //保护现场
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_COLOR,GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_COLOR,GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }


    public static void openColorblend() {
        //保护现场
        MatrixState.pushMatrix();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFuncSeparate(
                GLES20.GL_BLEND_SRC_RGB, GLES20.GL_ONE_MINUS_SRC_COLOR,
                GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA
        );
        GLES20.glBlendEquationSeparate(GLES20.GL_FUNC_ADD, GLES20.GL_FUNC_ADD);

    }

    public static void closeColorblend() {
        GLES20.glDisable(GLES20.GL_BLEND);
        //恢复现场
        MatrixState.popMatrix();
    }


    public static void closeAlphablend() {
        GLES20.glDisable(GLES20.GL_BLEND);
        //恢复现场
        MatrixState.popMatrix();
    }


    public static FloatBuffer addArrayToFloatBuffer(float[] arrays, FloatBuffer floatBuffer) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(arrays.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        floatBuffer = vbb.asFloatBuffer();
        floatBuffer.put(arrays);
        floatBuffer.position(0);
        return floatBuffer;
    }

    public static FloatBuffer addFixArrayToFloatBuffer(float[] arrays, FloatBuffer floatBuffer) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(arrays.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        floatBuffer = vbb.asFloatBuffer();
        floatBuffer.put(arrays);
        floatBuffer.position(0);
        return floatBuffer;
    }


    /*
    通过文件加载图片
   * */
    public static int loadBitmapByFile(String filePath, Context context) {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        int textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmapTmp;
        try {
            bitmapTmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        android.opengl.GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTmp, 0);
        bitmapTmp.recycle();
        return textureId;
    }

    /*
     通过DrawableID加载图片
    * */
    public static int loadBitmapById(int drawableId, Context context) {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        int textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        Bitmap bitmapTmp = BitmapFactory.decodeResource(context.getResources(), drawableId);
        android.opengl.GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmapTmp, 0);
        bitmapTmp.recycle();
        return textureId;
    }


    //rows，行，clos，列
    public static int[] loadBitmapArrayById(int drawableId, int rows, int clos, Context context) {
        int[] textureId = new int[rows * clos];
        Bitmap bitmapTmp = BitmapFactory.decodeResource(context.getResources(), drawableId);
        int w = bitmapTmp.getWidth();
        int h = bitmapTmp.getHeight();
        int unit_w = w / clos;
        int unit_h = h / rows;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < clos; j++) {
                Bitmap bitmap = Bitmap.createBitmap(bitmapTmp, unit_w * j, unit_h * i, unit_w, unit_h);
                textureId[i * clos + j] = loadBitmapByBitmap(bitmap);
            }
        }
        return textureId;
    }


    /*
   通过DrawableID加载图片
  * */
    public static int loadBitmapByBitmap(Bitmap bitmap) {
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        int textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        android.opengl.GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        return textureId;
    }

    /*
    *  绘制文本转Bitmap
    * */
    public static Bitmap DrawTextBitMap(String text, int height, int width, int textSize, int color) {
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        newBitmap.eraseColor(0);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(color);
        StaticLayout sl = new StaticLayout(text, textPaint, newBitmap.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        canvas.translate(0, height / 10);
        sl.draw(canvas);
        return newBitmap;
    }
}

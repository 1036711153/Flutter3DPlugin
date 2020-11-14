package com.taobao.flutter_3d_plugin.gl;

import android.content.Context;
import android.opengl.GLES20;


import com.taobao.flutter_3d_plugin.gl.utils.GL3DUtils;

import javax.microedition.khronos.egl.EGL10;


public abstract class BaseRender implements GLRender{
    protected float screenHalfX;
    protected float screenHalfY;
    protected Context context;
    protected MatrixState matrixState;


    public BaseRender(Context context) {
        this.context = context;
        this.matrixState = new MatrixState();
    }


    @Override
    public void onSurfaceCreated(EGL10 egl10, int width, int height) {
        //设置屏幕背景色RGBA
        GLES20.glClearColor(1, 1, 1, 0f);

        //设置为打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        GLES20.glViewport(0, 0, width, height);
        matrixState.setMatrixFrustM(width, height);

        screenHalfX = width * 1.0f / 2;
        screenHalfY = height * 1.0f / 2;

        //初始化光源方向
        matrixState.setLightLocation(0, screenHalfY / 2, 500f);

        initUI();
    }

    protected abstract void initUI();

    protected abstract void onDraw(EGL10 egl10);


    @Override
    public void onDrawFrame(EGL10 egl10) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        matrixState.pushMatrix();
        GL3DUtils.openAlphablend();
        matrixState.pushMatrix();
        onDraw(egl10);
        matrixState.popMatrix();
        GL3DUtils.closeAlphablend();
        matrixState.popMatrix();
    }

    @Override
    public void onSurfaceDestroy(EGL10 egl10) {}
}

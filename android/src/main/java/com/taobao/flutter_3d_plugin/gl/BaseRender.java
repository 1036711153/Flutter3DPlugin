package com.taobao.flutter_3d_plugin.gl;

import android.content.Context;
import android.opengl.GLES20;


import com.taobao.flutter_3d_plugin.gl.utils.GL3DUtils;
import com.taobao.flutter_3d_plugin.gl.utils.MatrixState;

import javax.microedition.khronos.egl.EGL10;


public abstract class BaseRender implements GLRender{
    public float screenHalfX;
    public float screenHalfY;
    public Context context;

    public BaseRender(Context context) {
        this.context = context;
    }


    @Override
    public void onSurfaceCreated(EGL10 egl10, int width, int height) {
        //设置屏幕背景色RGBA
        GLES20.glClearColor(1, 1, 1, 0f);

        //设置为打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        GLES20.glViewport(0, 0, width, height);
        MatrixState.setMatrixFrustM(width, height);

        screenHalfX = width * 1.0f / 2;
        screenHalfY = height * 1.0f / 2;

        //初始化光源方向
        MatrixState.setLightLocation(0, screenHalfY / 2, 500f);

        initUI();
    }

    protected abstract void initUI();

    protected abstract void onDraw(EGL10 egl10);


    @Override
    public boolean onDrawFrame(EGL10 egl10) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GL3DUtils.openAlphablend();
        MatrixState.pushMatrix();
        onDraw(egl10);
        MatrixState.popMatrix();
        GL3DUtils.closeAlphablend();
        return true;
    }

    @Override
    public void onSurfaceDestroy(EGL10 egl10) {}
}

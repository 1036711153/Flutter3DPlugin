package com.taobao.flutter_3d_plugin.demo;


import android.content.Context;
import android.opengl.GLES20;

import com.taobao.flutter_3d_plugin.gl.BaseRender;

import java.util.Random;

import javax.microedition.khronos.egl.EGL10;

public class Demo3DRender extends BaseRender {

    public Demo3DRender(Context context) {
        super(context);
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void onDraw(EGL10 egl10) {
        GLES20.glClearColor(new Random().nextFloat(), 1, 1, 0f);
    }
}

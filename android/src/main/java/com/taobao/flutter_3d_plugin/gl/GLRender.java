package com.taobao.flutter_3d_plugin.gl;


import javax.microedition.khronos.egl.EGL10;

public interface GLRender {
    void onSurfaceCreated(EGL10 egl10, int width, int height);

    void onDrawFrame(EGL10 egl10);

    void onSurfaceDestroy(EGL10 egl10);

}

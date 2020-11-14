package com.taobao.flutter_3d_plugin.gl;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import io.flutter.view.TextureRegistry;


public class GLThread implements Runnable {

    private static final String LOG_TAG = "GLThread";

    public TextureRegistry.SurfaceTextureEntry entry;
    //纹理
    private SurfaceTexture texture;
    //渲染回调
    private GLRender render;
    //宽
    private int width;
    //高
    private int height;

    //线程是否运行
    private volatile boolean isRunning = false;

    private int fps;

    public GLThread(TextureRegistry.SurfaceTextureEntry entry,
                    SurfaceTexture texture, GLRender render,
                    int width, int height, int fps) {
        this.entry = entry;
        this.texture = texture;
        this.render = render;
        this.width = width;
        this.height = height;
        this.fps = fps;
    }

    private EGL10 egl;
    private EGLDisplay eglDisplay;
    private EGLContext eglContext;
    private EGLSurface eglSurface;

    @Override
    public void run() {
        initEGL();
        render.onSurfaceCreated(egl, width, height);
        while (isRunning) {
            long startTime = System.currentTimeMillis();
            if (render.onDrawFrame(egl)) {
                if (!egl.eglSwapBuffers(eglDisplay, eglSurface)) {
                    Log.d(LOG_TAG, egl.eglGetError() + "");
                }
            }
            long waitTime = (long) (1000 * 1.0f / fps - (System.currentTimeMillis() - startTime));
            if (waitTime > 0) {
                SystemClock.sleep(waitTime);
            }
        }
        render.onSurfaceDestroy(egl);
        destroyEGL();
    }

    /**
     * 配置相关
     *
     * @return
     */
    private EGLConfig chooseEglConfig() {
        int attribs[] = {
                EGL10.EGL_LEVEL, 0,
                EGL10.EGL_RENDERABLE_TYPE, 4,  // EGL_OPENGL_ES2_BIT
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_DEPTH_SIZE, 16,
                EGL10.EGL_SAMPLE_BUFFERS, 1,
                EGL10.EGL_SAMPLES, 4,  // 在这里修改MSAA的倍数，4就是4xMSAA，再往上开程序可能会崩
                EGL10.EGL_NONE,
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] configCounts = new int[1];
        egl.eglChooseConfig(eglDisplay, attribs, configs, 1, configCounts);

        if (configCounts[0] == 0) {
            // Failed! Error handling.
            return null;
        } else {
            return configs[0];
        }
    }

    /**
     * 在当前线程初始化
     */
    private void initEGL() {
        egl = (EGL10) EGLContext.getEGL();
        eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl.eglInitialize(eglDisplay, new int[2]);
        EGLConfig eglConfig = chooseEglConfig();
        //纹理上下文获取并创建纹理窗口
        int[] attributeList = {EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};
        eglContext = egl.eglCreateContext(eglDisplay, eglConfig, EGL10.EGL_NO_CONTEXT, attributeList);
        eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, texture, null);
        egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext);
    }

    /**
     * 当前线程释放资源
     */
    private void destroyEGL() {
        egl.eglMakeCurrent(eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(eglDisplay, eglSurface);
        egl.eglDestroyContext(eglDisplay, eglContext);
        egl.eglTerminate(eglDisplay);
    }

    /**
     * GLThread启动渲染方法
     *
     * @param textureId
     */
    public void start(long textureId) {
        if (!isRunning) {
            isRunning = true;
            Thread thread = new Thread(this);
            thread.setName("GLThread-" + textureId);
            thread.start();
        } else {
            throw new RuntimeException("Current GLThread " + Thread.currentThread().getName() + " is running");
        }

    }

    /**
     * 释放回收线程方法
     */
    public void dispose() {
        isRunning = false;
    }

}

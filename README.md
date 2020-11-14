# flutter_3d_plugin

### 1.背景

目前Flutter其实都是通过skia渲染引擎，对应构建一些3D场景都是有一些不方便，根据官方介绍文档也不支持OpenGL ES或者Vulkan相关API的直接调用，因此如果想构建3D场景需要自行想办法去构建；

![image-20201114194947576](/Users/houjiawei/Library/Application Support/typora-user-images/image-20201114194947576.png)

### 2.分析

其实在闲鱼之前的《万万没想到——flutter这样外接纹理]》[https://juejin.im/post/6844903662548942855]文章中就介绍了一种外接纹理解决方案，可以将openGL ES通过外接纹理接入进来就可以构建3D场景；在后续官方推荐的外接纹理解决方案中有了一种新方式，比如Android如下代码通过createSurfaceTexture像FlutterJNI注册纹理，然后就生成对应的纹理id，返回到Flutter就可以根据textureId场景Widget;

```
  @Override
  public TextureRegistry.SurfaceTextureEntry createSurfaceTexture() {
    final SurfaceTexture surfaceTexture = new SurfaceTexture(0);
    surfaceTexture.detachFromGLContext();
    final SurfaceTextureRegistryEntry entry =
        new SurfaceTextureRegistryEntry(nextTextureId.getAndIncrement(), surfaceTexture);
    mNativeView.getFlutterJNI().registerTexture(entry.id(), surfaceTexture);
    return entry;
  }
```

于是乎我们可以通过如下方式就拿到了textureId了：

```
TextureRegistry.SurfaceTextureEntry surfaceTextureEntry =
                    flutterPluginBinding.getTextureRegistry().createSurfaceTexture();
            SurfaceTexture surfaceTexture = surfaceTextureEntry.surfaceTexture();
            surfaceTexture.setDefaultBufferSize(covertWidth, covertHeight);
            result.success(surfaceTextureEntry.id());
```



### 3.实现方案

Android端侧构建3D场景主要通过GLSurfaceView或者TextureView去构建，这里分析GLSurfaceView相关代码，

GLSurfaceView有如下关键代码：

```
 public void setRenderer(Renderer renderer) {
        ...
        if (mEGLWindowSurfaceFactory == null) {
            mEGLWindowSurfaceFactory = new DefaultWindowSurfaceFactory();
        }
        mRenderer = renderer;
        mGLThread = new GLThread(mThisWeakRef);
        mGLThread.start();
    }
```

可以发现，其实GLSurfaceView就是创建了一个GLThread,这个GLThread里面去初始化GL ES相关逻辑，比如获取纹理上下文的createContext以及关联视图窗口的eglCreateWindowSurface，完全可以自己构建相同的GLThread去回调GLSurfaceView的3个方法：

```
    void onSurfaceCreated(EGL10 egl10, int width, int height);

    boolean onDrawFrame(EGL10 egl10);

    void onSurfaceDestroy(EGL10 egl10);
```

关键代码如下：

```
 public void run() {
        //初始化GL
        initGL();
        //渲染回调onSurfaceCreated
        render.onSurfaceCreated(egl, width, height);
        //循环不退出
        while (isRunning) {
            long startTime = System.currentTimeMillis();
            //渲染回调onDrawFrame
            render.onDrawFrame(egl);
            //交换buffer上屏数据
            egl.eglSwapBuffers(eglDisplay, eglSurface);
            //根据fps适当休眠
            long waitTime = (long) (1000 * 1.0f / fps - (System.currentTimeMillis() - startTime));
            if (waitTime > 0) {
                SystemClock.sleep(waitTime);
            }
        }
        //渲染回调onSurfaceDestroy
        render.onSurfaceDestroy(egl);
        //回收GL
        destroyGL();
    }
```



### 4.代码实践

当有了上面3个生命周期的回调后，就可以根据openGL ES相关的逻辑去构建场景，然后将textureId返回给Flutter显示即可，如下3D的圆柱体是通过此方式构建的：

<video src="/Users/houjiawei/Desktop/Screenrecorder-2020-11-14-20-20-24-890.mp4" height="500"></video>

因为有2个Flutter中Texure的Widget,因此需要构建2个互相隔离的3D场景，需要我们去维护一个map去管理各自对应的texureId和GLThread，如下代码，在Widget的init/dispose时候分别调用addTexture/removeTexture,保证线程数据隔离，相互状态隔离；

```
private Map<Long, GLThread> map = new ConcurrentHashMap<>();

    public void addTexture(long textureId, GLThread glThread) {
        map.put(textureId, glThread);
        glThread.start(textureId);
    }

    public void removeTexture(long textureId) {
        GLThread glThread = map.remove(textureId);
        if (glThread != null) {
            glThread.dispose();
            glThread.entry.release();
        } else {
            Log.e(TAG, "textureId " + textureId + " has remove");
        }
    }
```



### 5.小结和展望

构建3D场景不是初衷，只是在Android中surfaceView/GLSurfaceView/TextureView在不同场景下发挥着各种不同的作用，但截止目前为止，Flutter只有额外的自定义View的canvas绘制方式，未在更高性能的绘制上做进一步尝试。通过本文介绍的一种方案，如果某一天Flutter中嵌入一些3D模型动画时候，就完全可以借助官方外接纹理+自定义GLThread去实现GLSurfaceView/TextureView相同的效果；






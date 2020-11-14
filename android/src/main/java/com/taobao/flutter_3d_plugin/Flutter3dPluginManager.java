package com.taobao.flutter_3d_plugin;

import android.util.Log;

import com.taobao.flutter_3d_plugin.gl.GLThread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Flutter3dPluginManager {

    private static final String TAG = "Flutter3dPluginManager";

    private Map<Long, GLThread> map = new ConcurrentHashMap<>();

    private static Flutter3dPluginManager sManager = new Flutter3dPluginManager();

    public static Flutter3dPluginManager getInstance() {
        return sManager;
    }

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

}

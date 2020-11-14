package com.taobao.flutter_3d_plugin;

import android.content.Context;
import android.graphics.SurfaceTexture;

import androidx.annotation.NonNull;

import com.taobao.flutter_3d_plugin.demo.Demo3DRender;
import com.taobao.flutter_3d_plugin.gl.GLThread;
import com.taobao.flutter_3d_plugin.gl.utils.ScreenUtils;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.TextureRegistry;

/**
 * Flutter3dPlugin
 */
public class Flutter3dPlugin implements FlutterPlugin, MethodCallHandler {
    private static final String TAG = "Flutter3dPlugin";

    private FlutterPluginBinding flutterPluginBinding;

    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_3d_plugin");
        channel.setMethodCallHandler(this);
        this.flutterPluginBinding = flutterPluginBinding;
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_3d_plugin");
        channel.setMethodCallHandler(new Flutter3dPlugin());
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("init")) {
            Map<String, Object> arguments = (Map<String, Object>) call.arguments;
            double width = (double) arguments.get("width");
            double height = (double) arguments.get("height");
            int fps = (int) arguments.get("fps");

            Context context = flutterPluginBinding.getApplicationContext();

            int covertWidth = ScreenUtils.dip2px(context, width);
            int covertHeight = ScreenUtils.dip2px(context, height);

            TextureRegistry.SurfaceTextureEntry surfaceTextureEntry =
                    flutterPluginBinding.getTextureRegistry().createSurfaceTexture();
            SurfaceTexture surfaceTexture = surfaceTextureEntry.surfaceTexture();
            surfaceTexture.setDefaultBufferSize(covertWidth, covertHeight);

            GLThread thread = new GLThread(surfaceTextureEntry,
                    surfaceTexture, new Demo3DRender(context),
                    covertWidth, covertHeight, fps);

            Flutter3dPluginManager.getInstance().addTexture(surfaceTextureEntry.id(), thread);
            result.success(surfaceTextureEntry.id());
        } else if (call.method.equals("dispose")) {
            Map<String, Object> arguments = (Map<String, Object>) call.arguments;
            long textureId = Long.parseLong(arguments.get("textureId").toString());
            Flutter3dPluginManager.getInstance().removeTexture(textureId);
            result.success(true);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}

package com.taobao.flutter_3d_plugin.gl.utils;

import android.content.Context;

public class ScreenUtils {
    public static int dip2px(Context context, double dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

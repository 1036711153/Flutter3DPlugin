package com.taobao.flutter_3d_plugin.gl.utils;

import android.graphics.Color;


public class ColorUtils {

    public static final float COLOR_SIZE_UNIT = 1.0f / 255;

    /**
     * Color的Int整型转Color的rgb数组
     * colorInt - -FF12590395
     * return Color的rgb数组 —— [63/255,226/255,197/255,1F]
     */
    public static float[] int2Rgba(int colorInt) {
        float[] rgb = new float[]{0, 0, 0, 0};

        int red = Color.red(colorInt);
        int green = Color.green(colorInt);
        int blue = Color.blue(colorInt);
        int alpha = Color.alpha(colorInt);

        rgb[0] = red * COLOR_SIZE_UNIT;
        rgb[1] = green * COLOR_SIZE_UNIT;
        rgb[2] = blue * COLOR_SIZE_UNIT;
        rgb[3] = alpha * COLOR_SIZE_UNIT;
        return rgb;
    }

}

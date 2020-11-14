package com.taobao.flutter_3d_plugin.demo;


import android.content.Context;
import android.opengl.GLES20;

import com.taobao.flutter_3d_plugin.demo.cell.HistogramUI;
import com.taobao.flutter_3d_plugin.demo.cell.model.Angle;
import com.taobao.flutter_3d_plugin.demo.cell.model.ColorConstant;
import com.taobao.flutter_3d_plugin.demo.cell.model.HistogramGrowthThread;
import com.taobao.flutter_3d_plugin.gl.BaseRender;

import java.util.Random;

import javax.microedition.khronos.egl.EGL10;

public class Demo3DRender extends BaseRender {

    private HistogramUI[] histogramUI = new HistogramUI[4];
    private Angle angle = new Angle(0f);
    private float[] bgColor;


    public Demo3DRender(Context context) {
        super(context);
    }

    @Override
    protected void initUI() {
        histogramUI[0] = new HistogramUI(context, matrixState,
                ColorConstant.PURPLE_COLOR, new float[]{0, 0}, 90, 0.8f * screenHalfY * 0.15f * 1.5f, 15, angle);
        histogramUI[1] = new HistogramUI(context, matrixState,
                ColorConstant.BLUE_COLOR, new float[]{0, 0}, 90, 0.8f * screenHalfY * 0.32f * 1.5f, 32, angle);
        histogramUI[2] = new HistogramUI(context, matrixState,
                ColorConstant.DEEP_PURPLE_COLOR, new float[]{0, 0}, 90, 0.8f * screenHalfY * 0.59f * 1.5f, 59, angle);
        histogramUI[3] = new HistogramUI(context, matrixState,
                ColorConstant.DEEP_BLUE_COLOR, new float[]{0, 0}, 90, 0.8f * screenHalfY * 0.84f * 1.5f, 84, angle);
//        bgColor = new float[]{new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 1f};
        bgColor = ColorConstant.BG_COLOR;
        new HistogramGrowthThread(histogramUI).start();

    }

    @Override
    protected void onDraw(EGL10 egl10) {
        //设置背景色
        GLES20.glClearColor(bgColor[0], bgColor[1], bgColor[2], bgColor[3]);
        angle.angle += 0.2f;
        for (int i = 0; i < histogramUI.length; i++) {
            matrixState.pushMatrix();
            matrixState.translate(2 * screenHalfX / 4.5f * (i + 1) - screenHalfX * 1.1f, screenHalfY * 0.7f, 0);
            matrixState.rotate(70, 1, 0, 0);
            histogramUI[i].draw();
            matrixState.popMatrix();
        }
    }
}

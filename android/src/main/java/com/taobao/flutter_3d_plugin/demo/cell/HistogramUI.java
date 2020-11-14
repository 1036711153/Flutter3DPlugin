package com.taobao.flutter_3d_plugin.demo.cell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.taobao.flutter_3d_plugin.demo.cell.model.Angle;
import com.taobao.flutter_3d_plugin.demo.cell.model.Particle;
import com.taobao.flutter_3d_plugin.gl.MatrixState;
import com.taobao.flutter_3d_plugin.gl.utils.GL3DUtils;

import java.util.ArrayList;
import java.util.List;


public class HistogramUI {
    public BaseTextUI textUI;
    private CoilUI coilUI;
    private ParticlesUI particlesUI;
    public CylinederUI cylinederUI;
    private List<Particle> particles = new ArrayList<>();

    private int textId;
    public float h;
    public MatrixState matrixState;


    public HistogramUI(Context context, MatrixState matrixState, float[] color, float[] centre, float r, float h, float percent, Angle angle) {
        this.coilUI = new CoilUI(context, matrixState, color, centre, r, angle);
        initParticles(100, r);
        particlesUI = new ParticlesUI(context, matrixState, particles, 1, color, angle);
        cylinederUI = new CylinederUI(context, matrixState, centre, r - 40, h, 100, color);
        textUI = new BaseTextUI(context, matrixState, new float[]{centre[0], -(h + 30), 0}, 60, 30, 1);
        textId = initTextBitmap(context, percent + "%");
        this.h = h;
        this.matrixState = matrixState;
    }


    private int initTextBitmap(Context context, String drawText) {
        Bitmap bm = GL3DUtils.DrawTextBitMap(drawText, 40, 80, 20, Color.WHITE);
        int texId = GL3DUtils.loadBitmapByBitmap(bm);
        if (!bm.isRecycled()) {
            bm.recycle();
        }
        return texId;
    }

    private void initParticles(int count, float r) {
        for (int i = 0; i < count; i++) {
            Particle particle = Particle.CreateSprayedParticle(r - 30);
            particles.add(particle);
        }
    }

    public void draw() {
        matrixState.pushMatrix();
        coilUI.drawSelf(-1);
        matrixState.popMatrix();

        matrixState.pushMatrix();
        particlesUI.drawSelf(-1);
        matrixState.popMatrix();

        matrixState.pushMatrix();
        cylinederUI.draw();
        matrixState.popMatrix();


        matrixState.pushMatrix();
        matrixState.rotate(-70, 1, 0, 0);
        textUI.drawSelf(textId);
        matrixState.popMatrix();
    }
}

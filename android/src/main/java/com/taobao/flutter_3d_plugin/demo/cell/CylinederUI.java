package com.taobao.flutter_3d_plugin.demo.cell;


import android.content.Context;

import com.taobao.flutter_3d_plugin.gl.MatrixState;

public class CylinederUI {
    public CylinderSideUI cylinderSideUI;
    public CircleUI circleUI;
    private MatrixState matrixState;

    public CylinederUI(Context context, MatrixState state, float[] centre, float r,
                       float h, int n, float[] color) {
        this.matrixState = state;
        cylinderSideUI = new CylinderSideUI(context, state, color, r, h, n);
        circleUI = new CircleUI(context, state, color, centre, r, h);
    }

    public void draw() {
        matrixState.pushMatrix();
        cylinderSideUI.drawSelf(-1);
        circleUI.drawSelf(-1);
        matrixState.popMatrix();
    }

}

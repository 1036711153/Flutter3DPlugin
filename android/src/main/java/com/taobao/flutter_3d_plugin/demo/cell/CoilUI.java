package com.taobao.flutter_3d_plugin.demo.cell;

import android.content.Context;
import android.opengl.GLES20;

import com.taobao.flutter_3d_plugin.demo.cell.model.Angle;
import com.taobao.flutter_3d_plugin.gl.MatrixState;
import com.taobao.flutter_3d_plugin.gl.utils.GL3DUtils;

import java.nio.FloatBuffer;


public class CoilUI extends BaseUI {
    private static final String VERTEX = "coil/coil.vertex.glsl";
    private static final String FRAF = "coil/coil.frag.glsl";
    int maColorHandler;
    int muCentreRrHandler;
    FloatBuffer mColorBuffer;
    private float[] centre;
    private float r;
    Angle angle;
    private float color[];

    public CoilUI(Context context, MatrixState matrixState, float[] color, float centre[], float r, Angle angle) {
        super(context, matrixState, VERTEX, FRAF);
        this.color = color;
        this.centre = centre;
        this.r = r;
        this.angle = angle;
        initVertex();
    }


    @Override
    public void initVertex() {
        super.initVertex();
        float vertices[] = new float[]
                {
                        -r + centre[0], -r + centre[1], 0,
                        -r + centre[0], r + centre[1], 0,
                        r + centre[0], -r + centre[1], 0,
                        -r + centre[0], r + centre[1], 0,
                        r + centre[0], r + centre[1], 0,
                        r + centre[0], -r + centre[1], 0
                };
        count = vertices.length / 3;
        vertexBuffer = GL3DUtils.addArrayToFloatBuffer(vertices, vertexBuffer);
        float[] colors = new float[]{
                color[0], color[1], color[2], color[3],
                0, 0, 0, 0,
                0, 0, 0, 0,

                0, 0, 0, 0,
                color[0], color[1], color[2], color[3],
                0, 0, 0, 0,
        };
        mColorBuffer = GL3DUtils.addArrayToFloatBuffer(colors, mColorBuffer);
    }

    @Override
    public void loadAttrs() {
        super.loadAttrs();
        maColorHandler = GLES20.glGetAttribLocation(program, "aColor");
        muCentreRrHandler = GLES20.glGetUniformLocation(program, "centreRr");
    }


    @Override
    public void drawSelf(int texid) {
        matrixState.rotate(angle.angle, 0, 0, 1);
        super.drawSelf(texid);
        GLES20.glVertexAttribPointer(
                maColorHandler,
                4,
                GLES20.GL_FLOAT,
                false,
                4 * 4,
                mColorBuffer
        );
        GLES20.glEnableVertexAttribArray(maColorHandler);
        GLES20.glUniform4fv(muCentreRrHandler, 1, new float[]{centre[0], centre[1], r, r - 8}, 0);
        DrawArray();
    }
}

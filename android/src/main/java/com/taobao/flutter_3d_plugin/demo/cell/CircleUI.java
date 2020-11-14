package com.taobao.flutter_3d_plugin.demo.cell;

import android.content.Context;
import android.opengl.GLES20;

import com.taobao.flutter_3d_plugin.gl.MatrixState;
import com.taobao.flutter_3d_plugin.gl.utils.GL3DUtils;


public class CircleUI extends BaseUI {
    private static final String VERTEX = "circle/textrect.vertex.glsl";
    private static final String FRAF = "circle/textrect.frag.glsl";
    int uCentreRHandler;
    int uColorHandler;
    int uMMatrixHandle;//位置、旋转变换矩阵引用
    int aLightLocationHandle;
    int uCameraHandle; //摄像机位置属性引用
    int uNormalHandle;
    private float[] centre;
    private float r;
    public float h;
    private float color[];

    public CircleUI(Context context, MatrixState matrixState, float[] color, float centre[], float r, float h) {
        super(context, matrixState, VERTEX, FRAF);
        this.centre = centre;
        this.r = r;
        this.h = h;
        this.color = color;
        initVertex();
    }


    @Override
    public void initVertex() {
        super.initVertex();
        float vertices[] = new float[]
                {
                        -r + centre[0], -r + centre[1], h,
                        -r + centre[0], r + centre[1], h,
                        r + centre[0], -r + centre[1], h,
                        -r + centre[0], r + centre[1], h,
                        r + centre[0], r + centre[1], h,
                        r + centre[0], -r + centre[1], h
                };
        count = vertices.length / 3;
        vertexBuffer = GL3DUtils.addArrayToFloatBuffer(vertices, vertexBuffer);
    }

    @Override
    public void loadAttrs() {
        super.loadAttrs();
        uCentreRHandler = GLES20.glGetUniformLocation(program, "centreR");
        uColorHandler = GLES20.glGetUniformLocation(program, "uColor");

        //获取程序中顶点法向量属性引用id
        uNormalHandle = GLES20.glGetUniformLocation(program, "uNormal");
        //获取位置、旋转变换矩阵引用
        uMMatrixHandle = GLES20.glGetUniformLocation(program, "uMMatrix");
        //获取程序中光源位置引用id
        aLightLocationHandle = GLES20.glGetUniformLocation(program, "uLightLocation");
        //获取程序中摄像机位置引用
        uCameraHandle = GLES20.glGetUniformLocation(program, "uCamera");
    }


    @Override
    public void drawSelf(int texid) {
        initVertex();
        super.drawSelf(texid);
        //将位置、旋转变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(uMMatrixHandle, 1, false, matrixState.getMMatrix(), 0);
        //将光源方向传入着色器程序
        GLES20.glUniform3fv(aLightLocationHandle, 1, matrixState.lightPositionFB);
        //将摄像机位置传入着色器程序
        GLES20.glUniform3fv(uCameraHandle, 1, matrixState.cameraFB);

        GLES20.glUniform3fv(uNormalHandle, 1, new float[]{0, 0, 1}, 0);
        GLES20.glUniform4fv(uColorHandler, 1, color, 0);
        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texid);
        GLES20.glUniform3fv(uCentreRHandler, 1, new float[]{centre[0], centre[1], r}, 0);
        DrawArray();
    }
}

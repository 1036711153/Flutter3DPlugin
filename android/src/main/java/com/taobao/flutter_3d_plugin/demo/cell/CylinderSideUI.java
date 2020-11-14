package com.taobao.flutter_3d_plugin.demo.cell;

import android.content.Context;
import android.opengl.GLES20;

import com.taobao.flutter_3d_plugin.gl.MatrixState;
import com.taobao.flutter_3d_plugin.gl.utils.GL3DUtils;
import java.nio.FloatBuffer;


public class CylinderSideUI extends BaseUI {
    private static final String VERTEX = "cylineder/cylinederside.vertex.glsl";
    private static final String FRAF = "cylineder/cylinederside.frag.glsl";
    private float r;
    public float h;
    private int n;
    int muColorHandler;
    int muMMatrixHandle;//位置、旋转变换矩阵引用
    int maLightLocationHandle;
    int muCameraHandle; //摄像机位置属性引用
    int maNormalHandle;
    FloatBuffer mNormalBuffer;
    float[] color;

    public CylinderSideUI(Context context, MatrixState matrixState, float[] color, float r, float h, int n) {
        super(context, matrixState, VERTEX, FRAF);
        this.r = r;
        this.h = h;
        this.n = n;
        this.color = color;
        initVertex();
    }

    @Override
    public void initVertex() {
        super.initVertex();
        float angdegSpan = 1;
        count = 3 * 360 * 4;//顶点个数，共有3*n*4个三角形，每个三角形都有三个顶点
        //坐标数据初始化
        float[] vertices = new float[count * 3];
        float[] textures = new float[count * 2];//顶点纹理S、T坐标值数组

        //坐标数据初始化
        int count = 0;

        for (float angdeg = 0; angdeg < 360; angdeg += angdegSpan)//侧面
        {
            double angrad = Math.toRadians(angdeg);//当前弧度
            double angradNext = Math.toRadians(angdeg + angdegSpan);//下一弧度
            //底圆当前点---0
            vertices[count++] = (float) (-r * Math.sin(angrad));
            vertices[count++] = (float) (-r * Math.cos(angrad));
            vertices[count++] = 0;

            //顶圆下一点---3
            vertices[count++] = (float) (-r * Math.sin(angradNext));
            vertices[count++] = (float) (-r * Math.cos(angradNext));
            vertices[count++] = h;

            //顶圆当前点---2
            vertices[count++] = (float) (-r * Math.sin(angrad));
            vertices[count++] = (float) (-r * Math.cos(angrad));
            vertices[count++] = h;

            //底圆当前点---0
            vertices[count++] = (float) (-r * Math.sin(angrad));
            vertices[count++] = (float) (-r * Math.cos(angrad));
            vertices[count++] = 0;

            //底圆下一点---1
            vertices[count++] = (float) (-r * Math.sin(angradNext));
            vertices[count++] = (float) (-r * Math.cos(angradNext));
            vertices[count++] = 0;

            //顶圆下一点---3
            vertices[count++] = (float) (-r * Math.sin(angradNext));
            vertices[count++] = (float) (-r * Math.cos(angradNext));
            vertices[count++] = h;
        }
        vertexBuffer = GL3DUtils.addArrayToFloatBuffer(vertices, vertexBuffer);
        //法向量数据初始化
        float[] normals = new float[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            if (i % 3 == 1) {
                normals[i] = 0;
            } else {
                normals[i] = vertices[i];
            }
        }
        mNormalBuffer = GL3DUtils.addArrayToFloatBuffer(normals, mNormalBuffer);

    }


    @Override
    public void loadAttrs() {

        super.loadAttrs();
        muColorHandler = GLES20.glGetUniformLocation(program, "uColor");
        //获取程序中顶点法向量属性引用id
        maNormalHandle = GLES20.glGetAttribLocation(program, "aNormal");
        //获取位置、旋转变换矩阵引用
        muMMatrixHandle = GLES20.glGetUniformLocation(program, "uMMatrix");
        //获取程序中光源位置引用id
        maLightLocationHandle = GLES20.glGetUniformLocation(program, "uLightLocation");
        //获取程序中摄像机位置引用
        muCameraHandle = GLES20.glGetUniformLocation(program, "uCamera");
    }


    @Override
    public void drawSelf(int texId) {
        initVertex();
        super.drawSelf(texId);
        //传送顶点法向量数据
        GLES20.glVertexAttribPointer
                (
                        maNormalHandle,
                        3,
                        GLES20.GL_FLOAT,
                        false,
                        3 * 4,
                        mNormalBuffer
                );
        //启用顶点法向量数据
        GLES20.glEnableVertexAttribArray(maNormalHandle);
        //将位置、旋转变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, matrixState.getMMatrix(), 0);
        //将光源方向传入着色器程序
        GLES20.glUniform3fv(maLightLocationHandle, 1, matrixState.lightPositionFB);
        //将摄像机位置传入着色器程序
        GLES20.glUniform3fv(muCameraHandle, 1, matrixState.cameraFB);
        GLES20.glUniform4fv(muColorHandler, 1, color, 0);
        DrawArray();
    }
}

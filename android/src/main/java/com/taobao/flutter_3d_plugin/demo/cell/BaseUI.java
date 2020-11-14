package com.taobao.flutter_3d_plugin.demo.cell;

import android.content.Context;
import android.opengl.GLES20;


import com.taobao.flutter_3d_plugin.gl.MatrixState;
import com.taobao.flutter_3d_plugin.gl.utils.ShaderUtil;

import java.nio.FloatBuffer;


public class BaseUI {
    public int program;
    public int uMVPMatrixHandle;//mvp矩阵id
    public int aPositionHandle;//顶点位置id
    public String vertexShader;//顶点着色器
    public String fragmentShader;//片元着色器
    public FloatBuffer vertexBuffer;
    public int count;//顶点数量
    public MatrixState matrixState;//变化矩阵


    public BaseUI(Context context, MatrixState matrixState, String vertexFilePath, String fragFilePath) {
        this.matrixState = matrixState;
        initShader(context, vertexFilePath, fragFilePath);
        loadAttrs();
    }

    //初始化顶点
    public void initVertex() {

    }

    /*
     *添加自己的属性值
     */
    public void loadAttrs() {

    }

    //初始化shader
    public void initShader(Context context, String vertexFilePath, String fragFilePath) {
        vertexShader = ShaderUtil.loadFromAssetsFile(vertexFilePath, context.getResources());
        fragmentShader = ShaderUtil.loadFromAssetsFile(fragFilePath, context.getResources());
        program = ShaderUtil.createProgram(vertexShader, fragmentShader);
        aPositionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        uMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
    }


    public void drawSelf(int texid) {
        GLES20.glUseProgram(program);
        GLES20.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, matrixState.getFinalMatrix(), 0);
        GLES20.glVertexAttribPointer(
                aPositionHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                3 * 4,
                vertexBuffer
        );
        GLES20.glEnableVertexAttribArray(aPositionHandle);
    }


    public void DrawArray() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, count);
    }


}

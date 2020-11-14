package com.taobao.flutter_3d_plugin.gl.utils;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


public class MatrixState {

    private static float[] mProjMatrix = new float[16];
    private static float[] mVMatrix = new float[16];
    private static float[] currMatrix;
    static float[][] mStack = new float[10][16];
    static int stackTop = -1;

    public static void setInitStack() {
        currMatrix = new float[16];
        Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
    }

    public static void pushMatrix() {
        stackTop++;
        for (int i = 0; i < 16; i++) {
            mStack[stackTop][i] = currMatrix[i];
        }
    }

    public static void popMatrix() {
        for (int i = 0; i < 16; i++) {
            currMatrix[i] = mStack[stackTop][i];
        }
        stackTop--;
    }

    public static void translate(float x, float y, float z)//设置沿xyz轴移动
    {
        Matrix.translateM(currMatrix, 0, x, y, z);
    }

    public static void rotate(float angle, int x, int y, int z) {
        Matrix.rotateM(currMatrix, 0, angle, x, y, z);
    }


    public static void scale(float x, float y, float z) {
        Matrix.scaleM(currMatrix, 0, x, y, z);
    }


    static ByteBuffer llbb = ByteBuffer.allocateDirect(3 * 4);
    static float[] cameraLocation = new float[3];//摄像机位置

    public static void setCamera
            (
                    float cx,    //摄像机位置x
                    float cy,   //摄像机位置y
                    float cz,   //摄像机位置z
                    float tx,   //摄像机目标点x
                    float ty,   //摄像机目标点y
                    float tz,   //摄像机目标点z
                    float upx,  //摄像机UP向量X分量
                    float upy,  //摄像机UP向量Y分量
                    float upz   //摄像机UP向量Z分量
            ) {
        Matrix.setLookAtM
                (
                        mVMatrix,
                        0,
                        cx,
                        cy,
                        cz,
                        tx,
                        ty,
                        tz,
                        upx,
                        upy,
                        upz
                );

        cameraLocation[0] = cx;
        cameraLocation[1] = cy;
        cameraLocation[2] = cz;

        llbb.clear();
        llbb.order(ByteOrder.nativeOrder());//设置字节顺序
        cameraFB = llbb.asFloatBuffer();
        cameraFB.put(cameraLocation);
        cameraFB.position(0);
    }


    //设置透视投影参数
    public static void setProjectFrustum
    (
            float left,        //near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,        //near面距离
            float far       //far面距离
    ) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    //设置正交投影参数
    public static void setProjectOrtho
    (
            float left,        //near面的left
            float right,    //near面的right
            float bottom,   //near面的bottom
            float top,      //near面的top
            float near,        //near面距离
            float far       //far面距离
    ) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    //获取具体物体的总变换矩阵
    static float[] mMVPMatrix = new float[16];

    public static float[] getFinalMatrix() {
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    //获取具体物体的变换矩阵
    public static float[] getMMatrix() {
        return currMatrix;
    }


    /*设置屏幕与世界坐标系1：1对应
     * */
    public static void setMatrixFrustM(int width, int height) {
        setInitStack();
        int distance = Math.max(width, height) * 10;
        MatrixState.setProjectFrustum(
                -width / 4, width / 4,
                height / 4, -height / 4
                , distance * 0.5f, distance * 1.5f
        );
        MatrixState.setCamera(0, 0, 0, 0, 0, -1, 0, 1, 0);
        MatrixState.translate(0, 0, -distance);
    }

    public static float[] lightLocation = new float[]{0, 0, 0};//定位光光源位置
    public static FloatBuffer lightPositionFB;
    public static float[] lightDirection = new float[]{0, 0, 1};//定向光光源方向
    public static FloatBuffer lightDirectionFB;
    public static FloatBuffer cameraFB;

    //设置灯光位置的方法
    static ByteBuffer llbbL = ByteBuffer.allocateDirect(3 * 4);

    //设置灯光方向的方法
    public static void setLightDirection(float x, float y, float z) {
        llbbL.clear();
        lightDirection[0] = x;
        lightDirection[1] = y;
        lightDirection[2] = z;
        llbbL.order(ByteOrder.nativeOrder());//设置字节顺序
        lightDirectionFB = llbbL.asFloatBuffer();
        lightDirectionFB.put(lightDirection);
        lightDirectionFB.position(0);
    }

    public static void setLightLocation(float x, float y, float z) {
        llbbL.clear();

        lightLocation[0] = x;
        lightLocation[1] = y;
        lightLocation[2] = z;

        llbbL.order(ByteOrder.nativeOrder());//设置字节顺序
        lightPositionFB = llbbL.asFloatBuffer();
        lightPositionFB.put(lightLocation);
        lightPositionFB.position(0);
    }
}

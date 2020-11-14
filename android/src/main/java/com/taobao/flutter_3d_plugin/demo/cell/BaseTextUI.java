package com.taobao.flutter_3d_plugin.demo.cell;

import android.content.Context;
import android.opengl.GLES20;

import com.taobao.flutter_3d_plugin.gl.MatrixState;
import com.taobao.flutter_3d_plugin.gl.utils.GL3DUtils;

import java.nio.FloatBuffer;

public class BaseTextUI extends BaseUI {

    public static final String VERTEXFILE = "textrect/textrect.vertex.glsl";
    public static final String FRAGFILE = "textrect/textrect.frag.glsl";
    int maTexCoorHandle;
    FloatBuffer mTexCoorBuffer;
    public float[] mCentreXYZ;
    private float mXoffset;
    private float mYoffset;
    int sTextureHandler;
    int uhideTextHandle;
    public int uHideText;//1，隐藏，0 显示
    public float mStatrX;//x方向最大偏移量


    public BaseTextUI(Context context, MatrixState state, float[] centre, float xoffset, float yoffset, int uHideText) {
        super(context, state, VERTEXFILE, FRAGFILE);
        this.mCentreXYZ = centre;
        this.mXoffset = xoffset;
        this.mYoffset = yoffset;
        this.uHideText = uHideText;
        mStatrX = centre[0];
        initVertex();
    }

    @Override
    public void initVertex() {
        super.initVertex();
        count = 6;
        float vertices[] = new float[]{
                mCentreXYZ[0] - mXoffset, mCentreXYZ[1] - mYoffset, mCentreXYZ[2],
                mCentreXYZ[0] - mXoffset, mCentreXYZ[1] + mYoffset, mCentreXYZ[2],
                mCentreXYZ[0] + mXoffset, mCentreXYZ[1] - mYoffset, mCentreXYZ[2],
                mCentreXYZ[0] - mXoffset, mCentreXYZ[1] + mYoffset, mCentreXYZ[2],
                mCentreXYZ[0] + mXoffset, mCentreXYZ[1] + mYoffset, mCentreXYZ[2],
                mCentreXYZ[0] + mXoffset, mCentreXYZ[1] - mYoffset, mCentreXYZ[2],

        };
        vertexBuffer = GL3DUtils.addArrayToFloatBuffer(vertices, vertexBuffer);
        float[] texcoor = new float[]
                {
                        0, 0, 0, 1, 1, 0,
                        0, 1, 1, 1, 1, 0
                };
        mTexCoorBuffer = GL3DUtils.addArrayToFloatBuffer(texcoor, vertexBuffer);
    }


    @Override
    public void loadAttrs() {
        super.loadAttrs();
        maTexCoorHandle = GLES20.glGetAttribLocation(program, "aTexCoor");
        sTextureHandler = GLES20.glGetUniformLocation(program, "sTexture");
        uhideTextHandle = GLES20.glGetUniformLocation(program, "hideText");
    }


    @Override
    public void drawSelf(int texId) {
        initVertex();
        super.drawSelf(texId);
        GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mTexCoorBuffer);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLES20.glUniform1i(sTextureHandler, 0);
        GLES20.glUniform1i(uhideTextHandle, uHideText);
        DrawArray();
    }
}

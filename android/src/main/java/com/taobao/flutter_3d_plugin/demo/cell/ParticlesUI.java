package com.taobao.flutter_3d_plugin.demo.cell;

import android.content.Context;
import android.opengl.GLES20;


import com.taobao.flutter_3d_plugin.demo.cell.model.Angle;
import com.taobao.flutter_3d_plugin.demo.cell.model.Particle;
import com.taobao.flutter_3d_plugin.gl.MatrixState;
import com.taobao.flutter_3d_plugin.gl.utils.GL3DUtils;

import java.nio.FloatBuffer;
import java.util.List;


public class ParticlesUI extends BaseUI {
    private static final String VERTEX = "particles/particles_vertex.glsl";
    private static final String FRAG = "particles/particles_frag.glsl";
    int attrsHandler;
    int uColorHanlder;
    public List<Particle> particles;
    float[] verticesArray;
    float[] attrsArray;
    FloatBuffer attrsBuffer;
    public float life;
    float[] color;
    Angle angle;

    public ParticlesUI(Context context, MatrixState state, List<Particle> particles, float life, float[] color, Angle angle) {
        super(context, state, VERTEX, FRAG);
        this.particles = particles;
        verticesArray = new float[particles.size() * 6 * 2];
        attrsArray = new float[particles.size() * 6 * 4];
        this.life = life;
        this.color = color;
        this.angle = angle;
        initBuffer();
    }

    int verticebuffers[] = new int[1];
    int attrsbuffers[] = new int[1];
    private int mverticesBufferId = 0;
    private int mattrsBufferId = 0;

    private void initBuffer() {
        GLES20.glGenBuffers(1, verticebuffers, 0);
        mverticesBufferId = verticebuffers[0];

        GLES20.glGenBuffers(1, attrsbuffers, 0);
        mattrsBufferId = attrsbuffers[0];
    }

    @Override
    public void initVertex() {
        super.initVertex();

        vertexBuffer = null;
        attrsBuffer = null;

        int verindex = 0;
        int attrindex = 0;
        for (Particle particle : particles) {
            verticesArray[verindex++] = -particle.r + particle.centre[0];
            verticesArray[verindex++] = -particle.r + particle.centre[1];

            attrsArray[attrindex++] = particle.centre[0];
            attrsArray[attrindex++] = particle.centre[1];
            attrsArray[attrindex++] = particle.r;
            attrsArray[attrindex++] = particle.life;

            verticesArray[verindex++] = -particle.r + particle.centre[0];
            verticesArray[verindex++] = particle.r + particle.centre[1];


            attrsArray[attrindex++] = particle.centre[0];
            attrsArray[attrindex++] = particle.centre[1];
            attrsArray[attrindex++] = particle.r;
            attrsArray[attrindex++] = particle.life;

            verticesArray[verindex++] = particle.r + particle.centre[0];
            verticesArray[verindex++] = -particle.r + particle.centre[1];


            attrsArray[attrindex++] = particle.centre[0];
            attrsArray[attrindex++] = particle.centre[1];
            attrsArray[attrindex++] = particle.r;
            attrsArray[attrindex++] = particle.life;

            verticesArray[verindex++] = -particle.r + particle.centre[0];
            verticesArray[verindex++] = particle.r + particle.centre[1];


            attrsArray[attrindex++] = particle.centre[0];
            attrsArray[attrindex++] = particle.centre[1];
            attrsArray[attrindex++] = particle.r;
            attrsArray[attrindex++] = particle.life;

            verticesArray[verindex++] = particle.r + particle.centre[0];
            verticesArray[verindex++] = particle.r + particle.centre[1];


            attrsArray[attrindex++] = particle.centre[0];
            attrsArray[attrindex++] = particle.centre[1];
            attrsArray[attrindex++] = particle.r;
            attrsArray[attrindex++] = particle.life;


            verticesArray[verindex++] = particle.r + particle.centre[0];
            verticesArray[verindex++] = -particle.r + particle.centre[1];


            attrsArray[attrindex++] = particle.centre[0];
            attrsArray[attrindex++] = particle.centre[1];
            attrsArray[attrindex++] = particle.r;
            attrsArray[attrindex++] = particle.life;
        }

        count = verticesArray.length / 3;


        vertexBuffer = GL3DUtils.addArrayToFloatBuffer(verticesArray, vertexBuffer);
        attrsBuffer = GL3DUtils.addArrayToFloatBuffer(attrsArray, attrsBuffer);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, verticebuffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexBuffer.capacity() * 4,
                vertexBuffer, GLES20.GL_DYNAMIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, attrsbuffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, attrsBuffer.capacity() * 4,
                attrsBuffer, GLES20.GL_DYNAMIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void loadAttrs() {
        super.loadAttrs();
        attrsHandler = GLES20.glGetAttribLocation(program, "attrs");
        uColorHanlder = GLES20.glGetUniformLocation(program, "ucolor");
    }


    @Override
    public void drawSelf(int texid) {
        matrixState.rotate(angle.angle, 0, 0, 1);
        initVertex();
        GLES20.glUseProgram(program);
        GLES20.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, matrixState.getFinalMatrix(), 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mverticesBufferId);
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(
                aPositionHandle,
                2,
                GLES20.GL_FLOAT,
                false,
                2 * 4,
                0
        );
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mattrsBufferId);
        GLES20.glEnableVertexAttribArray(attrsHandler);
        GLES20.glVertexAttribPointer(
                attrsHandler,
                4,
                GLES20.GL_FLOAT,
                false,
                4 * 4,
                0
        );
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        GLES20.glUniform4fv(uColorHanlder, 1, color, 0);
        DrawArray();
    }
}


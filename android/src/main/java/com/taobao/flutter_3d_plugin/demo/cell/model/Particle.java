package com.taobao.flutter_3d_plugin.demo.cell.model;

import java.util.List;


public class Particle {
    public float[] centre;
    public float r;
    public float[] colors;
    public float life;
    public float scalev;//缩放速度

    public float fade;

    public float xi;
    public float yi;

    public float xg;
    public float yg;

    public float originalX;
    public float originalY;

    public boolean offsetMaxDis;//默认为false，没达到过

    public float velocity;


    public Particle(float[] centre, float r, float[] colors, float life,
                    float v, float fade, float xi, float yi, float xg,
                    float yg, float originalX, float originalY, float velocity) {
        this.centre = centre;
        this.r = r;
        this.colors = colors;
        this.life = life;
        this.scalev = v;
        this.fade = fade;
        this.xi = xi;
        this.yi = yi;
        this.xg = xg;
        this.yg = yg;
        this.originalX = originalX;
        this.originalY = originalY;
        this.velocity = velocity;
    }

    public static Particle CreateRandomParticle() {
        float r = (float) (Math.random() * 5 + 3);
        float life = (float) (Math.random()); //0.0-1.0
        float v = 1.003f;//0.1-1.1
        float radius = (float) (Math.random() * 360);

        float centreX = (float) (220 * Math.sin(Math.toRadians(radius)));
        float centreY = (float) (220 * Math.cos(Math.toRadians(radius)));

        float fade = 0.006f;
        float xi = (float) (Math.random() - 0.5F);
        float yi = (float) (Math.random() - 0.5F);
        float xg = (float) (Math.random() - 0.5F);
        float yg = (float) (Math.random() - 0.5F);
        return new Particle(new float[]{centreX, centreY}, r,
                new float[]{1, 1, 1, 0.4f}, life, v,
                fade, xi, yi, xg, yg, centreX, centreY, 1);
    }


    public static Particle CreateSprayedParticle(float R) {
        float r = (float) (Math.random() * 2 + 2);
        float life = (float) (Math.random()); //0.0-1.0
        float v = (float) (1.003 + Math.random() * 0.001f);
        float radius = (float) (Math.random() * 360);

        float centreX = (float) ((R) * Math.sin(Math.toRadians(radius)));
        float centreY = (float) ((R) * Math.cos(Math.toRadians(radius)));

        float fade = 0.003f;
        float xi = (float) (Math.random() - 0.5F);
        float yi = (float) (Math.random() - 0.5F);
        float xg = (float) (Math.random() - 0.5F);
        float yg = (float) (Math.random() - 0.5F);
        return new Particle(new float[]{centreX, centreY}, r, new float[]{1, 1, 1, 0.4f},
                life, v, fade, xi, yi, xg, yg, centreX, centreY, 1);
    }

    public static boolean isAlive(Particle p) {
        if (p.life < 0 || moreThanDistance(p, 700))
            return false;
        return true;
    }

    //集体更新粒子状态
    public static void AllUpdete(List<Particle> particles) {
        for (Particle p : particles) {
            Particle particle = CreateSprayedParticle(1);
            p.r = particle.r;
            p.centre = particle.centre;
            p.life = particle.life;
            p.scalev = particle.scalev;

            p.xg = particle.xg;
            p.yg = particle.yg;

            p.xi = particle.xi;
            p.yi = particle.yi;

            p.originalX = particle.centre[0];
            p.originalY = particle.centre[1];

            p.fade = particle.fade;

            p.offsetMaxDis = false;
        }
    }

    //是否在指定距离范围内
    public static boolean moreThanDistance(Particle p, float dis) {
        float d = (float) Math.sqrt(Math.pow(p.centre[0], 2) + Math.pow(p.centre[1], 2));
        if (d > dis) {
            return true;
        }
        return false;
    }


    //运动到最大距离
    public static boolean offsetMaxdis(Particle p, float dis) {
        float d = (float) Math.sqrt(Math.pow(p.centre[0] - p.originalX, 2) + Math.pow(p.centre[1] - p.originalY, 2));
        if (d > dis) {
            p.offsetMaxDis = true;
            return true;
        }
        return false;
    }
}

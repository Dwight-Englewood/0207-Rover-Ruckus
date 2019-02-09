package wen.sim.bodies.mecanumRobot;

import org.ejml.simple.SimpleMatrix;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;

import wen.sim.bodies.Body;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;
import wen.sim.bodies.mecanumRobot.normFunction.DriveNorm;
import wen.sim.bodies.mecanumRobot.normFunction.MecanumNormMode;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;
import static java.lang.StrictMath.abs;
import static java.util.Collections.max;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class MecanumRobot extends Body {
    public float botMass = 5;
    public float friction;
    public float linearMotorScale;
    public float wheelRadius;
    public float wheelFR = 0;
    public float wheelFL = 0;
    public float wheelBL = 0;
    public float wheelBR = 0;
    public float target = 700;

    MecanumDriveMode drive;
    MecanumNormMode norm;

    public MecanumRobot(float botMass, float friction, float linearMotorScale, float wheelRadius, MecanumDriveMode mdm, MecanumNormMode mnm) {
        this.botMass = botMass;
        this.friction = friction;
        this.linearMotorScale = linearMotorScale;
        this.wheelRadius = wheelRadius;
        this.drive = mdm;
        this.norm = mnm;
    }

    @Override
    public void reset() {
        drive.reset();
        super.reset();
    }

    //move to body
    public void forceToAcceleration() {
        this.botXDD = this.botXF / this.botMass;
        this.botYDD = this.botYF / this.botMass;
        this.botRDD = this.botRF / this.botMass;
    }

    //move to body
    public void updateForce() {
        SimpleMatrix forces = wheelToForce(wheelFL, wheelFR, wheelBL, wheelBR);
        this.botXF = (float) (linearMotorScale * forces.get(1, 0) + friction * this.botXD);
        this.botYF = (float) (linearMotorScale * forces.get(0, 0) + friction * this.botYD);
        this.botRF = (float) (linearMotorScale * forces.get(2, 0) + friction * this.botRD);
    }

    public SimpleMatrix getJ(double t) {
        SimpleMatrix rotMatrix = new SimpleMatrix(new double[][]{{cos(t), sin(t), 0}, {-sin(t), cos(t), 0}, {0, 0, 1}});
        SimpleMatrix mecanumMatrix = new SimpleMatrix(new double[][]{{1, 1, 1, 1}, {1, -1, -1, 1}, {-.5, .5, -.5, .5}});
        return rotMatrix.mult(mecanumMatrix);
    }

    public SimpleMatrix velocityToWheel(float vx, float vy, float vr) {
        SimpleMatrix velocity = new SimpleMatrix(new double[][]{{vx}, {vy}, {vr}});
        SimpleMatrix inv = (getJ((float) (this.botR))).pseudoInverse();
        return inv.mult(velocity).scale((1 / wheelRadius) * 4);
    }

    public SimpleMatrix wheelToForce(float w1, float w2, float w3, float w4) {
        SimpleMatrix j = getJ((this.botR));
        SimpleMatrix w = new SimpleMatrix(new double[][]{{w1}, {w2}, {w3}, {w4}});
        return (j.mult(w).scale(wheelRadius * .25));
    }

    @Override
    public void update(long window) {
        drive.updateWheelPower(window, this);
        norm.normWheelPower(this);
        updateForce();
        forceToAcceleration();
    }


    public void draw(long window) {
        GL.createCapabilities();
        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glPushMatrix();
        //glScalef(1 / viewSize, 1 / viewSize, 1);
        glTranslatef(this.botX / 10, this.botY / 10, (float) 0);
        glRotatef((float) toDegrees(this.botR), 0, 0, 1);
        //glScalef(1 / viewSize, 1 / viewSize, 1);
        glColor3f(0.2f, 0.2f, 1.0f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glBegin(GL_POLYGON);
        float boxHalfDim = .1f;
        glColor3f(0.2f, 0.2f, 1.0f);
        glVertex2f(boxHalfDim, boxHalfDim);
        glVertex2f(-boxHalfDim, boxHalfDim);
        glColor3f(0.2f, 0.8f, 1.0f);
        glVertex2f(-boxHalfDim, -boxHalfDim);
        glVertex2f(boxHalfDim, -boxHalfDim);
        glEnd();
        glPopMatrix();

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glBegin(GL_POLYGON);
        glColor3f(1f, 0.5f, .5f);
        glVertex2f(0, target / 10);
        glVertex2f(1, target / 10);
        glVertex2f(1, (target + .1f) / 10);
        glVertex2f(0, (target + .1f) / 10);
        glEnd();

        glFlush();
    }
}

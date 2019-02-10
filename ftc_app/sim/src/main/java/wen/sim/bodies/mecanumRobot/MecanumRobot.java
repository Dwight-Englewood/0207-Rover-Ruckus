package wen.sim.bodies.mecanumRobot;

import org.ejml.simple.SimpleMatrix;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;

import wen.control.Coordinate;
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
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class MecanumRobot extends Body {
    public float botMass = 5;
    public float friction;
    public float linearMotorScale;
    public float wheelRadius;
    public float wheelFR = 0;
    public float wheelFL = 0;
    public float wheelBL = 0;
    public float wheelBR = 0;

    MecanumDriveMode drive;
    MecanumNormMode norm;

    ArrayList<Coordinate> path = new ArrayList<>();
    ArrayList<Coordinate> rot = new ArrayList<>();

    public MecanumRobot(float botMass, float friction, float linearMotorScale, float wheelRadius, MecanumDriveMode mdm, MecanumNormMode mnm) {
        this.botMass = botMass;
        this.friction = friction;
        this.linearMotorScale = linearMotorScale;
        this.wheelRadius = wheelRadius;
        this.drive = mdm;
        this.norm = mnm;
    }

    public MecanumRobot(float x, float y, float r, float botMass, float friction, float linearMotorScale, float wheelRadius, MecanumDriveMode mdm, MecanumNormMode mnm) {
        this.botMass = botMass;
        this.friction = friction;
        this.linearMotorScale = linearMotorScale;
        this.wheelRadius = wheelRadius;
        this.drive = mdm;
        this.norm = mnm;
        super.botX = x;
        super.defaultX = x;
        super.botY = y;
        super.defaultY = y;
        super.botR = r;
        super.defaultR = r;
    }

    @Override
    public void reset() {
        drive.reset();
        path.clear();
        rot.clear();
        wheelFL = 0;
        wheelFR = 0;
        wheelBR = 0;
        wheelBL = 0;
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
        SimpleMatrix velocity = new SimpleMatrix(new double[][]{{vy}, {vx}, {vr}});
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
        this.path.add(new Coordinate(this.botX, this.botY));
        this.rot.add(new Coordinate(this.botX, this.botR));
    }


    public void drawState(long window) {
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
        glFlush();
    }

    @Override
    public void drawData(long window) {
        GL.createCapabilities();
        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glBegin(GL_LINES);
        for (int i = 0; i < path.size(); i++) {
            Coordinate e = path.get(i);
            glColor3f(200f/255 , Math.abs(200- i % 400)/255f, Math.abs(200- i % 400)/255f);
            glVertex3f(e.x / 10, e.y / 10, 0);
            try {
                e = path.get(i + 1);
                glVertex3f(e.x / 10, e.y / 10, 0);
            } catch (IndexOutOfBoundsException error) {
            }

        }
        glEnd();

        glBegin(GL_LINES);

        int start =0;
        if (rot.size() > 300) {
            start = rot.size() - 300;
        }
        int j = 0;
        for (int i = start; i < rot.size(); i++) {
            Coordinate e = rot.get(i);
            glColor3f(Math.abs(225- i % 450)/255f , 100f, Math.abs(225- i % 450)/255f);
            double fixedRotation = (e.y % (Math.PI*2)) < 0 ? Math.PI*2+ (e.y % (Math.PI*2)) : (e.y % (Math.PI*2));
            glVertex3f(j/200f-1f, (float) (fixedRotation/(Math.PI*2))/2f-1, 0);
            try {
                e = rot.get(i + 1);
                fixedRotation = (e.y % (Math.PI*2)) < 0 ? Math.PI*2+ (e.y % (Math.PI*2)) : (e.y % (Math.PI*2));

                glVertex3f((j+1)/200f-1f, (float) (fixedRotation/(Math.PI*2))/2f - 1 , 0);
            } catch (IndexOutOfBoundsException error) {
            }
            j++;
        }
        glEnd();


        glFlush();
    }

}

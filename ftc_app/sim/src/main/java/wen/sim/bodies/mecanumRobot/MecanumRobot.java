package wen.sim.bodies.mecanumRobot;

import org.ejml.simple.SimpleMatrix;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;

import wen.control.function.Coordinate;
import wen.sim.bodies.Body;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;
import wen.sim.bodies.mecanumRobot.normFunction.MecanumNormMode;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static wen.sim.openglfix.OpenGLFix.glColor3f;
import static wen.sim.openglfix.OpenGLFix.glRotatef;
import static wen.sim.openglfix.OpenGLFix.glTranslatef;
import static wen.sim.openglfix.OpenGLFix.glVertex2f;
import static wen.sim.openglfix.OpenGLFix.glVertex3f;

public class MecanumRobot extends Body {
    public double botMass = 5;
    public double drag;
    public double linearMotorScale;
    public double wheelRadius;
    public double wheelFR = 0;
    public double wheelFL = 0;
    public double wheelBL = 0;
    public double wheelBR = 0;
    public double wheelFRDist = 0;
    public double wheelFLDist = 0;
    public double wheelBLDist = 0;
    public double wheelBRDist = 0;
    public boolean drawPos = true;

    MecanumDriveMode drive;
    MecanumNormMode norm;

    ArrayList<Coordinate> path = new ArrayList<>();
    ArrayList<Coordinate> rot = new ArrayList<>();

    public MecanumRobot(double botMass, double drag, double linearMotorScale, double wheelRadius, MecanumDriveMode mdm, MecanumNormMode mnm) {
        this.botMass = botMass;
        this.drag = drag;
        this.linearMotorScale = linearMotorScale;
        this.wheelRadius = wheelRadius;
        this.drive = mdm;
        this.norm = mnm;
    }

    public MecanumRobot(double x, double y, double r, double botMass, double drag, double linearMotorScale, double wheelRadius, MecanumDriveMode mdm, MecanumNormMode mnm) {
        this.botMass = botMass;
        this.drag = drag;
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

    public double getBotX() {
        return ((wheelFLDist + wheelBRDist) - (wheelFRDist + wheelBLDist))/4;
    }
    public double getBotY() {
        return ((wheelFLDist + wheelBRDist) + (wheelFRDist + wheelBLDist))/4;
    }public double getBotR() {
        return ((wheelFLDist + wheelBLDist) - (wheelFRDist + wheelBRDist))/4;
    }
    @Override
    public void simulateStep(long window) {
        double deltaTime = (System.currentTimeMillis() - lasttime) / (double) 1000;
        wheelFLDist += wheelFL * deltaTime;
        wheelFRDist += wheelFR * deltaTime;
        wheelBLDist += wheelBL * deltaTime;
        wheelBRDist += wheelBR * deltaTime;

        super.simulateStep(window);
    }

    //move to body
    public void updateForce() {
        SimpleMatrix forces = wheelToForce(wheelFL, wheelFR, wheelBL, wheelBR);
        this.botXF = (double) (linearMotorScale * forces.get(1, 0) + drag * this.botXD);
        this.botYF = (double) (linearMotorScale * forces.get(0, 0) + drag * this.botYD);
        this.botRF = (double) (linearMotorScale * forces.get(2, 0) + drag * this.botRD);
    }

    public SimpleMatrix getJ(double t) {
        SimpleMatrix rotMatrix = new SimpleMatrix(new double[][]{{cos(t), sin(t), 0}, {-sin(t), cos(t), 0}, {0, 0, 1}});
        SimpleMatrix mecanumMatrix = new SimpleMatrix(new double[][]{{1, 1, 1, 1}, {1, -1, -1, 1}, {-.5, .5, -.5, .5}});
        return rotMatrix.mult(mecanumMatrix);
    }

    public SimpleMatrix velocityToWheel(double vx, double vy, double vr) {
        SimpleMatrix velocity = new SimpleMatrix(new double[][]{{vy}, {vx}, {vr}});
        SimpleMatrix inv = (getJ((double) (this.botR))).pseudoInverse();
        return inv.mult(velocity).scale((1 / wheelRadius) * 4);
    }
    public static void main(String[] args) {
        MecanumRobot mdt = new MecanumRobot(5, -15, 30, 5, null, null);
        mdt.botR = Math.PI/2;
        System.out.println(mdt.velocityToWheel(0, 1, 0));
    }
    public SimpleMatrix wheelToForce(double w1, double w2, double w3, double w4) {
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
        System.out.println("MBot Ax: " + this.botXDD);
        System.out.println("MBot Ay: " + this.botYDD);
        System.out.println("--------");

        this.path.add(new Coordinate(this.botX, this.botY));
        this.rot.add(new Coordinate(this.botX, this.botR));
    }


    public void drawState(long window) {
        GL.createCapabilities();
        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glPushMatrix();
        //glScalef(1 / viewSize, 1 / viewSize, 1);
        glTranslatef(this.botX / 10, this.botY / 10, (double) 0);
        glRotatef((double) toDegrees(this.botR), 0, 0, 1);
        //glScalef(1 / viewSize, 1 / viewSize, 1);
        glColor3f(0.2f, 0.2f, 1.0f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glBegin(GL_POLYGON);
        double boxHalfDim = .1f;
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
        if (drawPos) {
            GL.createCapabilities();
            //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            glBegin(GL_LINES);
            for (int i = 0; i < path.size(); i++) {
                Coordinate e = path.get(i);
                //glColor3f(200f / 255, Math.abs(200 - i % 400) / 255f, Math.abs(200 - i % 400) / 255f);
                glColor3f(1, 0, 0);
                glVertex3f((double) e.x / 10, (double) e.y / 10, 0);
                try {
                    e = path.get(i + 1);
                    glVertex3f((double) e.x / 10, (double) e.y / 10, 0);
                } catch (IndexOutOfBoundsException error) {
                }

            }
            glEnd();

            glBegin(GL_LINES);

            int start = 0;
            if (rot.size() > 300) {
                start = rot.size() - 300;
            }
            int j = 0;
            for (int i = start; i < rot.size(); i++) {
                Coordinate e = rot.get(i);
                glColor3f(Math.abs(225 - i % 450) / 255f, 100f, Math.abs(225 - i % 450) / 255f);
                double y = (e.y + Math.PI);
                double fixedRotation = (y % (Math.PI * 2)) < 0 ? Math.PI * 2 + (y % (Math.PI * 2)) : (y % (Math.PI * 2));
                glVertex3f(j / 200f - 1f, (double) (fixedRotation / (Math.PI * 2)) / 2f - 1, 0);
                try {
                    e = rot.get(i + 1);
                    y = (e.y + Math.PI);
                    fixedRotation = (y % (Math.PI * 2)) < 0 ? Math.PI * 2 + (y % (Math.PI * 2)) : (y % (Math.PI * 2));

                    glVertex3f((j + 1) / 200f - 1f, (double) (fixedRotation / (Math.PI * 2)) / 2f - 1, 0);
                } catch (IndexOutOfBoundsException error) {
                }
                j++;
            }
            glEnd();


            glFlush();
        }
    }

}

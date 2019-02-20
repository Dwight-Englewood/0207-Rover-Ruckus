package wen.sim.bodies.mecanumRobot.driveFunction.auton;


import org.ejml.simple.SimpleMatrix;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;

import wen.control.PIDControllerBadOOP;
import wen.control.function.Coordinate;
import wen.sim.bodies.Drawable;
import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
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
import static wen.sim.openglfix.OpenGLFix.glVertex2f;
import static wen.sim.openglfix.OpenGLFix.glVertex3f;


public class PID1Drive implements MecanumDriveMode, Drawable {

    int target = 5;
    PIDControllerBadOOP pid;
    double height;
    ArrayList<Double> error = new ArrayList<>();
    double r, g, b;

    public PID1Drive(double kp, double ki, double kd, double target, double height, double r, double g, double b) {
        this.pid = new PIDControllerBadOOP(kp, ki, kd, target);
        this.height = height;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public void updateWheelPower(long window, MecanumRobot bot) {
        pid.updateError(bot.botX);
        double halp = pid.correction();
        this.error.add((double) bot.botX);
        SimpleMatrix wheelV = bot.velocityToWheel((double) halp, 0, 0);

        bot.wheelFL = (double) wheelV.get(0, 0);
        bot.wheelBL = (double) wheelV.get(2, 0);
        bot.wheelFR = (double) wheelV.get(1, 0);
        bot.wheelBR = (double) wheelV.get(3, 0);
    }

    @Override
    public void reset() {

    }

    @Override
    public void update(long window) {

    }

    public void drawState(long window) {
        GL.createCapabilities();
        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glPushMatrix();
        glColor3f(1f, 0.5f, .5f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glBegin(GL_POLYGON);
        double boxHalfDim = .05f;
        glVertex2f((target - boxHalfDim) / 10, (height - boxHalfDim) / 10);
        glVertex2f((target - boxHalfDim) / 10, (height + boxHalfDim) / 10);
        glVertex2f((target + boxHalfDim) / 10, (height + boxHalfDim) / 10);
        glVertex2f((target + boxHalfDim) / 10, (height - boxHalfDim) / 10);


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
        glColor3f(1, 1, 1);
        glVertex3f(-1, target / 10f, 0);
        glVertex3f(1, target / 10f, 0);
        glEnd();

        glBegin(GL_LINES);

        int start = 0;
        if (error.size() > 300) {
            start = error.size() - 300;
        }
        int j = 0;
        glColor3f(r, g, b);

        for (int i = start; i < error.size(); i++) {
            double e = error.get(i);
            glVertex3f(j / 200f - 1f, (double) e / 10, 0);
            try {
                e = error.get(i + 1);
                glVertex3f((j + 1) / 200f - 1f, (double) e / 10, 0);
            } catch (IndexOutOfBoundsException error) {
            }
            j++;
        }
        glEnd();


        glFlush();
    }

}

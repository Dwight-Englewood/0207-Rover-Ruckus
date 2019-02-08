package wen.sim;


import org.ejml.simple.SimpleMatrix;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.*;
import static java.util.Collections.max;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class RobotSimulator implements Simulator {

    float target = 0;
    private long lasttime = System.currentTimeMillis();
    private float botMass = 5;
    private float friction = -20;
    private float linearMotorScale = 5000;
    private float rotationMotorScale = 3000;
    private float botX = 0;
    private float botXD = 0;
    private float botXDD = 0;
    private float botY = -800;
    private float botYD = 0;
    private float botYDD = 0;
    private float botR = 0;
    private float botRD = 0;
    private float botRDD = 0;
    private float botXF = 0;
    private float botYF = 0;
    private float botRF = 0;
    private float wheelRadius = 5;
    private float wheelFR = 0;
    private float wheelFL = 0;
    private float wheelBL = 0;
    private float wheelBR = 0;
    private boolean bounded = false;
    private boolean shouldNorm = true;

    public static void main(String[] args) {
        RobotSimulator dab = new RobotSimulator();
        dab.velocityToWheel(1f, 0f, 0).print();
        dab.wheelToForce(1, 1, 1, 1).print();
    }

    public void updateSim(long window) {
        fieldCentricKey(window);
        normWheel();
        updateForce();
        forceToAcceleration();
        simulateStep();
    }

    public void normWheel() {
        float largest = max(new ArrayList<Float>(Arrays.asList(abs(wheelFL), abs(wheelFR), abs(wheelBR), abs(wheelBL))));
        if (largest != 0) {
            wheelFL = wheelFL / largest;
            wheelFR = wheelFR / largest;
            wheelBL = wheelBL / largest;
            wheelBR = wheelBR / largest;
        }
    }

    public void simulateStep() {
        float deltaTime = (System.currentTimeMillis() - lasttime) / (float) 1000;
        botXD = botXD + botXDD * deltaTime;
        botYD = botYD + botYDD * deltaTime;
        botRD = botRD + botRDD * deltaTime;
        botX = botX + botXD * deltaTime;
        botY = botY + botYD * deltaTime;
        botR = botR + botRD * deltaTime;
        if (bounded) {
            if (botX > 1000) {
                botX = 1000;
            } else if (botX < -1000) {
                botX = -1000;
            }
            if (botY > 1000) {
                botY = 1000;
            } else if (botY < -1000) {
                botY = -1000;
            }
        }
        lasttime = System.currentTimeMillis();

    }

    public void forceToAcceleration() {
        botXDD = botXF / botMass;
        botYDD = botYF / botMass;
        botRDD = botRF / botMass;
    }

    public void updateForce() {
        SimpleMatrix forces = wheelToForce(wheelFL, wheelFR, wheelBL, wheelBR);
        botXF = (float) (linearMotorScale * forces.get(1, 0) + friction * botXD);
        botYF = (float) (linearMotorScale * forces.get(0, 0) + friction * botYD);
        botRF = (float) (rotationMotorScale * forces.get(2, 0) + friction * botRD);
    }

    public void fieldCentricKey(long window) {
        float x = 0;
        float y = 0;
        float r = 0;
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            y = 1;
        } else if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            y = -1;
        }
        if (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS) {
            r = 1;
        } else if (glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS) {
            r = -1;
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            x = -1;
        } else if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            x = 1;
        }

        SimpleMatrix wheelV = velocityToWheel(y, x, r);
        wheelFL = (float) wheelV.get(0, 0);
        wheelBL = (float) wheelV.get(2, 0);
        wheelFR = (float) wheelV.get(1, 0);
        wheelBR = (float) wheelV.get(3, 0);

    }

    public SimpleMatrix getJ(double t) {
        SimpleMatrix rotMatrix = new SimpleMatrix(new double[][]{{cos(t), sin(t), 0}, {-sin(t), cos(t), 0}, {0, 0, 1}});
        SimpleMatrix mecanumMatrix = new SimpleMatrix(new double[][]{{1, 1, 1, 1}, {1, -1, -1, 1}, {-.5, .5, -.5, .5}});
        return rotMatrix.mult(mecanumMatrix);
    }

    public SimpleMatrix velocityToWheel(float vx, float vy, float vr) {
        SimpleMatrix velocity = new SimpleMatrix(new double[][]{{vx}, {vy}, {vr}});
        SimpleMatrix inv = (getJ((float) toRadians(botR))).pseudoInverse();
        return inv.mult(velocity).scale((1 / wheelRadius) * 4);
    }

    public SimpleMatrix wheelToForce(float w1, float w2, float w3, float w4) {
        SimpleMatrix j = getJ(toRadians(botR));
        SimpleMatrix w = new SimpleMatrix(new double[][]{{w1}, {w2}, {w3}, {w4}});
        return (j.mult(w).scale(wheelRadius * .25));
    }

    public void draw(long window) {
        updateSim(window);
        //System.out.println(this.botX);
        //System.out.println(this.botY);
        drawToScreen();

    }

    public void drawToScreen() {
        GL.createCapabilities();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glPushMatrix();
        //glScalef(1 / viewSize, 1 / viewSize, 1);
        glTranslatef(botX / 1000, botY / 1000, (float) 0);
        glRotatef(botR, 0, 0, 1);
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
        glVertex2f(0, target / 1000);
        glVertex2f(1, target / 1000);
        glVertex2f(1, (target + 10) / 1000);
        glVertex2f(0, (target + 10) / 1000);
        glEnd();

        glFlush();
    }

    public void normPID() {
        if (wheelBL > 1) {
            wheelBL = 1;
        } else if (wheelBL < -1) {
            wheelBL = -1;
        }
        if (wheelBR > 1) {
            wheelBR = 1;
        } else if (wheelBR < -1) {
            wheelBR = -1;
        }
        if (wheelFL > 1) {
            wheelFL = 1;
        } else if (wheelFL < -1) {
            wheelFL = -1;
        }
        if (wheelFR > 1) {
            wheelFR = 1;
        } else if (wheelFR < -1) {
            wheelFR = -1;
        }
    }
}

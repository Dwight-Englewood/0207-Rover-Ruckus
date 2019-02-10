package wen.sim.bodies.mecanumRobot.driveFunction.auton;


import org.ejml.simple.SimpleMatrix;
import org.lwjgl.opengl.GL;

import wen.control.PIDControllerBadOOP;
import wen.control.Trajectory;
import wen.sim.bodies.Body;
import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;

import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_G;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class PID2Drive extends Body implements MecanumDriveMode {

    //PIDControllerBadOOP pidMove = new PIDControllerBadOOP(-.1, -.02, -.5, 0);
    PIDControllerBadOOP pidMove = new PIDControllerBadOOP(-.05, -.0001, -.05, 0);

    //PIDControllerBadOOP pidRot = new PIDControllerBadOOP(.3, 2, .1, 0);
    PIDControllerBadOOP pidRot = new PIDControllerBadOOP(.3, 0, 0, 0);


    Trajectory targets;

    float targetX;
    float targetY;

    boolean shouldGo = false;

    int point = 0;
    private boolean shouldAdvance = true;

    public PID2Drive(Float[] targetX, Float[] targetY) {
        targets = new Trajectory(targetX, targetY);
        this.targetX = targets.xCoords.get(point);
        this.targetY = targets.yCoords.get(point);
    }

    @Override
    public void updateWheelPower(long window, MecanumRobot bot) {

        if (glfwGetKey(window, GLFW_KEY_G) == GLFW_PRESS) {
            shouldGo = true;
        }

        if (!shouldGo) {
            return;
        }
        pidMove.updateError(Math.sqrt(Math.pow(targetX - bot.botX, 2) + Math.pow(targetY - bot.botY, 2)));
        pidRot.setGoalNoReset(Math.atan((targetY - bot.botY)) / (targetX - bot.botX) - Math.PI / 2);
        pidRot.updateError(toRadians(toDegrees(bot.botR)));
        double planarCorrection = pidMove.correction();
        double rotationalCorrection = pidRot.correction();
        if (pidMove.goalReached(1) && shouldAdvance) {
            pidMove.setGoal(0);
            pidRot.setGoal(0);
            if (point + 1 < targets.xCoords.size()) {
                point++;
            }
            this.targetX = targets.xCoords.get(point);
            this.targetY = targets.yCoords.get(point);
        }
        SimpleMatrix wheelV = bot.velocityToWheel((float) planarCorrection * (targetX - bot.botX), (float) planarCorrection * (targetY - bot.botY), (float) rotationalCorrection);

        bot.wheelFL = (float) (wheelV.get(0, 0));
        bot.wheelBL = (float) (wheelV.get(2, 0));
        bot.wheelFR = (float) (wheelV.get(1, 0));
        bot.wheelBR = (float) (wheelV.get(3, 0));
    }

    @Override
    public void update(long window) {

    }

    @Override
    public void drawState(long window) {
        GL.createCapabilities();
        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glPushMatrix();
        glColor3f(1f, 0.5f, .5f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glBegin(GL_POLYGON);
        float boxHalfDim = .05f;
        glVertex2f((targetX - boxHalfDim) / 10, (targetY - boxHalfDim) / 10);
        glVertex2f((targetX - boxHalfDim) / 10, (targetY + boxHalfDim) / 10);
        glVertex2f((targetX + boxHalfDim) / 10, (targetY + boxHalfDim) / 10);
        glVertex2f((targetX + boxHalfDim) / 10, (targetY - boxHalfDim) / 10);


        glEnd();
        glPopMatrix();


        glFlush();

    }

    @Override
    public void drawData(long window) {

    }

    @Override
    public void reset() {
        this.point = 0;
        this.targetX = this.targets.xCoords.get(point);
        this.targetY = this.targets.yCoords.get(point);

        pidMove.setGoal(0);
        pidRot.setGoal(0);
        this.shouldGo = false;

    }


}

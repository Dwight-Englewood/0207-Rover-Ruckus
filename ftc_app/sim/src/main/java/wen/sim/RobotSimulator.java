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
    Body robot = new MecanumRobot(5, -20, 5000, 3000, 5);
    //Body robot2 = new MecanumRobot(10, 0, 5000, 3000, 5);
    Body[] bodies = {robot};

    private long lasttime = System.currentTimeMillis();
    private boolean bounded = false;
    private boolean shouldNorm = true;

    public void updateSim(long window) {
        for (Body a : bodies) {
            a.update(window);
        }
        simulateStep();
    }


    public void simulateStep() {
        float deltaTime = (System.currentTimeMillis() - lasttime) / (float) 1000;
        for (Body a : bodies) {
            a.botXD = a.botXD + a.botXDD * deltaTime;
            a.botYD = a.botYD + a.botYDD * deltaTime;
            a.botRD = a.botRD + a.botRDD * deltaTime;
            a.botX = a.botX + a.botXD * deltaTime;
            a.botY = a.botY + a.botYD * deltaTime;
            a.botR = a.botR + a.botRD * deltaTime;
            if (bounded) {
                if (a.botX > 1000) {
                    a.botX = 1000;
                } else if (a.botX < -1000) {
                    a.botX = -1000;
                }
                if (a.botY > 1000) {
                    a.botY = 1000;
                } else if (a.botY < -1000) {
                    a.botY = -1000;
                }
            }
        }
        lasttime = System.currentTimeMillis();

    }

    public void draw(long window) {
        updateSim(window);
        for (Body a  : bodies) {
            a.draw(window);
        }

    }


}

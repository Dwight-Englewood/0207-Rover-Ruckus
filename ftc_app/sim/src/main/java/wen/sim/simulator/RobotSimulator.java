package wen.sim.simulator;


import wen.sim.bodies.Body;

import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.auton.PID1Drive;
import wen.sim.bodies.mecanumRobot.driveFunction.auton.PID2Drive;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.MecanumDriveJoy;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.MecanumDriveKey;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.TankDriveJoy;
import wen.sim.bodies.mecanumRobot.normFunction.DriveNorm;
import wen.sim.bodies.mecanumRobot.normFunction.PIDNorm;

import static java.util.Collections.max;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class RobotSimulator implements Simulator {

    PID2Drive target = new PID2Drive(new float[]{0, 1, 2, 3, 4}, new float[]{0, .5f, 1, 1.25f, 1.5f});

    Body robotMecKey = new MecanumRobot(5, -15f, 50, 5, new MecanumDriveKey(), new DriveNorm());
    Body robotTankJoy = new MecanumRobot(5, -15f, 50, 5, new TankDriveJoy(), new DriveNorm());
    Body robotMecJoy = new MecanumRobot(5, -15f, 50, 5, new MecanumDriveJoy(), new DriveNorm());
    Body robotPID1 = new MecanumRobot(5, -15f, 50, 5, new PID1Drive(), new PIDNorm());
    Body robotPID2 = new MecanumRobot(-9, -9, 5, -15f, 50, 5, target, new PIDNorm());


    Body[] bodies = {robotPID2, target};

    private long lasttime = System.currentTimeMillis();
    private boolean bounded = false;
    private boolean shouldNorm = true;

    public void updateSim(long window) {
        if (glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS) {
            for (Body a : bodies) {
                a.reset();
            }
        }

        simulateStep();

        for (Body a : bodies) {
            a.update(window);
        }
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

    public void drawState(long window) {
        updateSim(window);
        for (Body a : bodies) {
            a.drawState(window);
            /*System.out.println(a.botX);
            System.out.println(a.botY);
            System.out.println("---");*/
        }

    }

    @Override
    public void drawData(long window) {
        for (Body a : bodies) {
            a.drawData(window);
            /*System.out.println(a.botX);
            System.out.println(a.botY);
            System.out.println("---");*/
        }

    }


}

package wen.sim.simulator;


import wen.sim.bodies.Body;

import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.auton.MotionProfile;
import wen.sim.bodies.mecanumRobot.driveFunction.auton.PIDDrive;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.MecanumDriveJoy;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.MecanumDriveKey;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.TankDriveJoy;
import wen.sim.bodies.mecanumRobot.normFunction.DriveNorm;
import wen.sim.bodies.mecanumRobot.normFunction.PIDNorm;

import static java.util.Collections.max;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class RobotSimulator implements Simulator {

    float target = 0;
    Body robotMecKey = new MecanumRobot(5, -15f, 50, 5, new MecanumDriveKey(), new DriveNorm());
    Body robotTankJoy = new MecanumRobot(5, -15f, 50, 5, new TankDriveJoy(), new DriveNorm());
    Body robotMecJoy = new MecanumRobot(5, -15f, 50, 5, new MecanumDriveJoy(), new DriveNorm());
    Body robotPID = new MecanumRobot(5, -15f, 50, 5, new PIDDrive(), new PIDNorm());
    Body robotMP = new MecanumRobot(5, -15f, 50, 5, new MotionProfile(), new PIDNorm());


    Body[] bodies = {robotMP};

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

    public void draw(long window) {
        updateSim(window);
        for (Body a : bodies) {
            a.draw(window);
            /*System.out.println(a.botX);
            System.out.println(a.botY);
            System.out.println("---");*/
        }

    }


}

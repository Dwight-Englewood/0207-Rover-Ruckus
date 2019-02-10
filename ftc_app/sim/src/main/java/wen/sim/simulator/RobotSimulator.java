package wen.sim.simulator;


import org.lwjgl.opengl.GL;

import wen.sim.bodies.Body;

import wen.sim.bodies.Cubic;
import wen.sim.bodies.Drawable;
import wen.sim.bodies.TrajectorySim;
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
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

public class RobotSimulator implements Simulator {

    PID2Drive target = new PID2Drive(new Float[]{0f, 1f, 2f, 3f, 4f}, new Float[]{0f, .5f, 1f, 1.25f, 1.5f});

    Body robotMecKey = new MecanumRobot(5, -15f, 30, 5, new MecanumDriveKey(), new DriveNorm());
    Body robotTankJoy = new MecanumRobot(5, -15f, 30, 5, new TankDriveJoy(), new DriveNorm());
    Body robotMecJoy = new MecanumRobot(5, -15f, 30, 5, new MecanumDriveJoy(), new DriveNorm());
    Body robotPID1 = new MecanumRobot(5, -15f, 30, 5, new PID1Drive(), new PIDNorm());
    Body robotPID2 = new MecanumRobot(-9, -9, 0, 5, -15f, 30, 5, target, new PIDNorm());

    TrajectorySim cubic = new TrajectorySim(new Cubic());

    //Drawable[] drawables = {robotPID2, target, cubic};
    Drawable[] drawables = {robotMecJoy};


    private long lasttime = System.currentTimeMillis();
    private boolean bounded = false;
    private boolean shouldNorm = true;

    public void updateSim(long window) {
        if (glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS) {
            for (Drawable a : drawables) {
                a.reset();
            }
        }

        simulateStep(window);

        for (Drawable a : drawables) {
            a.update(window);
        }
    }


    public void simulateStep(long window) {
        float deltaTime = (System.currentTimeMillis() - lasttime) / (float) 1000;
        for (Drawable a : drawables) {
            if (a instanceof Body) {
                Body b = (Body) a;
                b.simulateStep(window);
            }
        }

    }

    public void drawState(long window) {
        updateSim(window);
        for (Drawable a : drawables) {
            a.drawState(window);
            /*System.out.println(a.botX);
            System.out.println(a.botY);
            System.out.println("---");*/
        }

    }

    @Override
    public void drawData(long window) {
        for (Drawable a : drawables) {
            a.drawData(window);
            /*System.out.println(a.botX);
            System.out.println(a.botY);
            System.out.println("---");*/
        }

    }


}

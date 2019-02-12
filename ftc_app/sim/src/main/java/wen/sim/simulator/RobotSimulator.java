package wen.sim.simulator;


import wen.control.function.Quintic.Coordinate;
import wen.control.function.Quintic.QuinticHermiteSpline;
import wen.sim.bodies.Body;
import wen.sim.bodies.Cubic;
import wen.sim.bodies.Drawable;
import wen.sim.bodies.FunctionGrapher;
import wen.sim.bodies.TrajectorySim;
import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.auton.PID1Drive;
import wen.sim.bodies.mecanumRobot.driveFunction.auton.PID2Drive;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.MecanumDriveJoy;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.MecanumDriveKey;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.TankDriveJoy;
import wen.sim.bodies.mecanumRobot.normFunction.DriveNorm;
import wen.sim.bodies.mecanumRobot.normFunction.PIDNorm;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class RobotSimulator implements Simulator {

    PID2Drive target = new PID2Drive(new Double[]{0d, 1d, 2d, 3d, 4d}, new Double[]{0d, .5d, 1d, 1.25d, 1.5d});
    PID1Drive straightPID1 = new PID1Drive(.6, 0.00001, 6, 5, 5, 1, 0, 0);
    PID1Drive straightPID2 = new PID1Drive(1, 0, 0, 5, 0, 0, 1, 0);
    PID1Drive straightPID3 = new PID1Drive(.5, .001, .3, 5, -5, 0, 0, 1);


    Body robotMecKey = new MecanumRobot(5, -15f, 30, 5, new MecanumDriveKey(), new DriveNorm());
    Body robotTankJoy = new MecanumRobot(5, -15f, 30, 5, new TankDriveJoy(), new DriveNorm());
    Body robotMecJoy = new MecanumRobot(5, -20f, 50, 5, new MecanumDriveJoy(), new DriveNorm());
    Body robotPID11 = new MecanumRobot(-5, 5, 0, 5, -15f, 30, 5, straightPID1, new PIDNorm());
    Body robotPID12 = new MecanumRobot(-5, 0, 0, 5, -15f, 30, 5, straightPID2, new PIDNorm());
    Body robotPID13 = new MecanumRobot(-5, -5, 0, 5, -15f, 30, 5, straightPID3, new PIDNorm());
    Body robotPID2 = new MecanumRobot(-9, -9, 0, 5, -15f, 30, 5, target, new PIDNorm());

    TrajectorySim cubic = new TrajectorySim(new Cubic());

    //Drawable[] drawables = {robotPID2, target, cubic};
    //Drawable[] drawables = {robotPID11, robotPID12, robotPID13, straightPID1,straightPID2,straightPID3};
    //Drawable[] drawables = {robotMecJoy};
    //Drawable[] drawables = {new FunctionGrapher(QuinticHermiteBasis.h0,1,0,0),new FunctionGrapher(QuinticHermiteBasis.h1,0,1,0),new FunctionGrapher(QuinticHermiteBasis.h2,0,0,1),new FunctionGrapher(QuinticHermiteBasis.h3,1,1,0),new FunctionGrapher(QuinticHermiteBasis.h4,0,1,1),new FunctionGrapher(QuinticHermiteBasis.h5,1,0,1)};
    Drawable[] drawables = {new FunctionGrapher(new QuinticHermiteSpline(new Coordinate(-.5, -.5), new Coordinate(0, .5), new Coordinate(0, 0), new Coordinate(.5, .5), new Coordinate(0, .5), new Coordinate(0, 0)), 1, 1, 1)};

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
        double deltaTime = (System.currentTimeMillis() - lasttime) / (double) 1000;
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

package wen.sim.simulator;


import wen.control.function.Coordinate;
import wen.control.function.Function;
import wen.control.function.quintic.QuinticHermiteSpline;
import wen.control.function.quintic.QuinticHermiteSplineDerivitive;
import wen.control.function.quintic.QuinticHermiteSplineDerivitiveDerivitive;
import wen.sim.bodies.Body;
import wen.sim.bodies.Drawable;
import wen.sim.bodies.FunctionGrapher;
import wen.sim.bodies.FunctionGrapher2;
import wen.sim.bodies.FunctionGrapher3;
import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.auton.MotionProfile;
import wen.sim.bodies.mecanumRobot.driveFunction.auton.PID1Drive;
import wen.sim.bodies.mecanumRobot.driveFunction.auton.PID2Drive;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.MecanumDriveJoy;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.MecanumDriveKey;
import wen.sim.bodies.mecanumRobot.driveFunction.teleop.TankDriveJoy;
import wen.sim.bodies.mecanumRobot.normFunction.DriveNorm;
import wen.sim.bodies.mecanumRobot.normFunction.PIDNorm;
import wen.sim.bodies.primitives.Point;
import wen.sim.bodies.primitives.Vector;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
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


    //Drawable[] drawables = {robotPID2, target, cubic};
    //Drawable[] drawables = {robotPID11, robotPID12, robotPID13, straightPID1,straightPID2,straightPID3};
    //Drawable[] drawables = {robotMecJoy};
    //Drawable[] drawables = {new FunctionGrapher(QuinticHermiteBasis.h0,1,0,0),new FunctionGrapher(QuinticHermiteBasis.h1,0,1,0),new FunctionGrapher(QuinticHermiteBasis.h2,0,0,1),new FunctionGrapher(QuinticHermiteBasis.h3,1,1,0),new FunctionGrapher(QuinticHermiteBasis.h4,0,1,1),new FunctionGrapher(QuinticHermiteBasis.h5,1,0,1)};
    //Drawable[] drawables = {new FunctionGrapher(new QuinticHermiteSpline(new Coordinate(-.5, -.5), new Coordinate(0, .5), new Coordinate(0, 0), new Coordinate(.5, .5), new Coordinate(0, .5), new Coordinate(0, 0)), 1, 1, 1),new FunctionGrapher(new QuinticHermiteSpline(new Coordinate(-.5, -.5), new Coordinate(-1, 3), new Coordinate(0, 0), new Coordinate(.5, .5), new Coordinate(0, .5), new Coordinate(0, 0)), 1, 0, 1)};

    Coordinate p0 = new Coordinate(-.9, -.9);
    Coordinate p1 = new Coordinate(.8, .8);
    Coordinate v0 = new Coordinate(0, 0);
    Coordinate v1 = new Coordinate(0, -1);
    Coordinate a0 = new Coordinate(1, 0);
    Coordinate a1 = new Coordinate(0, 0);
    //Coordinate p2 = new Coordinate(.2, 0);
    //Coordinate v2 = new Coordinate(0, 0);
    //Coordinate a2 = new Coordinate(0, 0);

    QuinticHermiteSplineDerivitiveDerivitive qdd = new QuinticHermiteSplineDerivitiveDerivitive(p0, v0, a0, p1, v1, a1);
    QuinticHermiteSplineDerivitive qd = new QuinticHermiteSplineDerivitive(p0, v0, a0, p1, v1, a1);
    QuinticHermiteSpline q = new QuinticHermiteSpline(p0, v0, a0, p1, v1, a1);

    Function eml = new Function() {
        @Override
        public double eval(double t) {
            return 0;
        }
    };

    Body robotPath = new MecanumRobot(-9, -9, 0, 5, -15f, 30, 5, new MotionProfile(1, 0, q, qd, qdd), new PIDNorm());


    FunctionGrapher spline0 = new FunctionGrapher(q, 1, 1, 1);
    //FunctionGrapher spline1 = new FunctionGrapher(new QuinticHermiteSpline(p1, v1, a1, p2, v2, a2), 1, 1, 1);
    FunctionGrapher2 spline0d = new FunctionGrapher2(qd, 0, 0, 1);
    FunctionGrapher3 spline1d = new FunctionGrapher3(eml, 1, 1, 1);

    //FunctionGrapher spline1d = new FunctionGrapher(new QuinticHermiteSplineDerivitiveDerivitive(p1, v1, a1, p2, v2, a2), 1, 0, 0);


    //Drawable[] drawables = {robotPath, new Point(p0, 0f / 255, 206f / 255, 30f / 255, .02, GLFW_KEY_Q), new Point(p1, 12f / 255, 96f / 255, 25f / 255, .02, GLFW_KEY_A), new Vector(v0, 0, 0, 1, .01, GLFW_KEY_W, p0), new Vector(a0, 0, 1, 1, .01, GLFW_KEY_E, p0), new Vector(v1, 0, 0, 1, .01, GLFW_KEY_S, p1), new Vector(a1, 0, 1, 1, .01, GLFW_KEY_D, p1), new Point(p2, 0f / 255, 206f / 255, 30f / 255, .02, GLFW_KEY_Z), new Vector(v2, 0, 0, 1, .01, GLFW_KEY_X, p2), new Vector(a2, 0, 1, 1, .01, GLFW_KEY_C, p2), spline1, spline0, spline0d, spline1d};
    Drawable[] drawables = {spline1d, spline0d, robotPath, new Point(p0, 0f / 255, 206f / 255, 30f / 255, .02, GLFW_KEY_Q), new Point(p1, 12f / 255, 96f / 255, 25f / 255, .02, GLFW_KEY_A), new Vector(v0, 0, 0, 1, .01, GLFW_KEY_W, p0), new Vector(a0, 0, 1, 1, .01, GLFW_KEY_E, p0), new Vector(v1, 0, 0, 1, .01, GLFW_KEY_S, p1), new Vector(a1, 0, 1, 1, .01, GLFW_KEY_D, p1),spline0};

    //Drawable[] drawables = {new Point(p0, 0f/255, 206f/255, 30f/255, .02, GLFW_KEY_Q),new Point(p1, 12f/255, 96f/255, 25f/255, .02, GLFW_KEY_A), new Point(v0, 0,0,1,.01,GLFW_KEY_W), new Point(a0, 0,1,1,.01,GLFW_KEY_E),new Point(v1, 0,0,1,.01,GLFW_KEY_S),new Point(a1, 0,1,1,.01,GLFW_KEY_D), spline0};

    private long lasttime = System.currentTimeMillis();
    private boolean bounded = false;
    private boolean shouldNorm = true;

    public void updateSim(long window) {
        if (glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS) {
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
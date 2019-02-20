package wen.sim.bodies.mecanumRobot.driveFunction.auton;

import java.util.ArrayList;

import wen.control.function.Coordinate;
import wen.control.function.quintic.QuinticHermitePath;
import wen.control.function.quintic.QuinticHermiteSpline;
import wen.control.function.quintic.QuinticHermiteSplineDerivitive;
import wen.control.function.quintic.QuinticHermiteSplineDerivitiveDerivitive;
import wen.sim.bodies.Body;
import wen.sim.bodies.basicbody.BasicBody;
import wen.sim.bodies.basicbody.BasicBodyDriveMode;
import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class TankFollower implements BasicBodyDriveMode, MecanumDriveMode {

    long startTime;


    boolean shouldGo = false;
    double ka;
    double kv;
    double timeScale = 3000d;
    double displacement = 0;
    double lastBotX;
    double lastBotY;

    QuinticHermitePath q;

    public TankFollower(double kv, double ka, QuinticHermiteSpline path, QuinticHermiteSplineDerivitive pathd, QuinticHermiteSplineDerivitiveDerivitive pathdd, QuinticHermiteSpline path1, QuinticHermiteSplineDerivitive path1d, QuinticHermiteSplineDerivitiveDerivitive path1dd) {
        this.ka = ka;
        this.kv = kv;
        this.startTime = System.currentTimeMillis();

        ArrayList<QuinticHermiteSpline> paths = new ArrayList<>();
        paths.add(path);
        paths.add(path1);
        ArrayList<QuinticHermiteSplineDerivitive> pathds = new ArrayList<>();
        pathds.add(pathd);
        pathds.add(path1d);
        ArrayList<QuinticHermiteSplineDerivitiveDerivitive> pathdds = new ArrayList<>();
        pathdds.add(pathdd);
        pathdds.add(path1dd);

        q = new QuinticHermitePath(paths, pathds, pathdds);


    }


    @Override
    public void updateKinematics(long window, BasicBody bot) {
        this.displacement = this.displacement + Math.sqrt(Math.pow(bot.botX / 10 - this.lastBotX / 10, 2) + Math.pow(bot.botY / 10 - this.lastBotY / 10, 2));
        this.lastBotX = bot.botX;
        this.lastBotY = bot.botY;
        System.out.println("asd" + this.displacement);
        Coordinate dab = getAccelVector(window, bot);
        System.out.println("x:" + dab.x);
        System.out.println("y:" + dab.y);
        System.out.println("---");
        bot.botXDD = dab.x;
        bot.botYDD = dab.y;
    }

    public Coordinate getAccelVector(long window, Body bot) {
        System.out.println("wat");
        System.out.println("---------");
        if (shouldGo) {

            if (q.displacementToParameter(this.displacement, q.tMin, q.tMax) > q.tMax) {
                System.out.println(this.displacement);
                System.out.println(q.displacementToParameter(this.displacement, q.tMin, q.tMax));
                bot.botXD = 0;
                bot.botYD = 0;
                bot.botXDD = 0;
                bot.botYDD = 0;
                return new Coordinate(0, 0);
            } else {
                System.out.println("disp:" + this.displacement);
                System.out.println("t:" + q.displacementToParameter(this.displacement, q.tMin, q.tMax));
                Coordinate dab = q.evaldd(q.displacementToParameter(this.displacement, q.tMin, q.tMax));
                //bot.botXDD = dab.x;
                //System.out.println("x:" + bot.botXD);
                //bot.botYDD = dab.y;
                //System.out.println("y:" + bot.botYD);
                return dab;

            }
        } else {
            if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
                System.out.println("Started");
                this.shouldGo = true;
                this.displacement = 0;
                this.startTime = System.currentTimeMillis();
                this.lastBotX = bot.defaultX;
                this.lastBotY = bot.defaultY;
                bot.botXD = this.q.qd.get(0).v0.x;
                bot.botYD = this.q.qd.get(0).v0.y;
                bot.botXDD = this.q.qdd.get(0).v0.x;
                bot.botYDD = this.q.qdd.get(0).v0.y;


            }
            return new Coordinate(0, 0);

        }
    }

    @Override
    public void updateWheelPower(long window, MecanumRobot bot) {
        System.out.println("goodest");
        this.displacement = this.displacement + Math.sqrt(Math.pow(bot.botX / 10 - this.lastBotX / 10, 2) + Math.pow(bot.botY / 10 - this.lastBotY / 10, 2));
        this.lastBotX = bot.botX;
        this.lastBotY = bot.botY;
        System.out.println("asd" + this.displacement);
        Coordinate pose = new Coordinate(0, 0);
        Coordinate posed = new Coordinate(0, 0);
        Coordinate posedd = new Coordinate(0, 0);
        if (shouldGo) {
            if (q.displacementToParameter(this.displacement, q.tMin, q.tMax) > q.tMax) {
                System.out.println(this.displacement);
                System.out.println(q.displacementToParameter(this.displacement, q.tMin, q.tMax));
                bot.botXD = 0;
                bot.botYD = 0;
                bot.botXDD = 0;
                bot.botYDD = 0;


            } else {
                System.out.println("disp:" + this.displacement);
                System.out.println("t:" + q.displacementToParameter(this.displacement, q.tMin, q.tMax));
                Coordinate dab = q.evaldd(q.displacementToParameter(this.displacement, q.tMin, q.tMax));
                //bot.botXDD = dab.x;
                //System.out.println("x:" + bot.botXD);
                //bot.botYDD = dab.y;
                //System.out.println("y:" + bot.botYD);
                pose =q.eval(q.displacementToParameter(this.displacement, q.tMin, q.tMax));
                posed = q.evald(q.displacementToParameter(this.displacement, q.tMin, q.tMax));
                posedd =q.evaldd(q.displacementToParameter(this.displacement, q.tMin, q.tMax));

            }
        } else {
            if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
                System.out.println("Started");
                this.shouldGo = true;
                this.displacement = 0;
                this.startTime = System.currentTimeMillis();
                this.lastBotX = bot.defaultX;
                this.lastBotY = bot.defaultY;
                bot.botXD = this.q.qd.get(0).v0.x;
                bot.botYD = this.q.qd.get(0).v0.y;
                bot.botXDD = this.q.qdd.get(0).v0.x;
                bot.botYDD = this.q.qdd.get(0).v0.y;


            }

        }
        double vMag = posed.magnitude();
        double vHeading = pose.heading();
        if (Double.isNaN(vHeading)) {
            vHeading = 0;
        }
        System.out.println("vheading" + vHeading);
        bot.wheelFL = vMag + .5 * vHeading;
        bot.wheelBL = vMag + .5 * vHeading;
        bot.wheelFR = vMag - .5 * vHeading;
        bot.wheelBR = vMag - .5 * vHeading;


    }

    @Override
    public void reset() {
        System.out.println("KMSMKSKMS");
        this.shouldGo = false;
        this.startTime = System.currentTimeMillis();
    }

}

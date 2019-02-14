package wen.sim.bodies.mecanumRobot.driveFunction.auton;

import wen.control.PIDControllerBadOOP;
import wen.control.function.Coordinate;
import wen.control.function.Function;
import wen.control.function.quintic.QuinticHermiteSpline;
import wen.control.function.quintic.QuinticHermiteSplineDerivitive;
import wen.control.function.quintic.QuinticHermiteSplineDerivitiveDerivitive;
import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;

public class MotionProfile implements MecanumDriveMode {

    long startTime;
    boolean unset;

    double ka;
    double kv;

    QuinticHermiteSpline path;
    QuinticHermiteSplineDerivitive pathd;
    QuinticHermiteSplineDerivitiveDerivitive pathdd;
    double displacement;

    Function mpp = new Function() {
        @Override
        public double eval(double t) {
            t = t / path.length();
            if (t < 1) {
                return t * t / 2;
            } else if (t < 5) {
                return .5 + (t - 1);
            } else if (t < 6) {
                return 4.5 + .5 - (6 - t) * (6 - t) / 2;
            } else {
                return 0;
            }
        }
    };

    Function mpv = new Function() {
        @Override
        public double eval(double t) {
            t = t / path.length();

            if (t < 1) {
                return t;
            } else if (t < 5) {
                return 1;
            } else if (t < 6) {
                return (5 - t);
            } else {
                return 0;
            }
        }
    };

    Function mpa = new Function() {
        @Override
        public double eval(double t) {
            t = t / path.length();

            if (t < 3) {
                return 1;
            } else if (t < 8) {
                return 0;
            } else if (t < 11) {
                return -1;
            } else {
                return 0;
            }
        }
    };


    PIDControllerBadOOP pid = new PIDControllerBadOOP(0, 0, 0, 0);

    public MotionProfile(double kv, double ka, QuinticHermiteSpline path, QuinticHermiteSplineDerivitive pathd, QuinticHermiteSplineDerivitiveDerivitive pathdd) {
        this.unset = true;
        this.ka = ka;
        this.kv = kv;
        this.path = path;
        this.pathd = pathd;
        this.pathdd = pathdd;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void updateWheelPower(long window, MecanumRobot bot) {
/*
        Coordinate p = path.eval((System.currentTimeMillis() - this.startTime) / 10000d);

        pid.updateError(Math.sqrt(Math.pow(bot.botX - p.x, 2) + Math.pow(bot.botY - p.y, 2)));
        double correction = pid.correction();
        if (unset) {
            this.startTime = System.currentTimeMillis();
            unset = false;
        }

        Coordinate v = pathd.eval((System.currentTimeMillis() - this.startTime) / 10000d).norm();
        Coordinate a = pathdd.eval((System.currentTimeMillis() - this.startTime) / 10000d).norm();

        SimpleMatrix wheelV = bot.velocityToWheel(v.x, (kv * v.y + ka * a.y), 0);

        /*
        bot.wheelFL = (double) wheelV.get(0, 0);
        bot.wheelBL = (double) wheelV.get(2, 0);
        bot.wheelFR = (double) wheelV.get(1, 0);
        bot.wheelBR = (double) wheelV.get(3, 0);


        System.out.println((System.currentTimeMillis() - this.startTime) / 5000d);
        */
        if ((System.currentTimeMillis() - this.startTime) / 5000d > 1) {
            bot.botXD = 0;
            bot.botYD = 0;
            bot.botXDD = 0;
            bot.botYDD = 0;
            bot.wheelFL = 0;
            bot.wheelBL = 0;
            bot.wheelBR = 0;
            bot.wheelFR = 0;
        } else {
            Coordinate dab = pathd.eval((System.currentTimeMillis() - this.startTime) / 5000d);


            bot.botXD = dab.x+.9;
            System.out.println("x----x");
            System.out.println(bot.botXD);
            bot.botYD = dab.y+.9;
            System.out.println("y----y");
            System.out.println(bot.botYD);


        }
    }

    @Override
    public void reset() {
        this.unset = true;
        this.startTime = System.currentTimeMillis();
    }

}

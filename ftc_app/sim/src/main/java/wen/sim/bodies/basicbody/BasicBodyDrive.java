package wen.sim.bodies.basicbody;

import wen.control.function.Coordinate;
import wen.control.function.quintic.QuinticHermiteSpline;
import wen.control.function.quintic.QuinticHermiteSplineDerivitive;
import wen.control.function.quintic.QuinticHermiteSplineDerivitiveDerivitive;

public class BasicBodyDrive implements BasicBodyDriveMode {

    long startTime;


    double ka;
    double kv;

    QuinticHermiteSpline path;
    QuinticHermiteSplineDerivitive pathd;
    QuinticHermiteSplineDerivitiveDerivitive pathdd;

    public BasicBodyDrive(double kv, double ka, QuinticHermiteSpline path, QuinticHermiteSplineDerivitive pathd, QuinticHermiteSplineDerivitiveDerivitive pathdd) {
        this.ka = ka;
        this.kv = kv;
        this.path = path;
        this.pathd = pathd;
        this.pathdd = pathdd;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void updateKinematics(long window, BasicBody bot) {
        if ((System.currentTimeMillis() - this.startTime) / 2000d > 1) {
            bot.botXD = 0;
            bot.botYD = 0;
            bot.botXDD = 0;
            bot.botYDD = 0;
        } else {
            Coordinate dab = pathdd.eval((System.currentTimeMillis() - this.startTime) / 2000d);
            bot.botXDD = dab.x;
            System.out.println("x----x");
            System.out.println(bot.botXD);
            bot.botYDD = dab.y;
            System.out.println("y----y");
            System.out.println(bot.botYD);


        }
    }

    @Override
    public void reset() {
        System.out.println("KMSMKSKMS");
        this.startTime = System.currentTimeMillis();
    }

}

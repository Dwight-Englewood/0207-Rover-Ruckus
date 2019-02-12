package wen.sim.bodies.mecanumRobot.driveFunction.auton;

import org.ejml.simple.SimpleMatrix;

import wen.control.PIDControllerBadOOP;
import wen.control.function.Coordinate;
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

    PIDControllerBadOOP pid = new PIDControllerBadOOP(.6, 0,0, 0);

    public MotionProfile(double kv, double ka, QuinticHermiteSpline path, QuinticHermiteSplineDerivitive pathd, QuinticHermiteSplineDerivitiveDerivitive pathdd) {
        this.unset = true;
        this.ka = ka;
        this.kv = kv;
        this.path = path;
        this.pathd = pathd;
        this.pathdd = pathdd;
    }

    @Override
    public void updateWheelPower(long window, MecanumRobot bot) {

        Coordinate p = path.eval((System.currentTimeMillis() - this.startTime)/100000d);

        pid.updateError(Math.sqrt(Math.pow(bot.botX - p.x, 2)+Math.pow(bot.botY-p.y,2)));
        double correction = pid.correction();
        if (unset) {
            this.startTime = System.currentTimeMillis();
            unset = false;
        }

        Coordinate v = pathd.eval((System.currentTimeMillis() - this.startTime)/100000d);
        Coordinate a = pathdd.eval((System.currentTimeMillis() - this.startTime)/100000d);

        SimpleMatrix wheelV = bot.velocityToWheel(-correction * (kv * v.x+ka*a.x), -correction *(kv * v.y+ka*a.y),0);

        bot.wheelFL = (double) wheelV.get(0, 0);
        bot.wheelBL = (double) wheelV.get(2, 0);
        bot.wheelFR = (double) wheelV.get(1, 0);
        bot.wheelBR = (double) wheelV.get(3, 0);
    }

    @Override
    public void reset() {
        this.unset = true;
    }

}

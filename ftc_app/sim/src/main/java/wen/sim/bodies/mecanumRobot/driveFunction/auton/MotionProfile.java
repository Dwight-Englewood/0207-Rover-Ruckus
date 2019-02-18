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

    //long startTime;
    boolean unset;

    double ka;
    double kv;
    QuinticHermiteSpline path;
    QuinticHermiteSplineDerivitive pathd;
    QuinticHermiteSplineDerivitiveDerivitive pathdd;
    double displacement;
    double lastBotX;
    double lastBotY;

    PIDControllerBadOOP pid = new PIDControllerBadOOP(0, 0, 0, 0);

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
        this.displacement = this.displacement + Math.sqrt(Math.pow(bot.botX - this.lastBotX, 2) + Math.pow(bot.botY - this.lastBotY, 2));

        if (path.displacementToParameter(this.displacement, 0, 1) > path.tMax) {
            System.out.println("Done");
            bot.botXD = 0;
            bot.botYD = 0;
            bot.botXDD = 0;
            bot.botYDD = 0;
            bot.wheelFL = 0;
            bot.wheelBL = 0;
            bot.wheelBR = 0;
            bot.wheelFR = 0;
        } else {
            Coordinate dab = pathdd.eval(path.displacementToParameter(this.displacement, 0, 1));
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
        this.unset = true;
    }

}

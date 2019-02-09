package wen.sim.bodies.mecanumRobot.driveFunction.auton;

import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;

public class MotionProfile implements MecanumDriveMode {

    long startTime;
    boolean unset;

    public MotionProfile() {
        this.unset = true;
    }

    @Override
    public void updateWheelPower(long window, MecanumRobot bot) {
        if (unset) {
            this.startTime = System.currentTimeMillis();
            unset = false;
        }
        bot.wheelFL = (float) leftAccelerationCurve(System.currentTimeMillis() - this.startTime);
        bot.wheelBL = (float) leftAccelerationCurve(System.currentTimeMillis() - this.startTime);
        bot.wheelFR = (float) rightAccelerationCurve(System.currentTimeMillis() - this.startTime);
        bot.wheelBR = (float) rightAccelerationCurve(System.currentTimeMillis() - this.startTime);
    }

    @Override
    public void reset() {
        this.unset = true;
    }

    public double leftAccelerationCurve(long elapsedTime) {
        System.out.println(elapsedTime);
        if (elapsedTime < 500) {
            return .5+elapsedTime/(float)100*.1;
        } else if (elapsedTime < 700) {
            return 1;
        } else if (elapsedTime < 3000) {
            return .5;
        } else {
            return 0;
        }
    }

    public double rightAccelerationCurve(long elapsedTime) {
        if (elapsedTime < 500) {
            return .5+elapsedTime/(float)100*-.1;
        } else if (elapsedTime < 700) {
            return 0;
        } else if (elapsedTime < 3000) {
            return .5;
        } else {
            return 0;
        }
    }
}

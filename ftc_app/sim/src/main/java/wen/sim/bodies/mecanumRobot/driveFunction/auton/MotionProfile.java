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
        bot.wheelFL = (double) leftAccelerationCurve(System.currentTimeMillis() - this.startTime);
        bot.wheelBL = (double) leftAccelerationCurve(System.currentTimeMillis() - this.startTime);
        bot.wheelFR = (double) rightAccelerationCurve(System.currentTimeMillis() - this.startTime);
        bot.wheelBR = (double) rightAccelerationCurve(System.currentTimeMillis() - this.startTime);
    }

    @Override
    public void reset() {
        this.unset = true;
    }

    public double leftAccelerationCurve(long elapsedTime) {
        return 1 / (double) 30;

    }

    public double rightAccelerationCurve(long elapsedTime) {
        return (1 / (double) 30 * elapsedTime / (double) 1000);

    }
}

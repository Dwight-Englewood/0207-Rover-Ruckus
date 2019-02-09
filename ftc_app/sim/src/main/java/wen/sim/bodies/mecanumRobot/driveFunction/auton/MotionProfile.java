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
        return 1/(float)30;

    }

    public double rightAccelerationCurve(long elapsedTime) {
        return (1/(float)30 * elapsedTime/(float)1000);

    }
}

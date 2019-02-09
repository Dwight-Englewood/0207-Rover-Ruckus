package wen.sim.bodies.mecanumRobot.driveFunction.auton;


import org.ejml.simple.SimpleMatrix;

import wen.control.PIDController;
import wen.control.PIDControllerBadOOP;
import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;

public class PID1Drive implements MecanumDriveMode {

    PIDControllerBadOOP pid = new PIDControllerBadOOP(.6,0.00001,6, 7);

    @Override
    public void updateWheelPower(long window, MecanumRobot bot) {
        pid.updateError(bot.botX);
        double halp = pid.correction();
        SimpleMatrix wheelV = bot.velocityToWheel((float) halp, 0,0);

        bot.wheelFL = (float) wheelV.get(0, 0);
        bot.wheelBL = (float) wheelV.get(2, 0);
        bot.wheelFR = (float) wheelV.get(1, 0);
        bot.wheelBR = (float) wheelV.get(3, 0);
    }

    @Override
    public void reset() {

    }


}

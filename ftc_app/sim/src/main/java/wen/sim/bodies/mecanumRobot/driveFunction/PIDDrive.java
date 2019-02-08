package wen.sim.bodies.mecanumRobot.driveFunction;


import org.ejml.simple.SimpleMatrix;

import wen.control.PIDController;
import wen.sim.bodies.mecanumRobot.MecanumRobot;

public class PIDDrive implements MecanumDriveMode {

    PIDController pid = new PIDController(.6,0.00001,6, 7);

    @Override
    public void updateWheelPower(long window, MecanumRobot bot) {
        pid.updateError(bot.botY);
        double halp = pid.correction();
        SimpleMatrix wheelV = bot.velocityToWheel((float) halp, 0,0);

        bot.wheelFL = (float) wheelV.get(0, 0);
        bot.wheelBL = (float) wheelV.get(2, 0);
        bot.wheelFR = (float) wheelV.get(1, 0);
        bot.wheelBR = (float) wheelV.get(3, 0);
    }


}

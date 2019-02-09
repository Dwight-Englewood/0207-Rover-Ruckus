package wen.sim.bodies.mecanumRobot.driveFunction;

import wen.sim.bodies.mecanumRobot.MecanumRobot;

public interface MecanumDriveMode {

    public void updateWheelPower(long window, MecanumRobot bot);

    public void reset();
}

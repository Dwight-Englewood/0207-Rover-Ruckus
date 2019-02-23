package wen.sim.bodies.mecanumRobot.normFunction;

import wen.sim.bodies.mecanumRobot.MecanumRobot;

public class NullNorm implements MecanumNormMode {
    public final double max = 1f;
    public final double min = -1f;

    @Override
    public void normWheelPower(MecanumRobot bot) {
    }
}

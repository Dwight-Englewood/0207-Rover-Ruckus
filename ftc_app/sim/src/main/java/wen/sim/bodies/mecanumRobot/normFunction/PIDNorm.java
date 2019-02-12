package wen.sim.bodies.mecanumRobot.normFunction;

import wen.sim.bodies.mecanumRobot.MecanumRobot;

public class PIDNorm implements MecanumNormMode {

    public final double max = 1f;
    public final double min = -1f;

    @Override
    public void normWheelPower(MecanumRobot bot) {
        if (bot.wheelFL > max) {
            bot.wheelFL = max;
        } else if (bot.wheelFL < min) {
            bot.wheelFL = min;
        }
        if (bot.wheelFR > max) {
            bot.wheelFR = max;
        } else if (bot.wheelFR < min) {
            bot.wheelFR = min;
        }
        if (bot.wheelBL > max) {
            bot.wheelBL = max;
        } else if (bot.wheelBL < min) {
            bot.wheelBL = min;
        }
        if (bot.wheelBR > max) {
            bot.wheelBR = max;
        } else if (bot.wheelBR < min) {
            bot.wheelBR = min;
        }

    }
}

package wen.sim.bodies.mecanumRobot.normFunction;

import wen.sim.bodies.mecanumRobot.MecanumRobot;

public class PIDNorm implements MecanumNormMode{
    @Override
    public void normWheelPower(MecanumRobot bot) {
        if (bot.wheelFL > 1) {
            bot.wheelFL = 1;
        } else if (bot.wheelFL < -1) {
            bot.wheelFL = -1;
        }
        if (bot.wheelFR > 1) {
            bot.wheelFR = 1;
        } else if (bot.wheelFR < -1) {
            bot.wheelFR = -1;
        }
        if (bot.wheelBL > 1) {
            bot.wheelBL = 1;
        } else if (bot.wheelBL < -1) {
            bot.wheelBL = -1;
        }
        if (bot.wheelBR > 1) {
            bot.wheelBR = 1;
        } else if (bot.wheelBR < -1) {
            bot.wheelBR = -1;
        }

    }
}

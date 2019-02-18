package wen.sim.bodies.mecanumRobot.normFunction;

import java.util.ArrayList;
import java.util.Arrays;

import wen.sim.bodies.mecanumRobot.MecanumRobot;

import static java.lang.StrictMath.abs;
import static java.util.Collections.max;

public class DriveNorm implements MecanumNormMode {
    public final double max = 1f;
    public final double min = -1f;
    @Override
    public void normWheelPower(MecanumRobot bot) {
        double largest = max(new ArrayList<Double>(Arrays.asList(abs(bot.wheelFL), abs(bot.wheelFR), abs(bot.wheelBR), abs(bot.wheelBL))));
        if (largest != 0) {
            bot.wheelFL = bot.wheelFL / largest;
            bot.wheelFR = bot.wheelFR / largest;
            bot.wheelBL = bot.wheelBL / largest;
            bot.wheelBR = bot.wheelBR / largest;
        }
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

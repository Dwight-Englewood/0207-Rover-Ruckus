package wen.sim.bodies.mecanumRobot.normFunction;

import java.util.ArrayList;
import java.util.Arrays;

import wen.sim.bodies.mecanumRobot.MecanumRobot;

import static java.lang.StrictMath.abs;
import static java.util.Collections.max;

public class DriveNorm implements MecanumNormMode {

    @Override
    public void normWheelPower(MecanumRobot bot) {
        double largest = max(new ArrayList<Double>(Arrays.asList(abs(bot.wheelFL), abs(bot.wheelFR), abs(bot.wheelBR), abs(bot.wheelBL))));
        if (largest != 0) {
            bot.wheelFL = bot.wheelFL / largest;
            bot.wheelFR = bot.wheelFR / largest;
            bot.wheelBL = bot.wheelBL / largest;
            bot.wheelBR = bot.wheelBR / largest;
        }
    }
}

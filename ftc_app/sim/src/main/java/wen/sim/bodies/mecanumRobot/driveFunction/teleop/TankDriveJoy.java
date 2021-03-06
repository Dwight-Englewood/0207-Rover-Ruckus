package wen.sim.bodies.mecanumRobot.driveFunction.teleop;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;

import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;

public class TankDriveJoy implements MecanumDriveMode {

    public void updateWheelPower(long window, MecanumRobot bot) {
        try {
            FloatBuffer joysticks = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
            ByteBuffer buttons = glfwGetJoystickButtons(GLFW_JOYSTICK_1);

            double leftStickY = -1 * joysticks.get(1);

            double rightStickY = -1 * joysticks.get(4);

            double deadzone = .3f;
            if (leftStickY < deadzone && leftStickY > -deadzone) {
                leftStickY = 0;
            }
            if (rightStickY < deadzone && rightStickY > -deadzone) {
                rightStickY = 0;
            }

            bot.wheelFL = leftStickY;
            bot.wheelBL = leftStickY;
            bot.wheelFR = rightStickY;
            bot.wheelBR = rightStickY;

            if (buttons.get(7) == 1) {
                bot.wheelFL = 1;
                bot.wheelBL = -1;
                bot.wheelFR = -1;
                bot.wheelBR = 1;
            } else if (buttons.get(6) == 1) {
                bot.wheelFL = -1;
                bot.wheelBL = 1;
                bot.wheelFR = 1;
                bot.wheelBR = -1;
            }

        } catch (NullPointerException e) {

        }
    }

    @Override
    public void reset() {

    }

}

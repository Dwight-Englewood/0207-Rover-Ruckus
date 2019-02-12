package wen.sim.bodies.mecanumRobot.driveFunction.teleop;

import org.ejml.simple.SimpleMatrix;

import java.nio.FloatBuffer;

import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;

import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;

public class MecanumDriveJoy implements MecanumDriveMode {

    public void updateWheelPower(long window, MecanumRobot bot) {
        FloatBuffer joysticks = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        //Microsoft X-Box 360 pad
        //Logitech Logitech Dual Action
        double leftStickX = 0, leftStickY = 0, rightStickX = 0;
        if (glfwGetJoystickName(GLFW_JOYSTICK_1).equals("Logitech Logitech Dual Action")) {
            leftStickX = joysticks.get(0);
            leftStickY = -1 * joysticks.get(1);

            rightStickX = -joysticks.get(2);

        } else if (glfwGetJoystickName(GLFW_JOYSTICK_1).equals("Microsoft X-Box 360 pad")) {
            leftStickX = joysticks.get(0);
            leftStickY = -1 * joysticks.get(1);

            rightStickX = -joysticks.get(3);
        }
        double deadzone = .3f;
        if (leftStickY < deadzone && leftStickY > -deadzone) {
            leftStickY = 0;
        }
        if (leftStickX < deadzone && leftStickX > -deadzone) {
            leftStickX = 0;
        }
        if (rightStickX < deadzone && rightStickX > -deadzone) {
            rightStickX = 0;
        }
        SimpleMatrix wheelV = bot.velocityToWheel(leftStickX, leftStickY, rightStickX);

        bot.wheelFL = (double) wheelV.get(0, 0);
        bot.wheelBL = (double) wheelV.get(2, 0);
        bot.wheelFR = (double) wheelV.get(1, 0);
        bot.wheelBR = (double) wheelV.get(3, 0);

    }

    @Override
    public void reset() {

    }

}

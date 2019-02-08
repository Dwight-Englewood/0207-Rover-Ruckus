package wen.sim;

import org.ejml.simple.SimpleMatrix;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

class MecanumDriveJoy implements MecanumDriveMode {

    public void updateWheelPower(long window, MecanumRobot bot) {
        FloatBuffer joysticks = glfwGetJoystickAxes(GLFW_JOYSTICK_1);

        float leftStickX = joysticks.get(0);
        float leftStickY = -1 * joysticks.get(1);

        float rightStickX = -joysticks.get(2);
        float rightStickY = -1 * joysticks.get(3);

        float deadzone = .3f;
        if (leftStickY < deadzone && leftStickY > -deadzone) {
            leftStickY = 0;
        }
        if (leftStickX < deadzone && leftStickX > -deadzone) {
            leftStickX = 0;
        }
        if (rightStickX < deadzone && rightStickX > -deadzone) {
            rightStickX = 0;
        }

        SimpleMatrix wheelV = bot.velocityToWheel(leftStickY, leftStickX, rightStickX);

        bot.wheelFL = (float) wheelV.get(0, 0);
        bot.wheelBL = (float) wheelV.get(2, 0);
        bot.wheelFR = (float) wheelV.get(1, 0);
        bot.wheelBR = (float) wheelV.get(3, 0);

        System.out.println(bot.wheelFL);
        System.out.println(bot.wheelFR);
        System.out.println(bot.wheelBL);
        System.out.println(bot.wheelBR);

    }

}

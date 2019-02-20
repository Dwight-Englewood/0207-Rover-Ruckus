package wen.sim.bodies.mecanumRobot.driveFunction.teleop;

import org.ejml.simple.SimpleMatrix;

import wen.control.function.Coordinate;
import wen.sim.bodies.mecanumRobot.MecanumRobot;
import wen.sim.bodies.mecanumRobot.driveFunction.MecanumDriveMode;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class MecanumDriveKey implements MecanumDriveMode {

    public void updateWheelPower(long window, MecanumRobot bot) {
        double x = 0;
        double y = 0;
        double r = 0;
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            y = 1;
        } else if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            y = -1;
        }
        if (glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS) {
            r = 1;
        } else if (glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS) {
            r = -1;
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            x = -1;
        } else if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            x = 1;
        }

        SimpleMatrix wheelV = bot.velocityToWheel(x, y, r);
        bot.wheelFL = (double) wheelV.get(0, 0);
        bot.wheelBL = (double) wheelV.get(2, 0);
        bot.wheelFR = (double) wheelV.get(1, 0);
        bot.wheelBR = (double) wheelV.get(3, 0);

    }

    @Override
    public void reset() {

    }

}

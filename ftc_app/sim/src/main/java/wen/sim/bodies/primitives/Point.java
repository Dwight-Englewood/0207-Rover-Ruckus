package wen.sim.bodies.primitives;

import org.lwjgl.opengl.GL;

import java.nio.FloatBuffer;

import wen.control.function.Coordinate;
import wen.sim.bodies.Drawable;

import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickAxes;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static wen.sim.openglfix.OpenGLFix.glColor3f;
import static wen.sim.openglfix.OpenGLFix.glTranslatef;
import static wen.sim.openglfix.OpenGLFix.glVertex2f;

public class Point implements Drawable {

    public Coordinate loc;
    public double r, g, b;
    public double radius;
    public int togglebutton;

    public Point(Coordinate loc, double r, double g, double b, double radius, int togglebutton) {
        this.loc = loc;
        this.r = r;
        this.g = g;
        this.b = b;
        this.radius = radius;
        this.togglebutton = togglebutton;
    }

    @Override
    public void drawData(long window) {
        GL.createCapabilities();
        glLoadIdentity();
        glColor3f(r, g, b);
        glTranslatef(this.loc.x, this.loc.y, 0);
        glBegin(GL_POLYGON);
        for (int i = 0; i < 360; i++) {
            glVertex2f(radius * Math.cos(Math.toRadians(i)), radius * Math.sin(Math.toRadians(i)));
        }
        glEnd();


    }

    @Override
    public void drawState(long window) {

    }

    @Override
    public void reset() {

    }

    @Override
    public void update(long window) {
        FloatBuffer joysticks = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        //Microsoft X-Box 360 pad
        //Logitech Logitech Dual Action
        double leftStickX = 0, leftStickY = 0;
        if (glfwGetJoystickName(GLFW_JOYSTICK_1).equals("Logitech Logitech Dual Action")) {
            leftStickX = joysticks.get(2);
            leftStickY = -1 * joysticks.get(3);


        } else if (glfwGetJoystickName(GLFW_JOYSTICK_1).equals("Microsoft X-Box 360 pad")) {
            leftStickX = joysticks.get(3);
            leftStickY = -1 * joysticks.get(4);

        }
        double deadzone = .3f;
        if (leftStickY < deadzone && leftStickY > -deadzone) {
            leftStickY = 0;
        }
        if (leftStickX < deadzone && leftStickX > -deadzone) {
            leftStickX = 0;
        }
        if (glfwGetKey(window, this.togglebutton) == GLFW_PRESS) {
            this.loc.x = this.loc.x + leftStickX/30;
            this.loc.y = this.loc.y + leftStickY/30;
        }
    }
}

package wen.sim.openglfix;

import org.lwjgl.opengl.GL11;

public class OpenGLFix {

    public static void glVertex2f(double x, double y) {
        GL11.glVertex2f((float) x, (float) y);
    }

    public static void glVertex3f(double x, double y, double z) {
        GL11.glVertex3f((float) x, (float) y, (float) z);
    }

    public static void glColor3f(double r, double g, double b) {
        GL11.glColor3f((float) r, (float) g, (float) b);
    }

    public static void glTranslatef(double v, double v1, double v2) {
        GL11.glTranslatef((float) v, (float) v1, (float) v2);
    }

    public static void glRotatef(double v, double v1, double v2, double v3) {
        GL11.glRotatef((float) v, (float) v1, (float) v2, (float) v3);
    }

}

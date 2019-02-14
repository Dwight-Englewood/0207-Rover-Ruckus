package wen.sim.bodies.graphing;

import org.lwjgl.opengl.GL;

import wen.control.function.Function;
import wen.sim.bodies.Drawable;

import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static wen.sim.openglfix.OpenGLFix.glColor3f;
import static wen.sim.openglfix.OpenGLFix.glVertex3f;

public class FunctionGrapher implements Drawable {

    public double r;
    public double g;
    public double b;
    Function f;

    public FunctionGrapher(Function f, double r, double g, double b) {
        this.f = f;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public void drawState(long window) {

    }

    public void drawData(long window) {
        GL.createCapabilities();
        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glColor3f(r, g, b);

        glBegin(GL_POINTS);
        for (double i = this.f.xMin; i < this.f.xMax; i = i + (this.f.xMax - this.f.xMin) / 1000) {
            double c = this.f.eval(i);
            glVertex3f(i, c, 0);


        }
        glEnd();
        glFlush();
    }

    @Override
    public void reset() {

    }

    @Override
    public void update(long window) {

    }
}

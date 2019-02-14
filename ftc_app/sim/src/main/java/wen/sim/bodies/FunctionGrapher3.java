package wen.sim.bodies;

import org.lwjgl.opengl.GL;

import wen.control.function.Coordinate;
import wen.control.function.Function;
import wen.control.function.ParamatricFunction;

import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static wen.sim.openglfix.OpenGLFix.glColor3f;
import static wen.sim.openglfix.OpenGLFix.glVertex3f;

public class FunctionGrapher3 implements Drawable {

    public double r;
    public double g;
    public double b;
    Function f;

    public FunctionGrapher3(Function f, double r, double g, double b) {
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
        for (double i = this.f.tMin; i < this.f.tMax; i = i + (this.f.tMax - this.f.tMin) / 1000) {
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

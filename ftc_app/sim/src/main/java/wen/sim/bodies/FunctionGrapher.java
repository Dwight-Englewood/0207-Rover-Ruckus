package wen.sim.bodies;

import org.lwjgl.opengl.GL;

import wen.control.Coordinate;
import wen.control.ParamatricFunction;

import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class FunctionGrapher implements Drawable {

    ParamatricFunction f;
    public float r;
    public float g;
    public float b;

    public FunctionGrapher(ParamatricFunction f, float r, float g, float b) {
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
        glColor3f(r,g,b);

        glBegin(GL_POINTS);
        for (double i = this.f.tMin; i < this.f.tMax; i = i + (this.f.tMax - this.f.tMin)/1000) {
            Coordinate c = this.f.eval(i);
            double x = c.x;
            double y = c.y;
            glVertex3f((float)x , (float)y , 0);
            try {
                c = this.f.eval(i);
                x = c.x;
                y = c.y;
                glVertex3f((float)x , (float)y , 0);
            } catch (IndexOutOfBoundsException error) {
            }

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

package wen.sim.bodies;

import org.lwjgl.opengl.GL;

import wen.control.Coordinate;
import wen.control.PathFunction;
import wen.control.Trajectory;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class TrajectorySim extends Trajectory implements Drawable {
    public TrajectorySim(Float[] xCoords, Float[] yCoords) {
        super(xCoords, yCoords);
    }

    public TrajectorySim(PathFunction pf) {
        super(pf);
    }

    @Override
    public void drawState(long window) {

    }

    @Override
    public void drawData(long window) {
        GL.createCapabilities();
        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glBegin(GL_POINTS);
        for (int i = 0; i < this.xCoords.size(); i++) {
            float x = xCoords.get(i);
            float y = yCoords.get(i);
            glColor3f(1,1,1);
            glVertex3f(x , y , 0);
            try {
                x = xCoords.get(i);
                y = yCoords.get(i);
                glVertex3f(x , y , 0);
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

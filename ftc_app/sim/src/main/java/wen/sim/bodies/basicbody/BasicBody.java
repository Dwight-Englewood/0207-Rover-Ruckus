package wen.sim.bodies.basicbody;

import org.lwjgl.opengl.GL;

import java.util.ArrayList;

import wen.control.function.Coordinate;
import wen.sim.bodies.Body;

import static java.lang.Math.toDegrees;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static wen.sim.openglfix.OpenGLFix.glColor3f;
import static wen.sim.openglfix.OpenGLFix.glRotatef;
import static wen.sim.openglfix.OpenGLFix.glTranslatef;
import static wen.sim.openglfix.OpenGLFix.glVertex2f;
import static wen.sim.openglfix.OpenGLFix.glVertex3f;

public class BasicBody extends Body {

    BasicBodyDriveMode bbdm;

    ArrayList<Coordinate> path = new ArrayList<>();
    ArrayList<Coordinate> rot = new ArrayList<>();

    public BasicBody(double x, double y, double r, BasicBodyDriveMode bbdm) {
        this.bbdm = bbdm;
        super.botX = x;
        super.defaultX = x;
        super.botY = y;
        super.defaultY = y;
        super.botR = r;
        super.defaultR = r;
    }

    @Override
    public void reset() {
        bbdm.reset();
        path.clear();
        rot.clear();
        super.reset();
    }

    @Override
    public void update(long window) {
        bbdm.updateKinematics(window, this);
        this.path.add(new Coordinate(this.botX, this.botY));
        this.rot.add(new Coordinate(this.botX, this.botR));
    }


    public void drawState(long window) {
        GL.createCapabilities();
        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glPushMatrix();
        //glScalef(1 / viewSize, 1 / viewSize, 1);
        glTranslatef(this.botX / 10, this.botY / 10, (double) 0);
        glRotatef((double) toDegrees(this.botR), 0, 0, 1);
        //glScalef(1 / viewSize, 1 / viewSize, 1);
        glColor3f(0.2f, 0.2f, 1.0f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glBegin(GL_POLYGON);
        double boxHalfDim = .1f;
        glColor3f(.7f, 0.2f, 1.0f);
        glVertex2f(boxHalfDim, boxHalfDim);
        glVertex2f(-boxHalfDim, boxHalfDim);
        glColor3f(0.5f, 0.8f, .2f);
        glVertex2f(-boxHalfDim, -boxHalfDim);
        glVertex2f(boxHalfDim, -boxHalfDim);
        glEnd();
        glPopMatrix();
        glFlush();
    }


    @Override
    public void drawData(long window) {
        GL.createCapabilities();
        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        glBegin(GL_LINES);
        glColor3f(1, 0, 0);

        for (int i = 0; i < path.size(); i++) {
            Coordinate e = path.get(i);
            glVertex3f((double) e.x / 10, (double) e.y / 10, 0);
            try {
                e = path.get(i + 1);
                glVertex3f((double) e.x / 10, (double) e.y / 10, 0);
            } catch (IndexOutOfBoundsException error) {
            }

        }
        glEnd();

        glBegin(GL_LINES);

        int start = 0;
        if (rot.size() > 300) {
            start = rot.size() - 300;
        }
        int j = 0;
        for (int i = start; i < rot.size(); i++) {
            Coordinate e = rot.get(i);
            glColor3f(Math.abs(225 - i % 450) / 255f, 100f, Math.abs(225 - i % 450) / 255f);
            double y = (e.y + Math.PI);
            double fixedRotation = (y % (Math.PI * 2)) < 0 ? Math.PI * 2 + (y % (Math.PI * 2)) : (y % (Math.PI * 2));
            glVertex3f(j / 200f - 1f, (double) (fixedRotation / (Math.PI * 2)) / 2f - 1, 0);
            try {
                e = rot.get(i + 1);
                y = (e.y + Math.PI);
                fixedRotation = (y % (Math.PI * 2)) < 0 ? Math.PI * 2 + (y % (Math.PI * 2)) : (y % (Math.PI * 2));

                glVertex3f((j + 1) / 200f - 1f, (double) (fixedRotation / (Math.PI * 2)) / 2f - 1, 0);
            } catch (IndexOutOfBoundsException error) {
            }
            j++;
        }
        glEnd();


        glFlush();

    }

}

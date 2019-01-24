# coding: utf-8
from OpenGL.GL import * # OpenGL
from OpenGL.GLU import * # OpenGL 
#from OpenGL.GLUT import * # OpenGL 
#import cyglfw3 as glfw
from pyglfw.libapi import *
from math import *
import numpy as np


ye = False

buffers=None

vertices=[.1]


# Constants
#botMass = 15
#maxMotorForce = 2
#friction = 1

colors=[.1]
botX = 0
botY = 0
botXV = 0
botYV = 0
botXF = 0
botYF = 0
botR = 0
botRV = 0
botRT = 0

def initialize():
    glClearColor(0.125, 0.125, 0.125, 0) # set background color
    glClearDepth(1.0) # sets default depth - used by opengl to determine order of objects
    glDepthFunc(GL_LESS) # depth criteria
    glEnable(GL_DEPTH_TEST) # enable the testing of depth 

def draw():
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)      # Clears the screen
    glLoadIdentity()                                        # Loads the identity matrix for translation/scaling
    glPushMatrix()                                          # Sandboxes changes to the matrix
    #glScalef(1/viewSize, 1/viewSize, 1)                     # makes it not the entire screen
    glTranslatef(botX/1000, botY/1000, 0)
    glRotatef(botR, 0, 0, 1)
    #glScalef(1/viewSize, 1/viewSize, 1)                     # makes it not the entire screen
    #drawNewtons()                                           # draws the newtons fractal
    glColor3f(0.2, 0.2, 1.0)
    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
    glBegin(GL_POLYGON)
    boxHalfDim = .05
    glColor3f(0.2, 0.2, 1.0)
    glVertex2f(boxHalfDim,boxHalfDim)
    glVertex2f(-boxHalfDim,boxHalfDim)
    glColor3f(0.2, 0.8, 1.0)
    glVertex2f(-boxHalfDim,-boxHalfDim)
    glVertex2f(boxHalfDim,-boxHalfDim)
    glEnd()
    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    glPopMatrix()                                           # resets matrix transforms
    glFlush()

def create_vbo():
    buffers = glGenBuffers(2)                           # Create a buffer object - just a list of vertex locations and colors
    glBindBuffer(GL_ARRAY_BUFFER, buffers[0])           # Tell OpenGL to write to the 0th buffer
    glBufferData(GL_ARRAY_BUFFER,                       # Copy vertex location data to the GPU
            len(vertices)*4, # byte size 
            (ctypes.c_float*len(vertices))(*vertices),
            GL_STATIC_DRAW)
    glBindBuffer(GL_ARRAY_BUFFER, buffers[1])           # Tell OpenGL to write to the 1st buffer
    glBufferData(GL_ARRAY_BUFFER,                       # Copy color data to the GPU
            len(colors)*4, # byte size 
            (ctypes.c_float*len(colors))(*colors),
            GL_STATIC_DRAW)
    return buffers

def draw_vbo():
    glEnableClientState(GL_VERTEX_ARRAY);       # Tell OpenGL we have a vertex array
    glEnableClientState(GL_COLOR_ARRAY);        # Tell OpenGL we have a color array
    glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);  # Bind the buffer with vertex data
    glVertexPointer(2, GL_FLOAT, 0, None);      # Read the vertex data in pairs to get the coordinate locations
    glBindBuffer(GL_ARRAY_BUFFER, buffers[1]);  # Bind the buffer with color data
    glColorPointer(3, GL_FLOAT, 0, None);       # Read the color data in groups of 3 - R,G,B - to determine the color of the corresponding vertex
    glDrawArrays(GL_POINTS, 0, len(vertices));  # Draw the points to the screen
    glDisableClientState(GL_COLOR_ARRAY)        # Reset OpenGL state
    glDisableClientState(GL_VERTEX_ARRAY);

def drawNewtons():
    global buffers, ye
    if (not ye):                # Ensures we only make the VBO once, to prevent lag
        buffers=create_vbo()    # If the VBO has not been set, create it
        ye = True
    draw_vbo()                  # Draw the VBO to the screen

def disp_func():
    draw()

def update_func():
    global botX, botY, botXV, botYV, botR, botRV
    botX = botX + botXV
    botY = botY + botYV
    botR = botR + botRV

def idle_func():
    global botX, botY, botXV, botYV, botR, botRV
    for i in range(0,100): # look im not dealing with integer roundoff error
        if (botXV > 0):
            botXV = botXV - .01
        elif (botXV < 0):
            botXV = botXV + .01
        if (botYV > 0):
            botYV = botYV - .01
        elif (botYV < 0):
            botYV = botYV + .01
    for i in range(0,20):
        if (botRV > 0):
            botRV = botRV - .01
        elif(botRV < 0):
            botRV = botRV + .01
    update_func()


def fieldCentric(window):
    global botX, botY, botXV, botYV, botRV, botR
    if (glfwGetKey(window, GLFW_KEY_W ) == GLFW_PRESS):
        botYV = botYV + 1
    if (glfwGetKey(window, GLFW_KEY_S ) == GLFW_PRESS):
        botYV = botYV - 1
    if (glfwGetKey(window, GLFW_KEY_D ) == GLFW_PRESS):
        botXV = botXV + 1
    if (glfwGetKey(window, GLFW_KEY_A ) == GLFW_PRESS):
        botXV = botXV - 1
    if (glfwGetKey(window, GLFW_KEY_Q ) == GLFW_PRESS):
        botRV = botRV + 1
    if (glfwGetKey(window, GLFW_KEY_E ) == GLFW_PRESS):
        botRV = botRV - 1

def fieldCentricJoystick(stickX, stickY, rightStickX):
    dab = velocityToWheel(stickX, stickY, rightStickX)
    wheelToVelocity(dab[0], dab[1], dab[2], dab[3])

def tankDrive(leftStickY, rightStickY, leftTrigger, rightTrigger):
    print(leftTrigger)
    if (leftTrigger > .5):
        botRV = leftTrigger
    elif (rightTrigger > .5):
        botRV = -rightTrigger
    else:
        wheelToVelocity(leftStickY, rightStickY, rightStickY, leftStickY)
def velocityToWheel(vx,vy,vr):
    dab = np.matmul(np.array([[1,-1,-1],[1,1,1],[1,1,-1],[1,-1,1]]), np.array([vx,vy,vr]))
    return dab


def wheelToVelocity(w1, w2, w3, w4):
    global botXV, botYV, botRV
    dab = np.matmul(np.array([[1,1,1,1],[1,-1,-1,1],[-1,1,-1,1]]), np.array([w1,w2,w3,w4]))
    botXV = dab[0]
    botYV = dab[1]
    botRV = dab[2]

def main():
    global botX, botY, botXV, botYV, botRV, botR
    # Initialize the library
    if not glfwInit():
        return
    # Create a windowed mode window and its OpenGL context
    window = glfwCreateWindow(1000, 950, b'float', None, None)

    if not window:
        glfwTerminate()
        return

    # Make the window's context current
    glfwMakeContextCurrent(window)

    # Loop until the user closes the window
    while not glfwWindowShouldClose(window):
        # Render here, e.g. using pyOpenGL
        deadzone = 0
        fieldCentric(window)
        joysticks = glfwGetJoystickAxes(GLFW_JOYSTICK_1)
        # axes mapping
        # 0 - left stick x
        # 1 - left stick y (inverted)
        # 2 - left trigger (down is -1)
        # 3 - right stick x
        # 4 - right stick y (inverted)
        # 5 - right trigger (down is -1)
        # 6 - dpad x
        # 7 dpad y (inverted)

        leftStickX = joysticks[0]
        leftStickY = joysticks[1]

        rightStickX = joysticks[3]
        rightStickY = -1*joysticks[4]

        leftTrigger = (joysticks[2]+1)
        rightTrigger = (joysticks[5]+1)
        #fieldCentricJoystick(leftStickX, leftStickY, rightStickX)
        tankDrive(leftStickY, rightStickY, leftTrigger, rightTrigger)
        update_func()
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS):
            break
        if (glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS):
            botXV = 0
            botYV = 0
            botX = 0
            botY = 0
            botR = 0
            botRV = 0
        if (glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS):
            print(botXV)
            print(botYV)
        disp_func()
        idle_func()
        # Swap front and back buffers
        glfwSwapBuffers(window)

        # Poll for and process events
        glfwPollEvents()

    glfwTerminate()

if __name__ == "__main__":
    main()

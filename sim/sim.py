# coding: utf-8
from OpenGL.GL import * # OpenGL
from OpenGL.GLU import * # OpenGL 
#from OpenGL.GLUT import * # OpenGL 
#import cyglfw3 as glfw
from pyglfw.libapi import *
from math import *
import numpy as np
import time


ye = False

buffers=None

vertices=[.1]

colors=[.1]

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
    boxHalfDim = .1 # corresponds to 20cm 20cm
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


lasttime = time.time()

botMass = 5
friction = -20

#i think this means my motors can supply 5000 newtons lol
linearMotorScale = 5000
rotationMotorScale = 3000


botX = 0
botXD = 0
botXDD = 0

botY = 0
botYD = 0
botYDD = 0

botR = 0
botRD = 0
botRDD = 0

botXF = 0
botYF = 0
botRF = 0

wheelRadius = 5

wheelFR = 0
wheelFL = 0
wheelBL = 0
wheelBR = 0

def idle_func(window, drive):
    drive(window)
    updateForce()
    forceToAcceleration()
    simulateStep()
    disp_func()

def simulateStep():
    global botX, botY, botR, botXD, botYD, botRD, botXDD, botYDD, botRDD, lasttime
    deltaTime = time.time() - lasttime
    botXD = botXD + botXDD * deltaTime
    botYD = botYD + botYDD * deltaTime
    botRD = botRD + botRDD * deltaTime
    botX = botX + botXD * deltaTime
    botY = botY + botYD * deltaTime
    botR = botR + botRD * deltaTime
    lasttime = time.time()

def forceToAcceleration():
    global botXF, botYF, botRF, botMass, botXDD, botYDD, botRDD
    botXDD = botXF / botMass
    botYDD = botYF / botMass
    botRDD = botRF / botMass

def updateForce():
    global wheelFR, wheelFL, wheelBL, wheelBR, botXF, botYF, botRF, botXD, botYD, botRD
    forces = wheelToForce(wheelFL, wheelFR, wheelBL, wheelBR)
    botXF = linearMotorScale*forces[1] + friction*botXD
    botYF = linearMotorScale*forces[0] + friction*botYD
    botRF = rotationMotorScale*forces[2] + friction*botRD

def fieldCentricJoystick(stickX, stickY, rightStickX):
    dab = velocityToWheel(stickX, stickY, rightStickX)
    wheelToForce(dab[0], dab[1], dab[2], dab[3])

def tankDriveJoy(window):
    global wheelFR, wheelFL, wheelBL, wheelBR
    '''Once the modules are loaded, you should be able to find a new device: /dev/input/js0 and a file ending with -event-joystick in /dev/input/by-id directory. You can simply cat those devices to see if the joystick works - move the stick around, press all the buttons - you should see mojibake printed when you move the sticks or press buttons.'''
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
    leftStickY = -1*joysticks[1]

    rightStickX = joysticks[3]
    rightStickY = -1*joysticks[4]

    leftTrigger = (joysticks[2]+1)
    rightTrigger = (joysticks[5]+1)

    deadzone = .3
    if (leftStickY < deadzone and leftStickY > -deadzone):
        leftStickY = 0
    if (rightStickY < deadzone and rightStickY > -deadzone):
        rightStickY = 0

    wheelFL = leftStickY
    wheelBL = leftStickY
    wheelFR = rightStickY
    wheelBR = rightStickY

    if (leftTrigger > .5):
        wheelFR = 1
        wheelFL = -1
        wheelBL = 1
        wheelBR = -1
    if (rightTrigger > .5):
        wheelFR = -1
        wheelFL = 1
        wheelBL = -1
        wheelBR = 1


def tankDriveKey(window):
    global wheelFR, wheelFL, wheelBL, wheelBR
    if (glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS):
        wheelFL = 1
        wheelBL = 1
    elif (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS):
        wheelFL = -1
        wheelBL = -1
    else:
        wheelFL = 0
        wheelBL = 0
    if (glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS):
        wheelFR = 1
        wheelBR = 1
    elif (glfwGetKey(window, GLFW_KEY_L) == GLFW_PRESS):
        wheelFR = -1
        wheelBR = -1
    else:
        wheelFR = 0
        wheelBR = 0


def compassDrive(window):
    global wheelFR, wheelFL, wheelBL, wheelBR
    if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS):
        wheelFR = 1
        wheelFL = 1
        wheelBL = 1
        wheelBR = 1
    elif (glfwGetKey(window, GLFW_KEY_S ) == GLFW_PRESS):
        wheelFR = -1
        wheelFL = -1
        wheelBL = -1
        wheelBR = -1
    elif (glfwGetKey(window, GLFW_KEY_Q ) == GLFW_PRESS):
        wheelFR = 1
        wheelFL = -1
        wheelBL = -1
        wheelBR = 1
    elif (glfwGetKey(window, GLFW_KEY_E ) == GLFW_PRESS):
        wheelFR = -1
        wheelFL = 1
        wheelBL = 1
        wheelBR = -1
    elif (glfwGetKey(window, GLFW_KEY_A ) == GLFW_PRESS):
        wheelFR = 1
        wheelFL = -1
        wheelBL = 1
        wheelBR = -1
    elif (glfwGetKey(window, GLFW_KEY_D ) == GLFW_PRESS):
        wheelFR = -1
        wheelFL = 1
        wheelBL = -1
        wheelBR = 1
    else:
        wheelFR = 0
        wheelFL = 0
        wheelBL = 0
        wheelBR = 0

def getJ(t):
    a = sqrt(2)/2
    j = np.matmul(np.array([[a,a,-1],[a,-a,1],[-a,-a,-1],[-a,a,1]]),np.array([[cos(t),sin(t),0],[-sin(t),cos(t),0],[0,0,1]]))
    return j

def velocityToWheel(vx,vy,vr):
    global botR, wheelRadius
    dab = -(sqrt(2)/wheelRadius)*np.matmul(getJ(botR), np.array([vx,vy,vr]))
    return dab

def wheelToForce(w1, w2, w3, w4):
    global botR, wheelRadius
    t = radians(botR)
    #dab = wheelRadius*(-sqrt(2)/2)*np.matmul(np.linalg.pinv(getJ(radians(botR))), np.array([w1,w2,w3,w4]))
    merp = np.matmul(np.array([[cos(t),sin(t),0],[-sin(t),cos(t),0],[0,0,1]]), np.array([[1,1,1,1],[1,-1,-1,1],[-.5,.5,-.5,.5]]))
    dab = np.matmul(merp, np.array([w1,w2,w3,w4]))
    return dab

def main():
    np.set_printoptions(5,suppress=True)
    global botX, botY, botR, botXD, botYD, botRD, botXDD, botYDD, botRDD
    # Initialize the library
    if not glfwInit():
        return
    # Create a windowed mode window and its OpenGL context
    #glfwWindowHint(GLFW_SAMPLES, 4);

    window = glfwCreateWindow(1000, 1000, b'float', None, None)
    #glEnable(GL_MULTISAMPLE)


    if not window:
        glfwTerminate()
        return

    # Make the window's context current
    glfwMakeContextCurrent(window)
    driveType = compassDrive

    # Loop until the user closes the window
    while not glfwWindowShouldClose(window):
        # Render here, e.g. using pyOpenGL
        #fieldCentricJoystick(leftStickX, leftStickY, rightStickX)
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS):
            break
        if (glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS):
            botX = 0
            botXD = 0
            botXDD = 0
            botY = 0
            botYD = 0
            botYDD = 0
            botR = 0
            botRD = 0
            botRDD = 0
        if (glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS):
            print(botXDD)
            print(botYDD)
            print(botRDD)
        if (glfwGetKey(window, GLFW_KEY_N) == GLFW_PRESS):
            driveType = compassDrive
        if (glfwGetKey(window, GLFW_KEY_M) == GLFW_PRESS):
            driveType = tankDriveKey
        if (glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS):
            driveType = tankDriveJoy
        #disp_func()
        idle_func(window, driveType)
        # Swap front and back buffers
        glfwSwapBuffers(window)

        # Poll for and process events
        glfwPollEvents()

    glfwTerminate()

if __name__ == "__main__":
    main()

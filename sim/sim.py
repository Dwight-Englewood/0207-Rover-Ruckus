# coding: utf-8
from OpenGL.GL import * # OpenGL
from OpenGL.GLU import * # OpenGL 
#from OpenGL.GLUT import * # OpenGL 
#import cyglfw3 as glfw
from pyglfw.libapi import *
from math import *
import numpy as np
import time
import pid


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
    global target
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
    glPopMatrix()

    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
    glBegin(GL_POLYGON)
    glColor3f(1, 0.5, .5)
    glVertex2f(0,target/1000)
    glVertex2f(1,target/1000)
    glVertex2f(1,(target+10)/1000)
    glVertex2f(0,(target+10)/1000)
    glEnd()

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

botY = -800
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

bounded = False
shouldNorm = True

def idle_func(window, powerFunction, normFunction):
    powerFunction(window)
    normFunction()
    updateForce()
    forceToAcceleration()
    simulateStep()
    disp_func()

def normWheel():
    global wheelFL, wheelFR, wheelBL, wheelBR
    largest = max([abs(wheelFL), abs(wheelFR), abs(wheelBR), abs(wheelBL)])
    if (largest != 0):
        wheelFL = wheelFL / largest
        wheelFR = wheelFR / largest
        wheelBL = wheelBL / largest
        wheelBR = wheelBR / largest

def simulateStep():
    global botX, botY, botR, botXD, botYD, botRD, botXDD, botYDD, botRDD, lasttime, bounded
    deltaTime = time.time() - lasttime
    botXD = botXD + botXDD * deltaTime
    botYD = botYD + botYDD * deltaTime
    botRD = botRD + botRDD * deltaTime
    botX = botX + botXD * deltaTime
    botY = botY + botYD * deltaTime
    botR = botR + botRD * deltaTime
    if (bounded):
        if (botX > 1000):
            botX = 1000
        elif (botX < -1000):
            botX = -1000
        if (botY > 1000):
            botY = 1000
        elif (botY < -1000):
            botY = -1000
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

def fieldCentricKey(window):
    global wheelFR, wheelFL, wheelBL, wheelBR
    x = 0
    y = 0
    r = 0
    if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS):
        y = 1
    elif (glfwGetKey(window, GLFW_KEY_S ) == GLFW_PRESS):
        y = -1
    if (glfwGetKey(window, GLFW_KEY_Q ) == GLFW_PRESS):
        r = -1
    elif (glfwGetKey(window, GLFW_KEY_E ) == GLFW_PRESS):
        r = 1
    if (glfwGetKey(window, GLFW_KEY_A ) == GLFW_PRESS):
        x = -1
    elif (glfwGetKey(window, GLFW_KEY_D ) == GLFW_PRESS):
        x = 1

    wheelV = velocityToWheel(y, x, r)
    wheelFL = wheelV[0]
    wheelBL = wheelV[2]
    wheelFR = wheelV[1]
    wheelBR = wheelV[3]

def fieldCentricJoy(window):
    global wheelFR, wheelFL, wheelBL, wheelBR
    joysticks = glfwGetJoystickAxes(GLFW_JOYSTICK_1)

    leftStickX = joysticks[0]
    leftStickY = -1*joysticks[1]

    rightStickX = -joysticks[3]
    rightStickY = -1*joysticks[4]

    deadzone = .3
    if (leftStickY < deadzone and leftStickY > -deadzone):
        leftStickY = 0
    if (leftStickX < deadzone and leftStickX > -deadzone):
        leftStickX = 0
    if (rightStickX < deadzone and rightStickX > -deadzone):
        rightStickX = 0

    wheelV = velocityToWheel(leftStickY, leftStickX, rightStickX)

    wheelFL = wheelV[0]
    wheelBL = wheelV[2]
    wheelFR = wheelV[1]
    wheelBR = wheelV[3]

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
    return np.matmul(np.array([[cos(t),sin(t),0],[-sin(t),cos(t),0],[0,0,1]]), np.array([[1,1,1,1],[1,-1,-1,1],[-.5,.5,-.5,.5]]))

def velocityToWheel(vx,vy,vr):
    global botR, wheelRadius
    return((1/wheelRadius)*4*np.matmul(np.linalg.pinv(getJ(radians(botR))), np.array([vx,vy,vr])))

def wheelToForce(w1, w2, w3, w4):
    global botR, wheelRadius
    return (wheelRadius*0.25*np.matmul(getJ(radians(botR)), np.array([w1,w2,w3,w4])))

target = 700

pidC = pid.PIDController(1,.0001,20,target)

def pidLoop(window):
    global botX, wheelFL, wheelFR, wheelBR, wheelBL
    pidC.updateError(botY)
    print(pidC.error)
    halp = pidC.correction()
    wheels = velocityToWheel(halp,0, 0)
    wheelFL = wheels[0]
    wheelFR = wheels[1]
    wheelBL = wheels[2]
    wheelBR = wheels[3]

def normPID():
    global wheelBL, wheelBR, wheelFR, wheelFL
    if (wheelBL > 1):
        wheelBL = 1
    elif (wheelBL < -1):
        wheelBL = -1
    if (wheelBR > 1):
        wheelBR = 1
    elif (wheelBR < -1):
        wheelBR = -1
    if (wheelFL > 1):
        wheelFL = 1
    elif (wheelFL < -1):
        wheelFL = -1
    if (wheelFR > 1):
        wheelFR = 1
    elif (wheelFR < -1):
        wheelFR = -1
def main():
    np.set_printoptions(5,suppress=True)

    global botX, botY, botR, botXD, botYD, botRD, botXDD, botYDD, botRDD, shouldNorm

    if not glfwInit():
        return
    #glfwWindowHint(GLFW_SAMPLES, 4); # uncomment for AA, looks weird though

    window = glfwCreateWindow(1000, 1000, b'float', None, None)
    #glEnable(GL_MULTISAMPLE) # uncomment for AA, looks weird though

    if not window:
        glfwTerminate()
        return

    # Make the window's context current
    glfwMakeContextCurrent(window)
    powerFunction = compassDrive
    normFunction = normWheel

    # Loop until the user closes the window
    while not glfwWindowShouldClose(window):
        # Render here, e.g. using pyOpenGL
        if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS):
            break
        if (glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS):
            botX = 0
            botXD = 0
            botXDD = 0
            botY = -800
            botYD = 0
            botYDD = 0
            botR = 0
            botRD = 0
            botRDD = 0
        #if (glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS):
        #    print(botXDD)
        #    print(botYDD)
        #    print(botRDD)
        if (glfwGetKey(window, GLFW_KEY_N) == GLFW_PRESS):
            powerFunction = compassDrive
            normFunction = normWheel
            print("Compass Drive")
        if (glfwGetKey(window, GLFW_KEY_M) == GLFW_PRESS):
            powerFunction = tankDriveKey
            normFunction = normWheel
            print("Tank Drive - Keyboard")
        if (glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS):
            powerFunction = tankDriveJoy
            normFunction = normWheel
            print("Tank Drive - Joystick")
        if (glfwGetKey(window, GLFW_KEY_V) == GLFW_PRESS):
            powerFunction = fieldCentricJoy
            normFunction = normWheel
            print("Field Centric Drive - Joystick")
        if (glfwGetKey(window, GLFW_KEY_C) == GLFW_PRESS):
            powerFunction = fieldCentricKey
            normFunction = normWheel
            print("Field Centric Drive - Keyboard")
        if (glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS):
            powerFunction = pidLoop
            normFunction = normPID
            print("PID")

        idle_func(window, powerFunction, normFunction)

        glfwSwapBuffers(window)

        glfwPollEvents()

    glfwTerminate()

if __name__ == "__main__":
    main()

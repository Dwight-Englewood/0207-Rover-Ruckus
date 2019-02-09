package wen.sim.simulator;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Sim {


    // The windowState handle
    private long windowState;
    private long windowData;

    Simulator sim;

    public Sim() {
        this.sim = new RobotSimulator();
    }

    public static void main(String[] args) {
        new Sim().run();
    }

    public void initialize() {
        GL.createCapabilities();
        glClearColor(0.125f, 0.125f, 0.125f, 0f);
        glClearDepth(1.0);
        glDepthFunc(GL_LESS);
        glEnable(GL_DEPTH_TEST);
    }

    public void run() {
        //System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        initialize();
        loop();

        // Free the windowState callbacks and destroy the windowState
        glfwFreeCallbacks(windowState);
        glfwDestroyWindow(windowState);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current windowState hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the windowState will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the windowState will be resizable

        // Create the windowState
        windowState = glfwCreateWindow(1000, 1000, "float", NULL, NULL);
        if (windowState == NULL)
            throw new RuntimeException("Failed to create the GLFW windowState");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowState, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the windowState size passed to glfwCreateWindow
            glfwGetWindowSize(windowState, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the windowState
            glfwSetWindowPos(
                    windowState,
                    1000,
                    0
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowState);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the windowData visible
        glfwShowWindow(windowState);

        windowData = glfwCreateWindow(1000, 1000, "float", NULL, NULL);
        if (windowData == NULL)
            throw new RuntimeException("Failed to create the GLFW windowData");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowData, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the windowData size passed to glfwCreateWindow
            glfwGetWindowSize(windowData, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the windowData
            glfwSetWindowPos(
                    windowData,
                    0,
                    0
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowData);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the windowData visible
        glfwShowWindow(windowData);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
        //glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the windowState or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(windowState)) {
            glfwMakeContextCurrent(windowState);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            sim.drawState(windowState);
            glfwSwapBuffers(windowState); // swap the color buffers

            glfwMakeContextCurrent(windowData);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            sim.drawData(windowData);
            glfwSwapBuffers(windowData);


            // Poll for windowState events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

}
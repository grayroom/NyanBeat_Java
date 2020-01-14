package nyanbeat;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.List;

public class NyanBeat_Java {
    // the window handle.
    private long window;

    private Shader shader;

    // window size.
    public static int WIDTH = 1280;
    public static int HEIGHT = 720;

    public void run() {
        init();
        loop();
        terminate();
    }

    private void init() {
        // Set error Callback.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        // Create Window
        window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, "NyanBeat_Java", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Center the window on screen
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(window, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);

        // Set keyCallback
        // Press esc -> close
        GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS) {
                    GLFW.glfwSetWindowShouldClose(window, true);
                }
            }
        };

        // Setup a key callback. It will be called every time a key is pressed,
        // repeated or released.
        GLFW.glfwSetKeyCallback(window, keyCallback);

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);

        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        // Make the window visible
        GLFW.glfwShowWindow(window);
    }

    public static int frames = 0;

    // TODO: 사각형 그리는 방법에 대해서 알아보자
    /* float[] vertices = { -0.5f,  0.5f, 0.0f, //vertex 1 : Top-left
            0.5f, 0.5f, 0.0f, //vertex 2 : Top-right
            0.5f, -0.5f, 0.0f, //vertex 3 : Bottom-right
            0.5f, -0.5f, 0.0f, //vertex 4 : Bottom-right
            -0.5f, -0.5f, 0.0f, //vertex 5 : Bottom-left
            -0.5f,  0.5f, 0.0f //vertex 6 : Top-left
    }; */
    float[] vertices = { 0, 0.5f, 0, -0.5f, -0.5f, 0f, 0.5f, -0.5f, 0 };

    float[] colors = {
            1.0f, 0.0f, 0.0f, //vertex 1 : RED (1,0,0)
            0.0f, 1.0f, 0.0f, //vertex 2 : GREEN (0,1,0)
            0.0f, 0.0f, 1.0f,  //vertex 3 : BLUE (0,0,1)
            0.0f, 0.0f, 1.0f,  //vertex 4 : BLUE (0,0,1)
            1.0f, 1.0f, 1.0f,  //vertex 5 : WHITE (1,1,1)
            1.0f, 0.0f, 0.0f //vertex 6 : RED (1,0,0)
    };

    // triangle numbers.
    private final int TRIANGLE_NUMBER = 9;

    // vao, vbo ids manager list.
    List<Integer> vaoIDs = new ArrayList<Integer>();
    List<Integer> vboIDs = new ArrayList<Integer>();

    private void bindVAO() {
        float multiplier = 1;
        // bind VAOs and put data.

        for (int i = 0; i < TRIANGLE_NUMBER; i++) {
            // generate vao
            int ithVAO = GL30.glGenVertexArrays();
            vaoIDs.add(ithVAO);

            // bind vao
            GL30.glBindVertexArray(ithVAO);

            // 1. bind 0 to VertexVBO
            int ithVertexVBO = GL15.glGenBuffers();
            vboIDs.add(ithVertexVBO);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ithVertexVBO);

            float[] ithVertices = new float[9];
            for (int j = 0; j < 9; j++) {
                ithVertices[j] = vertices[j] * multiplier;
            }

            multiplier *= 0.7f;

            // push ithVertices data to GL_ARRAY_BUFFER
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, ithVertices, GL15.GL_STATIC_DRAW);
            // set saved data's metadata
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
            //assign 0 to GL_ARRAY_BUFFER
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            // 2. bind 1(index) to ColorVBO
            int ithColorVBO = GL15.glGenBuffers();
            vboIDs.add(ithColorVBO);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ithColorVBO);
            // save color data to ithColors
            float[] ithColors = new float[9];
            for (int j = 0; j < 9; j++) {
                ithColors[j] = (float) Math.random();
            }

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ithColorVBO);
            //GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ithColorVBO);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, ithColors, GL15.GL_STATIC_DRAW);
            // set saved data's metadata
            GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 0, 0);
            // assign 0 to GL_ARRAY_BUFFER
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            // unbind vao
            GL30.glBindVertexArray(0);
        }
    }

    private void loop() {
        // makes the OpenGL bindings available for use.
        GL.createCapabilities();

        // OpenGL configures.
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        shader = new Shader("src/nyanbeat/shaders/vertexShader.txt", "src/nyanbeat/shaders/fragmentShader.txt");

        bindVAO();

        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClearColor(0.3f, 0.3f, 0.3f, 0.0f);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT); // clear the framebuffer

            render();

            // Poll for window events.
            GLFW.glfwPollEvents();

            GLFW.glfwSwapBuffers(window); // swap the color buffers
            // accumulate frame count.

        }

    }

    private void render() {

        // shader start
        shader.start();

        for (int vaoID : vaoIDs) {
            GL30.glBindVertexArray(vaoID);
            // buffer enable
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);

            // draw
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);

            // buffer disable
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
        }

        // shader exit
        shader.stop();

    }

    private void terminate() {
        // delete vao / vbo
        for (int vaoID : vaoIDs) {
            GL30.glDeleteVertexArrays(vaoID);
        }
        for (int vboID : vboIDs) {
            GL15.glDeleteBuffers(vboID);
        }

        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        new NyanBeat_Java().run();
    }
}

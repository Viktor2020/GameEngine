package testEngine;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Render;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
        Loader loader = new Loader();
        Render render = new Render();
        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,

                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f,
        };
        RawModel model = loader.loadToVAO(vertices);



        while (!Display.isCloseRequested()) {

            render.prepare();

            render.render(model);

            DisplayManager.updateDisplay();

        }

        loader.cleatUp();
        DisplayManager.closeDisplay();

    }

}

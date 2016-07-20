package testEngine;

import models.RawModel;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.Render;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
        Loader loader = new Loader();
        Render render = new Render();

        StaticShader shader = new StaticShader();

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
        };

        int[] indices = {
                0, 1, 3,
                3, 1, 2
        };
        float[] textureCoords = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };

        RawModel model = loader.loadToVAO(vertices,textureCoords, indices);
        ModelTexture modelTexture = new ModelTexture(loader.loadTexture("texture1"));

        TextureModel textureModel = new TextureModel(model, modelTexture);

        while (!Display.isCloseRequested()) {

            render.prepare();
            shader.start();
            render.render(textureModel);
            shader.stop();
            DisplayManager.updateDisplay();

        }
        shader.cleanUp();
        loader.cleatUp();
        DisplayManager.closeDisplay();

    }

}

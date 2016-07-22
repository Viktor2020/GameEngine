package testEngine;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
        Loader loader = new Loader();

        StaticShader shader = new StaticShader();
        Render render = new Render(shader);

        RawModel model = OBJLoader.loadObjModel("dragon", loader);
        ModelTexture modelTexture = new ModelTexture(loader.loadTexture("white"));

        TextureModel textureModel = new TextureModel(model, modelTexture);
        textureModel.getTexture().setShineDamper(10);
        textureModel.getTexture().setReflectivity(2);

        Entity entity = new Entity(textureModel, new Vector3f(0, 0, -25), 0, 0, 0, 1);

        Camera camera = new Camera();
        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 0.5f, 1));

        while (!Display.isCloseRequested()) {
            entity.increaseRotation(0, 1, 0);
            camera.move();
            render.prepare();
            shader.start();
            shader.loadLight(light);
            shader.loadViewMatrix(camera);
            render.render(entity, shader);
            shader.stop();
            DisplayManager.updateDisplay();

        }
        shader.cleanUp();
        loader.cleatUp();
        DisplayManager.closeDisplay();

    }

}

package testEngine;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
        Loader loader = new Loader();

        RawModel model = OBJLoader.loadObjModel("stall", loader);
        ModelTexture modelTexture = new ModelTexture(loader.loadTexture("stallTexture"));

        TextureModel textureModel = new TextureModel(model, modelTexture);
        textureModel.getTexture().setShineDamper(10);
        textureModel.getTexture().setReflectivity(2);

        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        int range = 30;
        for (int i = 0; i < 10; i++) {
            float x = random.nextFloat() * range - range / 2;
            float y = 0;//random.nextFloat() * range - range / 2;
            float z = 0;//random.nextFloat() * range - range / 2;

            Entity entity = new Entity(textureModel, new Vector3f(x, y, z),
                    new Vector3f(random.nextFloat() * 180f, random.nextFloat() * 180f, random.nextFloat() * 180f), 1);
            entities.add(entity);
        }

        Camera camera = new Camera();
        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 0.5f, 1));

        MasterRender render = new MasterRender();

        while (!Display.isCloseRequested()) {
            camera.getRotation().x += 0.1;
            camera.move();
            for (Entity e : entities) {
                render.processEntity(e);
            }
            render.render(light, camera);
            DisplayManager.updateDisplay();

        }
        render.cleanUp();
        loader.cleatUp();
        DisplayManager.closeDisplay();

    }

}

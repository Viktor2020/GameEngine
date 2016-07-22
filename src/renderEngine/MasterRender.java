package renderEngine;


import entities.Camera;
import entities.Entity;
import models.TextureModel;
import shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRender {

    private StaticShader shader = new StaticShader();
    private Render render = new Render(shader);

    private Map<TextureModel, List<Entity>> entities = new HashMap<>();


    public void render(Light light, Camera camera) {
        render.prepare();
        shader.start();
        shader.loadLight(light);
        shader.loadViewMatrix(camera);
        render.render(entities);
        shader.stop();
        entities.clear();
    }

    public void processEntity(Entity entity) {
        TextureModel textureModel = entity.getModel();
        List<Entity> batch = entities.get(textureModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(textureModel, newBatch);
        }
    }

    public void cleanUp() {
        shader.cleanUp();
    }
}

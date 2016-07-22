package renderEngine;


import entities.Camera;
import entities.Entity;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRender {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private StaticShader shader = new StaticShader();
    private EntityRender render;

    private Map<TextureModel, List<Entity>> entities = new HashMap<>();
    private Matrix4f projectionMatrix;

    public MasterRender() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BACK);
        createProjectionMatrix();
        render = new EntityRender(shader, projectionMatrix);

    }

    public void render(Light light, Camera camera) {
        prepare();
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

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0.3f, 0.0f, 1);
    }

    private void createProjectionMatrix() {
        float aspectRation = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRation);
        float x_scale = y_scale / aspectRation;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public void cleanUp() {
        shader.cleanUp();
    }
}

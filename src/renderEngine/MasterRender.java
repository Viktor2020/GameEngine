package renderEngine;


import entities.Camera;
import entities.Entity;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRender;
import terrains.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRender {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private static final float RED = 0.5f;
    private static final float GREEN = 0.5f;
    private static final float BLUE = 0.6f;

    private SkyboxRender skyboxRender;

    private StaticShader staticShader = new StaticShader();
    private EntityRender entityRender;

    private TerrainShader terrainShader = new TerrainShader();
    private TerrainRender terrainRender;
    private List<Terrain> terrains = new ArrayList<>();

    private Map<TextureModel, List<Entity>> entities = new HashMap<>();
    private Matrix4f projectionMatrix;

    public MasterRender(Loader loader) {
        enableCulling();
        createProjectionMatrix();
        entityRender = new EntityRender(staticShader, projectionMatrix);
        terrainRender  = new TerrainRender(terrainShader, projectionMatrix);
        skyboxRender = new SkyboxRender(loader, projectionMatrix);
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BACK);
    }
    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void render(List<Light> lights, Camera camera) {
        prepare();
        staticShader.start();
        staticShader.loadSkyColour(RED, GREEN, BLUE);
        staticShader.loadLights(lights);
        staticShader.loadViewMatrix(camera);
        entityRender.render(entities);
        staticShader.stop();

        terrainShader.start();
        terrainShader.loadSkyColour(RED, GREEN, BLUE);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRender.render(terrains);
        terrainShader.stop();

        skyboxRender.render(camera, RED, GREEN, BLUE);

        terrains.clear();
        entities.clear();
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
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
        GL11.glClearColor(RED, GREEN, BLUE, 1);
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
        staticShader.cleanUp();
        terrainShader.cleanUp();
    }
}

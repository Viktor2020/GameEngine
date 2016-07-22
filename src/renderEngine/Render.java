package renderEngine;


import entities.Entity;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

public class Render {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private Matrix4f projectionMatrix;
    private StaticShader shader;

    public Render(StaticShader shader) {
        this.shader = shader;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BACK);
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0.3f, 0.0f, 1);
    }

    public void render(Map<TextureModel, List<Entity>> entities) {
        for (TextureModel textureModel : entities.keySet()) {
            prepareTextureModel(textureModel);
            List<Entity> batch = entities.get(textureModel);
            for (Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, textureModel.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTextureModel();
        }
    }

    private void prepareTextureModel(TextureModel textureModel) {
        RawModel model = textureModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture modelTexture = textureModel.getTexture();
        shader.loadShineVariables(modelTexture.getShineDamper(), modelTexture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureModel.getTexture().getTextureID());
    }

    private void unbindTextureModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);

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
}

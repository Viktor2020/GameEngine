package skybox;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolbox.Maths;

public class SkyboxShader extends ShaderProgram {

    private static final float ROTATION_SPEED = 0.1f;
    private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.txt";
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationFogColour;

    private float rotation = 0;

    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(locationProjectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        rotation += ROTATION_SPEED * DisplayManager.getFrameTimeSeconds();
        Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
        super.loadMatrix(locationViewMatrix, matrix);
    }

    public void loadFogColour(float r, float g, float b) {
        super.loadVector(locationFogColour, new Vector3f(r, g, b));
    }

    @Override
    protected void getAllUniformLocations() {
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationFogColour = super.getUniformLocation("fogColour");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
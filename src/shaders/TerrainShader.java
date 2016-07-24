package shaders;


import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Light;
import toolbox.Maths;

import java.util.List;

public class TerrainShader extends ShaderProgram {
    private static final int MAX_LIGHT = 4;
    private static final String FRAGMENT_FILE = "src/shaders/glsl/terrainFragmentShader.txt";
    private static final String VERTEX_FILE = "src/shaders/glsl/terrainVertexShader.txt";
    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationLightPosition[];
    private int locationLightColour[];
    private int locationShineDamper;
    private int locationReflectivity;
    private int locationSkyColour;

    private int locationBackgroundTexture;
    private int locationRTexture;
    private int locationGTexture;
    private int locationBTexture;
    private int locationBlendMapTexture;

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationShineDamper = super.getUniformLocation("shineDamper");
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationSkyColour = super.getUniformLocation("skyColour");
        locationBackgroundTexture = super.getUniformLocation("backgroundTexture");
        locationRTexture = super.getUniformLocation("rTexture");
        locationGTexture = super.getUniformLocation("gTexture");
        locationBTexture = super.getUniformLocation("bTexture");
        locationBlendMapTexture = super.getUniformLocation("blendMap");

        locationLightPosition = new int[MAX_LIGHT];
        locationLightColour = new int[MAX_LIGHT];

        for (int i = 0; i < MAX_LIGHT; i++) {
            locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            locationLightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
        }
    }

    public void connectTextureUnits() {
        super.loadInt(locationBackgroundTexture, 0);
        super.loadInt(locationRTexture, 1);
        super.loadInt(locationGTexture, 2);
        super.loadInt(locationBTexture, 3);
        super.loadInt(locationBlendMapTexture, 4);
    }

    public void loadSkyColour(float r, float g, float b) {
        super.loadVector(locationSkyColour, new Vector3f(r, g, b));
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(locationShineDamper, damper);
        super.loadFloat(locationReflectivity, reflectivity);
    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHT; i++) {
            if (i < lights.size()) {
                super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
                super.loadVector(locationLightColour[i], lights.get(i).getColour());
            } else {
                super.loadVector(locationLightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector(locationLightColour[i], new Vector3f(0, 0, 0));
            }
        }
    }

    public void loadTransformationMatrix(Matrix4f matrix4f) {
        super.loadMatrix(locationTransformationMatrix, matrix4f);
    }

    public void loadProjectionMatrix(Matrix4f matrix4f) {
        super.loadMatrix(locationProjectionMatrix, matrix4f);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(locationViewMatrix, viewMatrix);
    }
}

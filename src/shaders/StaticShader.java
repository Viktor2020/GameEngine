package shaders;


import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Light;
import toolbox.Maths;

import java.util.List;

public class StaticShader extends ShaderProgram {

    private static final int MAX_LIGHT = 4;
    private static final String FRAGMENT_FILE = "src/shaders/glsl/fragmentShader.txt";
    private static final String VERTEX_FILE = "src/shaders/glsl/vertexShader.txt";
    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationLightPosition[];
    private int locationLightColour[];
    private int locationAttenuation[];
    private int locationShineDamper;
    private int locationReflectivity;
    private int locationUseFakeLighting;
    private int locationSkyColour;
    private int locationNumberOfRows;
    private int locationOffset;

    public StaticShader() {
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
        locationUseFakeLighting = super.getUniformLocation("useFakeLighting");
        locationSkyColour = super.getUniformLocation("skyColour");
        locationNumberOfRows = super.getUniformLocation("numberOfRows");
        locationOffset = super.getUniformLocation("offset");

        locationLightPosition = new int[MAX_LIGHT];
        locationLightColour = new int[MAX_LIGHT];
        locationAttenuation = new int[MAX_LIGHT];

        for (int i = 0; i < MAX_LIGHT; i++) {
            locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            locationLightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
            locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    public void loadSkyColour(float r, float g, float b) {
        super.loadVector(locationSkyColour, new Vector3f(r, g, b));
    }

    public void loadFakeLightingVaribles(boolean useFake) {
        super.loadBoolean(locationUseFakeLighting, useFake);
    }

    public void loadNumberOfRows(int numberOfRows) {
        super.loadFloat(locationNumberOfRows, numberOfRows);
    }

    public void loadOffset(float x, float y) {
        super.load2DVector(locationOffset, new Vector2f(x, y));
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
                super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector(locationLightPosition[i], new Vector3f(0, 0, 0));
                super.loadVector(locationLightColour[i], new Vector3f(0, 0, 0));
                super.loadVector(locationAttenuation[i], new Vector3f(1, 0, 0));
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

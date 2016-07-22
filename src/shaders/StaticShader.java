package shaders;


import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Light;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {

    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
    private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationLightPosition;
    private int locationLightColour;

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
        locationLightPosition = super.getUniformLocation("lightPosition");
        locationLightColour = super.getUniformLocation("lightColour");
    }

    public void loadLight(Light light) {
        super.loadVector(locationLightPosition, light.getPosition());
        super.loadVector(locationLightColour, light.getColour());

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

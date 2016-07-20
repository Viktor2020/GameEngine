package shaders;

/**
 * Created by Admin on 20.07.2016.
 */
public class StaticShader extends ShaderProgram {

    public static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
    public static final String VERTEX_FILE = "src/shaders/vertexShader.txt";


    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}

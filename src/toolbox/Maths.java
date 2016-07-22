package toolbox;


import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Maths {

    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        Matrix4f.translate(translation, matrix4f, matrix4f);
        Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix4f, matrix4f);
        Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix4f, matrix4f);
        Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix4f, matrix4f);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix4f, matrix4f);
        return matrix4f;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        // inverse rotation x y
        Matrix4f.rotate((float) Math.toRadians(camera.getRotation().y), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRotation().x), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRotation().z), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);

        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }
}

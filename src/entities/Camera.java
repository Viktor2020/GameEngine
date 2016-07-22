package entities;


import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0,0,0);
    private Vector3f rotation = new Vector3f(0,0,0);

    public Camera() {
    }
    private float drag = 0.1f;
    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z-=drag;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x+=drag;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z+=drag;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x-=drag;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.y+=drag;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            position.y-=drag;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}

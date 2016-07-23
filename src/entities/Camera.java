package entities;


import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private Vector3f rotation = new Vector3f(0, 0, 0);

    private float distanceFromPlayer = 50;
    private float angelAroundPlayer = 0;

    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public void move() {
        calculateZoom();
        calculateAngel();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        rotation.x = 180 - (player.getRotation().y + angelAroundPlayer);

    }

    private float calculateHorizontalDistance() {
        return  (float) (distanceFromPlayer * Math.cos(Math.toRadians(rotation.y)));
    }
    private float calculateVerticalDistance() {
        return  (float) (distanceFromPlayer * Math.sin(Math.toRadians(rotation.y)));
    }
    private void calculateCameraPosition( float horizontalDistance, float verticalDistance ) {
        float theta = player.getRotation().y + angelAroundPlayer;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticalDistance;
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        distanceFromPlayer -= zoomLevel;
    }

    private void calculateAngel() {
        if (Mouse.isButtonDown(1)) {
            float changeY = Mouse.getDY() * 0.1f;
            rotation.y -= changeY;

            float changeX = Mouse.getDX() * 0.3f;
            angelAroundPlayer -= changeX;

        }

    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}

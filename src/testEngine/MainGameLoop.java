package testEngine;

import org.lwjgl.opengl.Display;

import org.lwjgl.opengl.GL11;
import renderEngine.DisplayManager;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));

		while (!Display.isCloseRequested()) {

			DisplayManager.updateDisplay();

		}

		DisplayManager.closeDisplay();

	}

}

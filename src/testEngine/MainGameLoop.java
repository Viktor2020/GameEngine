package testEngine;

import entities.Camera;
import entities.Entity;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import terrains.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();


        RawModel model = OBJLoader.loadObjModel("tree", loader);
        RawModel modelFern = OBJLoader.loadObjModel("fern", loader);
        RawModel modelGrass = OBJLoader.loadObjModel("grassModel", loader);

        TextureModel staticModel = new TextureModel(model,new ModelTexture(loader.loadTexture("tree")));

        TextureModel fernTexture = new TextureModel(modelFern,new ModelTexture(loader.loadTexture("fern")));
        fernTexture.getTexture().setHasTransparency(true);
        fernTexture.getTexture().setUseFakeLighting(true);

        TextureModel grassTexture = new TextureModel(modelGrass,new ModelTexture(loader.loadTexture("grassTexture")));
        grassTexture.getTexture().setHasTransparency(true);
        grassTexture.getTexture().setUseFakeLighting(true);

        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i=0;i<500;i++){
            entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),new Vector3f(),3));
        }
        for(int i=0;i<500;i++){
            entities.add(new Entity(fernTexture, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),new Vector3f(),1f));
        }

        for(int i=0;i<500;i++){
            entities.add(new Entity(grassTexture, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),new Vector3f(),1f));
        }



        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));

//        Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(-0.5f,-0.5f,loader,new ModelTexture(loader.loadTexture("grass")));

        Camera camera = new Camera();
        MasterRender renderer = new MasterRender();

        while(!Display.isCloseRequested()){
            camera.move();

//            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            for(Entity entity:entities){
                renderer.processEntity(entity);
            }
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}

package testEngine;

import entities.Camera;
import entities.Entity;
import entities.Player;
import models.RawModel;
import models.TextureModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        TerrainTexture bgTexture = new TerrainTexture(loader.loadTexture("grassy"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMapTexture = new TerrainTexture(loader.loadTexture("blendMap"));

        ModelData dataTree = OBJFileLoader.loadOBJ("tree");
        ModelData dataFern = OBJFileLoader.loadOBJ("fern");
        ModelData dataGrass = OBJFileLoader.loadOBJ("grassModel");

        RawModel model = loader.loadToVAO(dataTree.getVertices(), dataTree.getTextureCoords(), dataTree.getNormals(), dataTree.getIndices());
        RawModel modelFern = loader.loadToVAO(dataFern.getVertices(), dataFern.getTextureCoords(), dataFern.getNormals(), dataFern.getIndices());
        RawModel modelGrass = loader.loadToVAO(dataGrass.getVertices(), dataGrass.getTextureCoords(), dataGrass.getNormals(), dataGrass.getIndices());


        TextureModel textureTree = new TextureModel(model,new ModelTexture(loader.loadTexture("tree")));

        TextureModel fernTexture = new TextureModel(modelFern,new ModelTexture(loader.loadTexture("fern")));
        fernTexture.getTexture().setHasTransparency(true);
        fernTexture.getTexture().setUseFakeLighting(true);

        TextureModel grassTexture = new TextureModel(modelGrass,new ModelTexture(loader.loadTexture("grassTexture")));
        grassTexture.getTexture().setHasTransparency(true);
        grassTexture.getTexture().setUseFakeLighting(true);

        List<Entity> entities = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<500;i++){
            entities.add(new Entity(textureTree, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),new Vector3f(),3));
        }
        for(int i=0;i<500;i++){
            entities.add(new Entity(fernTexture, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),new Vector3f(),1f));
        }

        for(int i=0;i<500;i++){
            entities.add(new Entity(grassTexture, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),new Vector3f(),1f));
        }



        Light light = new Light(new Vector3f(20000,20000,2000),new Vector3f(1,1,1));

        Terrain terrain = new Terrain(-0f,-1f,loader,texturePack, blendMapTexture, "heightMap");
        Terrain terrain2 = new Terrain(-1f,-1f,loader,texturePack, blendMapTexture, "heightMap");

        ModelData bunnyData = OBJFileLoader.loadOBJ("stanfordBunny");
        RawModel bunnyModel = loader.loadToVAO(bunnyData.getVertices(), bunnyData.getTextureCoords(), bunnyData.getNormals(), bunnyData.getIndices());
        TextureModel bunnyTexure = new TextureModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));
        Player player = new Player(bunnyTexure, new Vector3f(0, 0, -30), new Vector3f(), 1);

        Camera camera = new Camera(player);
        MasterRender renderer = new MasterRender();

        while(!Display.isCloseRequested()){
            player.move();
            camera.move();


            renderer.processEntity(player);
            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain);
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

package testEngine;

import entities.Camera;
import entities.Entity;
import entities.Player;
import guis.GuiRender;
import guis.GuiTexture;
import models.RawModel;
import models.TextureModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Light;
import renderEngine.Loader;
import renderEngine.MasterRender;
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

        TerrainTexture bgTexture = new TerrainTexture(loader.loadTexture("grassy3"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMapTexture = new TerrainTexture(loader.loadTexture("blendMap"));
        Terrain terrain = new Terrain(-0f, -1f, loader, texturePack, blendMapTexture, "heightMap");

        ModelData dataTree = OBJFileLoader.loadOBJ("tree");
        ModelData dataFern = OBJFileLoader.loadOBJ("fern");
        ModelData dataGrass = OBJFileLoader.loadOBJ("grassModel");
        ModelData dataBox = OBJFileLoader.loadOBJ("box");
        ModelData dataLowTree = OBJFileLoader.loadOBJ("lowPolyTree");
        ModelData dataPerson = OBJFileLoader.loadOBJ("person");
        ModelData bunnyData = OBJFileLoader.loadOBJ("stanfordBunny");
        ModelData dataPine = OBJFileLoader.loadOBJ("pine");

        RawModel modelTree = loader.loadToVAO(dataTree.getVertices(), dataTree.getTextureCoords(), dataTree.getNormals(), dataTree.getIndices());
        RawModel modelFern = loader.loadToVAO(dataFern.getVertices(), dataFern.getTextureCoords(), dataFern.getNormals(), dataFern.getIndices());
        RawModel modelGrass = loader.loadToVAO(dataGrass.getVertices(), dataGrass.getTextureCoords(), dataGrass.getNormals(), dataGrass.getIndices());
        RawModel modelBox = loader.loadToVAO(dataBox.getVertices(), dataBox.getTextureCoords(), dataBox.getNormals(), dataBox.getIndices());
        RawModel modelLowTree = loader.loadToVAO(dataLowTree.getVertices(), dataLowTree.getTextureCoords(), dataLowTree.getNormals(), dataLowTree.getIndices());
        RawModel modelPerson = loader.loadToVAO(dataPerson.getVertices(), dataPerson.getTextureCoords(), dataPerson.getNormals(), dataPerson.getIndices());
        RawModel bunnyModel = loader.loadToVAO(bunnyData.getVertices(), bunnyData.getTextureCoords(), bunnyData.getNormals(), bunnyData.getIndices());
        RawModel modelPine = loader.loadToVAO(dataPine.getVertices(), dataPine.getTextureCoords(), dataPine.getNormals(), dataPine.getIndices());


        TextureModel treeTexture = new TextureModel(modelTree, new ModelTexture(loader.loadTexture("tree")));

        ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
        fernTextureAtlas.setNumberOfRows(2);

        TextureModel fernTexture = new TextureModel(modelFern, fernTextureAtlas);
        fernTexture.getTexture().setHasTransparency(true);
        fernTexture.getTexture().setUseFakeLighting(true);

        TextureModel grassTexture = new TextureModel(modelGrass, new ModelTexture(loader.loadTexture("grassTexture")));
        grassTexture.getTexture().setHasTransparency(true);
        grassTexture.getTexture().setUseFakeLighting(true);

        TextureModel boxTexture = new TextureModel(modelBox, new ModelTexture(loader.loadTexture("box")));
        TextureModel lowTreeTexture = new TextureModel(modelLowTree, new ModelTexture(loader.loadTexture("lowPolyTree")));
        TextureModel bunnyTexture = new TextureModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));
        TextureModel playerTexture = new TextureModel(modelPerson, new ModelTexture(loader.loadTexture("playerTexture")));
        TextureModel pineTexture = new TextureModel(modelPine, new ModelTexture(loader.loadTexture("pine")));


        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);

        for (int i = 0; i < 300; i++) {
            float x = 0;
            float z = 0;
            float y = 0;
            if (i % 20 == 0) {
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(treeTexture, new Vector3f(x, y, z), new Vector3f(0, random.nextFloat() * 360, 0), 1f));
            }
            if (i % 5 == 0) {
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fernTexture, random.nextInt(4), new Vector3f(x, y, z), new Vector3f(0, random.nextFloat() * 360, 0), 1f));

                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(pineTexture, random.nextInt(4), new Vector3f(x, y, z), new Vector3f(0, random.nextFloat() * 360, 0), 1f));
            }
            if (i % 25 == 0) {
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(grassTexture, new Vector3f(x, y, z), new Vector3f(0, random.nextFloat() * 360, 0), 1f));
            }
            if (i % 28 == 0) {
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(boxTexture, new Vector3f(x, y, z), new Vector3f(0, random.nextFloat() * 360, 0), 1f));
            }
            if (i % 32 == 0) {
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(lowTreeTexture, new Vector3f(x, y, z), new Vector3f(0, random.nextFloat() * 360, 0), 1f));
            }
            if (i % 100 == 0) {
                x = random.nextFloat() * 800 - 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(bunnyTexture, new Vector3f(x, y, z), new Vector3f(0, random.nextFloat() * 360, 0), 1f));
            }

        }

        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("health"), new Vector2f(0.5f,0.5f), new Vector2f(0.25f, 0.25f));
        guis.add(gui);

        Light light = new Light(new Vector3f(20000, 20000, 2000), new Vector3f(1, 1, 1));



        Player player = new Player(playerTexture, new Vector3f(0, 0, -30), new Vector3f(), 1);

        Camera camera = new Camera(player);
        MasterRender renderer = new MasterRender();

        GuiRender guiRender = new GuiRender(loader);

        while (!Display.isCloseRequested()) {
            player.move(terrain);
            camera.move();


            renderer.processEntity(player);
            renderer.processTerrain(terrain);
            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            renderer.render(light, camera);
            guiRender.render(guis);
            DisplayManager.updateDisplay();
        }

        guiRender.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}

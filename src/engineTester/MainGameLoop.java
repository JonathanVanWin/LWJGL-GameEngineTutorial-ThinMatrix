package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();

		Loader loader = new Loader();

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

		RawModel model = OBJLoader.loadOBJModel("tree", loader);

		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
		TexturedModel bobble = new TexturedModel(OBJLoader.loadOBJModel("lowPolyTree", loader),
				new ModelTexture(loader.loadTexture("lowPolyTree")));
		TexturedModel pine = new TexturedModel(OBJLoader.loadOBJModel("pine", loader),
				new ModelTexture(loader.loadTexture("pine")));

		TexturedModel lamp = new TexturedModel(OBJLoader.loadOBJModel("lamp", loader),
				new ModelTexture(loader.loadTexture("lamp")));

		TexturedModel grass = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flower = new TexturedModel(OBJLoader.loadOBJModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("flower")));
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		TexturedModel fern = new TexturedModel(OBJLoader.loadOBJModel("fern", loader), fernTextureAtlas);

		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);

		Terrain terrain1 = new Terrain(0, -1, loader, texturePack, blendMap, "heightmapPerlinp");
		Terrain terrain2 = new Terrain(0, 0, loader, texturePack, blendMap, "heightmapPerlinp");
		Terrain terrain3 = new Terrain(-1, 0, loader, texturePack, blendMap, "heightmapPerlinp");
		Terrain terrain4 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmapPerlinp");

		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain1);
		terrains.add(terrain2);
		terrains.add(terrain3);
		terrains.add(terrain4);

		List<Entity> entities = new ArrayList<Entity>();
		List<Entity> normalMapEntities = new ArrayList<Entity>();
		TexturedModel barrelModel = new TexturedModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
				new ModelTexture(loader.loadTexture("barrel")));
		barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
		barrelModel.getTexture().setShineDamper(10);
		barrelModel.getTexture().setReflectivity(0.5f);
		normalMapEntities.add(new Entity(barrelModel, new Vector3f(75, 10, -75), 0, 0, 0, 1f));
		Random random = new Random(676452);

		/*
		 * for (int i = 0; i < 400; i++) { if (i % 2 == 0) { float x =
		 * random.nextFloat() * 800 - 400; float z = random.nextFloat() * -600;
		 * float y = terrain1.getHeightOfTerrain(x, z); entities.add(new
		 * Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0,
		 * random.nextFloat() * 360, 0, 0.9f)); } if (i % 5 == 0) { float x =
		 * random.nextFloat() * 800 - 400; float z = random.nextFloat() * -600;
		 * float y = terrain1.getHeightOfTerrain(x, z); entities.add(new
		 * Entity(bobble, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0,
		 * random.nextFloat() * 0.1f + 0.6f)); x = random.nextFloat() * 800 -
		 * 400; z = random.nextFloat() * -600; y =
		 * terrain1.getHeightOfTerrain(x, z); entities.add(new
		 * Entity(staticModel, new Vector3f(x, y, z), 0, 0, 0,
		 * random.nextFloat() * 1 + 4)); }
		 * 
		 * }
		 */

		for (Terrain terrain : terrains) {

			for (int i = 0; i < 500; i++) {
				if (i % 2 == 0) {
					float x = random.nextInt((int) terrain.getSize()) + terrain.getX();
					float z = random.nextInt((int) terrain.getSize()) + terrain.getZ();
					float y = terrain.getHeightOfTerrain(x, z);
					entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360,
							0, 0.9f));
				}
				if (i % 5 == 0) {
					float x = random.nextInt((int) terrain.getSize()) + terrain.getX();
					float z = random.nextInt((int) terrain.getSize()) + terrain.getZ();
					float y = terrain.getHeightOfTerrain(x, z);
					/*
					 * entities.add(new Entity(bobble, new Vector3f(x, y, z), 0,
					 * random.nextFloat() * 360, 0,random.nextFloat() * 0.1f +
					 * 0.6f)); x = random.nextInt((int) terrain.getSize()) +
					 * terrain.getX(); z = random.nextInt((int)
					 * terrain.getSize()) + terrain.getZ(); y =
					 * terrain.getHeightOfTerrain(x, z); entities.add(new
					 * Entity(staticModel, new Vector3f(x, y, z), 0,
					 * random.nextFloat() * 360, 0, random.nextFloat() * 1 +
					 * 4)); x = random.nextInt((int) terrain.getSize()) +
					 * terrain.getX(); z = random.nextInt((int)
					 * terrain.getSize()) + terrain.getZ(); y =
					 * terrain.getHeightOfTerrain(x, z);
					 */
					entities.add(new Entity(pine, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0,
							random.nextFloat() * 2 + 1));
				}

			}
		}

		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(0, 10000, -7000), new Vector3f(1.4f, 1.4f, 1.4f));
		lights.add(sun);
		lights.add(new Light(new Vector3f(185, terrains.get(0).getHeightOfTerrain(185, -293) + 20, -293),
				new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(370, terrains.get(0).getHeightOfTerrain(370, -300) + 20, -300),
				new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(293, terrains.get(0).getHeightOfTerrain(293, -305) + 20, -305),
				new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));

		entities.add(
				new Entity(lamp, new Vector3f(185, terrains.get(0).getHeightOfTerrain(185, -293), -293), 0, 0, 0, 1));
		entities.add(
				new Entity(lamp, new Vector3f(370, terrains.get(0).getHeightOfTerrain(370, -300), -300), 0, 0, 0, 1));
		entities.add(
				new Entity(lamp, new Vector3f(293, terrains.get(0).getHeightOfTerrain(293, -305), -305), 0, 0, 0, 1));
		Light l1 = new Light(new Vector3f(180, terrains.get(0).getHeightOfTerrain(180, -290) + 20, -290),
				new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f));
		Entity lamp1 = new Entity(lamp, new Vector3f(180, terrains.get(0).getHeightOfTerrain(180, -290), -290), 0, 0, 0,
				1);
		MasterRenderer renderer = new MasterRenderer(loader);

		RawModel bunnyModel = OBJLoader.loadOBJModel("bunny", loader);
		TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));

		Player player = new Player(stanfordBunny, new Vector3f(100, 5, -150), 0, 180, 0, 0.6f);
		Camera camera = new Camera(player);

		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture gui = new GuiTexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f),
				new Vector2f(0.25f, 0.25f));
		guis.add(gui);

		GuiRenderer guiRenderer = new GuiRenderer(loader);
		Terrain[][] terrainArr = { { terrain1, terrain2 }, { terrain3, terrain4 } };
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain1);
		entities.add(lamp1);
		entities.add(player);
		lights.add(l1);
		picker.setCurrentTerrainPoint(
				new Vector3f(player.getPosition().x, player.getPosition().y, player.getPosition().z));

		WaterFrameBuffers buffers = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), buffers);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(75, -75, 0);
		waters.add(water);

		while (!Display.isCloseRequested()) {
			player.moveOnMultipleTerrains(terrains);
			System.out.println(player.getPosition());
			camera.move();

			picker.setMultipleTerrains(terrains);

			picker.update();
			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			if (terrainPoint != null) {
				lamp1.setPosition(terrainPoint);
				l1.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y, terrainPoint.z));
			}

			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

			// render reflection texture
			buffers.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera,
					new Vector4f(0, 1, 0, -waters.get(0).getHeight() + 1f));
			camera.getPosition().y += distance;
			camera.invertPitch();

			// render refraction texture
			buffers.bindRefractionFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera,
					new Vector4f(0, -1, 0, waters.get(0).getHeight()));

			// render to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			buffers.unbindCurrentFrameBuffer();
			renderer.renderScene(entities, normalMapEntities, terrains, lights, camera, new Vector4f(0, -1, 0, 100000));
			waterRenderer.render(waters, camera, sun);
			guiRenderer.render(guis);

			DisplayManager.updateDisplay();
		}

		buffers.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}

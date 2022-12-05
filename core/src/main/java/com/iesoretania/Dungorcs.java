package com.iesoretania;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.iesoretania.Personajes.Demonic;
import com.iesoretania.Personajes.Enemigo;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Dungorcs extends ApplicationAdapter {
	Stage stage;
	OrthographicCamera camera;
	OrthogonalTiledMapRenderer mapRenderer;
	TiledMap map;

	private int mapWidthInPixels;
	private int mapHeightInPixels;

	TiledMapTileLayer paredes;
	Actor protagonista, enemy1, enemy2, enemy3, enemy4;

	float offsetX, offsetY;

	@Override
	public void create() {
		stage = new Stage();
		map = new TmxMapLoader().load("mapa_dungorcs.tmx");

		paredes = (TiledMapTileLayer) map.getLayers().get("Paredes");
		protagonista = new Demonic(map);
		enemy1 = new Enemigo(map, 1);
		enemy2 = new Enemigo(map, 2);
		enemy3 = new Enemigo(map, 3);
		enemy4 = new Enemigo(map, 4);



		MapProperties properties = map.getProperties();
		int tileWidth = properties.get("tilewidth", Integer.class);
		int tileHeight = properties.get("tileheight", Integer.class);
		int mapWidthInTiles = properties.get("width", Integer.class);
		int mapHeightInTiles = properties.get("height", Integer.class);

		mapWidthInPixels = mapWidthInTiles * tileWidth;
		mapHeightInPixels = mapHeightInTiles * tileHeight;
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 600);

		Viewport viewport = new ScreenViewport(camera);
		stage.setViewport(viewport);


		offsetX = 0;
		offsetY = 0;

		stage.addActor(protagonista);
		stage.addActor(enemy1);
		stage.addActor(enemy2);
		stage.addActor(enemy3);
		stage.addActor(enemy4);
		protagonista.toFront();
		Gdx.input.setInputProcessor(stage);
		stage.setKeyboardFocus(protagonista);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(protagonista.getX() < camera.position.x - camera.viewportWidth / 2 + 100 && offsetX > 0) {
			offsetX -= 100 * Gdx.graphics.getDeltaTime();
		}

		if(protagonista.getX() + protagonista.getWidth() > camera.position.x + camera.viewportWidth / 2 - 100 && offsetX < mapWidthInPixels - camera.viewportWidth) {
			offsetX += 100 * Gdx.graphics.getDeltaTime();
		}

		if(protagonista.getY() < camera.position.y - camera.viewportHeight / 2 + 100 && offsetY > -mapHeightInPixels + camera.viewportHeight) {
			offsetY -= 100 * Gdx.graphics.getDeltaTime();
		}

		if(protagonista.getY() + protagonista.getHeight() > camera.position.y + camera.viewportHeight / 2 - 100 && offsetY < 0) {
			offsetY += 100 * Gdx.graphics.getDeltaTime();
		}


		if(offsetX < 0) {
			offsetX = 0;
		}

		if(offsetY > 0) {
			offsetY = 0;
		}

		if(offsetX > mapWidthInPixels - camera.viewportWidth) {
			offsetX = mapWidthInPixels - camera.viewportWidth;
		}

		if(offsetY < -mapHeightInPixels + camera.viewportHeight) {
			offsetY = -mapHeightInPixels + camera.viewportHeight;
		}

		camera.position.x = camera.viewportWidth / 2 + offsetX;
		camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
		camera.update();

		int[] layerAltura = {5};
		int[] layerMapa = {0, 1, 2, 3, 4};

		mapRenderer.setView(camera);
		mapRenderer.render(layerMapa);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		mapRenderer.render(layerAltura);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		camera.setToOrtho(false, width, height);
		camera.position.x = camera.viewportWidth / 2 + offsetX;
		camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
		camera.update();
	}

	@Override
	public void dispose() {
		stage.dispose();
		map.dispose();
		mapRenderer.dispose();
	}
}
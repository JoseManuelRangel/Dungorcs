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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Dungorcs extends ApplicationAdapter {
	Stage stage;
	OrthographicCamera camera;
	OrthogonalTiledMapRenderer mapRenderer;
	TiledMap map;

	private int mapWidthInPixels;
	private int mapHeightInPixels;

	TiledMapTileLayer paredes;
	Actor protagonista;

	float offsetX, offsetY;

	@Override
	public void create() {
		stage = new Stage();
		map = new TmxMapLoader().load("mapa_dungorcs.tmx");

		protagonista = new Demonic(map);
		protagonista.setPosition(150, 1100);

		MapProperties properties = map.getProperties();
		int tileWidth = properties.get("tilewidth", Integer.class);
		int tileHeight = properties.get("tileheight", Integer.class);
		int mapWidthInTiles = properties.get("width", Integer.class);
		int mapHeightInTiles = properties.get("height", Integer.class);

		mapWidthInPixels = mapWidthInTiles * tileWidth;
		mapHeightInPixels = mapHeightInTiles * tileHeight;
		mapRenderer = new OrthogonalTiledMapRenderer(map);

		paredes = (TiledMapTileLayer) map.getLayers().get("Paredes");

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 600);

		Viewport viewport = new ScreenViewport(camera);
		stage.setViewport(viewport);

		offsetX = 0;
		offsetY = 0;

		stage.addActor(protagonista);
		Gdx.input.setInputProcessor(stage);
		stage.setKeyboardFocus(protagonista);

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/*if(protagonista.getX() > (offsetX + camera.viewportWidth) * 0.7) {
			offsetX += 100 * Gdx.graphics.getDeltaTime();
		}

		if(protagonista.getX() < (offsetX + 0.3 * camera.viewportWidth)) {
			offsetX -= 100 * Gdx.graphics.getDeltaTime();
		}

		if(protagonista.getY() - 1100 < (offsetY + 0.3 * camera.viewportWidth)) {
			offsetY -= 100 * Gdx.graphics.getDeltaTime();
		}

		if(protagonista.getY() - 1100 > (offsetY + 0.7 * camera.viewportWidth)) {
			offsetY += 100 * Gdx.graphics.getDeltaTime();
		}*/


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

		int[] layerAltura = {3};
		int[] layerMapa = {0, 1, 2};

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
	}
}
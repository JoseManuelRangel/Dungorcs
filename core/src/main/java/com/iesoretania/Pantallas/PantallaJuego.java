package com.iesoretania.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.iesoretania.Dungorcs;
import com.iesoretania.Elementos.Puntuacion;
import com.iesoretania.Personajes.Demonic;
import com.iesoretania.Personajes.Enemigo;
import com.iesoretania.Personajes.Moneda;

import java.util.ArrayList;

public class PantallaJuego extends PantallaBase {
    Stage stage;
    OrthographicCamera camera;
    Dungorcs game;
    OrthogonalTiledMapRenderer mapRenderer;
    TiledMap map;
    SpriteBatch batch;
    BitmapFont font;

    private int mapWidthInPixels;
    private int mapHeightInPixels;

    TiledMapTileLayer paredes;
    Texture enemig, moned;
    Demonic protagonista;
    Puntuacion p;

    float offsetX, offsetY;

    public static ArrayList<Enemigo> enemigos = new ArrayList<>();
    public static ArrayList<Moneda> monedas = new ArrayList<>();

    public PantallaJuego(Dungorcs game) {
        super(game);

        this.game = game;

        stage = new Stage();
        map = new TmxMapLoader().load("mapa_dungorcs.tmx");
        enemig = new Texture(Gdx.files.internal("enemigos.png"));
        moned = new Texture(Gdx.files.internal("monedas.png"));
        batch = new SpriteBatch();
        font = new BitmapFont();

        paredes = (TiledMapTileLayer) map.getLayers().get("Paredes");
        /*Para cambiar un tile de textura.*/
        //paredes.getCell(0, 0).setTile(map.getTileSets().getTile(595));
        protagonista = new Demonic(map);

        p = new Puntuacion(protagonista);


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

        for(int i = 1; i < 36; i++) {
            Enemigo enemigo = new Enemigo(map, i);
            enemigos.add(enemigo);
            stage.addActor(enemigo);
        }

        for(int j = 1; j < 160; j++) {
            Moneda moneda = new Moneda(map, j);
            monedas.add(moneda);
            stage.addActor(moneda);
        }

        stage.addActor(protagonista);
        stage.addActor(p);

        protagonista.toFront();
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(protagonista);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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

        int[] layerEntidades = {5};
        int[] layerMonedas = {4};
        int[] layerAltura = {3};
        int[] layerMapa = {0, 1, 2};

        mapRenderer.setView(camera);
        mapRenderer.render(layerMapa);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        mapRenderer.render(layerEntidades);
        mapRenderer.render(layerMonedas);
        mapRenderer.render(layerAltura);

        batch.begin();
        batch.draw(moned, 150,555, moned.getWidth() + 9, moned.getHeight() + 9);
        batch.draw(enemig, 60, 550, enemig.getWidth() + 9, enemig.getHeight() + 9);
        font.draw(batch, ": " + p.enemigos_eliminados, 90, 565);
        font.draw(batch, ": " + p.monedas_recogidas, 170, 565);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        /*super.resize(width, height);*/

		/*camera.setToOrtho(false, width, height);
		camera.position.x = camera.viewportWidth / 2 + offsetX;
		camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
		camera.update();*/
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        map.dispose();
        mapRenderer.dispose();
    }
}

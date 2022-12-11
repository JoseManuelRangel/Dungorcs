package com.iesoretania.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.iesoretania.Dungorcs;
import com.iesoretania.Elementos.Manager;
import com.iesoretania.Items.Corazon;
import com.iesoretania.Items.PocionesPrisma;
import com.iesoretania.Personajes.Demonic;
import com.iesoretania.Personajes.Enemigo;
import com.iesoretania.Items.Moneda;

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
    Texture enemig, moned, cora, poc1, poc2, poc3, poc4;
    Demonic protagonista;
    Manager p;


    float offsetX, offsetY;

    public static ArrayList<Enemigo> enemigos = new ArrayList<>();
    public static ArrayList<Moneda> monedas = new ArrayList<>();
    public static ArrayList<Corazon> corazones = new ArrayList<>();
    public static ArrayList<PocionesPrisma> pociones = new ArrayList<>();

    PocionesPrisma pocion1, pocion2, pocion3, pocion4;

    public Music gameMusic;
    public Sound objective;

    public PantallaJuego(Dungorcs game) {
        super(game);

        this.game = game;

        enemigos.clear();
        monedas.clear();
        corazones.clear();
        pociones.clear();

        stage = new Stage();
        map = new TmxMapLoader().load("mapa_dungorcs.tmx");
        enemig = new Texture(Gdx.files.internal("Personajes/enemigos.png"));
        moned = new Texture(Gdx.files.internal("Items/monedas.png"));
        cora = new Texture(Gdx.files.internal("Items/corazones.png"));

        poc1 = new Texture(Gdx.files.internal("Items/pocion_naranja.png"));
        poc2 = new Texture(Gdx.files.internal("Items/pocion_azul.png"));
        poc3 = new Texture(Gdx.files.internal("Items/pocion_verde.png"));
        poc4 = new Texture(Gdx.files.internal("Items/pocion_amarilla.png"));

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sounds/juego_music.wav"));
        objective = Gdx.audio.newSound(Gdx.files.internal("Sounds/objetivo_sound.wav"));

        batch = new SpriteBatch();
        font = new BitmapFont();

        gameMusic.setLooping(true);
        gameMusic.play();
        gameMusic.setVolume(0.5f);

        objective.play(1f);
        paredes = (TiledMapTileLayer) map.getLayers().get("Paredes");
        /*Para cambiar un tile de textura.*/
        //paredes.getCell(0, 0).setTile(map.getTileSets().getTile(595));
        protagonista = new Demonic(map);

        p = new Manager(protagonista, game);


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


        for(int j = 1; j < 121; j++) {
            Moneda moneda = new Moneda(map, j);
            monedas.add(moneda);
            stage.addActor(moneda);
        }


        for(int c = 1; c < 6; c++) {
            Corazon corazon = new Corazon(map, c);
            corazones.add(corazon);
            stage.addActor(corazon);
        }

        pocion1 = new PocionesPrisma(map, 1);
        pocion2 = new PocionesPrisma(map, 2);
        pocion3 = new PocionesPrisma(map, 3);
        pocion4 = new PocionesPrisma(map, 4);

        pociones.add(pocion1);
        pociones.add(pocion2);
        pociones.add(pocion3);
        pociones.add(pocion4);

        stage.addActor(pocion1);
        stage.addActor(pocion2);
        stage.addActor(pocion3);
        stage.addActor(pocion4);


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
            offsetX -= 130 * Gdx.graphics.getDeltaTime();
        }

        if(protagonista.getX() + protagonista.getWidth() > camera.position.x + camera.viewportWidth / 2 - 100 && offsetX < mapWidthInPixels - camera.viewportWidth) {
            offsetX += 130 * Gdx.graphics.getDeltaTime();
        }

        if(protagonista.getY() < camera.position.y - camera.viewportHeight / 2 + 100 && offsetY > -mapHeightInPixels + camera.viewportHeight) {
            offsetY -= 130 * Gdx.graphics.getDeltaTime();
        }

        if(protagonista.getY() + protagonista.getHeight() > camera.position.y + camera.viewportHeight / 2 - 100 && offsetY < 0) {
            offsetY += 130 * Gdx.graphics.getDeltaTime();
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

        int[] layerEntidades = {7};
        int[] layerPociones = {6};
        int[] layerCorazones = {5};
        int[] layerMonedas = {4};
        int[] layerAltura = {3};
        int[] layerMapa = {0, 1, 2};

        mapRenderer.setView(camera);
        mapRenderer.render(layerMapa);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        mapRenderer.render(layerEntidades);
        mapRenderer.render(layerPociones);
        mapRenderer.render(layerCorazones);
        mapRenderer.render(layerMonedas);
        mapRenderer.render(layerAltura);

        batch.begin();
        batch.draw(enemig, 60, 550, enemig.getWidth() + 9, enemig.getHeight() + 9);
        batch.draw(moned, 150,555, moned.getWidth() + 9, moned.getHeight() + 9);
        batch.draw(cora, 235, 550, cora.getWidth() + 8, cora.getHeight() + 8);
        if(pocion1.cogida == true) {
            batch.draw(poc1, 300, 550, poc1.getWidth() + 9, poc1.getHeight() + 9);
        }

        if(pocion2.cogida == true) {
            batch.draw(poc2, 330, 550, poc2.getWidth() + 9, poc2.getHeight() + 9);
        }

        if(pocion3.cogida == true) {
            batch.draw(poc3, 360, 550, poc3.getWidth() + 9, poc3.getHeight() + 9);
        }

        if(pocion4.cogida == true) {
            batch.draw(poc4, 390, 550, poc4.getWidth() + 9, poc4.getHeight() + 9);
        }
        font.draw(batch, ": " + p.enemigosEliminados, 90, 565);
        font.draw(batch, ": " + p.monedasRecogidas, 170, 565);
        font.draw(batch, ": " + p.vidasRestantes, 260, 565);
        batch.end();


        if(p.vidasRestantes == 0) {
            gameMusic.stop();
        }
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

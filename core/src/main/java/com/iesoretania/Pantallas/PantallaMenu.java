package com.iesoretania.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.iesoretania.Dungorcs;

public class PantallaMenu extends PantallaBase {
    Dungorcs game;

    private Stage stage;
    private SpriteBatch batch;
    private Texture texture;

    Music musica;


    public PantallaMenu(Dungorcs game) {
        super(game);
        this.game = game;
        stage = new Stage(new ScreenViewport());

        musica = Gdx.audio.newMusic(Gdx.files.internal("Sounds/menu_music.wav"));
        musica.setLooping(true);
        musica.play();

        TextButton botonJugar = new TextButton("JUGAR NUEVA PARTIDA", this.game.skin, "default");
        botonJugar.setSize(500, 50);
        botonJugar.setPosition(150, 400);
        botonJugar.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new PantallaJuego(game));
                musica.stop();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        TextButton botonControles = new TextButton("CONTROLES DEL JUEGO", this.game.skin, "default");
        botonControles.setSize(500, 50);
        botonControles.setPosition(150, 340);
        botonControles.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new PantallaControles(game));
                musica.stop();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        TextButton botonComoJugar = new TextButton("COMO SE JUEGA", this.game.skin, "default");
        botonComoJugar.setSize(500, 50);
        botonComoJugar.setPosition(150, 280);
        botonComoJugar.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new PantallaComoseJuega(game));
                musica.stop();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        TextButton botonSalir = new TextButton("SALIR DEL JUEGO", this.game.skin, "default");
        botonSalir.setSize(500, 50);
        botonSalir.setPosition(150, 85);
        botonSalir.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.exit(0);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });



        stage.addActor(botonJugar);
        stage.addActor(botonControles);
        stage.addActor(botonComoJugar);
        stage.addActor(botonSalir);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("Fondos/fondo_menuprincipal.png"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(texture,0, 0, texture.getWidth(), texture.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
    }
}

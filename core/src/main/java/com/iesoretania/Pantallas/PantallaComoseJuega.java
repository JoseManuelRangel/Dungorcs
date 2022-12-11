package com.iesoretania.Pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.iesoretania.Dungorcs;

public class PantallaComoseJuega extends PantallaBase {
    Dungorcs game;

    private Stage stage;
    private SpriteBatch batch;
    private static Texture texture;

    public PantallaComoseJuega(Dungorcs dungorcs) {
        super(dungorcs);
        this.game = dungorcs;
        stage = new Stage(new ScreenViewport());

        TextButton botonVolver = new TextButton("VOLVER AL MENU PRINCIPAL", this.game.skin, "default");
        botonVolver.setSize(500, 50);
        botonVolver.setPosition(150, 30);
        botonVolver.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new PantallaMenu(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(botonVolver);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("Fondos/fondo_comosejuega.png"));
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

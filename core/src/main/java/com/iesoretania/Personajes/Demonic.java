package com.iesoretania.Personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Demonic extends Actor {
    enum VerticalMovement { UP, NONE, DOWN };
    enum HorizontalMovement { LEFT, NONE, RIGHT };
    enum BiteMovement { BITTING, NONE };
    float statetime, collisionX, collisionY;

    private static TextureRegion demonicReposo;
    private static TextureRegion demonicCamina1, demonicCome1, demonicCome2, demonicCome3;

    HorizontalMovement horizontalMovement;
    VerticalMovement verticalMovement;
    BiteMovement biteMovement;
    TextureRegion actual;
    TiledMapTileLayer paredes;

    Animation<TextureRegion> animacionAndando, animacionMordiendo;


    public Demonic(TiledMap map) {
        Texture completo = new Texture(Gdx.files.internal("dungeon_tileset.png"));
        paredes = (TiledMapTileLayer) map.getLayers().get("Paredes");

        demonicReposo = new TextureRegion(completo, 115, 372, 26, 29);
        demonicCamina1 = new TextureRegion(completo, 148, 369, 22, 28);
        demonicCome1 = new TextureRegion(completo, 180, 367,22, 29);
        demonicCome2 = new TextureRegion(completo, 212, 367, 23, 34);
        demonicCome3 = new TextureRegion(completo, 244, 372, 23, 28);

        TextureRegion[] animacionAnda = new TextureRegion[2];
        animacionAnda[0] = demonicReposo;
        animacionAnda[1] = demonicCamina1;

        TextureRegion[] animacionMuerde = new TextureRegion[3];
        animacionMuerde[0] = demonicCome1;
        animacionMuerde[1] = demonicCome2;
        animacionMuerde[2] = demonicCome3;

        animacionAndando = new Animation<TextureRegion>(0.125f, animacionAnda);
        animacionMordiendo = new Animation<TextureRegion>(0.100f, animacionMuerde);
        statetime = 0f;

        actual = demonicReposo;
        horizontalMovement = HorizontalMovement.NONE;
        verticalMovement = VerticalMovement.NONE;
        biteMovement = BiteMovement.NONE;

        setSize(actual.getRegionWidth(), actual.getRegionHeight());
        addListener(new DemonicInputListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(actual, getX(), getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        statetime += Gdx.graphics.getDeltaTime();

        collisionX = getX();
        collisionY = getY();

        if(verticalMovement == VerticalMovement.UP) {
            this.moveBy(0, 100 * delta);
        }

        if(verticalMovement == VerticalMovement.DOWN) {
            this.moveBy(0, -100 * delta);
        }

        if(horizontalMovement == HorizontalMovement.LEFT) {
            this.moveBy(-100 * delta, 0);
        }

        if(horizontalMovement == HorizontalMovement.RIGHT) {
            this.moveBy(100 * delta, 0);
        }

        if(getX() < 0) {
            setX(0);
        }

        if(getX() >= 1580) {
            setX(1570);
        }

        if(getY() < 0) {
            setY(0);
        }

        if(getY() >= 1250) {
            setY(1250);
        }

        TiledMapTileLayer.Cell celda1 = paredes.getCell((int)getX()/16, (int)getY()/16);
        TiledMapTileLayer.Cell celda2 = paredes.getCell((int) (getX() + demonicReposo.getRegionWidth())/16, (int)(getY()/16));

        if(celda1 != null || celda2 != null) {
            setPosition(collisionX, collisionY);
        }

        if(verticalMovement == VerticalMovement.UP || verticalMovement == VerticalMovement.DOWN) {
            actual = animacionAndando.getKeyFrame(statetime, true);
        }

        if(horizontalMovement == HorizontalMovement.LEFT || horizontalMovement == HorizontalMovement.RIGHT) {
            actual = animacionAndando.getKeyFrame(statetime, true);
        }

        if(biteMovement == BiteMovement.BITTING) {
            actual = animacionMordiendo.getKeyFrame(statetime, true);
        }
    }

    class DemonicInputListener extends InputListener {
        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch(keycode) {
                case Input.Keys.DOWN:
                    verticalMovement = VerticalMovement.DOWN;
                    break;
                case Input.Keys.UP:
                    verticalMovement = VerticalMovement.UP;
                    break;
                case Input.Keys.LEFT:
                    horizontalMovement = HorizontalMovement.LEFT;
                    break;
                case Input.Keys.RIGHT:
                    horizontalMovement = HorizontalMovement.RIGHT;
                    break;
                case Input.Keys.F:
                    biteMovement = BiteMovement.BITTING;
                    break;
            }

            return true;
        }

        @Override
        public boolean keyUp(InputEvent event, int keycode) {
            switch(keycode) {
                case Input.Keys.DOWN:
                    if(verticalMovement == VerticalMovement.DOWN) {
                        verticalMovement = VerticalMovement.NONE;
                        actual = demonicReposo;
                    }
                    break;
                case Input.Keys.UP:
                    if(verticalMovement == VerticalMovement.UP) {
                        verticalMovement = VerticalMovement.NONE;
                        actual = demonicReposo;
                    }
                    break;
                case Input.Keys.LEFT:
                    if(horizontalMovement == HorizontalMovement.LEFT) {
                        horizontalMovement = HorizontalMovement.NONE;
                        actual = demonicReposo;
                    }
                    break;
                case Input.Keys.RIGHT:
                    if(horizontalMovement == HorizontalMovement.RIGHT) {
                        horizontalMovement = HorizontalMovement.NONE;
                        actual = demonicReposo;
                    }
                    break;
                case Input.Keys.F:
                    if(biteMovement == BiteMovement.BITTING) {
                        biteMovement = BiteMovement.NONE;
                        actual = demonicReposo;
                    }
            }

            return true;
        }
    }

}

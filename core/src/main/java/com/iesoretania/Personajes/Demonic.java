package com.iesoretania.Personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Demonic extends Actor {
    enum VerticalMovement { UP, NONE, DOWN };
    enum HorizontalMovement { LEFT, NONE, RIGHT };
    enum AttackMovement { BITTING, NONE };
    enum SprintingMovement { SPRINTING, NONE };

    HorizontalMovement horizontalMovement;
    VerticalMovement verticalMovement;
    AttackMovement attackMovement;
    SprintingMovement sprintingMovement;


    static Rectangle cuerpo;
    static Texture completo;
    private static TextureRegion actual;


    private static TextureRegion demonicReposo;
    private static TextureRegion demonicCamina1;
    private static TextureRegion demonicCome1, demonicCome2, demonicCome3;


    float statetime;
    float collisionParedesX, collisionParedesY;
    public static boolean estaAtacando = false, retrocediendo = false, muerto = false;
    public Vector2 reaparicion;


    TiledMapTileLayer paredes;
    TiledMap mapa;
    MapLayer posicion;
    MapObject puntoReaparicion;


    static TextureRegion[] texturasAndando, texturasAtaque;
    static Animation<TextureRegion> animacionAndando, animacionAtaque;


    public Demonic(TiledMap map) {
        this.mapa = map;

        cuerpo = new Rectangle();
        completo = new Texture(Gdx.files.internal("dungeon_tileset.png"));
        paredes = (TiledMapTileLayer) map.getLayers().get("Paredes");

        demonicReposo = new TextureRegion(completo, 115, 372, 26, 29);

        demonicCamina1 = new TextureRegion(completo, 148, 369, 22, 28);

        demonicCome1 = new TextureRegion(completo, 180, 367,22, 29);
        demonicCome2 = new TextureRegion(completo, 212, 367, 23, 34);
        demonicCome3 = new TextureRegion(completo, 244, 372, 23, 28);


        texturasAndando = new TextureRegion[2];
        texturasAndando[0] = demonicReposo;
        texturasAndando[1] = demonicCamina1;

        texturasAtaque = new TextureRegion[3];
        texturasAtaque[0] = demonicCome1;
        texturasAtaque[1] = demonicCome2;
        texturasAtaque[2] = demonicCome3;

        animacionAndando = new Animation<TextureRegion>(0.125f, texturasAndando);
        animacionAtaque = new Animation<TextureRegion>(0.100f, texturasAtaque);

        statetime = 0f;


        actual = demonicReposo;
        horizontalMovement = HorizontalMovement.NONE;
        verticalMovement = VerticalMovement.NONE;
        attackMovement = AttackMovement.NONE;
        sprintingMovement = SprintingMovement.NONE;


        reaparicion = getAparicion();

        setPosition(reaparicion.x, reaparicion.y);
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
        collisionParedesX = getX();
        collisionParedesY = getY();

        controlDeMovimiento(delta);

        limitesDePantalla();

        TiledMapTileLayer.Cell celdaPared = paredes.getCell((int)getX()/16, (int)getY()/16);
        TiledMapTileLayer.Cell celdaDemonic = paredes.getCell((int) (getX() + demonicReposo.getRegionWidth())/16, (int)(getY()/16));

        if(celdaPared != null || celdaDemonic != null) {
            setPosition(collisionParedesX, collisionParedesY);
        }

        if(verticalMovement == VerticalMovement.UP || verticalMovement == VerticalMovement.DOWN) {
            actual = animacionAndando.getKeyFrame(statetime, true);
        }

        if(horizontalMovement == HorizontalMovement.LEFT || horizontalMovement == HorizontalMovement.RIGHT) {
            actual = animacionAndando.getKeyFrame(statetime, true);
        }

        if(attackMovement == AttackMovement.BITTING) {
            actual = animacionAtaque.getKeyFrame(statetime, true);
        }
    }

    class DemonicInputListener extends InputListener {
        @Override
        public boolean keyDown(InputEvent event, int keycode) {
            switch(keycode) {
                case Input.Keys.DOWN:
                    verticalMovement = VerticalMovement.DOWN;
                    retrocediendo = false;
                    break;
                case Input.Keys.UP:
                    verticalMovement = VerticalMovement.UP;
                    retrocediendo = false;
                    break;
                case Input.Keys.LEFT:
                    horizontalMovement = HorizontalMovement.LEFT;
                    retrocediendo = true;
                    break;
                case Input.Keys.RIGHT:
                    horizontalMovement = HorizontalMovement.RIGHT;
                    retrocediendo = false;
                    break;
                case Input.Keys.F:
                    if(retrocediendo == false) {
                        attackMovement = AttackMovement.BITTING;
                        estaAtacando = true;
                    }
                    break;
                case Input.Keys.A:
                    sprintingMovement = SprintingMovement.SPRINTING;
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
                    if(attackMovement == AttackMovement.BITTING) {
                        attackMovement = AttackMovement.NONE;
                        actual = demonicReposo;
                        estaAtacando = false;
                    }
                    break;
                case Input.Keys.A:
                    if(sprintingMovement == SprintingMovement.SPRINTING) {
                        sprintingMovement = SprintingMovement.NONE;
                        actual = demonicReposo;
                    }
            }
            return true;
        }
    }

    private Vector2 getAparicion() {
        posicion = mapa.getLayers().get("Entidades");
        puntoReaparicion = posicion.getObjects().get("Spawn");

        return new Vector2(puntoReaparicion.getProperties().get("x", Float.class) - actual.getRegionWidth() / 2f, puntoReaparicion.getProperties().get("y", Float.class));
    }


    public Rectangle getShape() {
        cuerpo.set((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        return cuerpo;
    }

    public void limitesDePantalla() {
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
    }

    public void controlDeMovimiento(float delta) {
        if(verticalMovement == VerticalMovement.UP) {
            if(attackMovement == AttackMovement.BITTING) {
                this.moveBy(0, 70 * delta);
            } else if(sprintingMovement == SprintingMovement.SPRINTING) {
                this.moveBy(0, 190 * delta);
            } else {
                this.moveBy(0, 130 * delta);
            }
        }

        if(verticalMovement == VerticalMovement.DOWN) {
            if(attackMovement == AttackMovement.BITTING) {
                this.moveBy(0, -70 * delta);
            } else if(sprintingMovement == SprintingMovement.SPRINTING) {
                this.moveBy(0, -190 * delta);
            } else {
                this.moveBy(0, -130 * delta);
            }
        }

        if(horizontalMovement == HorizontalMovement.LEFT) {
            if(attackMovement == AttackMovement.BITTING) {
                this.moveBy(-70 * delta, 0);
            } else if(sprintingMovement == SprintingMovement.SPRINTING) {
                this.moveBy(-190 * delta, 0);
            } else {
                this.moveBy(-130 * delta, 0);
            }
        }

        if(horizontalMovement == HorizontalMovement.RIGHT) {
            if(attackMovement == AttackMovement.BITTING) {
                this.moveBy(70 * delta, 0);
            } else if(sprintingMovement == SprintingMovement.SPRINTING) {
                this.moveBy(160 * delta, 0);
            } else {
                this.moveBy(130 * delta, 0);
            }
        }
    }
}

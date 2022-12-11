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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Demonic extends Actor {
    /**
     * VerticalMovement: Control de movimiento vertical.
     * HorizontalMovement: Control de movimiento horizontal.
     * AttackMovement: Control de movimiento al atacar.
     */
    enum VerticalMovement { UP, NONE, DOWN };
    enum HorizontalMovement { LEFT, NONE, RIGHT };
    enum AttackMovement { BITTING, NONE };
    HorizontalMovement horizontalMovement;
    VerticalMovement verticalMovement;
    AttackMovement attackMovement;
    static Rectangle cuerpo;
    static Texture completo;

    /**
     * statetime: Tiempo que tardará la animación en cuestión en realizarse.
     *
     * Colisiones:
     * CAPA PAREDES - Posición en el que Demonic chocará con un objeto de la capa Paredes
     * collisionParedesX: Eje x.
     * collisionParedesY: Eje y.
     *
     * estaAtacando: Variable booleana que controlará si el personaje se encuentra en estado de ataque o no.
     */
    float statetime;
    float collisionParedesX, collisionParedesY;
    public static boolean estaAtacando = false, retrocediendo = false, muerto = false;


    /**
     * TextureRegion de los distintos estados de Demonic.
     */
    private static TextureRegion demonicReposo;
    private static TextureRegion demonicCamina1;
    private static TextureRegion demonicCome1, demonicCome2, demonicCome3;

    /**
     * reaparicion: Variable que controlará la posición en el eje x e y del punto de spawn del personaje.
     * actual: Region de texturas utilizada en cada momento por Demonic.
     * paredes: Variable que obtendrá la capa "Paredes" del tilemap.
     * mapa: Variable que contendrá el mapa.
     * posicion: Variable que obtendrá la capa de objetos "Objetos" del tilemap.
     * puntoreaparicion: Variable que obtendrá el objeto de reaparición asignado en el tilemap dentro de la capa "Objetos".
     */
    private TextureRegion actual;
    TiledMapTileLayer paredes, pinchos;
    TiledMap mapa;
    public Vector2 reaparicion;
    MapLayer posicion;
    MapObject puntoreaparicion;


    /**
     * animacionAndando: Animación de movimiento para Demonic.
     * animacionMordiendo: Animación de ataque para Demonic.
     */
    static TextureRegion[] animacionAnda, animacionMuerde;
    static Animation<TextureRegion> animacionAndando, animacionMordiendo;




    public Demonic(TiledMap map) {
        this.mapa = map;

        cuerpo = new Rectangle();

        /**
         * Cargamos la textura completa del tileset usado, obtenemos la capa Paredes del mapa y definimos los TextureRegion
         * que visualizarán el estado del personaje.
         */
        completo = new Texture(Gdx.files.internal("dungeon_tileset.png"));
        paredes = (TiledMapTileLayer) map.getLayers().get("Paredes");
        pinchos = (TiledMapTileLayer) map.getLayers().get("Pinchos");

        demonicReposo = new TextureRegion(completo, 115, 372, 26, 29);
        demonicCamina1 = new TextureRegion(completo, 148, 369, 22, 28);
        demonicCome1 = new TextureRegion(completo, 180, 367,22, 29);
        demonicCome2 = new TextureRegion(completo, 212, 367, 23, 34);
        demonicCome3 = new TextureRegion(completo, 244, 372, 23, 28);

        /**
         * animacionAnda: Animacion de movimiento en el que le pondremos dos texturas distintas.
         * animacionMuerde: Animación de ataque de Demonic.
         *
         * Después se meterán en un objeto de tipo Animation en el que se establecerá el tiempo que tardará en realizar la animación y el Array de TextureRegion que usará.
         */
        animacionAnda = new TextureRegion[2];
        animacionAnda[0] = demonicReposo;
        animacionAnda[1] = demonicCamina1;

        animacionMuerde = new TextureRegion[3];
        animacionMuerde[0] = demonicCome1;
        animacionMuerde[1] = demonicCome2;
        animacionMuerde[2] = demonicCome3;

        animacionAndando = new Animation<TextureRegion>(0.125f, animacionAnda);
        animacionMordiendo = new Animation<TextureRegion>(0.100f, animacionMuerde);

        statetime = 0f;

        /**
         * Al empezar tendrá la textura de reposo y no se moverá ni horizontal ni verticalmente. Tampoco empezará atacando.
         */
        actual = demonicReposo;
        horizontalMovement = HorizontalMovement.NONE;
        verticalMovement = VerticalMovement.NONE;
        attackMovement = AttackMovement.NONE;

        /**
         * Establecemos el punto de reaparición llamando al método creado más abajo llamado getAparicion().
         */
        reaparicion = getAparicion();

        /**
         * Tras obtener las coordenadas del punto de spawn le indicamos que reaparezca allí, le establecemos su tamaño y le añadimos el Escuchador para presionar el teclado.
         */
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

        /**
         * Se coge las coordenadas del personaje para comprobar en cada momento si choca o no.
         */
        collisionParedesX = getX();
        collisionParedesY = getY();

        /**
         * Movimiento para Demonic en función de la tecla pulsada en el teclado.
         * UP:    Flecha direccional hacia arriba.
         * DOWN:  Flecha direccional hacia abajo.
         * LEFT:  Flecha direccional hacia la izquierda.
         * RIGHT: Flecha direccional hacia la derecha.
         */
        if(verticalMovement == VerticalMovement.UP) {
            if(attackMovement == AttackMovement.BITTING) {
                this.moveBy(0, 70 * delta);
            } else {
                this.moveBy(0, 130 * delta);
            }
        }

        if(verticalMovement == VerticalMovement.DOWN) {
            if(attackMovement == AttackMovement.BITTING) {
                this.moveBy(0, -70 * delta);
            } else {
                this.moveBy(0, -130 * delta);
            }
        }

        if(horizontalMovement == HorizontalMovement.LEFT) {
            if(attackMovement == AttackMovement.BITTING) {
                this.moveBy(-70 * delta, 0);
            } else {
                this.moveBy(-130 * delta, 0);
            }
        }

        if(horizontalMovement == HorizontalMovement.RIGHT) {
            if(attackMovement == AttackMovement.BITTING) {
                this.moveBy(70 * delta, 0);
            } else {
                this.moveBy(130 * delta, 0);
            }
        }


        /**
         * Establece los límites de movimiento del personaje para que no se pueda salir de las
         * coordenadas establecidas como límite.
         */
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
            actual = animacionMordiendo.getKeyFrame(statetime, true);
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
            }

            return true;
        }
    }

    private Vector2 getAparicion() {
        posicion = mapa.getLayers().get("Entidades");
        puntoreaparicion = posicion.getObjects().get("Spawn");

        return new Vector2(puntoreaparicion.getProperties().get("x", Float.class) - actual.getRegionWidth() / 2f, puntoreaparicion.getProperties().get("y", Float.class));
    }


    public Rectangle getShape() {
        cuerpo.set((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        return cuerpo;
    }
}

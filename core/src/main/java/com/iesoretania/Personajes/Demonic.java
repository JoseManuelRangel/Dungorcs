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
    public static boolean estaAtacando = false;


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
    TextureRegion actual;
    TiledMapTileLayer paredes;
    TiledMap mapa;
    Vector2 reaparicion;
    MapLayer posicion;
    MapObject puntoreaparicion;


    /**
     * animacionAndando: Animación de movimiento para Demonic.
     * animacionMordiendo: Animación de ataque para Demonic.
     */
    Animation<TextureRegion> animacionAndando, animacionMordiendo;


    public Demonic(TiledMap map) {
        this.mapa = map;

        /**
         * Cargamos la textura completa del tileset usado, obtenemos la capa Paredes del mapa y definimos los TextureRegion
         * que visualizarán el estado del personaje.
         */
        Texture completo = new Texture(Gdx.files.internal("dungeon_tileset.png"));
        paredes = (TiledMapTileLayer) map.getLayers().get("Paredes");

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


        TiledMapTileLayer.Cell celda1 = paredes.getCell((int)getX()/16, (int)getY()/16);
        TiledMapTileLayer.Cell celda2 = paredes.getCell((int) (getX() + demonicReposo.getRegionWidth())/16, (int)(getY()/16));


        if(celda1 != null || celda2 != null) {
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
                    attackMovement = AttackMovement.BITTING;
                    estaAtacando = true;
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
        posicion = mapa.getLayers().get("Objetos");
        puntoreaparicion = posicion.getObjects().get("Spawn");

        return new Vector2(puntoreaparicion.getProperties().get("x", Float.class) - actual.getRegionWidth() / 2f, puntoreaparicion.getProperties().get("y", Float.class));
    }

    public Rectangle getShape() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}

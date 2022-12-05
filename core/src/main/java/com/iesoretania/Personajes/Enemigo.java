package com.iesoretania.Personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Enemigo extends Actor {
    TiledMap mapa;
    Animation<TextureRegion> movimiento;
    SpriteBatch spriteBatch;

    private TextureRegion mov1, mov2, mov3;
    private TextureRegion actual;

    float stateTime;
    int num;
    Vector2 reaparicion;
    MapLayer posicion;
    MapObject puntoreaparicion;
    TiledMapTileLayer paredes;
    SequenceAction secuencia_movimiento;

    public static boolean muerto = false;

    public Enemigo(TiledMap map, int n) {
        this.mapa = map;
        this.num = n;

        Texture completo = new Texture(Gdx.files.internal("dungeon_tileset.png"));
        paredes = (TiledMapTileLayer) map.getLayers().get("Paredes");


        mov1 = new TextureRegion(completo, 387, 339, 11, 14);
        mov2 = new TextureRegion(completo, 403, 337, 11, 15);
        mov3 = new TextureRegion(completo, 371, 334, 11, 18);

        TextureRegion[] text_movimiento = new TextureRegion[3];
        text_movimiento[0] = mov1;
        text_movimiento[1] = mov2;
        text_movimiento[2] = mov3;

        movimiento = new Animation<TextureRegion>(1f, text_movimiento);
        stateTime = 0f;

        actual = movimiento.getKeyFrame(stateTime, true);

        reaparicion = getAparicion(n);
        setPosition(reaparicion.x, reaparicion.y);

        secuencia_movimiento = obtener_patron(n);
        addAction(Actions.repeat(RepeatAction.FOREVER, secuencia_movimiento));

        setSize(actual.getRegionWidth() + 5, actual.getRegionHeight() + 5);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(muerto == true) {
            stateTime += Gdx.graphics.getDeltaTime();

            if(stateTime > 0.4f) {
                muerto = false;

                SequenceAction eliminado = Actions.sequence(Actions.fadeOut(1f), Actions.removeActor());
                addAction(eliminado);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(actual, getX(), getY(), getOriginX(), getOriginY(), getScaleX(), getScaleY(), getWidth(), getHeight(), getRotation());
    }

    private Vector2 getAparicion(int num) {
        String numm = String.valueOf(num);
        posicion = mapa.getLayers().get("Entidades");

        puntoreaparicion = posicion.getObjects().get("Enemigo" + numm);

        return new Vector2(puntoreaparicion.getProperties().get("x", Float.class) - actual.getRegionWidth() / 2f, puntoreaparicion.getProperties().get("y", Float.class));
    }

    private SequenceAction obtener_patron(int num) {
        switch (num) {
            case 1:
                MoveToAction first1 = Actions.moveTo(reaparicion.x, 1000, 2.5f, Interpolation.sine);
                MoveToAction second1 = Actions.moveTo(reaparicion.x, reaparicion.y, 2f, Interpolation.sine);
                MoveToAction third1 = Actions.moveTo(reaparicion.x + 300, reaparicion.y, 2.5f, Interpolation.sineOut);
                MoveToAction fourth1 = Actions.moveTo(reaparicion.x, reaparicion.y, 2f, Interpolation.sine);
                SequenceAction sequence1 = Actions.sequence(first1, second1, third1, fourth1);
                return sequence1;
            case 2:
                MoveToAction first2 = Actions.moveTo(350, reaparicion.y, 2.5f, Interpolation.sine);
                MoveToAction second2 = Actions.moveTo(reaparicion.x - 30, reaparicion.y, 2.5f, Interpolation.sine);
                MoveToAction third2 = Actions.moveTo(reaparicion.x - 30, reaparicion.y + 200, 2.75f, Interpolation.sine);
                MoveToAction fourth2 = Actions.moveTo(reaparicion.x - 30, reaparicion.y, 2f, Interpolation.sine);
                MoveToAction fifth2 = Actions.moveTo(reaparicion.x, reaparicion.y, 1f, Interpolation.sineOut);
                SequenceAction sequence2 = Actions.sequence(first2, second2, third2, fourth2, fifth2);
                return sequence2;
            case 3:
                MoveToAction first3 = Actions.moveTo(350, reaparicion.y, 2.85f, Interpolation.sine);
                MoveToAction second3 = Actions.moveTo(reaparicion.x, reaparicion.y, 2.85f, Interpolation.sine);
                MoveToAction third3 = Actions.moveTo(reaparicion.x, reaparicion.y + 60, 1f, Interpolation.sine);
                MoveToAction fourth3 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.5f, Interpolation.sineOut);
                SequenceAction sequence3 = Actions.sequence(first3, second3, third3, fourth3);
                return sequence3;
            case 4:
                MoveToAction first4 = Actions.moveTo(100, reaparicion.y - 50, 3f, Interpolation.sine);
                MoveToAction second4 = Actions.moveTo(reaparicion.x + 80, reaparicion.y - 50, 2.5f, Interpolation.sine);
                MoveToAction third4 = Actions.moveTo(reaparicion.x, reaparicion.y, 3f,  Interpolation.sineOut);
                SequenceAction sequence4 = Actions.sequence(first4, second4, third4);
                return sequence4;
            default:
                return null;
        }
    }

    public Rectangle getShape() {
        return new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }
}

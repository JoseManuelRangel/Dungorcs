package com.iesoretania.Personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class Moneda extends Actor {
    TiledMap mapa;
    Animation<TextureRegion> movimiento;

    private TextureRegion mon1, mon2, mon3, mon4;
    private TextureRegion actual;
    float stateTime;
    int num;

    Vector2 reaparicion;
    MapLayer posicion;
    MapObject puntoreaparicion;
    SequenceAction secuenciaMoneda;

    public static boolean cogida = false;

    public Moneda(TiledMap map, int n) {
        this.mapa = map;
        this.num = n;

        Texture completo = new Texture(Gdx.files.internal("dungeon_tileset.png"));
        mon1 = new TextureRegion(completo, 289, 273, 6, 7);
        mon2 = new TextureRegion(completo, 298, 273, 4, 7);
        mon3 = new TextureRegion(completo, 307, 273, 2, 7);
        mon4 = new TextureRegion(completo, 314, 273, 4, 7);

        TextureRegion[] text_movimiento = new TextureRegion[4];
        text_movimiento[0] = mon1;
        text_movimiento[1] = mon2;
        text_movimiento[2] = mon3;
        text_movimiento[3] = mon4;

        movimiento = new Animation<TextureRegion>(0.25f, text_movimiento);
        stateTime = 0f;
        actual = mon1;

        reaparicion = getAparicion(n);
        setPosition(reaparicion.x, reaparicion.y);
        setSize(actual.getRegionWidth() + 4, actual.getRegionHeight() + 4);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += Gdx.graphics.getDeltaTime();
        actual = movimiento.getKeyFrame(stateTime, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        actual = movimiento.getKeyFrame(stateTime, true);
        batch.draw(actual, getX(), getY(), getOriginX(), getOriginY(), getScaleX(), getScaleY(), getWidth(), getHeight(), getRotation());
    }

    private Vector2 getAparicion(int coin) {
        String nummoneda = String.valueOf(coin);
        posicion = mapa.getLayers().get("Monedas");

        puntoreaparicion = posicion.getObjects().get("M" + nummoneda);

        return new Vector2(puntoreaparicion.getProperties().get("x", Float.class) - actual.getRegionWidth() / 2f, puntoreaparicion.getProperties().get("y", Float.class));
    }

    public Rectangle getShape() {
        return new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }
}

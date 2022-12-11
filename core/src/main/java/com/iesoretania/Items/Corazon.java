package com.iesoretania.Items;

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

public class Corazon extends Actor {
    static Animation<TextureRegion> movimiento;
    static Texture completo;
    private TextureRegion actual;
    static TextureRegion cor1, cor2, cor3;
    static TextureRegion[] corMovimiento;
    static Rectangle cuerpo;
    public static boolean cogido = false;
    float stateTime;
    int num;

    TiledMap mapa;
    Vector2 reaparicion;
    MapLayer posicion;
    MapObject puntoReaparicion;


    public Corazon(TiledMap map, int n) {
        this.mapa = map;
        this.num = n;

        cuerpo = new Rectangle();

        completo = new Texture(Gdx.files.internal("dungeon_tileset.png"));
        cor1 = new TextureRegion(completo, 289, 258, 13, 12);
        cor2 = new TextureRegion(completo, 305, 258, 13, 12);
        cor3 = new TextureRegion(completo, 321, 258, 13, 12);

        corMovimiento = new TextureRegion[3];
        corMovimiento[0] = cor3;
        corMovimiento[1] = cor2;
        corMovimiento[2] = cor1;

        movimiento = new Animation<TextureRegion>(0.10f, corMovimiento);
        stateTime = 0f;
        actual = cor1;


        reaparicion = getAparicion(n);
        setPosition(reaparicion.x, reaparicion.y);
        setSize(actual.getRegionWidth() + 6, actual.getRegionHeight() + 6);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        stateTime += Gdx.graphics.getDeltaTime();
        actual = movimiento.getKeyFrame(stateTime, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(actual, getX(), getY(), getOriginX(), getOriginY(), getScaleX(), getScaleY(), getWidth(), getHeight(), getRotation());
    }

    private Vector2 getAparicion(int heart) {
        String numcorazon = String.valueOf(heart);
        posicion = mapa.getLayers().get("Corazones");

        puntoReaparicion = posicion.getObjects().get("C" + numcorazon);

        return new Vector2(puntoReaparicion.getProperties().get("x", Float.class) - actual.getRegionWidth() / 2f, puntoReaparicion.getProperties().get("y", Float.class));
    }

    public Rectangle getShape() {
        cuerpo.set((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        return cuerpo;
    }
}

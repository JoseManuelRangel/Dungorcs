package com.iesoretania.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Rectangle;

public class PocionesPrisma extends Actor {
    static Texture completo;
    static Rectangle cuerpo;

    private TextureRegion actual;
    int num;

    static TextureRegion poc1, poc2, poc3, poc4;
    public boolean cogida;

    TiledMap mapa;
    Vector2 reaparicion;
    MapLayer posicion;
    MapObject puntoReaparicion;

    public PocionesPrisma(TiledMap map, int n) {
        this.mapa = map;
        this.num = n;

        cogida = false;
        cuerpo = new Rectangle();
        completo = new Texture(Gdx.files.internal("dungeon_tileset.png"));

        poc1 = new TextureRegion(completo, 291, 228, 10, 11);
        poc2 = new TextureRegion(completo, 307, 228, 10, 11);
        poc3 = new TextureRegion(completo, 323, 228, 10, 11);
        poc4 = new TextureRegion(completo, 339, 228, 10, 11);

        if(n == 1) {
            actual = poc1;
        } else if(n == 2) {
            actual = poc2;
        } else if(n == 3) {
            actual = poc3;
        } else if(n == 4) {
            actual = poc4;
        }

        reaparicion = getAparicion(n);
        setPosition(reaparicion.x, reaparicion.y);
        setSize(actual.getRegionWidth() + 8, actual.getRegionHeight() + 8);
        setVisible(false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(actual, getX(), getY(), getOriginX(), getOriginY(), getScaleX(), getScaleY(), getWidth(), getHeight(), getRotation());
    }

    private Vector2 getAparicion(int potion) {
        String numpotion = String.valueOf(potion);
        posicion = mapa.getLayers().get("Pociones");

        puntoReaparicion = posicion.getObjects().get("Pocion" + numpotion);

        return new Vector2(puntoReaparicion.getProperties().get("x", Float.class) - actual.getRegionWidth() / 2f, puntoReaparicion.getProperties().get("y", Float.class));
    }

    public Rectangle getShape() {
        cuerpo.set((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        return cuerpo;
    }
}

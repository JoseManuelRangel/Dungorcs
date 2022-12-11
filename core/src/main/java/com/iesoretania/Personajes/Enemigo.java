package com.iesoretania.Personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
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
    static Animation<TextureRegion> movimiento;
    static Texture completo;
    static TextureRegion ene1, ene2, ene3;
    static TextureRegion[] text_movimiento;
    static Rectangle cuerpo;
    private TextureRegion actual;

    float stateTime;
    int num;
    Vector2 reaparicion;
    MapLayer posicion;
    MapObject puntoreaparicion;
    SequenceAction secuenciaEnemigo;

    public static boolean muerto = false;

    public Enemigo(TiledMap map, int n) {
        this.mapa = map;
        this.num = n;

        cuerpo = new Rectangle();
        completo = new Texture(Gdx.files.internal("dungeon_tileset.png"));
        ene1 = new TextureRegion(completo, 387, 339, 11, 14);
        ene2 = new TextureRegion(completo, 403, 337, 11, 15);
        ene3 = new TextureRegion(completo, 371, 334, 11, 18);

        text_movimiento = new TextureRegion[3];
        text_movimiento[0] = ene1;
        text_movimiento[1] = ene2;
        text_movimiento[2] = ene3;

        movimiento = new Animation<TextureRegion>(0.1f, text_movimiento);
        stateTime = 0f;
        actual = ene1;

        reaparicion = getAparicion(n);
        setPosition(reaparicion.x, reaparicion.y);

        if(n!=10 && n!=12 && n!=16 && n!=18 && n!=25 && n!=27 && n!=31 && n!=32 && n!=34 && n!=35) {
            secuenciaEnemigo = obtener_patron(n);
            addAction(Actions.repeat(RepeatAction.FOREVER, secuenciaEnemigo));
        }

        setSize(actual.getRegionWidth() + 6, actual.getRegionHeight() + 6);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += Gdx.graphics.getDeltaTime();
        actual = movimiento.getKeyFrame(stateTime, true);

        /*if(muerto == true) {
            muerto = false;

            SequenceAction eliminado = Actions.sequence(Actions.fadeOut(1f), Actions.removeActor());
            addAction(eliminado);
        }*/
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        actual = movimiento.getKeyFrame(stateTime, true);
        batch.draw(actual, getX(), getY(), getOriginX(), getOriginY(), getScaleX(), getScaleY(), getWidth(), getHeight(), getRotation());
    }

    private Vector2 getAparicion(int enemy) {
        String nummenemigo = String.valueOf(enemy);
        posicion = mapa.getLayers().get("Entidades");

        puntoreaparicion = posicion.getObjects().get("E" + nummenemigo);

        return new Vector2(puntoreaparicion.getProperties().get("x", Float.class) - actual.getRegionWidth() / 2f, puntoreaparicion.getProperties().get("y", Float.class));
    }

    private SequenceAction obtener_patron(int num) {
        switch (num) {
            case 1:
                MoveToAction p1 = Actions.moveTo(reaparicion.x, 1000, 2.5f, Interpolation.sine);
                MoveToAction s1 = Actions.moveTo(reaparicion.x, reaparicion.y, 2f, Interpolation.sine);
                MoveToAction t1 = Actions.moveTo(reaparicion.x + 300, reaparicion.y, 2.5f, Interpolation.sineOut);
                MoveToAction c1 = Actions.moveTo(reaparicion.x, reaparicion.y, 2f, Interpolation.sine);
                SequenceAction sequence1 = Actions.sequence(p1, s1, t1, c1);
                return sequence1;
            case 2:
                MoveToAction p2 = Actions.moveTo(350, reaparicion.y, 2.5f, Interpolation.sine);
                MoveToAction s2 = Actions.moveTo(reaparicion.x - 30, reaparicion.y, 2.5f, Interpolation.sine);
                MoveToAction t2 = Actions.moveTo(reaparicion.x - 30, reaparicion.y + 200, 2.75f, Interpolation.sine);
                MoveToAction c2 = Actions.moveTo(reaparicion.x - 30, reaparicion.y, 2f, Interpolation.sine);
                MoveToAction q2 = Actions.moveTo(reaparicion.x, reaparicion.y, 1f, Interpolation.sineOut);
                SequenceAction sequence2 = Actions.sequence(p2, s2, t2, c2, q2);
                return sequence2;
            case 3:
                MoveToAction p3 = Actions.moveTo(350, reaparicion.y, 2.85f, Interpolation.sine);
                MoveToAction s3 = Actions.moveTo(reaparicion.x, reaparicion.y, 2.85f, Interpolation.sine);
                MoveToAction t3 = Actions.moveTo(reaparicion.x, reaparicion.y + 60, 2.5f, Interpolation.sine);
                MoveToAction c3 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.5f, Interpolation.sineOut);
                SequenceAction sequence3 = Actions.sequence(p3, s3, t3, c3);
                return sequence3;
            case 4:
                MoveToAction p4 = Actions.moveTo(100, reaparicion.y - 50, 2.75f, Interpolation.sine);
                MoveToAction s4 = Actions.moveTo(reaparicion.x + 80, reaparicion.y - 50, 2.5f, Interpolation.sine);
                MoveToAction t4 = Actions.moveTo(reaparicion.x + 80, (reaparicion.y - 50) - 20, 1f, Interpolation.sine);
                MoveToAction c4 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.5f,  Interpolation.sineOut);
                SequenceAction sequence4 = Actions.sequence(p4, s4, t4, c4);
                return sequence4;
            case 5:
                MoveToAction p5 = Actions.moveTo(200, reaparicion.y, 2f, Interpolation.sine);
                MoveToAction s5 = Actions.moveTo(reaparicion.x, reaparicion.y, 2f, Interpolation.sineOut);
                SequenceAction sequence5 = Actions.sequence(p5, s5);
                return sequence5;
            case 6:
                MoveToAction p6 = Actions.moveTo(reaparicion.x, reaparicion.y + 200, 1.5f, Interpolation.sine);
                MoveToAction s6 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.75f, Interpolation.sine);
                MoveToAction t6 = Actions.moveTo(reaparicion.x + 100, reaparicion.y, 2f, Interpolation.sine);
                MoveToAction c6 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.5f, Interpolation.sineOut);
                SequenceAction sequence6 = Actions.sequence(p6, s6, t6, c6);
                return sequence6;
            case 7:
                MoveToAction p7 = Actions.moveTo(reaparicion.x, reaparicion.y + 100, 2f, Interpolation.sine);
                MoveToAction s7 = Actions.moveTo(reaparicion.x - 80, reaparicion.y + 100, 2f, Interpolation.sine);
                MoveToAction t7 = Actions.moveTo(reaparicion.x, reaparicion.y + 100, 1.75f, Interpolation.sine);
                MoveToAction c7 = Actions.moveTo(reaparicion.x, reaparicion.y, 2f, Interpolation.sineOut);
                SequenceAction sequence7 = Actions.sequence(p7, s7, t7,  c7);
                return sequence7;
            case 8:
                MoveToAction p8 = Actions.moveTo(reaparicion.x - 80, reaparicion.y, 1.25f, Interpolation.sine);
                MoveToAction s8 = Actions.moveTo(reaparicion.x - 80, reaparicion.y - 80, 1f, Interpolation.sine);
                MoveToAction t8 = Actions.moveTo(reaparicion.x - 80, reaparicion.y, 1.75f, Interpolation.sine);
                MoveToAction c8 = Actions.moveTo(reaparicion.x, reaparicion.y, 2f, Interpolation.sineOut);
                SequenceAction sequence8 = Actions.sequence(p8, s8, t8,  c8);
                return sequence8;
            case 9:
                MoveToAction p9 = Actions.moveTo(reaparicion.x + 100, reaparicion.y, 1.5f, Interpolation.sine);
                MoveToAction s9 = Actions.moveTo((reaparicion.x + 100), reaparicion.y - 90, 2f, Interpolation.sine);
                MoveToAction t9 = Actions.moveTo(reaparicion.x, reaparicion.y - 90, 1.75f, Interpolation.sine);
                MoveToAction c9 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.5f, Interpolation.sineOut);
                SequenceAction sequence9 = Actions.sequence(p9, s9, t9, c9);
                return sequence9;
            case 11:
                MoveToAction p11 = Actions.moveTo(reaparicion.x, reaparicion.y - 100, 1.5f, Interpolation.sine);
                MoveToAction s11 = Actions.moveTo((reaparicion.x - 200), reaparicion.y - 100, 0.5f, Interpolation.sine);
                MoveToAction t11 = Actions.moveTo(reaparicion.x, reaparicion.y - 100, 1.25f, Interpolation.sine);
                MoveToAction c11 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.5f, Interpolation.sineOut);
                SequenceAction sequence11 = Actions.sequence(p11, s11, t11, c11);
                return sequence11;
            case 13:
                MoveToAction p13 = Actions.moveTo(reaparicion.x + 80, reaparicion.y, 0.5f, Interpolation.sine);
                MoveToAction s13 = Actions.moveTo((reaparicion.x + 80), reaparicion.y - 80, 0.75f, Interpolation.sine);
                MoveToAction t13 = Actions.moveTo(reaparicion.x, reaparicion.y - 80, 0.5f, Interpolation.sine);
                MoveToAction c13 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.5f, Interpolation.sineOut);
                SequenceAction sequence13 = Actions.sequence(p13, s13, t13, c13);
                return sequence13;
            case 14:
                MoveToAction p14 = Actions.moveTo(reaparicion.x + 50, reaparicion.y, 1f, Interpolation.sine);
                MoveToAction s14 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.5f, Interpolation.sineOut);
                SequenceAction sequence14 = Actions.sequence(p14, s14);
                return sequence14;
            case 15:
                MoveToAction p15 = Actions.moveTo(reaparicion.x, reaparicion.y + 50, 1.5f, Interpolation.sine);
                MoveToAction s15 = Actions.moveTo(reaparicion.x + 100, reaparicion.y + 50, 0.15f, Interpolation.sine);
                MoveToAction t15 = Actions.moveTo(reaparicion.x, reaparicion.y + 50, 1f, Interpolation.sine);
                MoveToAction c15 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.75f, Interpolation.sineOut);
                SequenceAction sequence15 = Actions.sequence(p15, s15, t15, c15);
                return sequence15;
            case 17:
                MoveToAction p17 = Actions.moveTo(reaparicion.x - 100, reaparicion.y, 1.5f, Interpolation.sine);
                MoveToAction s17 = Actions.moveTo((reaparicion.x - 100), reaparicion.y + 200, 0.25f, Interpolation.sine);
                MoveToAction t17 = Actions.moveTo(reaparicion.x, reaparicion.y, 0.75f, Interpolation.sineOut);
                SequenceAction sequence17 = Actions.sequence(p17, s17, t17);
                return sequence17;
            case 19:
                MoveToAction p19 = Actions.moveTo(reaparicion.x + 200, reaparicion.y, 1.5f, Interpolation.sine);
                MoveToAction s19 = Actions.moveTo((reaparicion.x + 200), reaparicion.y - 80, 1f, Interpolation.sine);
                MoveToAction t19 = Actions.moveTo(reaparicion.x + 100, reaparicion.y - 50, 1.75f, Interpolation.sine);
                MoveToAction c19 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.5f, Interpolation.sineOut);
                SequenceAction sequence19 = Actions.sequence(p19, s19, t19, c19);
                return sequence19;
            case 20:
                MoveToAction p20 = Actions.moveTo(reaparicion.x + 80, reaparicion.y, 1.85f, Interpolation.sine);
                MoveToAction s20 = Actions.moveTo((reaparicion.x + 80), reaparicion.y + 70, 2f, Interpolation.sine);
                MoveToAction t20 = Actions.moveTo(reaparicion.x + 80, reaparicion.y, 1.25f, Interpolation.sine);
                MoveToAction c20 = Actions.moveTo(reaparicion.x, reaparicion.y, 1f, Interpolation.sineOut);
                SequenceAction sequence20 = Actions.sequence(p20, s20, t20, c20);
                return sequence20;
            case 21:
                MoveToAction p21 = Actions.moveTo(reaparicion.x - 80, reaparicion.y, 1f, Interpolation.sine);
                MoveToAction s21 = Actions.moveTo((reaparicion.x + 30), reaparicion.y - 70, 1f, Interpolation.sine);
                MoveToAction t21 = Actions.moveTo(reaparicion.x + 30, reaparicion.y - 90, 1.25f, Interpolation.sine);
                MoveToAction c21 = Actions.moveTo(reaparicion.x + 30, reaparicion.y, 1f, Interpolation.sine);
                MoveToAction q21 = Actions.moveTo(reaparicion.x, reaparicion.y, 1f, Interpolation.sineOut);
                SequenceAction sequence21 = Actions.sequence(p21, s21, t21, c21, q21);
                return sequence21;
            case 22:
                MoveToAction p22 = Actions.moveTo(reaparicion.x + 250, reaparicion.y, 3f, Interpolation.sine);
                MoveToAction s22 = Actions.moveTo(reaparicion.x, reaparicion.y, 2f, Interpolation.sineOut);
                SequenceAction sequence22 = Actions.sequence(p22, s22);
                return sequence22;
            case 23:
                MoveToAction p23 = Actions.moveTo(reaparicion.x + 150, reaparicion.y, 3f, Interpolation.sine);
                MoveToAction s23 = Actions.moveTo(reaparicion.x, reaparicion.y, 2f, Interpolation.sineOut);
                SequenceAction sequence23 = Actions.sequence(p23, s23);
                return sequence23;
            case 24:
                MoveToAction p24 = Actions.moveTo(reaparicion.x + 100, reaparicion.y, 3f, Interpolation.sine);
                MoveToAction s24 = Actions.moveTo(reaparicion.x, reaparicion.y, 2.5f, Interpolation.sineOut);
                SequenceAction sequence24 = Actions.sequence(p24, s24);
                return sequence24;
            case 26:
                MoveToAction p26 = Actions.moveTo(reaparicion.x - 60, reaparicion.y, 1.75f, Interpolation.sine);
                MoveToAction s26 = Actions.moveTo(reaparicion.x, reaparicion.y, 0.25f, Interpolation.sineOut);
                SequenceAction sequence26 = Actions.sequence(p26, s26);
                return sequence26;
            case 28:
                MoveToAction p28 = Actions.moveTo(reaparicion.x - 200, reaparicion.y, 3f, Interpolation.sine);
                MoveToAction s28 = Actions.moveTo(reaparicion.x - 200, reaparicion.y - 300, 2.5f, Interpolation.sine);
                MoveToAction t28 = Actions.moveTo(reaparicion.x - 200, reaparicion.y, 3f, Interpolation.sine);
                MoveToAction c28 = Actions.moveTo(reaparicion.x, reaparicion.y, 1f, Interpolation.sineOut);
                SequenceAction sequence28 = Actions.sequence(p28, s28, t28, c28);
                return sequence28;
            case 29:
                MoveToAction p29 = Actions.moveTo(reaparicion.x, reaparicion.y - 50, 1.75f, Interpolation.sine);
                MoveToAction s29 = Actions.moveTo(reaparicion.x - 100, reaparicion.y - 50, 0.25f, Interpolation.sine);
                MoveToAction t29 = Actions.moveTo(reaparicion.x, reaparicion.y - 50, 0.75f, Interpolation.sine);
                MoveToAction c29 = Actions.moveTo(reaparicion.x, reaparicion.y, 1f, Interpolation.sineOut);
                SequenceAction sequence29 = Actions.sequence(p29, s29, t29, c29);
                return sequence29;
            case 30:
                MoveToAction p30 = Actions.moveTo(reaparicion.x, reaparicion.y - 80, 1.5f, Interpolation.sine);
                MoveToAction s30 = Actions.moveTo(reaparicion.x + 100, reaparicion.y - 80, 0.75f, Interpolation.sine);
                MoveToAction t30 = Actions.moveTo(reaparicion.x + 100, reaparicion.y, 1f, Interpolation.sine);
                MoveToAction c30 = Actions.moveTo(reaparicion.x, reaparicion.y, 0.25f, Interpolation.sineOut);
                SequenceAction sequence30 = Actions.sequence(p30, s30, t30, c30);
                return sequence30;
            case 33:
                MoveToAction p33 = Actions.moveTo(reaparicion.x - 250, reaparicion.y, 2.5f, Interpolation.sine);
                MoveToAction s33 = Actions.moveTo(reaparicion.x, reaparicion.y, 1.5f, Interpolation.sine);
                SequenceAction sequence33 = Actions.sequence(p33, s33);
                return sequence33;
            default:
                return null;
        }
    }

    public Rectangle getShape() {
        cuerpo.set((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        return cuerpo;
    }
}

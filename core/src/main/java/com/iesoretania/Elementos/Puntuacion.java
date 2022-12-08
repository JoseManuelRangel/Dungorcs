package com.iesoretania.Elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.iesoretania.Dungorcs;
import com.iesoretania.Pantallas.PantallaJuego;
import com.iesoretania.Personajes.Demonic;
import com.iesoretania.Personajes.Enemigo;
import com.iesoretania.Personajes.Moneda;

import java.util.Iterator;

public class Puntuacion extends Actor {
    public int enemigos_eliminados, monedas_recogidas;
    Demonic demonic;
    Enemigo enemigo;
    Moneda moneda;

    private Sound coinSound, bittingSound, deathSound;

    public Puntuacion(Demonic demonic) {
        this.demonic = demonic;
        enemigos_eliminados = 0;
        monedas_recogidas = 0;


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        coinSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/coin_sound.wav"));
        bittingSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/bite_sound.wav"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/death_sound.wav"));

        Iterator<Enemigo> itenemigo = PantallaJuego.enemigos.iterator();
        while(itenemigo.hasNext()) {
            enemigo = itenemigo.next();

            if(enemigo.isVisible() && Intersector.overlaps(demonic.getShape(), enemigo.getShape())) {
                if(demonic.estaAtacando == true) {
                    enemigo.addAction(Actions.removeActor());
                    bittingSound.play(0.15f);
                    enemigo.muerto = true;
                    enemigos_eliminados += 1;
                    itenemigo.remove();
                } else {
                    demonic.addAction(Actions.removeActor());
                    if(demonic.muerto == false) {
                        deathSound.play(0.10f);
                        demonic.setVisible(false);
                        demonic.muerto = true;
                    }

                }
            }
        }

        Iterator<Moneda> itmoneda = PantallaJuego.monedas.iterator();
        while(itmoneda.hasNext()) {
            moneda = itmoneda.next();

            if(moneda.isVisible() && Intersector.overlaps(demonic.getShape(), moneda.getShape())) {
                moneda.addAction(Actions.removeActor());
                coinSound.play(0.15f);
                moneda.cogida = true;
                monedas_recogidas += 1;
                itmoneda.remove();
            }
        }
    }
}

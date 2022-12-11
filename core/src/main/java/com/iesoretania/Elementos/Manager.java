package com.iesoretania.Elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.iesoretania.Dungorcs;
import com.iesoretania.Items.PocionesPrisma;
import com.iesoretania.Pantallas.PantallaFinal;
import com.iesoretania.Pantallas.PantallaGameOver;
import com.iesoretania.Pantallas.PantallaJuego;
import com.iesoretania.Items.Corazon;
import com.iesoretania.Personajes.Demonic;
import com.iesoretania.Personajes.Enemigo;
import com.iesoretania.Items.Moneda;

import java.util.Iterator;

public class Manager extends Actor {
    public int enemigosEliminados, monedasRecogidas, vidasRestantes, pocionesPrisma;
    private Sound coinSound, bittingSound, deathSound, heartSound, gameOverSound, potionSound, winSound;

    Dungorcs game;
    Demonic demonic;
    Enemigo enemigo;
    Moneda moneda;
    Corazon corazon;
    PocionesPrisma pocion;

    public Manager(Demonic demonic, Dungorcs game) {
        this.demonic = demonic;
        this.game = game;

        enemigosEliminados = 0;
        monedasRecogidas = 0;
        vidasRestantes = 3;
        pocionesPrisma = 0;

        coinSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/coin_sound.wav"));
        bittingSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/bite_sound.wav"));
        deathSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/death_sound.wav"));
        heartSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/hearth_sound.wav"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/gameover_sound.wav"));
        potionSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/potion_sound.wav"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/win_sound.wav"));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        Iterator<Enemigo> itenemigo = PantallaJuego.enemigos.iterator();
        while(itenemigo.hasNext()) {
            enemigo = itenemigo.next();

            if(enemigo.isVisible() && Intersector.overlaps(demonic.getShape(), enemigo.getShape())) {
                if(demonic.estaAtacando == true) {
                    enemigo.addAction(Actions.removeActor());
                    bittingSound.play(0.15f);
                    enemigo.muerto = true;
                    enemigosEliminados += 1;
                    itenemigo.remove();
                } else {
                    if(vidasRestantes > 0) {
                        demonic.setPosition(demonic.reaparicion.x, demonic.reaparicion.y);
                        vidasRestantes -= 1;
                        demonic.muerto = false;
                        demonic.setVisible(true);
                        deathSound.play();
                    }

                    if(vidasRestantes == 0) {
                        PantallaJuego.gameMusic.stop();
                        deathSound.stop();
                        game.setScreen(new PantallaGameOver(game));
                        gameOverSound.play();
                    }
                }
            }
        }

        Iterator<Moneda> itMoneda = PantallaJuego.monedas.iterator();
        while(itMoneda.hasNext()) {
            moneda = itMoneda.next();

            if(moneda.isVisible() && Intersector.overlaps(demonic.getShape(), moneda.getShape())) {
                moneda.addAction(Actions.removeActor());
                coinSound.play(0.15f);
                moneda.cogida = true;
                monedasRecogidas += 1;
                itMoneda.remove();
            }
        }


        Iterator<Corazon> itCorazon = PantallaJuego.corazones.iterator();
        while(itCorazon.hasNext()) {
            corazon = itCorazon.next();

            if(corazon.isVisible() && Intersector.overlaps(demonic.getShape(), corazon.getShape())) {
                corazon.addAction(Actions.removeActor());
                heartSound.play(0.50f);
                corazon.cogido = true;
                vidasRestantes += 1;
                itCorazon.remove();
            }
        }

        Iterator<PocionesPrisma> itPociones = PantallaJuego.pociones.iterator();
        while(itPociones.hasNext()) {
            pocion = itPociones.next();

            if(pocion.isVisible() && Intersector.overlaps(demonic.getShape(), pocion.getShape())) {
                pocion.addAction(Actions.removeActor());
                potionSound.play(0.45f);
                pocion.cogida = true;
                pocionesPrisma += 1;
                itPociones.remove();

                if(pocionesPrisma == 4) {
                    PantallaJuego.gameMusic.stop();
                    potionSound.stop();
                    winSound.play(0.50f);
                    game.setScreen(new PantallaFinal(game));
                }
            }
        }
    }
}

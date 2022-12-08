package com.iesoretania.Pantallas;

import com.badlogic.gdx.Screen;
import com.iesoretania.Dungorcs;

public abstract class PantallaBase implements Screen {
    private Dungorcs dungorcs;

    public PantallaBase(Dungorcs dungorcs) {
        this.dungorcs = dungorcs;
    }
}

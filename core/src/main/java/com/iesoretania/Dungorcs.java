package com.iesoretania;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.iesoretania.Pantallas.PantallaMenu;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Dungorcs extends Game {
	PantallaMenu menu;
	public static Skin skin;

	@Override
	public void create() {
		skin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));
		menu = new PantallaMenu(this);

		setScreen(menu);
	}
}
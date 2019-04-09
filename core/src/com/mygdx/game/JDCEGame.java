package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class JDCEGame extends Game {
    private static I18NBundle myBundle;
    SpriteBatch batch;
	protected static PlatformResolver m_platformResolver = null;
	boolean noSensor;

	@Override
	public void create () {
		Locale defaultLocale = Locale.getDefault();
		updateLanguage(new Locale("en", "UK"));
		noSensor = true;

        System.out.println(myBundle.getLocale());
        System.out.println(myBundle.get("play"));
		batch = new SpriteBatch();
		if(m_platformResolver.isAndroid())
			this.setScreen(new ConnectScreen(this));
		else {
			this.setScreen(new MainMenuScreen(this));
		}

	}

	@Override
	public void render () {
		super.render();

	}

	public SpriteBatch getBatch() {
		return batch;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public static PlatformResolver getPlatformResolver() {
		return m_platformResolver;
	}

	public static void setPlatformResolver(PlatformResolver platformResolver) {
		m_platformResolver = platformResolver;
	}

	public static void updateLanguage(Locale locale) {
        myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
    }

	public JDCEGame getGame() {
		return this;
	}

	public I18NBundle getBundle() {
	    return myBundle;
    }
}

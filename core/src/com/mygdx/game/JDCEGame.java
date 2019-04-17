package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class JDCEGame extends Game {
    private static I18NBundle myBundle;
    SpriteBatch batch;
	protected static PlatformResolver m_platformResolver = null;
	boolean noSensor;
	public static Preferences settings;
    public static boolean musicOn;
    public static boolean soundEffectsOn;

    private BitmapFont font48;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private GlyphLayout layout48;
    private Texture background;
    private Skin uiSkin;
    private TextureAtlas textureAtlas;

	@Override
	public void create () {
		Locale defaultLocale = Locale.getDefault();
		updateLanguage(new Locale("en", "UK"));
		noSensor = true;
		settings = Gdx.app.getPreferences("JDCE_settings");
		musicOn = settings.getBoolean("MusicOn",true);
		soundEffectsOn = settings.getBoolean("SoundEffectsOn", true);
        System.out.println(myBundle.getLocale());
        System.out.println(myBundle.get("play"));
		batch = new SpriteBatch();

        //musiikki
        /*NewScreen.music = Gdx.audio.newMusic(Gdx.files.internal("sound/JDCE_menu_music_v4.mp3"));
        NewScreen.music.setLooping(true);
        if(JDCEGame.musicOn){
            NewScreen.music.play();
        }
        */

        generator = new FreeTypeFontGenerator(Gdx.files.internal("ariblk.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        setFontParameter();

        font48 = generator.generateFont(parameter);
        layout48 = new GlyphLayout();

        uiSkin = new Skin();
        uiSkin.add("myFont", getFont48(), BitmapFont.class);

        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("ui_skin/clean-crispy-ui.atlas"));

        uiSkin.addRegions(textureAtlas) ;
        uiSkin.load(Gdx.files.internal("ui_skin/clean-crispy-ui.json"));

        this.setScreen(new MainMenuScreen(this));
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

    public void setFontParameter() {
        parameter.size = (int) (Gdx.graphics.getHeight() / 24);
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
    }

    public FreeTypeFontGenerator.FreeTypeFontParameter getFontParameter() {
        return parameter;
    }

    public BitmapFont getFont48() {
        return font48;
    }

    public void setFont48(BitmapFont font48) {
        this.font48 = font48;
    }

    public GlyphLayout getLayout48() {
        return layout48;
    }

    public void setLayout48(GlyphLayout layout48) {
        this.layout48 = layout48;
    }

    public Texture getBackground() {
        return background;
    }

    public void setBackground(Texture background) {
        this.background = background;
    }

    public void setUiSkin(Skin s) {
        uiSkin = s;
    }

    public Skin getUiSkin() {
        return uiSkin;
    }





}



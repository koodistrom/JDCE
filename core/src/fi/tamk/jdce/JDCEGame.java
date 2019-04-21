package fi.tamk.jdce;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

import static com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.DEFAULT_CHARS;

public class JDCEGame extends Game {
    private static I18NBundle myBundle;
    SpriteBatch batch;
	protected static PlatformResolver m_platformResolver = null;
	boolean noSensor;
	public static Preferences settings;
    public static boolean musicOn;
    public static boolean soundEffectsOn;
    public static boolean isEnglish;

    private BitmapFont font48;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private GlyphLayout layout48;
    private Texture background;
    private Skin uiSkin;
    private TextureAtlas textureAtlas;
    boolean skipConnect;
    LevelCreator2 levelCreator;
    TextureRegionDrawable world1;
    TextureRegionDrawable world2;
    TextureRegionDrawable world3;
    TextureRegionDrawable world4;
    TextureRegionDrawable backButtonTextRegDrawable;
    TextureRegionDrawable finTextRegDrawable;
    TextureRegionDrawable finOffTextRegDrawable;
    TextureRegionDrawable engTextRegDrawable;
    TextureRegionDrawable engOffTextRegDrawable;
    TextureRegionDrawable muteMusicOn;
    TextureRegionDrawable muteMusicOff;
    TextureRegionDrawable muteSoundFxOff;
    TextureRegionDrawable muteSoundFxOn;
    public static Locale fi;
    public static Locale en;
    private Sound buttonSound;

	@Override
	public void create () {
		Locale defaultLocale = Locale.getDefault();
		fi = new Locale("fi", "FI");
		en = new Locale("en", "UK");

		noSensor = true;
		settings = Gdx.app.getPreferences("JDCE_settings");
		isEnglish = settings.getBoolean("Language", true);
		musicOn = settings.getBoolean("MusicOn",true);
		soundEffectsOn = settings.getBoolean("SoundEffectsOn", true);

		buttonSound = Gdx.audio.newSound(Gdx.files.internal("sound/JDCE_button_sfx_1.mp3"));

		if(isEnglish) {
		    updateLanguage(en);
        } else {
		    updateLanguage(fi);
        }

        System.out.println(myBundle.getLocale());
        System.out.println(myBundle.get("play"));
		batch = new SpriteBatch();

        //musiikki
        NewScreen.music = Gdx.audio.newMusic(Gdx.files.internal("sound/JDCE_menu_music_v4.mp3"));
        NewScreen.music.setLooping(true);
        if(JDCEGame.musicOn){
            NewScreen.music.play();
        }

        skipConnect=false;

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Bold.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        setFontParameter();

        font48 = generator.generateFont(parameter);
        layout48 = new GlyphLayout();

        uiSkin = new Skin();
        uiSkin.add("myFont", getFont48(), BitmapFont.class);

        textureAtlas = new TextureAtlas(Gdx.files.internal("ui_skin/clean-crispy-ui.atlas"));
        background = new Texture("tausta_valikko.png");

        uiSkin.addRegions(textureAtlas);
        uiSkin.load(Gdx.files.internal("ui_skin/clean-crispy-ui.json"));

        backButtonTextRegDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("takaisin.png")));


        finTextRegDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("suomi2.png")));
        finOffTextRegDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("suomitumma2.png")));

        engTextRegDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("enkku2.png")));
        engOffTextRegDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("enkkutumma2.png")));

        muteMusicOff = new TextureRegionDrawable(new Texture(Gdx.files.internal("musiikkipaalla.png")));
        muteMusicOn = new TextureRegionDrawable(new Texture(Gdx.files.internal("musiikkipois.png")));

        muteSoundFxOff = new TextureRegionDrawable(new Texture(Gdx.files.internal("äänetpäällä.png")));
        muteSoundFxOn = new TextureRegionDrawable(new Texture(Gdx.files.internal("äänetpois.png")));

        world1 = new TextureRegionDrawable(new Texture(Gdx.files.internal("kuvake1.png")));
        world2 = new TextureRegionDrawable(new Texture(Gdx.files.internal("kuvake2.png")));
        world3 = new TextureRegionDrawable(new Texture(Gdx.files.internal("kuvake3.png")));
        world4 = new TextureRegionDrawable(new Texture(Gdx.files.internal("kuvake4.png")));


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
        parameter.size = Gdx.graphics.getHeight() / 24;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
        parameter.characters = DEFAULT_CHARS;
    }

    public FreeTypeFontGenerator.FreeTypeFontParameter getFontParameter() {
        return parameter;
    }

    public BitmapFont getFont48() {
        return font48;
    }

    public GlyphLayout getLayout48() {
        return layout48;
    }

    public Texture getBackground() {
        return background;
    }

    public Skin getUiSkin() {
        return uiSkin;
    }

    public Sound getButtonSound() {
        return buttonSound;
    }
}



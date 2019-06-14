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
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.DEFAULT_CHARS;

/**
 * JDCEGame is the main game class.
 *
 * It extends the Game-class and creates objects
 * that are used by several classes:
 *  - myBundle for the language controls.
 *  - batch for drawing the wanted assets.
 *  - settings for controlling the music and sound effects.
 *  - Most of the textures for the game
 *  - font48 the BitmapFont used by the game
 *  - uiSkin the skin used by the UI
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class JDCEGame extends Game {
    /**
     * I18NBundle used for controlling the language options.
     */
    private static I18NBundle myBundle;

    /**
     * Used to draw the game's assets in render().
     */
    SpriteBatch batch;

    /**
     * Gives information about the user's device.
     */
    protected static PlatformResolver m_platformResolver = null;

    /**
     * Checks if the cycling motion tracking censor
     * is connected.
     */
	boolean noSensor;

    /**
     * Controls the settings for the game (music, sound effects).
     */
	public static Preferences settings;

    /**
     * Checks if the music is on in the settings.
     */
    public static boolean musicOn;

    /**
     * Checks if the sound effects are on in the settings.
     */
    public static boolean soundEffectsOn;

    /**
     * Checks if the game's language is English.
     */
    public static boolean isEnglish;

    /**
     * BitmapFont used to create texts and labels in the UI.
     */
    private BitmapFont font48;

    /**
     * Used to generate the BitmapFont font48.
     */
    private FreeTypeFontGenerator generator;

    /**
     * Used to create the font's parameters for the BitmapFont font48.
     */
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    /**
     * Used to get the BitmapFont font48's layout.
     */
    private GlyphLayout layout48;

    /**
     * Used for the screen background for UI screens.
     */
    private Texture background;

    /**
     * Used for the visual layout of the UI elements: TextButton, Button, Label.
     */
    private Skin uiSkin;

    /**
     * The uiSkin's visuals.
     */
    private TextureAtlas textureAtlas;

    /**
     * Checks if the user chooses to skip the Bluetooth connection of
     * the cycling motion tracking censor.
     */
    boolean skipConnect;

    /**
     * Visual asset for the backButton.
     */
    TextureRegionDrawable backButtonTextRegDrawable;

    /**
     * Visual asset for the languageFI (When chosen language is Finnish).
     */
    TextureRegionDrawable finTextRegDrawable;

    /**
     * Visual asset for the languageFI (When chosen language is not Finnish).
     */
    TextureRegionDrawable finOffTextRegDrawable;

    /**
     * Visual asset for the languageEN (When chosen language is English).
     */
    TextureRegionDrawable engTextRegDrawable;

    /**
     * Visual asset for the languageEN (When chosen language is not English).
     */
    TextureRegionDrawable engOffTextRegDrawable;

    /**
     * Visual asset for the muteMusic (When the music is on).
     */
    TextureRegionDrawable muteMusicOn;

    /**
     * Visual asset for the muteMusic (When the music is off).
     */
    TextureRegionDrawable muteMusicOff;

    /**
     * Visual asset for the muteSoundFx (When the sound effects are off).
     */
    TextureRegionDrawable muteSoundFxOff;

    /**
     * Visual asset for the muteSoundFx (When the sound effects are on).
     */
    TextureRegionDrawable muteSoundFxOn;

    /**
     * Locale used for Finnish.
     */
    public static Locale fi;

    /**
     * Locale used for English.
     */
    public static Locale en;

    /**
     * Soundeffect used for the Buttons in the UI.
     */
    private Sound buttonSound;




    /**
     * The default constructor for JDCEGame.
     */
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

        generator.dispose();

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

        this.setScreen(new MainMenuScreen(this));



	}

    /**
     * Gets batch.
     * @return batch
     */
	public SpriteBatch getBatch() {
		return batch;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

    /**
     * Gets m_platformResolver.
     * @return m_platformResolver
     */
	public static PlatformResolver getPlatformResolver() {
		return m_platformResolver;
	}

    /**
     * Sets m_platformResolver
     * @param platformResolver
     */
	public static void setPlatformResolver(PlatformResolver platformResolver) {
		m_platformResolver = platformResolver;
	}

    /**
     * Updates the game's language with the given locale.
     * @param locale
     */
	public static void updateLanguage(Locale locale) {
        myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"), locale);
    }

    /**
     * Gets JDCEGame.
     * @return this
     */
	public JDCEGame getGame() {
		return this;
	}

    /**
     * Gets myBundle.
     * @return myBundle
     */
	public I18NBundle getBundle() {
	    return myBundle;
    }

    /**
     * Sets up the parameter that is used for the BitmapFont font48.
     */
    public void setFontParameter() {
        parameter.size = Gdx.graphics.getHeight() / 24;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
        parameter.characters = DEFAULT_CHARS;
    }

    /**
     * Gets parameter.
     * @return parameter
     */
    public FreeTypeFontGenerator.FreeTypeFontParameter getFontParameter() {
        return parameter;
    }

    /**
     * Gets font48.
     * @return font48
     */
    public BitmapFont getFont48() {
        return font48;
    }

    /**
     * Gets layout48.
     * @return layout48
     */
    public GlyphLayout getLayout48() {
        return layout48;
    }

    /**
     * Gets background.
     * @return background
     */
    public Texture getBackground() {
        return background;
    }

    /**
     * Gets uiSkin.
     * @return uiSkin
     */
    public Skin getUiSkin() {
        return uiSkin;
    }

    /**
     * Gets buttonSound.
     * @return buttonSound
     */
    public Sound getButtonSound() {
        return buttonSound;
    }



}



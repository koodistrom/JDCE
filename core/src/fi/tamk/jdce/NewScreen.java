package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * NewScreen is the superclass for the game's screens.
 *
 * It implements the Screen-class and creates objects
 * that are used by several screens, like for example:
 * gameStage, backButton, meterViewport etc.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class NewScreen implements Screen {
    /**
     * Holds the JDCEGame class, that is received as the parameter in NewScreen's constructor.
     */
    private JDCEGame game;

    /**
     * Holds the SpriteBatch for the game.
     */
    private SpriteBatch batch;

    /**
     * Changes pixels to meters.
     */
    final float PIXELS_TO_METERS = 100f;

    /**
     * Screen width turned to meters and multiplied with 1.2.
     */
    private float screenWidth = (Gdx.graphics.getWidth()/PIXELS_TO_METERS)*1.2f;

    /**
     * Screen height turned to meters and multiplied with 1.2.
     */
    private float screenHeight = (Gdx.graphics.getHeight()/PIXELS_TO_METERS)*1.2f;

    /**
     * Holds the Stage for the screen.
     */
    private Stage gameStage;

    /**
     * The width of the gameStage divided by 10.
     */
    private float stageWidthTenth;

    /**
     * The height of the gameStage divided by 10.
     */
    private float stageHeightTenth;

    /**
     * The height for most of the TextButtons.
     */
    private float textButtonHeight;

    /**
     * The width for most of the TextButtons.
     */
    private float textButtonWidth;

    /**
     * The height for most of the Buttons that use an image.
     */
    private float imageButtonHeight;

    /**
     * The width for most of the Buttons that use an image.
     */
    private float imageButtonWidth;

    /**
     * The Button that takes you to the previous screen in the menus.
     */
    private Button backButton;

    /**
     * The Button that allows you to mute or un-mute the game's sound effects.
     */
    private Button muteSoundFx;

    /**
     * The Button that allows you to mute or un-mute the game's music.
     */
    private Button muteMusic;

    /**
     * The location of the backButton on the x-axis/width-axis.
     */
    private float backButtonX;

    /**
     * The location of the backButton on the y-axis/height-axis.
     */
    private float backButtonY;

    /**
     * The location of the muteMusic on the x-axis/width-axis.
     */
    private float muteMusicX;

    /**
     * The location of the muteMusic on the y-axis/height-axis.
     */
    private float muteMusicY;

    /**
     * The location of the muteSoundFx on the x-axis/width-axis.
     */
    private float muteSoundEffectsX;

    /**
     * The location of the muteSoundFx on the y-axis/height-axis.
     */
    private float MuteSoundEffectsY;

    /**
     * The game's Viewport in pixels.
     */
    private ScreenViewport pixelViewport;

    /**
     * The game's Viewport in meters.
     */
    private FitViewport meterViewport;

    /**
     * Contains the music of the game.
     */
    protected static Music music;

    /**
     * The default constructor for NewScreen.
     *
     * @param g the JDCEGame-class. It allows the NewScreen access to the: batch, myBundle,
     *          the game's settings, textures, uiSkin and font48.
     */
    public NewScreen(JDCEGame g) {
        game = g;
        batch = game.getBatch();
        pixelViewport = new ScreenViewport();
        meterViewport = new FitViewport(screenWidth, screenHeight);
        gameStage = new Stage(pixelViewport, batch);

        setupButtonBounds();

        backButton = new Button(getGame().backButtonTextRegDrawable);
        muteMusic = new Button(getGame().muteMusicOff, getGame().muteMusicOn, getGame().muteMusicOn);
        muteMusic.setChecked(!JDCEGame.musicOn);
        muteSoundFx = new Button(getGame().muteSoundFxOff, getGame().muteSoundFxOn, getGame().muteSoundFxOn);
        muteSoundFx.setChecked(!JDCEGame.soundEffectsOn);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(getGame().m_platformResolver.isAndroid()&&!getGame().m_platformResolver.isConnected()&&getGame().getScreen().getClass()!=ConnectScreen.class && !getGame().skipConnect){
            getGame().setScreen(new ConnectScreen(getGame()));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getPixelViewport().apply();
        getSpriteBatch().setProjectionMatrix(getPixelViewport().getCamera().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(getGame().getBackground(), 0, 0, getPixelViewport().getWorldWidth(), getPixelViewport().getWorldHeight());
        getSpriteBatch().end();

        getGameStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        setScreenHeight(height / PIXELS_TO_METERS * 1.2f);
        setScreenWidth(width / PIXELS_TO_METERS * 1.2f);

        pixelViewport.update(width, height, true);
        meterViewport.update(width, height, true);

        setupButtonBounds();
        setupButtons();
        updateTables();

        getGame().setFontParameter();

    }

    /**
     * Sets up or updates the buttons' sizes and position's coordinates.
     */
    public void setupButtonBounds() {
        updateTenths();

        textButtonHeight = gameStage.getHeight() / 6;
        textButtonWidth = gameStage.getWidth() / 4;
        imageButtonHeight = textButtonHeight;
        imageButtonWidth = imageButtonHeight;

        backButtonX = getStageWidthTenth() - (getImageButtonWidth() / 2);
        backButtonY = getStageHeightTenth() * 1 - (getImageButtonHeight() / 2);

        muteMusicY = stageHeightTenth * 9 - (getImageButtonHeight() / 2);
        MuteSoundEffectsY = stageHeightTenth * 6.333f - (getImageButtonHeight() / 2);

        muteMusicX = stageWidthTenth * 9 - (getImageButtonWidth() / 2);
        muteSoundEffectsX = muteMusicX;
    }

    /**
     * Sets up or updates the buttons with their size and position.
     */
    public void setupButtons() {
        getBackButton().setWidth(getImageButtonWidth());
        getBackButton().setHeight(getImageButtonHeight());
        getBackButton().setPosition(getBackButtonX(), getBackButtonY());

        getMuteMusicButton().setWidth(getImageButtonWidth());
        getMuteMusicButton().setHeight(getImageButtonHeight());
        getMuteMusicButton().setPosition(getMuteMusicX(), getMuteMusicY());

        getMuteSoundFxButton().setWidth(getImageButtonWidth());
        getMuteSoundFxButton().setHeight(getImageButtonHeight());
        getMuteSoundFxButton().setPosition(getMuteSoundEffectsX(), getMuteSoundEffectsY());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gameStage.dispose();
    }

    /**
     * Sets up the Buttons' clickListeners.
     */
    public void clickListeners() {
        getMuteMusicButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JDCEGame.musicOn = !muteMusic.isChecked();
                JDCEGame.settings.putBoolean("MusicOn", !muteMusic.isChecked());
                JDCEGame.settings.flush();
                if(JDCEGame.musicOn){
                    music.play();
                }else{
                    music.pause();
                }
                playButtonSound();
            }
        });

        getMuteSoundFxButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JDCEGame.soundEffectsOn = !muteSoundFx.isChecked();
                JDCEGame.settings.putBoolean("SoundEffectsOn", !muteSoundFx.isChecked());
                JDCEGame.settings.flush();
                playButtonSound();
            }
        });
    }

    /**
     * Plays the Buttons' sound effect.
     */
    public void playButtonSound(){
        if(JDCEGame.soundEffectsOn){
            getGame().getButtonSound().play(1f);
        }
    }

    /**
     * Gets the location of the muteMusic on the x-axis/width-axis.
     * @return muteMusicX
     */
    public float getMuteMusicX() {
        return muteMusicX;
    }

    /**
     * Gets the location of the muteMusic on the y-axis/height-axis.
     * @return muteMusicX
     */
    public float getMuteMusicY() {
        return muteMusicY;
    }

    /**
     * Gets the location of the muteSoundFx on the x-axis/width-axis.
     * @return muteSoundEffectsX
     */
    public float getMuteSoundEffectsX() {
        return muteSoundEffectsX;
    }

    /**
     * Gets the location of the muteSoundFx on the y-axis/height-axis.
     * @return muteSoundEffectsY
     */
    public float getMuteSoundEffectsY() {
        return MuteSoundEffectsY;
    }

    /**
     * Gets the stageWidthTenth, that is the gameStage width divided by 10.
     * @return stageWidthTenth
     */
    public float getStageWidthTenth() {
        return stageWidthTenth;
    }

    /**
     * Gets the stageHeightTenth, that is the gameStage height divided by 10.
     * @return stageHeightTenth
     */
    public float getStageHeightTenth() {
        return stageHeightTenth;
    }

    /**
     * Updates the stageHeightTenth and stageWidthTenth.
     */
    public void updateTenths() {
        stageHeightTenth = gameStage.getHeight() / 10;
        stageWidthTenth = gameStage.getWidth() / 10;
    }

    /**
     * Gets the backButton.
     * @return backButton
     */
    public Button getBackButton() {
        return backButton;
    }

    /**
     * Gets the location of the backButton on the x-axis/width-axis.
     * @return backButtonX
     */
    public float getBackButtonX() {
        return backButtonX;
    }

    /**
     * Gets the location of the backButton on the y-axis/height-axis.
     * @return backButtonY
     */
    public float getBackButtonY() {
        return backButtonY;
    }

    /**
     * Sets the strings with the current JDCEGame.myBundle.
     */
    public void updateTexts() {

    }

    /**
     * Updates the sizes and positions of the Tables.
     */
    public void updateTables() {

    }

    /**
     * Gets the gameStage width.
     * @return getGameStage().getWidth()
     */
    public float getStageWidth() {
        return getGameStage().getWidth();
    }

    /**
     * Gets the gameStage height.
     * @return getGameStage().getHeight()
     */
    public float getStageHeight() {
        return getGameStage().getHeight();
    }

    /**
     * Sets the JDCEGame for game.
     * @param g JDCEGame
     */
    public void setGame(JDCEGame g) {
        game = g;
    }

    /**
     * Gets the game.
     * @return game
     */
    public JDCEGame getGame() {
        return game;
    }

    /**
     * Gets the batch.
     * @return batch
     */
    public SpriteBatch getSpriteBatch() {
        return batch;
    }

    /**
     * Gets the meterViewPort.
     * @return meterViewport
     */
    public FitViewport getMeterViewport() {
        return meterViewport;
    }

    /**
     * Gets the pixelViewport.
     * @return pixelViewport
     */
    public ScreenViewport getPixelViewport() {
        return pixelViewport;
    }

    /**
     * Sets the screenWidth.
     * @param width the width that will be set
     */
    public void setScreenWidth(float width) {
        screenWidth = width;
    }

    /**
     * Gets the screenWidth
     * @return screenWidth
     */
    public float getScreenWidth() {
        return screenWidth;
    }

    /**
     * Sets the screenHeight.
     * @param height the height that will be set
     */
    public void setScreenHeight(float height) {
        screenHeight = height;
    }

    /**
     * Gets the screenHeight.
     * @return screenHeight
     */
    public float getScreenHeight() {
        return screenHeight;
    }

    /**
     * Gets the gameStage.
     * @return gameStage
     */
    public Stage getGameStage() {
        return gameStage;
    }

    /**
     * Gets the textButtonHeight.
     * @return textButtonHeight
     */
    public float getTextButtonHeight() {
        return textButtonHeight;
    }

    /**
     * Gets the textButtonWidth.
     * @return textButtonWidth
     */
    public float getTextButtonWidth() {
        return textButtonWidth;
    }

    /**
     * Gets the muteMusic.
     * @return muteMusic
     */
    public Button getMuteMusicButton() {
        return muteMusic;
    }

    /**
     * Gets the muteSoundFx.
     * @return muteSoundFx
     */
    public Button getMuteSoundFxButton() {
        return muteSoundFx;
    }

    /**
     * Gets the imageButtonHeight.
     * @return imageButtonHeight
     */
    public float getImageButtonHeight() {
        return imageButtonHeight;
    }

    /**
     * Gets the imageButtonWidth.
     * @return imageButtonWidth
     */
    public float getImageButtonWidth() {
        return imageButtonWidth;
    }
}


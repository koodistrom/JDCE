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
     * Holds the JDCEGame class, that is receieved as the parameter in NewScreen's constructor
     */
    private JDCEGame game;

    /**
     * Holds the spritebatch for the game
     */
    private SpriteBatch batch;

    /**
     * Changes pixels to meters
     */
    final float PIXELS_TO_METERS = 100f;


    private float screenWidth = (Gdx.graphics.getWidth()/PIXELS_TO_METERS)*1.2f;
    private float screenHeight = (Gdx.graphics.getHeight()/PIXELS_TO_METERS)*1.2f;

    private Stage gameStage;

    private float stageWidthTenth;
    private float stageHeightTenth;

    private float textButtonHeight;
    private float textButtonWidth;

    private float imageButtonHeight;
    private float imageButtonWidth;

    private Button backButton;
    private Button muteSoundFx;
    private Button muteMusic;

    private float backButtonX;
    private float backButtonY;
    private float muteMusicX;
    private float muteMusicY;
    private float muteSoundEffectsX;
    private float MuteSoundEffectsY;

    private ScreenViewport pixelViewport;
    private FitViewport meterViewport;

    protected static Music music;


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

    public void playButtonSound(){
        if(JDCEGame.soundEffectsOn){
            getGame().getButtonSound().play(1f);
        }
    }

    public float getMuteMusicX() {
        return muteMusicX;
    }

    public float getMuteMusicY() {
        return muteMusicY;
    }

    public float getMuteSoundEffectsX() {
        return muteSoundEffectsX;
    }

    public float getMuteSoundEffectsY() {
        return MuteSoundEffectsY;
    }

    public float getStageWidthTenth() {
        return stageWidthTenth;
    }

    public float getStageHeightTenth() {
        return stageHeightTenth;
    }

    public void updateTenths() {
        stageHeightTenth = gameStage.getHeight() / 10;
        stageWidthTenth = gameStage.getWidth() / 10;
    }

    public Button getBackButton() {
        return backButton;
    }

    public float getBackButtonX() {
        return backButtonX;
    }

    public float getBackButtonY() {
        return backButtonY;
    }

    public void updateTexts() {

    }

    public void updateTables() {

    }

    public float getStageWidth() {
        return getGameStage().getWidth();
    }

    public float getStageHeight() {
        return getGameStage().getHeight();
    }

    public void setGame(JDCEGame g) {
        game = g;
    }

    public JDCEGame getGame() {
        return game;
    }

    public SpriteBatch getSpriteBatch() {
        return batch;
    }

    public FitViewport getMeterViewport() {
        return meterViewport;
    }

    public ScreenViewport getPixelViewport() {
        return pixelViewport;
    }

    public void setScreenWidth(float width) {
        screenWidth = width;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public void setScreenHeight(float height) {
        screenHeight = height;
    }

    public float getScreenHeight() {
        return screenHeight;
    }

    public Stage getGameStage() {
        return gameStage;
    }

    public float getTextButtonHeight() {
        return textButtonHeight;
    }

    public float getTextButtonWidth() {
        return textButtonWidth;
    }

    public Button getMuteMusicButton() {
        return muteMusic;
    }

    public Button getMuteSoundFxButton() {
        return muteSoundFx;
    }

    public float getImageButtonHeight() {
        return imageButtonHeight;
    }

    public float getImageButtonWidth() {
        return imageButtonWidth;
    }
}


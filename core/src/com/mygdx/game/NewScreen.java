package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class NewScreen implements Screen {
    private JDCEGame game;
    private SpriteBatch batch;
    //private OrthographicCamera camera;
    final float PIXELS_TO_METERS = 100f;
    private float screenWidth = (Gdx.graphics.getWidth()/PIXELS_TO_METERS)*1.2f;
    private float screenHeight = (Gdx.graphics.getHeight()/PIXELS_TO_METERS)*1.2f;

    private Stage gameStage;

    private float stageWidthTenth;
    private float stageHeightTenth;

    private Skin uiSkin;

    private float textButtonHeight;
    private float textButtonWidth;

    private float imageButtonHeight;
    private float imageButtonWidth;

    private Button backButton;
    private Button muteSoundFx;
    private Button muteMusic;
    private Button languageFI;
    private Button languageEN;

    private float backButtonX;
    private float backButtonY;
    private float muteMusicX;
    private float muteMusicY;
    private float muteSoundEffectsX;
    private float MuteSoundEffectsY;

    private ScreenViewport gameViewport;
    private FitViewport meterViewport;

    private BitmapFont font48;
    private FreeTypeFontGenerator generator;
    private GlyphLayout layout48;
    private Texture background;


    public NewScreen(JDCEGame g) {
        game = g;
        batch = game.getBatch();
        /*camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);*/
        gameViewport = new ScreenViewport();
        meterViewport = new FitViewport(screenWidth, screenHeight);
        gameStage = new Stage(gameViewport, batch);
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

        setupButtonBounds();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
        font48 = generator.generateFont(parameter);
        layout48 = new GlyphLayout();

        TextureRegionDrawable backButtonTextRegDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("back_button_ph.png")));
        backButton = new Button(backButtonTextRegDrawable);

        TextureRegionDrawable finTextRegDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("fin.png")));
        languageFI = new Button(finTextRegDrawable);


        TextureRegionDrawable engTextRegDrawable = new TextureRegionDrawable(new Texture(Gdx.files.internal("en.png")));
        languageEN = new Button(engTextRegDrawable);


        TextureRegionDrawable muteMusicOff = new TextureRegionDrawable(new Texture(Gdx.files.internal("mute_off.png")));
        TextureRegionDrawable muteMusicOn = new TextureRegionDrawable(new Texture(Gdx.files.internal("mute_on.png")));
        muteMusic = new Button(muteMusicOff, muteMusicOn, muteMusicOn);

        TextureRegionDrawable muteSoundFxOff = new TextureRegionDrawable(new Texture(Gdx.files.internal("soundfx_on.png")));
        TextureRegionDrawable muteSoundFxOn = new TextureRegionDrawable(new Texture(Gdx.files.internal("soundfx_off.png")));
        muteSoundFx = new Button(muteSoundFxOff, muteSoundFxOn, muteSoundFxOn);

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

    public void setSpriteBatch(SpriteBatch sb) {
        batch = sb;
    }

    public SpriteBatch getSpriteBatch() {
        return batch;
    }

    public FitViewport getMeterViewport() {
        return meterViewport;
    }

    public void setMeterViewport(FitViewport meterViewport) {
        this.meterViewport = meterViewport;
    }

    public ScreenViewport getGameViewport() {
        return gameViewport;
    }

    public void setGameViewport(ScreenViewport gameViewport) {
        this.gameViewport = gameViewport;
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

    public void setGameStage(Stage s) {
        gameStage = s;
    }

    public Stage getGameStage() {
        return gameStage;
    }

    public void setUiSkin(Skin s) {
        uiSkin = s;
    }

    public Skin getUiSkin() {
        return uiSkin;
    }

    public void setTextButtonHeight(float height) {
        textButtonHeight = height;
    }

    public float getTextButtonHeight() {
        return textButtonHeight;
    }

    public void setTextButtonWidth(float width) {
        textButtonWidth = width;
    }

    public float getTextButtonWidth() {
        return textButtonWidth;
    }

    public Button getButtonFI() {
        return languageFI;
    }

    public Button getButtonEN() {
        return languageEN;
    }

    public Button getMuteMusicButton() {
        return muteMusic;
    }

    public Button getMuteSoundFxButton() {
        return muteSoundFx;
    }

    public void setMuteSoundFxButton(Button muteSoundFx) {
        this.muteSoundFx = muteSoundFx;
    }

    public void setImageButtonHeight(float height) {
        imageButtonHeight = height;
    }

    public float getImageButtonHeight() {
        return imageButtonHeight;
    }

    public void setImageButtonWidth(float width) {
        imageButtonWidth = width;
    }

    public float getImageButtonWidth() {
        return imageButtonWidth;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
        meterViewport.update(width, height, true);

        setupButtonBounds();
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
        uiSkin.dispose();
        if(background!= null){
            background.dispose();
        }

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

    public float getMuteMusicX() {
        return muteMusicX;
    }

    public void setMuteMusicX(float muteMusicX) {
        this.muteMusicX = muteMusicX;
    }

    public float getMuteMusicY() {
        return muteMusicY;
    }

    public void setMuteMusicY(float muteMusicY) {
        this.muteMusicY = muteMusicY;
    }

    public float getMuteSoundEffectsX() {
        return muteSoundEffectsX;
    }

    public void setMuteSoundEffectsX(float muteSoundEffectsX) {
        this.muteSoundEffectsX = muteSoundEffectsX;
    }

    public float getMuteSoundEffectsY() {
        return MuteSoundEffectsY;
    }

    public void setMuteSoundEffectsY(float muteSoundEffectsY) {
        MuteSoundEffectsY = muteSoundEffectsY;
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

    public void setBackButtonX(float backButtonX) {
        this.backButtonX = backButtonX;
    }

    public float getBackButtonY() {
        return backButtonY;
    }

    public void setBackButtonY(float backButtonY) {
        this.backButtonY = backButtonY;
    }
}


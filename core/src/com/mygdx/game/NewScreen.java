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
    private Skin uiSkin;
    private float textButtonHeight = 150;
    private float textButtonWidth = 400;
    private float imageButtonHeight = 50;
    private float imageButtonWidth = 50;
    private Button mute;
    private Button languageFI;
    private Button languageEN;
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

        generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
        font48 = generator.generateFont(parameter);
        layout48 = new GlyphLayout();

        Texture fin = new Texture(Gdx.files.internal("fin.png"));
        TextureRegion finTextureRegion = new TextureRegion(fin);
        TextureRegionDrawable finTextRegDrawable = new TextureRegionDrawable(finTextureRegion);
        languageFI = new Button(finTextRegDrawable);

        Texture eng = new Texture(Gdx.files.internal("en.png"));
        TextureRegion engTextureRegion = new TextureRegion(eng);
        TextureRegionDrawable engTextRegDrawable = new TextureRegionDrawable(engTextureRegion);
        languageEN = new Button(engTextRegDrawable);

        Texture muteOff = new Texture(Gdx.files.internal("mute_off.png"));
        TextureRegion muteOffTextureRegion = new TextureRegion(muteOff);
        TextureRegionDrawable muteOffTextRegDrawable = new TextureRegionDrawable(muteOffTextureRegion);

        Texture muteOn = new Texture(Gdx.files.internal("mute_on.png"));
        TextureRegion muteOnTextureRegion = new TextureRegion(muteOn);
        TextureRegionDrawable muteOnTextRegDrawable = new TextureRegionDrawable(muteOnTextureRegion);

        mute = new Button(muteOffTextRegDrawable, muteOffTextRegDrawable, muteOnTextRegDrawable);

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

    public Button getMuteButton() {
        return mute;
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
}


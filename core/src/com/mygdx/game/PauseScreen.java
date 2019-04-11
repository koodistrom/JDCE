package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Locale;

public class PauseScreen extends NewScreen {
    private TextButton continueButton;
    private TextButton retryButton;
    private TextButton mainMenuButton;

    private float textButtonX;
    private float continueButtonY;
    private float retryButtonY;
    private float mainMenuButtonY;

    private String continueButtonText;
    private String retryButtonText;
    private String mainMenuButtonText;
    private String pausedText;

    private int levelNumber;
    private int worldNumber;

    public PauseScreen(JDCEGame g, int levelNumber, int worldNumber) {
        super(g);

        this.levelNumber = levelNumber;
        this.worldNumber = worldNumber;

        setupButtonBounds();

        setBackground(new Texture(Gdx.files.internal("bluebackground.png")));

        continueButton = new TextButton(continueButtonText, getUiSkin());
        retryButton = new TextButton(retryButtonText, getUiSkin());
        mainMenuButton = new TextButton(mainMenuButtonText, getUiSkin());

        updateTexts();
        setupButtons();

        getGameStage().addActor(continueButton);
        getGameStage().addActor(retryButton);
        getGameStage().addActor(mainMenuButton);
        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());

        Gdx.input.setInputProcessor(getGameStage());

        clickListeners();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        /*Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);



        getSpriteBatch().begin();
        getSpriteBatch().draw(getBackground(), 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        getGameStage().draw();

        getPixelViewport().apply();
        getSpriteBatch().setProjectionMatrix(getPixelViewport().getCamera().combined);*/

        getLayout48().setText(getFont48(), pausedText);

        getSpriteBatch().begin();
        getFont48().draw(getSpriteBatch(), pausedText, getStageWidthTenth() * 5 - getLayout48().width / 2,
                getStageHeightTenth() * 9 - getLayout48().height / 2);
        getSpriteBatch().end();
    }

/*    @Override
    public void resize(int width, int height) {
        getPixelViewport().update(width, height, true);
        getMeterViewport().update(width, height, true);

        setupButtonBounds();
        setupButtons();
    }*/

    @Override
    public void setupButtonBounds() {
        super.setupButtonBounds();
        /*updateTenths();

        setTextButtonHeight(getGameStage().getHeight() / 6);
        setTextButtonWidth(getGameStage().getWidth() / 3);
        setImageButtonWidth(getTextButtonHeight());
        setImageButtonHeight(getTextButtonHeight());

        setMuteMusicY(getStageHeightTenth() * 9 - (getImageButtonHeight() / 2));
        setMuteSoundEffectsY(getStageHeightTenth() * 6.333f - (getImageButtonHeight() / 2));
        setMuteMusicX(getStageWidthTenth() * 9 - (getImageButtonWidth() / 2));
        setMuteSoundEffectsX(getMuteMusicX());*/

        textButtonX = getStageWidthTenth() * 5 - (getTextButtonWidth() / 2);
        continueButtonY = getStageHeightTenth() * 6.333f - (getTextButtonHeight() / 2);
        retryButtonY = getStageHeightTenth() * 3.666f - (getTextButtonHeight() / 2);
        mainMenuButtonY = getStageHeightTenth() - (getTextButtonHeight() / 2);
    }

    @Override
    public void setupButtons() {
        super.setupButtons();

        continueButton.setWidth(getTextButtonWidth());
        continueButton.setHeight(getTextButtonHeight());
        continueButton.setPosition(textButtonX, continueButtonY);

        retryButton.setWidth(getTextButtonWidth());
        retryButton.setHeight(getTextButtonHeight());
        retryButton.setPosition(textButtonX, retryButtonY);

        mainMenuButton.setWidth(getTextButtonWidth());
        mainMenuButton.setHeight(getTextButtonHeight());
        mainMenuButton.setPosition(textButtonX, mainMenuButtonY);

        /*getMuteMusicButton().setWidth(getImageButtonWidth());
        getMuteMusicButton().setHeight(getImageButtonHeight());
        getMuteMusicButton().setPosition(getMuteMusicX(), getMuteMusicY());

        getMuteSoundFxButton().setWidth(getImageButtonWidth());
        getMuteSoundFxButton().setHeight(getImageButtonHeight());
        getMuteSoundFxButton().setPosition(getMuteSoundEffectsX(), getMuteSoundEffectsY());*/

        continueButton.setText(continueButtonText);
        retryButton.setText(retryButtonText);
        mainMenuButton.setText(mainMenuButtonText);

        getGameStage().setDebugAll(true);
    }

    public void clickListeners() {
        super.clickListeners();

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), worldNumber));
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new GameScreen(getGame(), levelNumber, worldNumber));
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MainMenuScreen(getGame()));
            }
        });
/*

        getMuteMusicButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        getMuteSoundFxButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });*/

    }

    @Override
    public void updateTexts() {
        continueButtonText = getGame().getBundle().get("continue");
        retryButtonText = getGame().getBundle().get("retry");
        mainMenuButtonText = getGame().getBundle().get("mainmenu");
        pausedText = getGame().getBundle().get("paused");
    }
}

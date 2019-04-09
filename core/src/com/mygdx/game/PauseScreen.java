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

    public PauseScreen(JDCEGame g, int levelnum) {
        super(g);

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

        clickListeners(levelnum);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

        getLayout48().setText(getFont48(), pausedText);

        getSpriteBatch().begin();
        getSpriteBatch().draw(getBackground(), 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        getGameStage().draw();

        getGameViewport().apply();
        getSpriteBatch().setProjectionMatrix(getGameViewport().getCamera().combined);

        getSpriteBatch().begin();
        getFont48().draw(getSpriteBatch(), pausedText, getStageWidthTenth() * 5 - getLayout48().width / 2,
                getStageHeightTenth() * 9 - getLayout48().height / 2);
        getSpriteBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        getGameViewport().update(width, height, true);
        getMeterViewport().update(width, height, true);

        setupButtonBounds();
        setupButtons();
    }

    public void dispose() {
        super.dispose();
    }

    @Override
    public void setupButtonBounds() {
        updateTenths();

        setTextButtonHeight(getGameStage().getHeight() / 6);
        setTextButtonWidth(getGameStage().getWidth() / 3);
        setImageButtonWidth(getTextButtonHeight());
        setImageButtonHeight(getTextButtonHeight());

        textButtonX = getStageWidthTenth() * 5 - (getTextButtonWidth() / 2);
        continueButtonY = getStageHeightTenth() * 6.333f - (getTextButtonHeight() / 2);
        retryButtonY = getStageHeightTenth() * 3.666f - (getTextButtonHeight() / 2);
        mainMenuButtonY = getStageHeightTenth() - (getTextButtonHeight() / 2);

        setMuteMusicY(getStageHeightTenth() * 9 - (getImageButtonHeight() / 2));
        setMuteSoundEffectsY(getStageHeightTenth() * 6.333f - (getImageButtonHeight() / 2));
        setMuteMusicX(getStageWidthTenth() * 9 - (getImageButtonWidth() / 2));
        setMuteSoundEffectsX(getMuteMusicX());
    }

    @Override
    public void setupButtons() {
        continueButton.setWidth(getTextButtonWidth());
        continueButton.setHeight(getTextButtonHeight());
        continueButton.setPosition(textButtonX, continueButtonY);

        retryButton.setWidth(getTextButtonWidth());
        retryButton.setHeight(getTextButtonHeight());
        retryButton.setPosition(textButtonX, retryButtonY);

        mainMenuButton.setWidth(getTextButtonWidth());
        mainMenuButton.setHeight(getTextButtonHeight());
        mainMenuButton.setPosition(textButtonX, mainMenuButtonY);

        getMuteMusicButton().setWidth(getImageButtonWidth());
        getMuteMusicButton().setHeight(getImageButtonHeight());
        getMuteMusicButton().setPosition(getMuteMusicX(), getMuteMusicY());

        getMuteSoundFxButton().setWidth(getImageButtonWidth());
        getMuteSoundFxButton().setHeight(getImageButtonHeight());
        getMuteSoundFxButton().setPosition(getMuteSoundEffectsX(), getMuteSoundEffectsY());

        continueButton.setText(continueButtonText);
        retryButton.setText(retryButtonText);
        mainMenuButton.setText(mainMenuButtonText);

        getGameStage().setDebugAll(true);
    }

    public void clickListeners(int levelnum) {
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });


        getMuteMusicButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        getMuteSoundFxButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

    }
    public void updateTexts() {
        continueButtonText = getGame().getBundle().get("continue");
        retryButtonText = getGame().getBundle().get("retry");
        mainMenuButtonText = getGame().getBundle().get("mainmenu");
        pausedText = getGame().getBundle().get("paused");
    }
}

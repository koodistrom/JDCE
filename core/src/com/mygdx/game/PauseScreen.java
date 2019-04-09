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

    /*private String
    private String
    private String*/

    public PauseScreen(JDCEGame g) {
        super(g);

        setupButtonBounds();

        setBackground(new Texture(Gdx.files.internal("bluebackground.png")));


        updateTexts();
        setupButtons();

        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());

        Gdx.input.setInputProcessor(getGameStage());

        clickListeners();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(getBackground(), 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        getGameStage().draw();
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

        /*textButtonX = getStageWidthTenth() * 5 - (getTextButtonWidth() / 2);
        playButtonY = getStageHeightTenth() * 9 - (getTextButtonHeight() / 2);
        highScoreButtonY = getStageHeightTenth() * 6.333f - (getTextButtonHeight() / 2);
        quitButtonY = getStageHeightTenth() * 3.666f  - (getTextButtonHeight() / 2);

        ENbuttonX = getStageWidthTenth() - (getImageButtonWidth() / 2);
        FIbuttonX = getStageWidthTenth() * 2.5f - (getImageButtonWidth() / 2);
        languageButtonY = getStageHeightTenth() * 1 - (getImageButtonHeight() / 2);*/

        setMuteMusicY(getStageHeightTenth() * 9 - (getImageButtonHeight() / 2));
        setMuteSoundEffectsY(getStageHeightTenth() * 6.333f - (getImageButtonHeight() / 2));
        setMuteMusicX(getStageWidthTenth() * 9 - (getImageButtonWidth() / 2));
        setMuteSoundEffectsX(getMuteMusicX());
    }

    @Override
    public void setupButtons() {
        /*playButton.setWidth(getTextButtonWidth());
        playButton.setHeight(getTextButtonHeight());
        playButton.setPosition(textButtonX, playButtonY);

        highScoreButton.setWidth(getTextButtonWidth());
        highScoreButton.setHeight(getTextButtonHeight());
        highScoreButton.setPosition(textButtonX, highScoreButtonY);

        quitButton.setWidth(getTextButtonWidth());
        quitButton.setHeight(getTextButtonHeight());
        quitButton.setPosition(textButtonX, quitButtonY);*/

        getMuteMusicButton().setWidth(getImageButtonWidth());
        getMuteMusicButton().setHeight(getImageButtonHeight());
        getMuteMusicButton().setPosition(getMuteMusicX(), getMuteMusicY());

        getMuteSoundFxButton().setWidth(getImageButtonWidth());
        getMuteSoundFxButton().setHeight(getImageButtonHeight());
        getMuteSoundFxButton().setPosition(getMuteSoundEffectsX(), getMuteSoundEffectsY());

        /*playButton.setText(playButtonText);
        highScoreButton.setText(highScoreButtonText);
        quitButton.setText(quitButtonText);
        confirmAffirmative.setText(confirmAffirmativeText);
        confirmNegative.setText(confirmNegativeText);*/

        getGameStage().setDebugAll(true);
    }

    public void clickListeners() {
        /*playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isQuitConfirmOn == false) {
                    getGame().setScreen(new LevelSelectScreen(getGame()));
                }
            }
        });

        highScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isQuitConfirmOn == false) {
                    getGame().setScreen(new HighScoreScreen(getGame()));
                }
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isQuitConfirmOn = true;
            }
        });

        confirmAffirmative.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                System.exit(-1);
            }
        });

        confirmNegative.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isQuitConfirmOn = false;
            }
        });*/

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
        /*playButtonText = getGame().getBundle().get("play");
        highScoreButtonText = getGame().getBundle().get("highscores");
        quitButtonText = getGame().getBundle().get("quit");
        confirmAffirmativeText = getGame().getBundle().get("affirmative");
        confirmNegativeText = getGame().getBundle().get("negative");
        quitConfirmText = getGame().getBundle().get("quitConfirmText");*/
    }
}

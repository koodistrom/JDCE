package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen extends NewScreen {
    private float stageHeightTenth;
    private float stageWidthTenth;
    private float textButtonX;
    private float textButtonY1;
    private float textButtonY2;
    private float textButtonY3;
    private float imageButtonX1;
    private float imageButtonX2;
    private float imageButtonX3;
    private float imageButtonY1;
    private float imageButtonY2;
    private float imageButtonY3;

    private TextButton playButton;
    private TextButton highScoreButton;
    private TextButton quitButton;


    public MainMenuScreen(JDCEGame g) {
        super(g);
        System.out.println("korkeus "+getScreenHeight());
        System.out.println("leveys "+getScreenWidth());

        setupButtonBounds();

        setBackground(new Texture(Gdx.files.internal("bluebackground.png")));

        playButton = new TextButton("Play", getUiSkin());
        highScoreButton = new TextButton("High Scores", getUiSkin());
        quitButton = new TextButton("Quit", getUiSkin());

        setupButtons();

        getGameStage().addActor(playButton);
        getGameStage().addActor(highScoreButton);
        getGameStage().addActor(quitButton);
        getGameStage().addActor(getButtonEN());
        getGameStage().addActor(getButtonFI());
        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        Gdx.input.setInputProcessor(getGameStage());

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                LevelSelectScreen gs = new LevelSelectScreen(getGame());
                getGame().setScreen(gs);
            }
        });

        highScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HighScoreScreen hss = new HighScoreScreen(getGame());
                getGame().setScreen(hss);
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

    public void setupButtonBounds() {
        stageHeightTenth = getStageHeight() / 10;
        stageWidthTenth = getStageWidth() / 10;
        textButtonX = stageWidthTenth * 5 - (getTextButtonWidth() / 2);
        textButtonY1 = stageHeightTenth * 9 - (getTextButtonHeight() / 2);
        textButtonY2 = stageHeightTenth * 6.333f - (getTextButtonHeight() / 2);
        textButtonY3 = stageHeightTenth * 3.666f  - (getTextButtonHeight() / 2);
        imageButtonX1 = stageWidthTenth - (getImageButtonWidth() / 2);
        imageButtonX2 = stageWidthTenth * 2.5f - (getImageButtonWidth() / 2);
        imageButtonX3 = stageWidthTenth * 9 - (getImageButtonWidth() / 2);
        imageButtonY1 = stageHeightTenth * 1 - (getImageButtonHeight() / 2);
        imageButtonY2 = textButtonY1; //stageHeightTenth * 9;
        imageButtonY3 = textButtonY2; //stageHeightTenth * 8;
    }

    public void setupButtons() {
        playButton.setWidth(getTextButtonWidth());
        playButton.setHeight(getTextButtonHeight());
        playButton.setPosition(textButtonX, textButtonY1);

        highScoreButton.setWidth(getTextButtonWidth());
        highScoreButton.setHeight(getTextButtonHeight());
        highScoreButton.setPosition(textButtonX, textButtonY2);

        quitButton.setWidth(getTextButtonWidth());
        quitButton.setHeight(getTextButtonHeight());
        quitButton.setPosition(textButtonX, textButtonY3);

        getButtonEN().setWidth(getImageButtonWidth());
        getButtonEN().setHeight(getImageButtonHeight());
        getButtonEN().setPosition(imageButtonX1, imageButtonY1);

        getButtonFI().setWidth(getImageButtonWidth());
        getButtonFI().setHeight(getImageButtonHeight());
        getButtonFI().setPosition(imageButtonX2, imageButtonY1);

        getMuteMusicButton().setWidth(getImageButtonWidth());
        getMuteMusicButton().setHeight(getImageButtonHeight());
        getMuteMusicButton().setPosition(imageButtonX3, imageButtonY2);

        getMuteSoundFxButton().setWidth(getImageButtonWidth());
        getMuteSoundFxButton().setHeight(getImageButtonHeight());
        getMuteSoundFxButton().setPosition(imageButtonX3, imageButtonY3);
    }
}


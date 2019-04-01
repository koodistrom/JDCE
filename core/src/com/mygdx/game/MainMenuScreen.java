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
    /*public Button play;
    public Button highScores;*/
    private Texture background;
    private float stageHeightTenth = getStageHeight() / 10;
    private float stageWidthTenth = getStageWidth() / 10;
    private float textButtonX = stageWidthTenth * 5 - (getTextButtonWidth() / 2);
    private float textButtonY1 = stageHeightTenth * 8 - (getTextButtonHeight() / 2);
    private float textButtonY2 = stageHeightTenth * 5 - (getTextButtonHeight() / 2);
    private float textButtonY3 = stageHeightTenth * 2  - (getTextButtonHeight() / 2);
    private float imageButtonX1 = stageWidthTenth - (getImageButtonWidth() / 2);
    private float imageButtonX2 = stageWidthTenth * 2.5f - (getImageButtonWidth() / 2);
    private float imageButtonX3 = stageWidthTenth * 9 - (getImageButtonWidth() / 2);
    private float imageButtonY1 = 5;
    private float imageButtonY2 = 5;
    private float imageButtony3 = 5;

    public MainMenuScreen(JDCEGame g) {
        super(g);

        /*play = new Button(getGame(), 32, "arialbd.ttf", Color.BLACK, "New Game", getScreenWidth()/2, getScreenHeight()/2);
        highScores = new Button(getGame(), 32, "arialbd.ttf", Color.BLACK, "High Scores", getScreenWidth()/2, getScreenHeight()/3);*/
        background = new Texture(Gdx.files.internal("bluebackground.png"));

        TextButton playButton = new TextButton("Play", getUiSkin());
        TextButton highScoreButton = new TextButton("High Scores", getUiSkin());
        TextButton quitButton = new TextButton("Quit", getUiSkin());

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

        getMuteButton().setWidth(getImageButtonWidth());
        getMuteButton().setHeight(getImageButtonHeight());
        getMuteButton().setPosition(imageButtonX3, imageButtonY1);

        getGameStage().addActor(playButton);
        getGameStage().addActor(highScoreButton);
        getGameStage().addActor(quitButton);
        getGameStage().addActor(getButtonEN());
        getGameStage().addActor(getButtonFI());
        getGameStage().addActor(getMuteButton());
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

        getMuteButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(getMuteButton().isChecked()) {
                    getMuteButton().setChecked(true);
                } else {
                    getMuteButton().setChecked(false);
                }
            }
        });

    }

    @Override
    public void render(float delta) {
        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);
        /*Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/

        /*if(play.isClicked(getCamera())) {
            getGame().setScreen(new GameScreen(getGame()));
        }

        if(highScores.isClicked(getCamera())) {
            getGame().setScreen(new HighScoreScreen(getGame()));
        }*/

        getSpriteBatch().begin();
        getSpriteBatch().draw(background, 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        getGameStage().draw();
    }



    public void dispose() {
        background.dispose();
        super.dispose();
    }
}


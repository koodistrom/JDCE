package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class LevelInfoScreen extends NewScreen {
    private TextButton playButton;
    private TextButton highScoreButton;
    private String playButtonText;
    private String highScoreButtonText;
    private String levelText;
    private int worldNumber;
    private int levelNumber;
    private float playButtonX;
    private float playButtonY;
    private float highScoreButtonX;
    private float highScoreButtonY;
    private float levelTextX;
    private float levelTextY;

    private ShapeRenderer shapeRenderer;
    private GameScreen gameScreen;

    public LevelInfoScreen(JDCEGame g, int levelNumber, int worldNumber) {
        super(g);

        this.worldNumber = worldNumber;
        this.levelNumber = levelNumber;

        setupButtonBounds();


        playButton = new TextButton(playButtonText, getGame().getUiSkin());
        highScoreButton = new TextButton(highScoreButtonText, getGame().getUiSkin());

        updateTexts();
        setupButtons();

        gameScreen = new GameScreen(getGame(), levelNumber, worldNumber);

        for(int i=0; i<gameScreen.getModules().size();i++){
            gameScreen.getModules().get(i).createMapOutlines(25f,0,getScreenHeight());

        }

        shapeRenderer = new ShapeRenderer();
        Gdx.gl.glLineWidth(2);

        getGameStage().addActor(playButton);
        getGameStage().addActor(highScoreButton);
        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(getBackButton());


        getGameStage().setDebugAll(true);
        Gdx.input.setInputProcessor(getGameStage());

        clickListeners();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        getGame().getLayout48().setText(getGame().getFont48(), levelText + " " + levelNumber);

        levelTextX = playButtonX + getTextButtonWidth() / 2 - (getGame().getLayout48().width / 2);
        levelTextY = getStageHeightTenth() * 3.5f - (getTextButtonHeight() / 2);

        getSpriteBatch().begin();
        getGame().getFont48().draw(getSpriteBatch(), levelText + " " + levelNumber, levelTextX, levelTextY);

        getSpriteBatch().end();

        getMeterViewport().apply();

        shapeRenderer.setProjectionMatrix(getMeterViewport().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i=0; i<gameScreen.getModules().size(); i++) {
            gameScreen.getModules().get(i).drawMapOutlines(shapeRenderer);
        }
        shapeRenderer.end();


    }

    @Override
    public void setupButtonBounds() {
        super.setupButtonBounds();

        playButtonX = getMuteMusicX() + getImageButtonWidth() - getTextButtonWidth();
        playButtonY = getStageHeightTenth() * 1 - (getTextButtonHeight() / 2);
        highScoreButtonX = playButtonX - getTextButtonWidth() * 1.25f;
        highScoreButtonY = playButtonY;
    }

    @Override
    public void setupButtons() {
        super.setupButtons();

        playButton.setSize(getTextButtonWidth(), getTextButtonHeight());
        highScoreButton.setSize(getTextButtonWidth(), getTextButtonHeight());

        playButton.setPosition(playButtonX, playButtonY);
        highScoreButton.setPosition(highScoreButtonX, highScoreButtonY);

        playButton.setText(playButtonText);
        highScoreButton.setText(highScoreButtonText);

    }
    @Override
    public void clickListeners() {
        super.clickListeners();

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(gameScreen);
                dispose();
            }
        });

        highScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new HighScoreScreen(getGame(), levelNumber, worldNumber));
                dispose();
            }
        });

        getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), worldNumber));
                gameScreen.dispose();
                dispose();
            }
        });
    }
    @Override
    public void updateTexts() {
        playButtonText = getGame().getBundle().get("play");
        highScoreButtonText = getGame().getBundle().get("highscores");
        levelText = getGame().getBundle().get("level");
    }
}

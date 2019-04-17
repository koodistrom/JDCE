package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class LevelInfoScreen extends NewScreen {
    private TextButton playButton;
    private String playButtonText;
    private String levelText;
    private int worldNumber;
    private int levelNumber;
    private float playButtonX;
    private float playButtonY;
    private float levelTextX;
    private float levelTextY;
    private ArrayList<LevelModule> modules;
    private LevelCreator2 levelCreator;
    private PolygonSpriteBatch polygonSpriteBatch;

    public LevelInfoScreen(JDCEGame g, int levelNumber, int worldNumber) {
        super(g);

        this.worldNumber = worldNumber;
        this.levelNumber = levelNumber;

        setupButtonBounds();

        setBackground(new Texture(Gdx.files.internal("tausta_valikko.png")));

        playButton = new TextButton(playButtonText, getUiSkin());

        updateTexts();
        setupButtons();
        levelCreator = new LevelCreator2(new GameScreen(getGame(), levelNumber, worldNumber));
        modules = levelCreator.createModules("rata2.svg","lumitausta.png", Color.GRAY);
        polygonSpriteBatch = new PolygonSpriteBatch();
        System.out.println("moduleita "+modules.size());
        getGameStage().addActor(playButton);
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
        getLayout48().setText(getFont48(), levelText + " " + levelNumber);

        levelTextX = playButtonX + getTextButtonWidth() / 2 - (getLayout48().width / 2);
        levelTextY = getStageHeightTenth() * 3.5f - (getTextButtonHeight() / 2);

        getSpriteBatch().begin();
        getFont48().draw(getSpriteBatch(), levelText + " " + levelNumber, levelTextX, levelTextY);

        getSpriteBatch().end();

        getMeterViewport().apply();
        polygonSpriteBatch.setProjectionMatrix(getMeterViewport().getCamera().combined);
        polygonSpriteBatch.begin();

        for(int i=0; i<modules.size();i++){
            modules.get(i).drawMap(25f,polygonSpriteBatch,0,getScreenHeight());

        }

        polygonSpriteBatch.end();
    }

    @Override
    public void setupButtonBounds() {
        super.setupButtonBounds();

        playButtonX = getMuteMusicX() + getImageButtonWidth() - getTextButtonWidth();
        playButtonY = getStageHeightTenth() * 1 - (getTextButtonHeight() / 2);
    }

    @Override
    public void setupButtons() {
        super.setupButtons();

        playButton.setSize(getTextButtonWidth(), getTextButtonHeight());

        playButton.setPosition(playButtonX, playButtonY);

        playButton.setText(playButtonText);

    }
    @Override
    public void clickListeners() {
        super.clickListeners();

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new GameScreen(getGame(), levelNumber, worldNumber));
                dispose();
            }
        });

        getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), worldNumber));
                dispose();
            }
        });
    }
    @Override
    public void updateTexts() {
        playButtonText = getGame().getBundle().get("play");
        levelText = getGame().getBundle().get("level");
    }
}

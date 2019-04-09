package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HighScoreScreen extends NewScreen {
    Preferences highscores;
    Table table;
    Label highscoreLabel;
    public HighScoreScreen(JDCEGame g) {
        super(g);

        table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        highscoreLabel = new Label("Highscores", getUiSkin());
        table.add(highscoreLabel);
        table.row();
        highscores = Gdx.app.getPreferences("JDCE_highscores");

        displayHighScores(2);
        setupButtonBounds();

        setBackground(new Texture(Gdx.files.internal("bluebackground.png")));

        setupButtons();

        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(getBackButton());
        Gdx.input.setInputProcessor(getGameStage());

        getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MainMenuScreen(getGame()));
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

        getGameStage().addActor(table);
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

    @Override
    public void setupButtonBounds() {
        updateTenths();

        setTextButtonHeight(getGameStage().getHeight() / 6);
        setTextButtonWidth(getGameStage().getWidth() / 3);
        setImageButtonWidth(getTextButtonHeight());
        setImageButtonHeight(getTextButtonHeight());

        setBackButtonX(getStageWidthTenth() - (getImageButtonWidth() / 2));
        setBackButtonY(getStageHeightTenth() * 1 - (getImageButtonHeight() / 2));
        setMuteMusicY(getStageHeightTenth() * 9 - (getImageButtonHeight() / 2));
        setMuteSoundEffectsY(getStageHeightTenth() * 6.333f - (getImageButtonHeight() / 2));
        setMuteMusicX(getStageWidthTenth() * 9 - (getImageButtonWidth() / 2));
        setMuteSoundEffectsX(getMuteMusicX());
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



    public void displayHighScores(int levelNum) {
        String level = String.valueOf(levelNum);
        String highscoreString = highscores.getString(level, "");
        String scoreToDisplay = "";
        for(int i=0;i<highscoreString.length();i++){
            if(highscoreString.charAt(i)!='#'){
                scoreToDisplay += highscoreString.charAt(i);
            }else {
                table.add(new Label(scoreToDisplay, getUiSkin()));
                table.row();
                scoreToDisplay = "";
            }
        }
    }
    //addHighScore(Utilities.secondsToString(time),levelNum);
    public void addHighScore(String score, int levelNum) {
        String level = String.valueOf(levelNum);
        String valueToSave = highscores.getString(level, "")+score+"#";
        highscores.putString(level, valueToSave);
        highscores.flush();
    }
}


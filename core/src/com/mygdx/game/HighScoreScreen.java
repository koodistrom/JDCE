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

        setBackground(new Texture(Gdx.files.internal("tausta_valikko.png")));

        setupButtons();

        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(getBackButton());
        Gdx.input.setInputProcessor(getGameStage());

        getGameStage().addActor(table);
        getGameStage().setDebugAll(true);

        clickListeners();
    }

    /*@Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(getBackground(), 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        getGameStage().draw();
    }*/

/*    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        *//*getPixelViewport().update(width, height, true);
        getMeterViewport().update(width, height, true);

        setupButtonBounds();*//*
        setupButtons();
    }*/

/*    @Override
    public void setupButtonBounds() {
        super.setupButtonBounds();
        *//*updateTenths();

        setTextButtonHeight(getGameStage().getHeight() / 6);
        setTextButtonWidth(getGameStage().getWidth() / 3);
        setImageButtonWidth(getTextButtonHeight());
        setImageButtonHeight(getTextButtonHeight());

        setBackButtonX(getStageWidthTenth() - (getImageButtonWidth() / 2));
        setBackButtonY(getStageHeightTenth() * 1 - (getImageButtonHeight() / 2));
        setMuteMusicY(getStageHeightTenth() * 9 - (getImageButtonHeight() / 2));
        setMuteSoundEffectsY(getStageHeightTenth() * 6.333f - (getImageButtonHeight() / 2));
        setMuteMusicX(getStageWidthTenth() * 9 - (getImageButtonWidth() / 2));
        setMuteSoundEffectsX(getMuteMusicX());*//*
    }*/

   /* @Override
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
    }*/



    public void displayHighScores(int levelNum) {
        String level = String.valueOf(levelNum);
        String highscoreString = highscores.getString(level, "");
        String scoreToDisplay = "";
        String name = "";
        Boolean addToName= true;

        if(highscoreString.length()!=0) {
            for (int i = 0; i < highscoreString.length(); i++) {
                if (highscoreString.charAt(i) != '#' && highscoreString.charAt(i) != '%') {
                    if (addToName) {
                        name += highscoreString.charAt(i);
                    } else {
                        scoreToDisplay += highscoreString.charAt(i);
                    }

                } else if (highscoreString.charAt(i) == '%') {
                    table.add(new Label(name, getUiSkin()));

                    name = "";
                    addToName = false;

                } else if (highscoreString.charAt(i) == '#') {
                    table.add(new Label(Utilities.secondsToString(Float.valueOf(scoreToDisplay)), getUiSkin()));
                    table.row();
                    scoreToDisplay = "";
                    addToName = true;
                }

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

    @Override
    public void clickListeners() {
        super.clickListeners();

        getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MainMenuScreen(getGame()));
            }
        });
    }
}


package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class FinishView extends NewScreen {
    Preferences highscores;
    float finishTime;
    /*private float textButtonX = getStageWidth() / 2 - (getTextButtonWidth() / 2);
    private float textButtonY1 = getStageHeight() / 2 - (getTextButtonHeight() / 2);*/
    private String score;
    private String loseMessage;
    /*    private float textX;
        private float textY;*/
    private Table winTable;
    private Table loseTable;
    private TextButton menuButton;
    private TextButton retryButton;
    //private OrthographicCamera pixelCamera;

    public FinishView(JDCEGame g, float time, boolean isItAWin, int levelNum) {
        super(g);
        highscores = Gdx.app.getPreferences("JDCE_highscores");
        setBackground(new Texture(Gdx.files.internal("bluebackground.png")));


        winTable = new Table();
        loseTable = new Table();
        score = getGame().getBundle().get("yourTime") + " " + highscores.getString("High Score");
        loseMessage = getGame().getBundle().get("loseMessage");

        winTable.setDebug(true);
        loseTable.setDebug(true);

        menuButton = new TextButton(getGame().getBundle().get("continue"), getUiSkin());
        retryButton = new TextButton(getGame().getBundle().get("retry"), getUiSkin());

        //menuButton.setPosition(textButtonX, textButtonY1);

        if(isItAWin) {
            addHighScore(Utilities.secondsToString(time),levelNum);
            setUpWinTable();
            getGameStage().addActor(winTable);
        } else {
            setUpLoseTable();
            getGameStage().addActor(loseTable);
        }

        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());

        clickListeners(levelNum);

        //pixelCamera.setToOrtho(false, g, getScreenHeight());

        //getLayout48().setText(getFont48(), score);

        /*textX = Gdx.graphics.getWidth()/2 - getLayout48().width / 2;
        textY = Gdx.graphics.getHeight()/1.5f - getLayout48().height;*/
        Gdx.input.setInputProcessor(getGameStage());
    }

    public void setUpWinTable() {
        updateTables();
        winTable.add(new Label(score, getUiSkin())).height(50).spaceBottom(30);
        winTable.center();
        winTable.row();
        winTable.add(menuButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);
        winTable.row();
        winTable.add(retryButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);

    }

    public void setUpLoseTable() {
        updateTables();
        loseTable.add(new Label(loseMessage, getUiSkin())).height(50).spaceBottom(30);
        loseTable.center();
        loseTable.row();
        loseTable.add(menuButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);
        loseTable.row();
        loseTable.add(retryButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);

    }

    public void updateTables() {
        winTable.setFillParent(true);
        loseTable.setFillParent(true);
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

        getGameViewport().apply();
        getSpriteBatch().setProjectionMatrix(getGameViewport().getCamera().combined);

        /*getSpriteBatch().begin();
        getFont48().draw(getSpriteBatch(), score, textX, textY);
        getSpriteBatch().end();*/
    }

    public void addHighScore(String score, int levelNum) {
        String level = String.valueOf(levelNum);
        String valueToSave = highscores.getString(level, "")+score+"#";
        highscores.putString(level, valueToSave);
        highscores.flush();
    }

    @Override
    public void resize(int width, int height) {
        getGameViewport().update(width, height, true);
        getMeterViewport().update(width, height, true);

        setupButtonBounds();
        setupButtons();
        updateTables();
    }

    public void setupButtonBounds() {
        updateTenths();

        setTextButtonHeight(getGameStage().getHeight() / 6);
        setTextButtonWidth(getGameStage().getWidth() / 3);
        setImageButtonWidth(getTextButtonHeight());
        setImageButtonHeight(getTextButtonHeight());


        setMuteMusicY(getStageHeightTenth() * 9 - (getImageButtonHeight() / 2));
        setMuteSoundEffectsY(getStageHeightTenth() * 6.333f - (getImageButtonHeight() / 2));
        setMuteMusicX(getStageWidthTenth() * 9 - (getImageButtonWidth() / 2));
        setMuteSoundEffectsX(getMuteMusicX());
    }

    public void setupButtons() {
        getMuteMusicButton().setWidth(getImageButtonWidth());
        getMuteMusicButton().setHeight(getImageButtonHeight());
        getMuteMusicButton().setPosition(getMuteMusicX(), getMuteMusicY());

        getMuteSoundFxButton().setWidth(getImageButtonWidth());
        getMuteSoundFxButton().setHeight(getImageButtonHeight());
        getMuteSoundFxButton().setPosition(getMuteSoundEffectsX(), getMuteSoundEffectsY());

        getGameStage().setDebugAll(true);
    }

    public void clickListeners(final int levelNum) {
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame()));
                dispose();
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new GameScreen(getGame(), levelNum));
                dispose();
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();

    }
}

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class FinishView extends NewScreen implements Input.TextInputListener {
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
    private String name;
    private float time;
    private int levelNumber;
    private int worldNumber;
    private int nameLenghtLimit;
    //private OrthographicCamera pixelCamera;

    public FinishView(JDCEGame g, float time, boolean isItAWin, int levelNumber, int worldNumber) {
        super(g);
        this.time = time;
        this.levelNumber = levelNumber;
        this.worldNumber = worldNumber;
        highscores = Gdx.app.getPreferences("JDCE_highscores");
        setBackground(new Texture(Gdx.files.internal("tausta_valikko.png")));

        nameLenghtLimit = 10;
        winTable = new Table();
        loseTable = new Table();
        score = getGame().getBundle().get("yourTime") + " " + highscores.getString("High Score");
        loseMessage = getGame().getBundle().get("loseMessage");

        name = "";
        /*winTable.setDebug(true);
        loseTable.setDebug(true);*/

        menuButton = new TextButton(getGame().getBundle().get("continue"), getUiSkin());
        retryButton = new TextButton(getGame().getBundle().get("retry"), getUiSkin());

        //menuButton.setPosition(textButtonX, textButtonY1);

        if(isItAWin) {

            if(fitsToHighscore(time,levelNumber)){
                enterName();
            }


            setUpWinTable();
            getGameStage().addActor(winTable);
        } else {
            setUpLoseTable();
            getGameStage().addActor(loseTable);
        }

        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());

        clickListeners();

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
    @Override
    public void updateTables() {
        winTable.setFillParent(true);
        loseTable.setFillParent(true);
    }

/*
    @Override
    public void render(float delta) {
        super.render(delta);
        */
/*Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(getBackground(), 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        getGameStage().draw();*//*


        getPixelViewport().apply();
        getSpriteBatch().setProjectionMatrix(getPixelViewport().getCamera().combined);

        */
/*getSpriteBatch().begin();
        getFont48().draw(getSpriteBatch(), score, textX, textY);
        getSpriteBatch().end();*//*

    }
*/

    public boolean fitsToHighscore(float score, int levelNum){
        String level = String.valueOf(levelNum);
        String HSOfLevel = highscores.getString(level, "");
        String comparisonTime = "";
        String turnedComparisonTime="";
        String valueToSave = "";
        String lastEntry = "";
        boolean fits = false;
        boolean addToTime = false;
        int numOfEntries = 0;
        for(int i=HSOfLevel.length()-1;i>=0;i--){
            if(addToTime && HSOfLevel.charAt(i)!='%'){
                comparisonTime +=HSOfLevel.charAt(i);
            }
            if (HSOfLevel.charAt(i) =='#'){
                if(numOfEntries==0){
                    addToTime = true;
                }
                numOfEntries++;
            }

            if (HSOfLevel.charAt(i) =='%'){
                addToTime = false;

            }

        }

        for(int i=comparisonTime.length()-1;i>=0;i--){
            turnedComparisonTime+=comparisonTime.charAt(i);
        }

        if (!turnedComparisonTime.equals("") && Float.valueOf(turnedComparisonTime)>score){
            fits = true;
        }


        if(numOfEntries<10 ){
            fits = true;
        }
        return fits;
    }

    public void addHighScore(float score, int levelNum) {

        String level = String.valueOf(levelNum);
        String HSOfLevel = highscores.getString(level, "");
        String comparisonTime = "";
        String valueToSave = "";
        String lastEntry = "";
        boolean entryMade = false;
        boolean addToTime = false;
        int numOfEntries = 0;
        for(int i=0;i<HSOfLevel.length();i++){
            lastEntry+=HSOfLevel.charAt(i);

            if (HSOfLevel.charAt(i) != '#' && HSOfLevel.charAt(i) != '%') {
                if (addToTime) {
                    comparisonTime += HSOfLevel.charAt(i);
                }

            } else if (HSOfLevel.charAt(i) == '%') {

                comparisonTime = "";
                addToTime = true;

            } else if (HSOfLevel.charAt(i) == '#') {
                numOfEntries++;
                addToTime = false;
                if(Float.valueOf(comparisonTime)>score&&!entryMade){
                   valueToSave+=name+"%"+String.valueOf(score)+"#";
                   numOfEntries++;
                   entryMade=true;
                }

                if(numOfEntries<10) {
                    valueToSave += lastEntry;
                }
                lastEntry="";
            }
            if(numOfEntries>10){
                break;
            }
        }
        if(numOfEntries<10&&!entryMade){
            valueToSave+=name+"%"+String.valueOf(score)+"#";
        }

        highscores.putString(level, valueToSave);
        highscores.flush();
    }

    public void enterName(){
        Gdx.input.getTextInput(this, "Name", "pasi", "Hint Value");
    }

    /*@Override
    public void resize(int width, int height) {
        getPixelViewport().update(width, height, true);
        getMeterViewport().update(width, height, true);

        setupButtonBounds();
        setupButtons();
        updateTables();
    }*/

    /*@Override
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
    }*/

/*    @Override
    public void setupButtons() {
        getMuteMusicButton().setWidth(getImageButtonWidth());
        getMuteMusicButton().setHeight(getImageButtonHeight());
        getMuteMusicButton().setPosition(getMuteMusicX(), getMuteMusicY());

        getMuteSoundFxButton().setWidth(getImageButtonWidth());
        getMuteSoundFxButton().setHeight(getImageButtonHeight());
        getMuteSoundFxButton().setPosition(getMuteSoundEffectsX(), getMuteSoundEffectsY());

        getGameStage().setDebugAll(true);
    }*/

    @Override
    public void clickListeners() {
        super.clickListeners();

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), worldNumber));
                dispose();
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new GameScreen(getGame(), levelNumber, worldNumber));
                dispose();
            }
        });
    }

    @Override
    public void dispose() {
        super.dispose();

    }

    @Override
    public void input(String text) {
        name = "";
        for(int i=0; i<text.length();i++){
            if(text.charAt(i)!='#'&&text.charAt(i)!='%'){
                name+=text.charAt(i);
            }
        }
        if(name.length()<=nameLenghtLimit){
            addHighScore(time, levelNumber);

        }else{
            Gdx.input.getTextInput(this, "Name", "pasi", "Hint Value");
        }

    }

    @Override
    public void canceled() {

    }

}

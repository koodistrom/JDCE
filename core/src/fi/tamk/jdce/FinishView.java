package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class FinishView extends NewScreen implements Input.TextInputListener {
    Preferences highscores;
    float finishTime;
    private String score;
    private String loseMessage;
    private Table winTable;
    private Table loseTable;
    private TextButton menuButton;
    private TextButton retryButton;
    private String name;
    private float time;
    private int levelNumber;
    private int worldNumber;
    private int nameLengthLimit;
    private  GameScreen gameScreen;

    public FinishView(JDCEGame g,GameScreen gameScreen, float time, boolean isItAWin, int levelNumber, int worldNumber) {
        super(g);
        this.time = time;
        this.levelNumber = levelNumber;
        this.worldNumber = worldNumber;
        highscores = Gdx.app.getPreferences("JDCE_highscores");
        this.gameScreen = gameScreen;

        nameLengthLimit = 10;
        winTable = new Table();
        loseTable = new Table();
        score = getGame().getBundle().get("yourTime") + " " + Utilities.secondsToString(time);
        loseMessage = getGame().getBundle().get("loseMessage");

        name = "";

        menuButton = new TextButton(getGame().getBundle().get("continue"), getGame().getUiSkin());
        retryButton = new TextButton(getGame().getBundle().get("retry"), getGame().getUiSkin());

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

        Gdx.input.setInputProcessor(getGameStage());
    }

    public void setUpWinTable() {
        updateTables();
        winTable.add(new Label(score, getGame().getUiSkin())).height(50).spaceBottom(30);
        winTable.center();
        winTable.row();
        winTable.add(menuButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);
        winTable.row();
        winTable.add(retryButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);

    }

    public void setUpLoseTable() {
        updateTables();
        loseTable.add(new Label(loseMessage, getGame().getUiSkin())).height(50).spaceBottom(30);
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
            lastEntry += HSOfLevel.charAt(i);

            if (HSOfLevel.charAt(i) != '#' && HSOfLevel.charAt(i) != '%') {
                if (addToTime) {
                    comparisonTime += HSOfLevel.charAt(i);
                }

            } else if (HSOfLevel.charAt(i) == '%') {

                comparisonTime = "";
                addToTime = true;

            } else if (HSOfLevel.charAt(i) == '#') {

                addToTime = false;
                if(Float.valueOf(comparisonTime)>score&&!entryMade){
                   valueToSave+=name+"%"+String.valueOf(score)+"#";
                   numOfEntries++;
                   entryMade=true;
                }

                if(numOfEntries<10) {
                    valueToSave += lastEntry;
                    numOfEntries++;
                }
                lastEntry="";
            }
            if(numOfEntries>=10){
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
        Gdx.input.getTextInput(this, "Name", "", "max 10 chars");
    }

    @Override
    public void clickListeners() {
        super.clickListeners();

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), worldNumber));
                playButtonSound();
                gameScreen.dispose();
                dispose();
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.reset();
                getGame().setScreen(gameScreen);
                playButtonSound();
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
        if(name.length()<=nameLengthLimit){
            addHighScore(time, levelNumber);

        }else{
            enterName();
        }

    }

    @Override
    public void canceled() {

    }

}

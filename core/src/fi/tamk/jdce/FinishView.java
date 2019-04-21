package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * FinishView is displayed when the game ends either through succeeding or failing to finish a level.
 *
 * It extends the NewScreen-class, implements Input.TextInputListener and has
 * mute-buttons for the game's music and sound effects
 *
 * If the level is passed, user is asked to input a name and
 * the screen displays the time the level was passed in.
 *
 * From the FinishView the user can either retry the level via retryButton or
 * go to the LevelSelectScreen via menuButton.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class FinishView extends NewScreen implements Input.TextInputListener {
    /**
     * A file where the high scores are stored.
     */
    Preferences highscores;

    /**
     * Tells the time that was achieved upon finishing a level.
     */
    private String score;

    /**
     * Message displayed upon failing to finish a level.
     */
    private String loseMessage;

    /**
     * Table created upon finishing a level.
     */
    private Table winTable;

    /**
     * Table created upon failing to finish a level.
     */
    private Table loseTable;

    /**
     * The Button that moves you to LevelSelectScreen.
     */
    private TextButton menuButton;

    /**
     * The Button that lets the user retry the level.
     */
    private TextButton retryButton;

    /**
     * Stores the name user inputs.
     */
    private String name;

    /**
     * Stores the time that the level was passed in.
     */
    private float time;

    /**
     * Holds the the number of the level.
     */
    private int levelNumber;

    /**
     * Holds the the number of the world.
     */
    private int worldNumber;

    /**
     * The limit of characters the user can input for the name.
     */
    private int nameLengthLimit;

    /**
     * Screen for playing the level.
     */
    private  GameScreen gameScreen;

    /**
     * The default constructor for FinishView.
     *
     * @param g the JDCEGame-class. It allows FinishView and NewScreen access to the: batch, myBundle,
     *          the game's settings, textures, uiSkin and font48.
     * @param gameScreen screen for playing the level.
     * @param time the time that the level was passed in.
     * @param isItAWin tells FinishView if the level was successfully finished.
     * @param levelNumber the number of the level.
     * @param worldNumber the number of the world.
     */
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

    /**
     * Sets up the winTable. Called if the level is successfully finished.
     */
    public void setUpWinTable() {
        updateTables();
        winTable.add(new Label(score, getGame().getUiSkin())).height(50).spaceBottom(30);
        winTable.center();
        winTable.row();
        winTable.add(menuButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);
        winTable.row();
        winTable.add(retryButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);

    }

    /**
     * Sets up the loseTable. Called if the level is not successfully finished.
     */
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

    /**
     * Checks if the scored time is fast enough to fit the high scores.
     *
     * @param score the time that the level was finished in.
     * @param levelNum the number of that level.
     * @return true if the score can be added to the level's high scores.
     */
    public boolean fitsToHighscore(float score, int levelNum){
        String level = String.valueOf(levelNum);
        String HSOfLevel = highscores.getString(level, "");
        String comparisonTime = "";
        String turnedComparisonTime="";
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

    /**
     * Adds the score to the level.
     *
     * @param score the time that the level was finished in.
     * @param levelNum the number of that level.
     */
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

    /**
     * Asks the user to input a name with maximum of 10 characters.
     */
    public void enterName(){
        Gdx.input.getTextInput(this, getGame().getBundle().get("nameText"), "", getGame().getBundle().get("maxChars"));
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

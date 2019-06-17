package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

/**
 * HighScoreScreen displays the highest 10 scored times on the chosen level.
 *
 * It extends the NewScreen-class and has
 * mute-buttons for the game's music and sound effects
 * and backButton to access the previous screen.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class HighScoreScreen extends NewScreen {
    /**
     * A file where the high scores are stored.
     */
    Preferences highscores;

    /**
     * Table that controls and sets the layout of the high scores.
     */
    Table localHSTable;

    /**
     * Label for the high score display.
     */
    Label localHSLabel;

    /**
     * Holds the the number of the level.
     */
    private int levelNumber;

    /**
     * Holds the the number of the world.
     */
    private int worldNumber;

    Table worldHSTable;

    Table mainTable;

    /**
     * The default constructor for HighScoreScreen.
     *
     * @param g the JDCEGame-class. It allows HighScoreScreen and NewScreen access to the: batch, myBundle,
     *          the game's settings, textures, uiSkin and font48.
     */
    public HighScoreScreen(JDCEGame g, int levelNum, int worldNumber) {
        super(g);

        this.worldNumber = worldNumber;
        levelNumber = levelNum;
        localHSTable = new Table();
        worldHSTable = new Table();
        mainTable = new Table();


        String HStext = getGame().getBundle().get("highscores") + " " + getGame().getBundle().get("level") + " " + levelNumber;

        String localHStext ="Local ";
        localHSLabel = new Label(localHStext, getGame().getUiSkin());
        localHSTable.add(localHSLabel).colspan(2).height(localHSLabel.getHeight()*2);
        localHSTable.row();
        highscores = Gdx.app.getPreferences("JDCE_highscores");


        String worldHStext ="World ";
        worldHSTable.add(new Label(worldHStext, getGame().getUiSkin())).colspan(2).height(localHSLabel.getHeight()*2);
        worldHSTable.row();
        worldHSTable.add(new Label("tähän highsocoreja", getGame().getUiSkin())).colspan(2).height(localHSLabel.getHeight()*2);
        worldHSTable.padLeft(getGameStage().getHeight()*0.1f);
        worldHSTable.top();

        mainTable.setFillParent(true);

        mainTable.add(new Label(HStext, getGame().getUiSkin())).colspan(4).height(localHSLabel.getHeight()*2);
        mainTable.row();
        mainTable.add(localHSTable);
        mainTable.add(worldHSTable);
        mainTable.left().top();
        mainTable.padLeft(getGameStage().getHeight()*0.1f).padTop(getGameStage().getHeight()*0.07f);

        displayHighScores(levelNum);


        setupButtonBounds();

        setupButtons();

        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(getBackButton());
        Gdx.input.setInputProcessor(getGameStage());

        getGameStage().addActor(mainTable);
        getGameStage().setDebugAll(true);

        clickListeners();
    }

    /**
     * Puts the high scores of the level that is given as parameter in the localHSTable.
     *
     * @param levelNum number of the level that the high scores
     *                 are from.
     */
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
                    localHSTable.add(new Label(name, getGame().getUiSkin())).align(Align.left);

                    name = "";
                    addToName = false;

                } else if (highscoreString.charAt(i) == '#') {
                    localHSTable.add(new Label(Utilities.secondsToString(Float.valueOf(scoreToDisplay)), getGame().getUiSkin()));
                    localHSTable.row();
                    scoreToDisplay = "";
                    addToName = true;
                }
            }
        }
        int rows = localHSTable.getRows();
        if (rows<11){
            for(int i=0; i<11-rows;i++){
                localHSTable.add(new Label("--", getGame().getUiSkin())).align(Align.left);
                localHSTable.add(new Label("--", getGame().getUiSkin()));
                localHSTable.row();
            }
        }
    }

    @Override
    public void clickListeners() {
        super.clickListeners();

        getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelInfoScreen(getGame(), levelNumber, worldNumber));
                playButtonSound();
                dispose();
            }
        });
    }
}


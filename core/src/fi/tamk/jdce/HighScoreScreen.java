package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class HighScoreScreen extends NewScreen {
    Preferences highscores;
    Table table;
    Label highscoreLabel;
    private int levelNumber;
    private int worldNumber;
    public HighScoreScreen(JDCEGame g, int levelNum, int worldNumber) {
        super(g);

        this.worldNumber = worldNumber;
        levelNumber = levelNum;
        table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        String labelText = getGame().getBundle().get("highscores") + " " + getGame().getBundle().get("level") + " " + levelNumber;
        highscoreLabel = new Label(labelText, getGame().getUiSkin());
        table.add(highscoreLabel).colspan(2).height(highscoreLabel.getHeight()*2);
        table.row();
        highscores = Gdx.app.getPreferences("JDCE_highscores");

        displayHighScores(levelNum);
        setupButtonBounds();

        setupButtons();

        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(getBackButton());
        Gdx.input.setInputProcessor(getGameStage());

        getGameStage().addActor(table);
        getGameStage().setDebugAll(true);

        clickListeners();
    }

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
                    table.add(new Label(name, getGame().getUiSkin())).align(Align.left);

                    name = "";
                    addToName = false;

                } else if (highscoreString.charAt(i) == '#') {
                    table.add(new Label(Utilities.secondsToString(Float.valueOf(scoreToDisplay)), getGame().getUiSkin()));
                    table.row();
                    scoreToDisplay = "";
                    addToName = true;
                }

            }
        }
        int rows = table.getRows();
        if (rows<11){
            for(int i=0; i<11-rows;i++){
                table.add(new Label("--", getGame().getUiSkin())).align(Align.left);
                table.add(new Label("--", getGame().getUiSkin()));
                table.row();
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
                getGame().setScreen(new LevelInfoScreen(getGame(), levelNumber, worldNumber));
                playButtonSound();
                dispose();
            }
        });
    }
}


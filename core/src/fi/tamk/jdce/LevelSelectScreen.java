package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

/**
 * LevelSelectScreen is the level selection menu for the game.
 *
 * It extends the NewScreen-class and has
 * mute-buttons for the game's music and sound effects.
 *
 * From the LevelSelectScreen you choose the level
 * that you want to play.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class LevelSelectScreen extends NewScreen {
    private float levelSelectTextButtonWidth;
    private float levelSelectTextButtonHeight;
    private ArrayList<ArrayList<TextButton>> buttonGrid;
    private int levelnumber;
    private Table levelTable;

    public LevelSelectScreen(JDCEGame g, final int worldNumber) {
        super(g);

        setupButtonBounds();

        levelTable = new Table();
        levelTable.defaults().pad(17.5f).space(17.5f);


        switch(worldNumber) {
            case 1:
                levelnumber = 1;
                break;
            case 2:
                levelnumber = 11;
                break;
            case 3:
                levelnumber = 21;
                break;
            case 4:
                levelnumber = 31;
                break;
        }

        buttonGrid = new ArrayList<ArrayList<TextButton>>();
        for(int i = 0; i<5;i++){
            levelTable.row();
            buttonGrid.add(new ArrayList<TextButton>());
            for(int n=0; n<2;n++){
                buttonGrid.get(i).add( new TextButton(getGame().getBundle().get("level") + " " + levelnumber, getGame().getUiSkin()));

                levelTable.add(buttonGrid.get(i).get(n)).size(levelSelectTextButtonWidth, levelSelectTextButtonHeight);

                buttonGrid.get(i).get(n).addListener(new ClickListener() {
                    int level = levelnumber;
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        playButtonSound();
                        getGame().setScreen(new LevelInfoScreen(getGame(), level, worldNumber));
                    }
                });

                levelnumber++;

            }
        }

        setupButtons();

        getGameStage().addActor(levelTable);
        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(getBackButton());
        Gdx.input.setInputProcessor(getGameStage());

        updateTables();

        clickListeners();

        Gdx.input.setInputProcessor(getGameStage());
    }

    @Override
    public void setupButtonBounds() {
        super.setupButtonBounds();

        levelSelectTextButtonHeight = getStageHeightTenth();
        levelSelectTextButtonWidth = getStageWidthTenth() * 1.75f;
    }

    @Override
    public void updateTables() {
        levelTable.defaults().pad(17.5f).space(17.5f);
        levelTable.setSize(getStageWidth() / 3, getStageHeightTenth() * 8);

        levelTable.setPosition(getGameStage().getWidth() / 2 - (levelTable.getWidth() / 2),
                getGameStage().getHeight() / 2 - (levelTable.getHeight() / 2));
    }

    public float getStageWidth() {
        return getGameStage().getWidth();
    }

    public float getStageHeight() {
        return getGameStage().getHeight();
    }

    @Override
    public void clickListeners() {
        super.clickListeners();

        getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new WorldSelectScreen(getGame()));
                dispose();
            }
        });
    }
}

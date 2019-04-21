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
 * mute-buttons for the game's music and sound effects
 * and backButton to access the previous screen.
 *
 * From the LevelSelectScreen you choose the level
 * that you want to play.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class LevelSelectScreen extends NewScreen {
    /**
     * The width for the levelSelectTextButtons.
     */
    private float levelSelectTextButtonWidth;

    /**
     * The height for the levelSelectTextButtons.
     */
    private float levelSelectTextButtonHeight;

    /**
     * ArrayList for controlling the levelSelectTextButtons.
     */
    private ArrayList<ArrayList<TextButton>> buttonGrid;

    /**
     * Holds and tracks the level number for the levelSelectTextButtons.
     */
    private int levelnumber;

    /**
     * Table that controls and sets the layout of the levelSelectTextButtons.
     */
    private Table levelTable;


    /**
     * The default constructor for LevelSelectScreen.
     *
     * Creates 10 levelSelectTextButtons to levelTable, based on the worldNumber parameter.
     * For World:
     * - ...1 it creates buttons for levels 1-10.
     * - ...2 it creates buttons for levels 11-20.
     * - ...3 it creates buttons for levels 21-30.
     * - ...4 it creates buttons for levels 31-40.
     *
     * @param g the JDCEGame-class. It allows LevelSelectScreen and NewScreen access to the: batch, myBundle,
     *          the game's settings, textures, uiSkin and font48.
     * @param worldNumber tells LevelSelectScreen which world to build
     *                    the levelSelectTextButtons for.
     */
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

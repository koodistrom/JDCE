package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * LevelInfoScreen displays info about chosen level.
 *
 * It extends the NewScreen-class and has
 * mute-buttons for the game's music and sound effects
 * and backButton to access the previous screen.
 *
 * LevelInfoScreen shows a scaled down map of the chosen level,
 * allows user to see the highest scores achieved on the level via highScoreButton
 * and allows user to launch GameScreen and play the level via playButton.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class LevelInfoScreen extends NewScreen {
    /**
     * The Button that sets the screen to GameScreen.
     */
    private TextButton playButton;

    /**
     * The Button that sets the screen to HighScoreScreen.
     */
    private TextButton highScoreButton;

    /**
     * The string that includes the text displayed on the playButton.
     */
    private String playButtonText;

    /**
     * The string that includes the text displayed on the highScoreButton.
     */
    private String highScoreButtonText;

    /**
     * The string that includes the level's number displayed above the playButton ("Level X").
     */
    private String levelText;

    /**
     * Holds the the number of the world.
     */
    private int worldNumber;

    /**
     * Holds the the number of the level.
     */
    private int levelNumber;

    /**
     * The location of the playButton on the x-axis/width-axis.
     */
    private float playButtonX;

    /**
     * The location of the playButton on the y-axis/height-axis.
     */
    private float playButtonY;

    /**
     * The location of the highScoreButton on the x-axis/width-axis.
     */
    private float highScoreButtonX;

    /**
     * The location of the highScoreButton on the y-axis/height-axis.
     */
    private float highScoreButtonY;

    /**
     * The location of the levelText String on the x-axis/width-axis.
     */
    private float levelTextX;

    /**
     * The location of the levelText String on the y-axis/height-axis.
     */
    private float levelTextY;

    /**
     * Scaler used to downscale the level for the display.
     */
    private float mapScaler;

    /**
     * The background texture for the level display.
     */
    private Texture mapBackground;

    /**
     * Renders the shape of the level for the display.
     */
    private ShapeRenderer shapeRenderer;

    /**
     * Screen for playing the level.
     */
    private GameScreen gameScreen;

    /**
     * The default constructor for LevelInfoScreen.
     *
     * Creates a downscaled display of the chosen level.
     *
     * @param g the JDCEGame-class. It allows LevelInfoScreen and NewScreen access to the: batch, myBundle,
     *          the game's settings, textures, uiSkin and font48.
     * @param levelNumber tells LevelInfoScreen which level is chosen.
     * @param worldNumber tells LevelInfoScreen which world the level belongs to.
     */
    public LevelInfoScreen(JDCEGame g, int levelNumber, int worldNumber) {
        super(g);

        this.worldNumber = worldNumber;
        this.levelNumber = levelNumber;

        setupButtonBounds();

        playButton = new TextButton(playButtonText, getGame().getUiSkin());
        highScoreButton = new TextButton(highScoreButtonText, getGame().getUiSkin());

        updateTexts();
        setupButtons();

        gameScreen = new GameScreen(getGame(), levelNumber, worldNumber);
        setMapScaler();
        for(int i=0; i<gameScreen.getModules().size();i++){
            gameScreen.getModules().get(i).createMapOutlines(mapScaler,0.1f*getScreenWidth(),0.8f*getScreenHeight());
        }
        mapBackground = new Texture("karttarausta.png");
        shapeRenderer = new ShapeRenderer();
        Gdx.gl.glLineWidth(2);

        getGameStage().addActor(playButton);
        getGameStage().addActor(highScoreButton);
        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(getBackButton());

        Gdx.input.setInputProcessor(getGameStage());

        clickListeners();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        getGame().getLayout48().setText(getGame().getFont48(), levelText + " " + levelNumber);

        levelTextX = playButtonX + getTextButtonWidth() / 2 - (getGame().getLayout48().width / 2);
        levelTextY = getStageHeightTenth() * 3.5f - (getTextButtonHeight() / 2);

        getSpriteBatch().begin();
        getGame().getFont48().draw(getSpriteBatch(), levelText + " " + levelNumber, levelTextX, levelTextY);
        getSpriteBatch().draw(mapBackground,0.01f*getScreenWidth()*PIXELS_TO_METERS,0.2f*getScreenHeight()*PIXELS_TO_METERS,0.7f*getScreenWidth()*PIXELS_TO_METERS,0.6f*getScreenHeight()*PIXELS_TO_METERS);
        getSpriteBatch().end();

        getMeterViewport().apply();

        shapeRenderer.setProjectionMatrix(getMeterViewport().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i=0; i<gameScreen.getModules().size(); i++) {
            gameScreen.getModules().get(i).drawMapOutlines(shapeRenderer);
        }
        shapeRenderer.end();
    }

    @Override
    public void setupButtonBounds() {
        super.setupButtonBounds();

        playButtonX = getMuteMusicX() + getImageButtonWidth() - getTextButtonWidth();
        playButtonY = getStageHeightTenth() * 1 - (getTextButtonHeight() / 2);
        highScoreButtonX = playButtonX - getTextButtonWidth() * 1.25f;
        highScoreButtonY = playButtonY;
    }

    @Override
    public void setupButtons() {
        super.setupButtons();

        playButton.setSize(getTextButtonWidth(), getTextButtonHeight());
        highScoreButton.setSize(getTextButtonWidth(), getTextButtonHeight());

        playButton.setPosition(playButtonX, playButtonY);
        highScoreButton.setPosition(highScoreButtonX, highScoreButtonY);

        playButton.setText(playButtonText);
        highScoreButton.setText(highScoreButtonText);

    }
    @Override
    public void clickListeners() {
        super.clickListeners();

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(gameScreen);
                playButtonSound();
                dispose();
            }
        });

        highScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new HighScoreScreen(getGame(), levelNumber, worldNumber));
                playButtonSound();
                gameScreen.dispose();
                dispose();
            }
        });

        getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), worldNumber));
                playButtonSound();
                gameScreen.dispose();
                dispose();
            }
        });
    }

    @Override
    public void updateTexts() {
        playButtonText = getGame().getBundle().get("play");
        highScoreButtonText = getGame().getBundle().get("highscores");
        levelText = getGame().getBundle().get("level");
    }

    /**
     * Sets up the mapScaler based on the level layout.
     */
    public void setMapScaler(){
        float mapAspectRatio = (gameScreen.getLevelCreator().highest-gameScreen.getLevelCreator().lowest)/gameScreen.getLevelCreator().lastX;
        if(mapAspectRatio<0.33f){
            mapScaler = (getScreenWidth()*0.65f)/ gameScreen.getLevelCreator().lastX;
        }else {
            mapScaler = (getScreenHeight()*0.4f)/ (gameScreen.getLevelCreator().highest-gameScreen.getLevelCreator().lowest);
        }
    }

    @Override
    public void dispose(){
        super.dispose();
        mapBackground.dispose();
    }
}

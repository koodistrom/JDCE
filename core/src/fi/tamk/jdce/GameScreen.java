package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;

/**
 * The game screen where actual playing happens.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class GameScreen extends NewScreen {
    private World world;
    private PolygonSpriteBatch polyBatch;
    private LevelCreator levelCreator;
    private ArrayList<LevelModule> modules;
    private Player player;
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;
    private Boolean paused = false;

    /**
     * The Array of assets to be drawn along side the track
     */
    ArrayList<Asset> assets;
    /**
     * The second array of assets to be drawn along side the track.
     */
    ArrayList<Asset> assets2;
    /**
     * The third array of assets to be drawn along side the track
     */
    ArrayList<Asset> assets3;
    /**
     * The collectables that give the player turboboost to be drawn along the track.
     */
    ArrayList<Collectable> turbos;

    /**
     * The Level number defines which track is instantiated.
     */
    int levelNumber;
    /**
     * The World number defines which theme the track is.
     */
    int worldNumber;
    /**
     * The Background.
     */
    Background background;
    /**
     * The Shape renderer.
     */
    ShapeRenderer shapeRenderer;

    private TextButton continueButton;
    private TextButton retryButton;
    private TextButton mainMenuButton;

    private String continueButtonText;
    private String retryButtonText;
    private String mainMenuButtonText;
    private String pausedText;

    private Label title;
    private Table pauseTable;

    /**
     * The game ending boolean is changed true when game should end.
     */
    boolean endGame;


    /**
     * Instantiates a new Game screen.
     *
     * @param g           the main class
     * @param levelNumber the level number
     * @param worldNumber the world number
     */
    public GameScreen(JDCEGame g, int levelNumber, int worldNumber) {
        super(g);

        this.levelNumber = levelNumber;
        this.worldNumber = worldNumber;
        world = new World(new Vector2(0, -3f),true);
        /*worldWidth = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        worldHeight = Gdx.graphics.getHeight()/PIXELS_TO_METERS;

        batch = new SpriteBatch();*/
        polyBatch = new PolygonSpriteBatch(); // To assign at the beginning

        setupButtonBounds();

        pauseTable = new Table();

        continueButton = new TextButton(continueButtonText, getGame().getUiSkin());
        retryButton = new TextButton(retryButtonText, getGame().getUiSkin());
        mainMenuButton = new TextButton(mainMenuButtonText, getGame().getUiSkin());

        pauseTable.setBackground(new TextureRegionDrawable(new Texture(Gdx.files.internal("valikko_popup.png"))));

        updateTexts();
        setupButtons();

        setUpPauseTable();

        getGameStage().addActor(pauseTable);
        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());

        Gdx.input.setInputProcessor(getGameStage());

        clickListeners();

        getGameStage().addActor(pauseTable);

        levelCreator = new LevelCreator(this);
        assets = new ArrayList<Asset>();

        shapeRenderer = new ShapeRenderer();


        turbos = new ArrayList<Collectable>();
        setTheme(worldNumber);
        modules = levelCreator.createModules( "rata"+levelNumber+".svg");


        endGame=false;
        debugRenderer = new Box2DDebugRenderer();


    }

    /**
     * Sets up pause localHSTable that contains buttons displayed when game is paused.
     */
    public void setUpPauseTable() {
        title = new Label(pausedText, getGame().getUiSkin());
        updateTables();
        pauseTable.defaults().pad(5);
        pauseTable.row();
        pauseTable.add(title).height(getGame().getFontParameter().size).spaceTop(50);
        pauseTable.row();
        pauseTable.add(continueButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceTop(50).spaceBottom(10);
        pauseTable.row();
        pauseTable.add(retryButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(10);
        pauseTable.row();
        pauseTable.add(mainMenuButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(10);
    }

    @Override
    public void updateTables() {
        title.setText(pausedText);
        pauseTable.setSize(getStageWidth() / 2.5f, getStageHeight() - getStageHeightTenth() / 2);
        pauseTable.setPosition(getGameStage().getWidth() / 2 - (pauseTable.getWidth() / 2),
                getGameStage().getHeight() / 2 - (pauseTable.getHeight() / 2));

    }

    @Override
    public void setupButtons() {
        super.setupButtons();

        continueButton.setWidth(getTextButtonWidth());
        continueButton.setHeight(getTextButtonHeight());

        retryButton.setWidth(getTextButtonWidth());
        retryButton.setHeight(getTextButtonHeight());

        mainMenuButton.setWidth(getTextButtonWidth());
        mainMenuButton.setHeight(getTextButtonHeight());

        continueButton.setText(continueButtonText);
        retryButton.setText(retryButtonText);
        mainMenuButton.setText(mainMenuButtonText);

    }

    public void clickListeners() {
        super.clickListeners();

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(player);
                playButtonSound();
                paused = false;
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                playButtonSound();
                reset();
                show();

            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButtonSound();

                getGame().setScreen(new MainMenuScreen(getGame()));
                dispose();
            }
        });
    }

    @Override
    public void updateTexts() {
        continueButtonText = getGame().getBundle().get("continue");
        retryButtonText = getGame().getBundle().get("retry");
        mainMenuButtonText = getGame().getBundle().get("mainmenu");
        pausedText = getGame().getBundle().get("paused");
    }

    @Override
    public void render(float delta) {
        //System.out.println("aika: "+delta);
        if(!paused) {


            world.step(delta, 6, 2);
            moveCamera();

            JDCEGame.m_platformResolver.getPedalSpeed();
            player.update();

            for (int i = 0; i < turbos.size(); i++) {
                turbos.get(i).update();
            }
        }

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            getMeterViewport().apply();

            Gdx.gl.glLineWidth(2);

            shapeRenderer.setProjectionMatrix(getMeterViewport().getCamera().combined);
            polyBatch.setProjectionMatrix(getMeterViewport().getCamera().combined);
            getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

            debugMatrix = getSpriteBatch().getProjectionMatrix();

            getSpriteBatch().begin();

            background.draw();

            for (int i = 0; i < assets.size(); i++) {
                assets.get(i).draw();
            }

            for (int i = 0; i < assets2.size(); i++) {
            assets2.get(i).draw();
            }

            for (int i = 0; i < assets3.size(); i++) {
            assets3.get(i).draw();
            }

            for (int i = 0; i < turbos.size(); i++) {
            turbos.get(i).draw();
            }


            levelCreator.goal.draw();
            //debugRenderer.render(world, debugMatrix);

            getSpriteBatch().end();

            polyBatch.begin();

            for (int i = 0; i < modules.size(); i++) {
                modules.get(i).draw();
            }


            polyBatch.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (int i = 0; i < modules.size(); i++) {
                modules.get(i).drawOutlines();
            }
            shapeRenderer.end();

            getSpriteBatch().begin();

            player.draw(player.currentFrame);


            getSpriteBatch().end();


        if (paused) {

            getGameStage().draw();

        }

        if(endGame){
            getGame().setScreen(new FinishView(getGame(),this, player.trackTime, player.win, levelNumber, worldNumber));
            for (int i = 0; i < turbos.size(); i++) {
                world.destroyBody(turbos.get(i).getBody());
            }
            //dispose();
        }


    }

    @Override
    public void pause(){
        paused = true;
        Gdx.input.setInputProcessor(getGameStage());

    }

    private void moveCamera() {

        getMeterViewport().getCamera().position.set(player.getX()+5f,
                player.getY()+0.7f,
                0);
    }

    /**
     * Prepares the game to be reseted when show() method is called next time.
     */
    public void reset(){
        world.setContactListener(null);
        world.clearForces();
        endGame =false;

        if(levelCreator.collectableAtlas!=null){
            levelCreator.collectableAtlas.dispose();
        }

        for (int i = 0; i < turbos.size(); i++) {
            world.destroyBody(turbos.get(i).getBody());
        }

        player.dispose();

    }

    /**
     * Gets world.
     *
     * @return the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Sets world.
     *
     * @param world the world
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Gets poly batch.
     *
     * @return the poly batch
     */
    public PolygonSpriteBatch getPolyBatch() {
        return polyBatch;
    }

    /**
     * Sets poly batch.
     *
     * @param polyBatch the poly batch
     */
    public void setPolyBatch(PolygonSpriteBatch polyBatch) {
        this.polyBatch = polyBatch;
    }

    /**
     * Gets modules.
     *
     * @return the modules
     */
    public ArrayList<LevelModule> getModules() {
        return modules;
    }

    /**
     * Sets modules.
     *
     * @param modules the modules
     */
    public void setModules(ArrayList<LevelModule> modules) {
        this.modules = modules;
    }

    /**
     * Gets level creator.
     *
     * @return the level creator
     */
    public LevelCreator getLevelCreator() {
        return levelCreator;
    }

    /**
     * Sets level creator.
     *
     * @param levelCreator the level creator
     */
    public void setLevelCreator(LevelCreator levelCreator) {
        this.levelCreator = levelCreator;
    }

    /**
     * Gets player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets player.
     *
     * @param player the player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void dispose(){
        super.dispose();
        background.dispose();
        if(player!=null){
            player.dispose();
        }
        polyBatch.dispose();
        world.dispose();

        levelCreator.dispose();
    }

    @Override
    public void hide() {
        NewScreen.music.stop();
        NewScreen.music.dispose();
        NewScreen.music = null;
        NewScreen.music = Gdx.audio.newMusic(Gdx.files.internal("sound/JDCE_menu_music_v4.mp3"));
        music.setLooping(true);
        if(JDCEGame.musicOn) {
            music.play();
        }

        if(levelCreator.collectableAtlas!=null){
            levelCreator.collectableAtlas.dispose();
        }
    }

    @Override
    public void show() {
        player = new Player(this);
        world.setContactListener(new ContactListenerClass(this));
        paused = false;
        NewScreen.music.stop();
        NewScreen.music.dispose();
        NewScreen.music = null;
        NewScreen.music = Gdx.audio.newMusic(Gdx.files.internal("sound/JDCE_gamesong_v5.mp3"));
        music.setLooping(true);
        if (JDCEGame.musicOn) {
            NewScreen.music.play();
        }

        assets(worldNumber);

        if(levelNumber%2 == 1){
            //turbos = levelCreator.createCollectables(randomFloatArray(2));
        }

        Gdx.input.setInputProcessor(player);
    }

    /**
     * Gets paused.
     *
     * @return the paused
     */
    public boolean getPaused() {
        return paused;
    }

    /**
     * Sets paused.
     *
     * @param b the b
     */
    public void setPaused(boolean b) {
        paused = b;
    }



    /**
     * Create level.
     *
     * @param levelNumber the level number
     * @param worldNumber the world number
     */
    public void createLevel(int levelNumber, int worldNumber){


    }

    /**
     * Set theme.
     *
     * @param themeNum the theme number
     */
    public void setTheme(int themeNum){
        switch (themeNum){
            case 2:
                levelCreator.setTextureAndLine("lumitausta.png",Color.GRAY);
                background = new Background(this,"tausta4taso1.jpg","tausta4taso2.png","tausta4taso3.png",0,0);
                break;

            case 1:
                levelCreator.setTextureAndLine( "aavikkotausta.png",Color.TAN);
                background = new Background(this,"tausta3taso11.jpg","tausta3taso2.png","tausta3taso3.png",0,-50);
                break;

            case 4:
                levelCreator.setTextureAndLine( "tausta.png",Color.BROWN);
                background = new Background(this,"tausta2taso1.jpg","tausta2taso2.png","tausta2taso3.png",0,-50);

                break;

            case 3:
                levelCreator.setTextureAndLine( "looppaavamaa.png",Color.BROWN);
                background = new Background(this,"tausta1taso1.jpg","tausta1taso2.png","tausta1taso3.png",0,0);

                break;

        }
    }

    /**
     * Creates assets according to the theme dictated by world number
     *
     * @param worldNumber the world number
     */
    public void assets(int worldNumber){
        switch (worldNumber){
            case 2:
                assets = levelCreator.createAssets("kuusi3.png",randomFloatArray(39),false,1f);
                assets2 = new ArrayList<Asset>();
                assets3 = new ArrayList<Asset>();

                break;
            case 1:
                assets = levelCreator.createAssets("kaktus.png",randomFloatArray(20),false,1f);
                assets2 = levelCreator.createAssets("kivijakallo.png",randomFloatArray(6),true,0.3f);
                assets3 = levelCreator.createAssets("kaktus2.png",randomFloatArray(15),false,1.3f);

                break;

            case 4:

                assets = levelCreator.createAssets("kiviasetelma1.png",randomFloatArray(7),true,2f);
                assets2 = levelCreator.createAssets("kasvi2.png",randomFloatArray(15),false,0.5f);
                assets3 = levelCreator.createAssets("kivi.png",randomFloatArray(7),true,1f);

                break;
            case 3:
                assets = levelCreator.createAssets("puu2.png",randomFloatArray(15),false,1.2f);
                assets2 = levelCreator.createAssets("kuusi2.png",randomFloatArray(15),false,1.2f);
                assets3 = levelCreator.createAssets("kasvi.png",randomFloatArray(15),false, 0.5f);

                break;
        }
     }

    /**
     * Creates an array of random floats.
     *
     * @param ammount the ammount of floats in the array also normal person might have named this array length..
     * @return the float [ ]
     */
    public float[] randomFloatArray(int ammount){
        float[] array = new float[ammount];
        for (int i=0; i<ammount; i++){
            array[i] = 1f+((float)(Math.random())*98f);
        }
        return array;
     }
}

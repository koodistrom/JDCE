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

public class GameScreen extends NewScreen {
    private World world;
    private PolygonSpriteBatch polyBatch;
    private LevelCreator2 levelCreator;
    private ArrayList<LevelModule> modules;
    private ArrayList<HasBody> collisionCheckModules;
    private Player player;
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;
    private Boolean paused = false;
    Collectable collectable;
    ArrayList<HasBody> rotkos = new ArrayList<HasBody>();
    ArrayList<HasBody> collectables = new ArrayList<HasBody>();
    ArrayList<Asset> assets;
    ArrayList<Asset> assets2;
    ArrayList<Asset> assets3;
    ArrayList<Collectable> turbos;
    Stegosaurus stegosaurus;

    int levelNumber;
    int worldNumber;
    Background background;
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
    private boolean gamePaused = false;

    private static int SNOW = 1;
    private static int DESERT = 2;
    private static int FOREST1 = 3;
    private static int FOREST2 = 4;


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

        levelCreator = new LevelCreator2(this);
        assets = new ArrayList<Asset>();

        shapeRenderer = new ShapeRenderer();

        //selectLevel(levelNumber);
        /*music.dispose();
        music = Gdx.audio.newMusic(Gdx.files.internal("sound/JDCE_gamesong_v5.mp3"));
        music.setLooping(true);
        if(JDCEGame.musicOn){
            music.play();
        }
        */
        turbos = new ArrayList<Collectable>();
        setTheme(worldNumber);
        createLevel(levelNumber,worldNumber);

        /*camera = new OrthographicCamera();
        camera.setToOrtho(false,worldWidth,worldHeight);*/


        player = new Player(this);
        //stegosaurus = new Stegosaurus(this);

        //collectable = new Collectable(this, new Texture("collectable.png"));
        //collectable.setLocationInLevel(20f,levelCreator);

        world.setContactListener(new ContactListenerClass(this));

        debugRenderer = new Box2DDebugRenderer();


    }

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
        System.out.println("aika: "+delta);
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

            //collectable.update();

            levelCreator.goal.draw();
            debugRenderer.render(world, debugMatrix);

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

    public void reset(){
        getGame().setScreen(new GameScreen(getGame(), levelNumber, worldNumber));
        dispose();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public PolygonSpriteBatch getPolyBatch() {
        return polyBatch;
    }

    public void setPolyBatch(PolygonSpriteBatch polyBatch) {
        this.polyBatch = polyBatch;
    }

    public ArrayList<LevelModule> getModules() {
        return modules;
    }

    public void setModules(ArrayList<LevelModule> modules) {
        this.modules = modules;
    }

    public LevelCreator2 getLevelCreator() {
        return levelCreator;
    }

    public void setLevelCreator(LevelCreator2 levelCreator) {
        this.levelCreator = levelCreator;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void dispose(){
        super.dispose();
        background.dispose();
        player.dispose();
        levelCreator.dispose();
    }

    @Override
    public void hide() {
        NewScreen.music.stop();
        NewScreen.music.dispose();
        NewScreen.music = null;
        NewScreen.music = Gdx.audio.newMusic(Gdx.files.internal("sound/JDCE_menu_music_v4.mp3"));
        if(JDCEGame.musicOn) {
            music.play();
        }
    }

    @Override
    public void show() {
        NewScreen.music.stop();
        NewScreen.music.dispose();
        NewScreen.music = null;
        NewScreen.music = Gdx.audio.newMusic(Gdx.files.internal("sound/JDCE_gamesong_v5.mp3"));

        if (JDCEGame.musicOn) {
            NewScreen.music.play();
        }

        Gdx.input.setInputProcessor(player);
    }

    public boolean getPaused() {
        return paused;
    }

    public void setPaused(boolean b) {
        paused = b;
    }

    /*public void selectLevel(int levelNumber){
        switch (levelNumber){
            case 1:

                modules = levelCreator.createModules( "rata25.svg");
                assets = levelCreator.createAssets("kuusi3.png",new float[]{5,10,20,30,40,50,60,70,80,90},false);

                break;
            case 2:

                assets = levelCreator.createAssets("kaktus.png",new float[]{5,4.5f,7,10,11,37,66,55,45,20,30,40,50,60,70,80,90},false);

                break;
            case 3:

                assets = levelCreator.createAssets("puu2.png",new float[]{5,10,20,30,31,42,56,44,56,66,67,65,40,50,60,70,80,90},false);
                break;
            case 4:

                assets = levelCreator.createAssets("kuusi2.png",new float[]{5,10,20,30,40,50,60,70,80,90},false);
                turbos = levelCreator.createCollectables(new float[]{1});
                break;
            case 5:

                assets = levelCreator.createAssets("kuusi2.png",new float[]{5,10,20,30,40,50,60,70,80,90},false);
                break;
            case 6:

                assets = levelCreator.createAssets("kuusi2.png",new float[]{5,10,20,30,40,50,60,70,80,90},false);
                break;
        }
    }*/

    public void createLevel(int levelNumber, int worldNumber){
        modules = levelCreator.createModules( "rata"+levelNumber+".svg");
        assets(worldNumber);
    }

    public void setTheme(int themeNum){
        switch (themeNum){
            case 1:
                levelCreator.setTextureAndLine("lumitausta.png",Color.GRAY);
                background = new Background(this,"tausta4taso1.jpg","tausta4taso2.png","tausta4taso3.png",0,0);
                break;

            case 2:
                levelCreator.setTextureAndLine( "aavikkotausta.png",Color.TAN);
                background = new Background(this,"tausta3taso1.jpg","tausta3taso2.png","tausta3taso3.png",0,-50);
                break;

            case 3:
                levelCreator.setTextureAndLine( "tausta.png",Color.BROWN);
                background = new Background(this,"tausta2taso1.jpg","tausta2taso2.png","tausta2taso3.png",0,-50);

                break;

            case 4:
                levelCreator.setTextureAndLine( "looppaavamaa.png",Color.BROWN);
                background = new Background(this,"tausta1taso1.jpg","tausta1taso2.png","tausta1taso3.png",0,0);

                break;

        }
    }

     public void assets(int worldNumber){
        switch (worldNumber){
            case 1:
                assets = levelCreator.createAssets("kuusi3.png",randomFloatArray(39),false,1f);
                assets2 = new ArrayList<Asset>();
                assets3 = new ArrayList<Asset>();
                break;
            case 2:
                assets = levelCreator.createAssets("kaktus.png",randomFloatArray(20),false,1f);
                assets2 = levelCreator.createAssets("kivijakallo.png",randomFloatArray(6),true,0.3f);
                assets3 = levelCreator.createAssets("kaktus2.png",randomFloatArray(15),false,1.3f);

                break;

            case 3:

                assets = levelCreator.createAssets("kiviasetelma1.png",randomFloatArray(7),true,2f);
                assets2 = levelCreator.createAssets("kasvi2.png",randomFloatArray(15),false,0.5f);
                assets3 = levelCreator.createAssets("kivi.png",randomFloatArray(7),true,1f);

                break;
            case 4:
                assets = levelCreator.createAssets("puu3.png",randomFloatArray(15),false,1f);
                assets2 = levelCreator.createAssets("kuusi2.png",randomFloatArray(15),false,1f);
                assets3 = levelCreator.createAssets("kasvi.png",randomFloatArray(15),false, 1f);
                break;
        }
     }

     public float[] randomFloatArray(int ammount){
        float[] array = new float[ammount];
        for (int i=0; i<ammount; i++){
            array[i] = 1f+((float)(Math.random())*98f);
        }
        return array;
     }
}

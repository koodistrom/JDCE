package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;

import static com.mygdx.game.JDCEGame.m_platformResolver;

public class GameScreen extends NewScreen {
    private World world;
    private PolygonSpriteBatch polyBatch;
    private LevelCreator2 levelCreator;
    private ArrayList<LevelModule> modules;
    private ArrayList<HasBody> collisionCheckModules;
    private Player player;
    private Box2DDebugRenderer debugRenderer;
    private BitmapFont font;
    private Matrix4 debugMatrix;
    Collectable collectable;
    ArrayList<HasBody> rotkos = new ArrayList<HasBody>();
    ArrayList<HasBody> collectables = new ArrayList<HasBody>();
    ArrayList<Asset> assets;
    Stegosaurus stegosaurus;
    Sprite sprite;
    int levelNum;
    int worldNumber;
    Background background;
    ShapeRenderer shapeRenderer;

    private Table pauseTable;
    private boolean gamePaused = false;


    public GameScreen(JDCEGame g, int levelnum, int worldNumber) {
        super(g);
        this.levelNum = levelnum;
        this.worldNumber = worldNumber;
        world = new World(new Vector2(0, -3f),true);
        /*worldWidth = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        worldHeight = Gdx.graphics.getHeight()/PIXELS_TO_METERS;

        batch = new SpriteBatch();*/
        polyBatch = new PolygonSpriteBatch(); // To assign at the beginning

        pauseTable = new Table();
        pauseTable.setBackground(new TextureRegionDrawable(new Texture(Gdx.files.internal("orangebackground.png"))));

        getGameStage().addActor(pauseTable);

        levelCreator = new LevelCreator2(this);
        assets = new ArrayList<Asset>();
        shapeRenderer = new ShapeRenderer();

        music.dispose();
        music = Gdx.audio.newMusic(Gdx.files.internal("sound/JDCE_gamesong_v5.mp3"));
        music.setLooping(true);
        if(JDCEGame.musicOn){
            music.play();
        }


        switch (levelnum){
            case 1:
                modules = levelCreator.createModules( "rata2.svg","lumitausta.png",Color.GRAY);
                assets = levelCreator.createAssets("kuusi3.png",new float[]{5,10,20,30,40,50,60,70,80,90});
                background = new Background(this,"tausta4taso1.jpg","tausta4taso2.png","tausta4taso3.png");
                break;
            case 2:
                modules = levelCreator.createModules( "test15.svg","aavikkotausta.png",Color.TAN);
                assets = levelCreator.createAssets("kaktus.png",new float[]{5,4.5f,7,10,11,37,66,55,45,20,30,40,50,60,70,80,90});
                background = new Background(this,"tausta3taso1.jpg","tausta3taso2.png","tausta3taso3.png");
                break;
            case 3:
                modules = levelCreator.createModules( "rata8.svg","tausta.png",Color.BROWN);
                background = new Background(this,"tausta2taso1.jpg","tausta2taso2.png","tausta2taso3.png");
                assets = levelCreator.createAssets("puu2.png",new float[]{5,10,20,30,40,50,60,70,80,90});
                break;
            case 4:
                modules = levelCreator.createModules( "rata4.svg","looppaavamaa.png",Color.BROWN);
                background = new Background(this,"tausta4taso1.jpg","tausta1taso2.png","tausta1taso3.png");
                assets = levelCreator.createAssets("kuusi2.png",new float[]{5,10,20,30,40,50,60,70,80,90});
                break;
            case 5:
                modules = levelCreator.createModules( "rata5.svg","lumitausta.png", Color.BLUE);
                background = new Background(this,"tausta4taso1.jpg","tausta1taso2.png","tausta1taso3.png");
                assets = levelCreator.createAssets("kuusi2.png",new float[]{5,10,20,30,40,50,60,70,80,90});
                break;
            case 6:
                modules = levelCreator.createModules( "rata6.svg","lumitausta.png", Color.BLUE);
                background = new Background(this,"tausta4taso1.jpg","tausta1taso2.png","tausta1taso3.png");
                assets = levelCreator.createAssets("kuusi2.png",new float[]{5,10,20,30,40,50,60,70,80,90});
                break;
        }





        /*camera = new OrthographicCamera();
        camera.setToOrtho(false,worldWidth,worldHeight);*/


        player = new Player(this);
        //stegosaurus = new Stegosaurus(this);

        //collectable = new Collectable(this, new Texture("collectable.png"));
        //collectable.setLocationInLevel(20f,levelCreator);

        world.setContactListener(new ContactListenerClass(this));

        debugRenderer = new Box2DDebugRenderer();
        font = new BitmapFont();
        font.setColor(Color.BLACK);

    }

    @Override
    public void render(float delta) {
        //getMeterViewport().getCamera().update();
        // Step the physics simulation forward at a rate of 60hz
        world.step(1/60f, 6, 2);

        moveCamera();
        m_platformResolver.getPedalSpeed();
        player.update();

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

        for(int i=0; i<assets.size(); i++) {
            assets.get(i).draw();
        }

        //collectable.update();

        levelCreator.goal.draw();
        debugRenderer.render(world, debugMatrix);

        getSpriteBatch().end();

        polyBatch.begin();

        for(int i=0; i<modules.size(); i++) {
            modules.get(i).draw();
        }

        polyBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i=0; i<modules.size(); i++) {
            modules.get(i).drawOutlines();
        }
        shapeRenderer.end();

        getSpriteBatch().begin();

        player. draw(player.currentFrame);


        getSpriteBatch().end();

        getGameStage().draw();


    }

    private void moveCamera() {

        getMeterViewport().getCamera().position.set(player.getX()+5f,
                player.getY()+0.7f,
                0);
    }

    public void reset(){
        getGame().setScreen(new GameScreen(getGame(), levelNum, worldNumber));
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
}

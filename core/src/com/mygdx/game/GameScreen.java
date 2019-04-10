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
    Background background;
    Texture spruce;
    private Table pauseTable;
    private boolean gamePaused = false;


    public GameScreen(JDCEGame g, int levelnum) {
        super(g);
        this.levelNum = levelnum;
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
        spruce = new Texture("kuusi2.png");

        switch (levelnum){
            case 1:
                modules = levelCreator.createModules( "rata1.svg");
                break;
            case 2:
                modules = levelCreator.createModules( "rata2.svg");
                //assets = levelCreator.createAssets(spruce,new float[]{10,21});
                break;
            case 3:
                modules = levelCreator.createModules( "rata3.svg");
                break;
            case 4:
                modules = levelCreator.createModules( "rata4.svg");

                break;
            case 5:
                modules = levelCreator.createModules( "rata5.svg");

                break;
            case 6:
                modules = levelCreator.createModules( "rata6.svg");
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
        background = new Background(this,"tausta4taso1.jpg","tausta4taso22.png","tausta4taso3.png");
    }

    @Override
    public void render(float delta) {
        //getMeterViewport().getCamera().update();
        // Step the physics simulation forward at a rate of 60hz
        world.step(1/60f, 6, 2);


        moveCamera();
        m_platformResolver.getPedalSpeed();
        player.update();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getMeterViewport().apply();

        polyBatch.setProjectionMatrix(getMeterViewport().getCamera().combined);
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

        debugMatrix = getSpriteBatch().getProjectionMatrix();

        getSpriteBatch().begin();
        background.draw();

        for(int i=0; i<assets.size(); i++) {
            assets.get(i).draw();
        }

        player. draw();
        //collectable.update();




        levelCreator.goal.draw();

        getSpriteBatch().end();

        polyBatch.begin();

        for(int i=0; i<modules.size(); i++) {
            modules.get(i).draw();
        }


        polyBatch.end();

        getGameStage().draw();

        debugRenderer.render(world, debugMatrix);
    }

    private void moveCamera() {

        getMeterViewport().getCamera().position.set(player.getX()+5f,
                player.getY()+0.7f,
                0);
    }

    public void reset(){
        getGame().setScreen(new GameScreen(getGame(),levelNum));
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

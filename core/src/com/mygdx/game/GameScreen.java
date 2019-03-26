package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import static com.mygdx.game.JDCEGame.m_platformResolver;

public class GameScreen extends NewScreen {
    private World world;
    private PolygonSpriteBatch polyBatch;
    private LevelCreator levelCreator;
    private LevelModule[] modules;
    private Player player;
    private Box2DDebugRenderer debugRenderer;
    private BitmapFont font;
    private Matrix4 debugMatrix;
    Collectable collectable;
    ArrayList<HasBody> rotkos = new ArrayList<HasBody>();
    ArrayList<HasBody> collectables = new ArrayList<HasBody>();
    Stegosaurus stegosaurus;
    Sprite sprite;

    public GameScreen(JDCEGame g) {
        super(g);

        world = new World(new Vector2(0, -3f),true);
        /*worldWidth = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        worldHeight = Gdx.graphics.getHeight()/PIXELS_TO_METERS;

        batch = new SpriteBatch();*/
        polyBatch = new PolygonSpriteBatch(); // To assign at the beginning

        levelCreator = new LevelCreator(this);

        //levelCreator.createLevel(world, "test3.SVG");
        //levelCreator.createTexture(this,"test3.SVG");

        modules = levelCreator.createModules( new String[] {"test2.svg"}, new float[]{1});






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
        getCamera().update();
        // Step the physics simulation forward at a rate of 60hz
        world.step(1f/60f, 6, 2);


        moveCamera();
        m_platformResolver.getPedalSpeed();
        player.update();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        polyBatch.setProjectionMatrix(getCamera().combined);
        getSpriteBatch().setProjectionMatrix(getCamera().combined);

        debugMatrix = getSpriteBatch().getProjectionMatrix();
        getSpriteBatch().begin();

        player. draw();
        //collectable.update();

        levelCreator.goal.draw();

        getSpriteBatch().end();

        polyBatch.begin();

        //polySprite.draw(polyBatch);
        //polyBatch.draw(levelCreator.polyReg, 0,0,levelCreator.polySprite.getWidth()/PIXELS_TO_METERS, levelCreator.polySprite.getHeight()/PIXELS_TO_METERS);
        for(int i=0; i<modules.length; i++){
            modules[i].draw();
        }
        polyBatch.end();

        debugRenderer.render(world, debugMatrix);
    }

    private void moveCamera() {

        getCamera().position.set(player.getX()+2.5f,
                player.getY()+1.5f,
                0);
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

    public LevelModule[] getModules() {
        return modules;
    }

    public void setModules(LevelModule[] modules) {
        this.modules = modules;
    }

    public LevelCreator getLevelCreator() {
        return levelCreator;
    }

    public void setLevelCreator(LevelCreator levelCreator) {
        this.levelCreator = levelCreator;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}

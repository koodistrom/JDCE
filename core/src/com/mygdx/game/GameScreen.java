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
    private LevelCreator2 levelCreator;
    private ArrayList<LevelModule> modules;
    private Player player;
    private Box2DDebugRenderer debugRenderer;
    private BitmapFont font;
    private Matrix4 debugMatrix;
    Collectable collectable;
    ArrayList<HasBody> rotkos = new ArrayList<HasBody>();
    ArrayList<HasBody> collectables = new ArrayList<HasBody>();
    Stegosaurus stegosaurus;
    Sprite sprite;
    int levelNum;

    public GameScreen(JDCEGame g, int levelnum) {
        super(g);
        this.levelNum = levelnum;
        world = new World(new Vector2(0, -3f),true);
        /*worldWidth = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        worldHeight = Gdx.graphics.getHeight()/PIXELS_TO_METERS;

        batch = new SpriteBatch();*/
        polyBatch = new PolygonSpriteBatch(); // To assign at the beginning

        levelCreator = new LevelCreator2(this);

        //levelCreator.createLevel(world, "test3.SVG");
        //levelCreator.createTexture(this,"test3.SVG");

        switch (levelnum){
            case 1:
                modules = levelCreator.createModules( "test6.svg");
                break;
            case 2:
                modules = levelCreator.createModules( "test7.svg");
                break;
            case 3:
                modules = levelCreator.createModules( "test8.svg");
                break;
            case 4:
                modules = levelCreator.createModules( "test10.svg");
                break;
            case 5:
                modules = levelCreator.createModules( "test11.svg");
                break;
            case 6:
                modules = levelCreator.createModules( "test12.svg");
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
        world.step(1f/60f, 6, 2);


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

        player. draw();
        //collectable.update();

        levelCreator.goal.draw();

        getSpriteBatch().end();

        polyBatch.begin();

        System.out.println(Gdx.graphics.getDeltaTime());
        for(int i=0; i<modules.size(); i++){
            modules.get(i).draw();
        }
        polyBatch.end();

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

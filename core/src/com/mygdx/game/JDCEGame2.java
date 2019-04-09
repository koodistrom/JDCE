package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ShortArray;

import java.util.ArrayList;

public class JDCEGame2 extends ApplicationAdapter {
	/*SpriteBatch batch;
	Sprite sprite;

    PolygonSpriteBatch polyBatch;

	World world;
	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	OrthographicCamera camera;
	BitmapFont font;

	Texture texture;
	TextureRegion textureRegion;

    protected static PlatformResolver m_platformResolver = null;
    Player player;
    Stegosaurus stegosaurus;
    LevelCreator levelCreator;
    LevelModule[] modules;
    Collectable collectable;
    ArrayList<HasBody> rotkos = new ArrayList<HasBody>();
    ArrayList<HasBody> collectables = new ArrayList<HasBody>();
	final float PIXELS_TO_METERS = 100f;
    Float worldWidth;
    Float worldHeight;

	
	@Override
	public void create () {

        world = new World(new Vector2(0, -3f),true);
        worldWidth = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        worldHeight = Gdx.graphics.getHeight()/PIXELS_TO_METERS;

        batch = new SpriteBatch();
        polyBatch = new PolygonSpriteBatch(); // To assign at the beginning

        levelCreator = new LevelCreator(this);

        //levelCreator.createLevel(world, "test3.SVG");
        //levelCreator.createPolygonRegion(this,"test3.SVG");

        modules = levelCreator.createModules( new String[] {"test2.svg"}, new float[]{1});






        camera = new OrthographicCamera();
        camera.setToOrtho(false,worldWidth,worldHeight);


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
	public void render () {
		camera.update();
		// Step the physics simulation forward at a rate of 60hz
		world.step(1f/60f, 6, 2);


        moveCamera();
		m_platformResolver.getPedalSpeed();
		player.update();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        polyBatch.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);

		debugMatrix = batch.getProjectionMatrix();
		batch.begin();

        player. draw();
        //collectable.update();

        levelCreator.goal.draw();

		batch.end();

        polyBatch.begin();

        //polySprite.draw(polyBatch);
        //polyBatch.draw(levelCreator.polyReg, 0,0,levelCreator.polySprite.getWidth()/PIXELS_TO_METERS, levelCreator.polySprite.getHeight()/PIXELS_TO_METERS);
        for(int i=0; i<modules.length; i++){
            modules[i].draw();
        }
        polyBatch.end();

		debugRenderer.render(world, debugMatrix);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
	}



    public static PlatformResolver getPlatformResolver() {
        return m_platformResolver;
    }

    public static void setPlatformResolver(PlatformResolver platformResolver) {
        m_platformResolver = platformResolver;
    }




    private void moveCamera() {

        camera.position.set(player.getX()+2.5f,
                player.getY()+1.5f,
                0);
    }




*/
}

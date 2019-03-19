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

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ShortArray;

public class JDCEGame extends ApplicationAdapter {
	SpriteBatch batch;
	Sprite sprite;

    PolygonSpriteBatch polyBatch;
    PolygonSprite polySprite;

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
    PolygonRegion polyReg;

	final float PIXELS_TO_METERS = 100f;
    Float worldWidth;
    Float worldHeight;
	
	@Override
	public void create () {
	    texture = new Texture("dirt.jpg");
        texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
	    textureRegion = new TextureRegion(texture);
        //textureRegion.setRegion(0,0,texture.getWidth()*100,texture.getHeight()*100);

        world = new World(new Vector2(0, -3f),true);
        levelCreator = new LevelCreator();
        levelCreator.createLevel(world);

		batch = new SpriteBatch();
        polyBatch = new PolygonSpriteBatch(); // To assign at the beginning

        float[] vertices = levelCreator.createFromSVG("test2.svg");
        for(int i=0; i<vertices.length;i++){
            vertices[i] = vertices[i]*PIXELS_TO_METERS;
        }

        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray triangleIndices = triangulator.computeTriangles(vertices);

        polyReg = new PolygonRegion(textureRegion, vertices, triangleIndices.toArray());

        polySprite = new PolygonSprite(polyReg);

        //polySprite.scale(1/PIXELS_TO_METERS);

        worldWidth = Gdx.graphics.getWidth()/PIXELS_TO_METERS;
        worldHeight = Gdx.graphics.getHeight()/PIXELS_TO_METERS;

        camera = new OrthographicCamera();
        camera.setToOrtho(false,worldWidth,worldHeight);


        player = new Player(this);
        //stegosaurus = new Stegosaurus(this);



        world.setContactListener(new ContactListenerClass());

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

		batch.end();

        polyBatch.begin();

        //polySprite.draw(polyBatch);
        polyBatch.draw(polyReg, 0,0,polySprite.getWidth()/PIXELS_TO_METERS, polySprite.getHeight()/PIXELS_TO_METERS);
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





}

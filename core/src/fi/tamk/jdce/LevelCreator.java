package fi.tamk.jdce;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ShortArray;

import java.util.ArrayList;

import svg.parser.ExtractSVGPaths;

/**
 * LevelCreator creates objects used by the game.
 *
 * It calculates vertices from svg files for levelmodules using svg classes from the svg package. classes in the svg package are created by Martin Davis and modified a little for the purposes of this game.
 * LevelCreator also creates assets and collectables.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class LevelCreator {


    /**
     * The polygon region used in the texture of the ground.
     */
    PolygonRegion polyReg;
    /**
     * The Poly sprite.
     */
    PolygonSprite polySprite;
    /**
     * The Goal drawn in the end of the track.
     */
    GameObject goal;
    /**
     * The game screen.
     */
    GameScreen game;
    /**
     * The All vertices.
     */
    ArrayList<Vector2> allVertices;
    /**
     * The  atlas used for collectable animations.
     */
    TextureAtlas collectableAtlas;
    /**
     * The Collectable animation.
     */
    Animation<TextureRegion> collectableAnimation;
    /**
     * The Texture used for ground polygon regions.
     */
    Texture texture;
    /**
     * The Line color.
     */
    Color lineColor;

    /**
     * The Lowest point in a level.
     */
    float lowest;
    /**
     * The Highest point in a level.
     */
    float highest;

    /**
     * The First x coordinate in a level.
     */
    float firstX;
    /**
     * The First y coordinate in a level.
     */
    float firstY;
    /**
     * The Last x the right most x coordinate in a level.
     */
    float lastX;
    /**
     * The Last y the right most y coordinate in a level.
     */
    float lastY;


    /**
     * Instantiates a new Level creator.
     *
     * @param game the game
     */
    public LevelCreator(GameScreen game){

        this.game = game;
        allVertices = new ArrayList<Vector2>();
        goal = new GameObject(game);
        goal.setTexture(new Texture("finish.png"));


    }

    /**
     * Create body for level module.
     *
     * @param points   the points through which the bodyshape goes
     * @param world    the world
     * @param x        the x location
     * @param y        the y location
     * @return the body
     */
    public Body createBody(Vector2[] points, World world, float x, float y){
        Body bodyGround;
        String userData = "levelModule";

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();

        ChainShape chainShape = new ChainShape();
        chainShape.createLoop(points);

        fixtureDef.shape = chainShape;
        fixtureDef.friction = 1.0f;
        bodyDef.position.set(x,y);
        bodyGround = world.createBody(bodyDef);

        bodyGround.createFixture(fixtureDef);

        chainShape.dispose();
        return bodyGround;
    }


    /**
     * Create modules array list for all the levelmodules for a level.
     *
     * Each part of ground separated by gaps is as it's own path in a svg file.
     * This method creates box2D bodies and textures for those modules and puts this information in level modules hold by the returned array list
     *
     * @param fileName the svg file name from which the modules are created
     * @return the array list of levelmodules
     */
    public ArrayList<LevelModule> createModules (String fileName){

        ArrayList<ArrayList<Vector2>> paths = ExtractSVGPaths.extract("levels/"+fileName);

        firstX = paths.get(0).get(0).x;
        firstY = paths.get(0).get(0).y;
        lastX = 0;
        lastY = 0;
        lowest = 0;
        highest = 0;




        ArrayList<LevelModule> modules;
        modules = new ArrayList<LevelModule>();

        for(int i=0; i<paths.size(); i++){

            modules.add( new LevelModule());
            Vector2[] points =new Vector2[paths.get(i).size()];

            for(int n=0; n<paths.get(i).size();n++){

                points[n]=paths.get(i).get(n);

                if(paths.get(i).get(n).y<lowest){
                    lowest = paths.get(i).get(n).y;
                }
                if(paths.get(i).get(n).y>highest){
                    highest = paths.get(i).get(n).y;
                }
                if(paths.get(i).get(n).x>lastX){
                    lastX = paths.get(i).get(n).x;
                    lastY = paths.get(i).get(n).y;
                }

                if(n<paths.get(i).size()-2){
                    allVertices.add(paths.get(i).get(n));
                }
            }


            modules.get(i).setBody(createBody(points, game.getWorld(), 0, 0));
            modules.get(i).getBody().setUserData(modules.get(i));
            modules.get(i).setPolygonRegion(createPolygonRegion(game, points, texture));
            modules.get(i).setHeight(polySprite.getHeight()/game.PIXELS_TO_METERS);
            modules.get(i).setLength(polySprite.getWidth()/game.PIXELS_TO_METERS);
            modules.get(i).setOutlines(createOutlines(points));


            modules.get(i).setX(0);
            modules.get(i).setY(0);
            modules.get(i).setVectors(paths.get(i));
            modules.get(i).setLineColor(lineColor);
            modules.get(i).setGame(game);

        }

        goal.setX(lastX);
        goal.setY(lastY);

        return modules;
    }

    /**
     * Create polygon region for a level module.
     *
     * @param game    the game
     * @param vectors the vectors
     * @param texture the texture
     * @return the polygon region
     */
    public PolygonRegion createPolygonRegion(GameScreen game, Vector2[] vectors, Texture texture){

        texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(texture);

        PolygonRegion polygonRegion;
        float[] vertices= new float[vectors.length*2];
        for(int i=0; i<vectors.length;i++){
            vertices[i*2]= vectors[i].x;
            vertices[i*2+1]= vectors[i].y;
        }
        for(int i=0; i<vertices.length;i++){
            vertices[i] = vertices[i]*game.PIXELS_TO_METERS;
        }

        EarClippingTriangulator triangulator = new EarClippingTriangulator();
        ShortArray triangleIndices = triangulator.computeTriangles(vertices);

        polygonRegion = new PolygonRegion(textureRegion, vertices, triangleIndices.toArray());
        polyReg = polygonRegion;

        polySprite = new PolygonSprite(polyReg);


        return polygonRegion;

    }

    /**
     * Create outlines float [ ] for a level module to be used with shaperenderer to create outlines for the module.
     *
     * @param vectors the Vector2 array from which the coordinates are read.
     * @return the float array containing modules outline coordinates in x,y,x,y... order
     */
    public float[] createOutlines(Vector2[] vectors){
        float[] vertices= new float[vectors.length*2];
        for(int i=0; i<vectors.length;i++){
            vertices[i*2]= vectors[i].x;
            vertices[i*2+1]= vectors[i].y;
            //System.out.println(vertices[i*2]);
        }
        return vertices;
    }


    /**
     * Create assets array list. Assets are drawn along the track.
     *
     * @param fileName the file name of the texture to be drawn
     * @param xs       the x locations where assets are to be placed as persentages of the full track width
     * @param rotate   the boolean that defines if asset should be rotated to match track angle
     * @param scale    the scale of the asset
     * @return the array list of assets for the game screen
     */
    public ArrayList<Asset> createAssets(String fileName, float[] xs, boolean rotate,float scale){
        ArrayList<Asset> assets = new ArrayList<Asset>();
        Texture texture = new Texture("trees/"+fileName);
        for(int i = 0; i<xs.length; i++){
            float x =scale*(0.4f+(float)(Math.random()*0.2f));
           float y= scale*(0.4f+(float)(Math.random()*0.2f));
            assets.add(new Asset(game, xs[i], texture,x,y,rotate));

        }
        return assets;
    }

    /**
     * Create collectables array list.
     *
     * @param xs the x locations where collectables are to be placed as persentages of the full track width
     * @return the array list of collectables for game screen
     */
    public ArrayList<Collectable> createCollectables(float[] xs){
        ArrayList<Collectable> collectables = new ArrayList<Collectable>();
        collectableAtlas = new TextureAtlas("animations/collectable.atlas");
        collectableAnimation = new Animation<TextureRegion>(0.05f, collectableAtlas.findRegions("collectable"), Animation.PlayMode.LOOP);
        System.out.println("animaatio: "+ collectableAnimation.getKeyFrames().length);
        for(int i = 0; i<xs.length; i++){
            float x =(0.4f+(float)(Math.random()*0.2f));
            float y= (0.4f+(float)(Math.random()*0.2f));
            collectables.add(new Collectable(game,collectableAnimation, xs[i],this));

        }
        return collectables;
    }

    /**
     * Gets texture.
     *
     * @return the texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Sets texture and line.
     *
     * @param textureFileName the texture file name
     * @param lineColor       the line color
     */
    public void setTextureAndLine(String textureFileName, Color lineColor) {
        this.texture= new Texture("earth/"+textureFileName);
        this.lineColor = lineColor;
    }

    /**
     * Dispose.
     */
    public void dispose(){
        goal.dispose();
        texture.dispose();
        if(collectableAtlas!=null) {
            collectableAtlas.dispose();
        }

    }


}

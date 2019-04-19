package fi.tamk.jdce;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
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

public class LevelCreator2 {


    PolygonRegion polyReg;
    PolygonSprite polySprite;
    PolygonSprite polygonSprites[];
    PolygonRegion polyRegs[];
    GameObject goal;
    GameScreen game;
    ArrayList<Vector2> allVertices;

    float lowest;
    float highest;
    float bottom;

    float firstX;
    float firstY;
    float lastX;
    float lastY;

    int verticeIndex = 0;

    public LevelCreator2(GameScreen game){

        this.game = game;
        allVertices = new ArrayList<Vector2>();
        goal = new GameObject(game);
        goal.setTexture(new Texture("finish.png"));

        //textureRegion.setRegion(0,0,texture.getWidth()*100,texture.getHeight()*100);
    }

    public Body createBody(Vector2[] points, World world, float x, float y, boolean isSensor){
        Body bodyGround;
        String userData = "levelModule";

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();

        ChainShape chainShape = new ChainShape();
        chainShape.createLoop(points);

        fixtureDef.shape = chainShape;
        fixtureDef.friction = 1.0f;
        fixtureDef.isSensor = isSensor;
        bodyDef.position.set(x,y);
        bodyGround = world.createBody(bodyDef);
        bodyGround.setUserData(userData);
        bodyGround.createFixture(fixtureDef);

        chainShape.dispose();
        return bodyGround;
    }





    public ArrayList<LevelModule> createModules (String fileName, String textureFileName, Color lineColor){

        ArrayList<ArrayList<Vector2>> paths = ExtractSVGPaths.extract("levels/"+fileName);
        Texture texture = new Texture("earth/"+textureFileName);
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


            modules.get(i).setBody(createBody(points, game.getWorld(), 0, 0,false));
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
        /*LevelModule rotko = new LevelModule();
        Vector2[] rotkoPoints = new Vector2[]{new Vector2(firstX-5, lowest+5), new Vector2(lastX+5,lowest+5),new Vector2(lastX+5,lowest),new Vector2(firstX-5,lowest)};
        rotko.setBody(createBody(rotkoPoints, game.getWorld(), 0, 0,true));
        rotko.outlines = createOutlines(rotkoPoints);
        game.rotkos.add(rotko);
        rotko.setPolygonRegion(createPolygonRegion(game, rotkoPoints, "spikes.png"));

        rotko.setHeight(polySprite.getHeight()/game.PIXELS_TO_METERS);
        rotko.setLength(polySprite.getWidth()/game.PIXELS_TO_METERS);
        rotko.setX(0);
        rotko.setY(0);
        rotko.setGame(game);*/


        goal.setX(lastX);
        goal.setY(lastY);

        return modules;
    }

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

    public float[] createOutlines(Vector2[] vectors){
        float[] vertices= new float[vectors.length*2];
        for(int i=0; i<vectors.length;i++){
            vertices[i*2]= vectors[i].x;
            vertices[i*2+1]= vectors[i].y;
            //System.out.println(vertices[i*2]);
        }
        return vertices;
    }



    public float[] scalePoints(float[] points, float scaler){
        for(int i=1; i<points.length;i+=2){
            points[i]*=scaler;
        }
        return points;
    }

    public ArrayList<Asset> createAssets(String fileName, float[] xs){
        ArrayList<Asset> assets = new ArrayList<Asset>();
        Texture texture = new Texture("trees/"+fileName);
        for(int i = 0; i<xs.length; i++){
            float x =(0.4f+(float)(Math.random()*0.2f));
           float y= (0.4f+(float)(Math.random()*0.2f));
            assets.add(new Asset(game, xs[i], texture,x,y));

        }
        return assets;
    }


}

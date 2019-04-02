package com.mygdx.game;

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

    Texture dirtTexture;
    TextureRegion dirt;
    Texture spikeTexture;
    TextureRegion spikes;
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

    int verticeIndex = 0;

    public LevelCreator2(GameScreen game){

        this.game = game;
        allVertices = new ArrayList<Vector2>();
        goal = new GameObject(game);
        goal.setTexture(new Texture("finish.png"));
        dirtTexture = new Texture("dirt.jpg");
        dirtTexture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        dirt = new TextureRegion(dirtTexture);

        spikeTexture = new Texture("spikes.png");
        spikeTexture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        spikes = new TextureRegion(spikeTexture);


        //textureRegion.setRegion(0,0,texture.getWidth()*100,texture.getHeight()*100);
    }

    public Body createBody(Vector2[] points, World world, float x, float y, boolean isSensor){
        Body bodyGround;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();

        ChainShape chainShape = new ChainShape();
        chainShape.createLoop(points);

        fixtureDef.shape = chainShape;
        fixtureDef.friction = 1.5f;
        fixtureDef.isSensor = isSensor;
        bodyDef.position.set(x,y);
        bodyGround = world.createBody(bodyDef);
        bodyGround.createFixture(fixtureDef);

        chainShape.dispose();
        return bodyGround;
    }

    public float[] createFromSVG (String filePath){

        float[] vertices;
        lowest = 0;
        highest = 0;

        ArrayList<Vector2> verticeArray;

        verticeArray = ExtractSVGPaths.extract(filePath).get(0);
        vertices = new float[verticeArray.size()*2+4];
        for(int i = 0; i <verticeArray.size(); i++){

            vertices[i*2] = verticeArray.get(i).x;
            vertices[i*2+1] = verticeArray.get(i).y;
            if(verticeArray.get(i).y<lowest){
                lowest = verticeArray.get(i).y;
            }
            if(verticeArray.get(i).y>highest){
                highest = verticeArray.get(i).y;
            }
        }
        bottom = lowest-3;
        vertices[vertices.length-4] = vertices[vertices.length-6];
        vertices[vertices.length-3] = bottom;
        vertices[vertices.length-2] = vertices[0];
        vertices[vertices.length-1] = bottom;
        return vertices;
    }



    public ArrayList<LevelModule> createModules ( String SVG){

        ArrayList<ArrayList<Vector2>> paths = ExtractSVGPaths.extract(SVG);
        System.out.println("vektoreita: "+paths.size());
        float firstX = paths.get(0).get(0).x;
        float firstY = paths.get(0).get(0).y;
        float lastX = 0;
        float lastY = 0;
        lowest = 0;
        highest = 0;



        ArrayList<LevelModule> modules;
        modules = new ArrayList<LevelModule>();

        for(int i=0; i<paths.size(); i++){

            modules.add( new LevelModule());
            Vector2[] points =new Vector2[paths.get(i).size()];


            for(int n=0; n<paths.get(i).size();n++){

                points[n]=paths.get(i).get(n);
                System.out.println("Vektoreihin listätty " +points[n]);
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
            }


            modules.get(i).setBody(createBody(points, game.getWorld(), paths.get(i).get(0).x, paths.get(i).get(0).y,false));
            modules.get(i).setPolygonRegion(createTexture(game, points, dirt));
            modules.get(i).setHeight(polySprite.getHeight()/game.PIXELS_TO_METERS);
            modules.get(i).setLength(polySprite.getWidth()/game.PIXELS_TO_METERS);
            modules.get(i).setX(paths.get(i).get(0).x);
            modules.get(i).setY(paths.get(i).get(0).y);
            modules.get(i).setGame(game);

        }
        LevelModule rotko = new LevelModule();
        Vector2[] rotkoPoints = new Vector2[]{new Vector2(firstX-5, lowest+5), new Vector2(lastX+5,lowest+5),new Vector2(lastX+5,lowest),new Vector2(firstX-5,lowest)};
        rotko.setBody(createBody(rotkoPoints, game.getWorld(), rotkoPoints[0].x, rotkoPoints[0].y,true));
        game.rotkos.add(rotko);
        rotko.setPolygonRegion(createTexture(game, rotkoPoints, spikes));

        rotko.setHeight(polySprite.getHeight()/game.PIXELS_TO_METERS);
        rotko.setLength(polySprite.getWidth()/game.PIXELS_TO_METERS);
        rotko.setX(rotkoPoints[0].x);
        rotko.setY(rotkoPoints[0].y);
        rotko.setGame(game);
        modules.add(rotko);

        goal.setX(lastX);
        goal.setY(lastY);

        return modules;
    }

    public PolygonRegion createTexture(GameScreen game, Vector2[] vectors, TextureRegion textureRegion){
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

    public float[] scalePoints(float[] points, float scaler){
        for(int i=1; i<points.length;i+=2){
            points[i]*=scaler;
        }
        return points;
    }
}

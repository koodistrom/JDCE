package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ShortArray;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import svg.parser.ExtractSVGPaths;

//luokka muunneltu netistä kopioidustaluokasta: https://www.stkent.com/2015/07/03/building-smooth-paths-using-bezier-curves.html
public class LevelCreator {

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
    /**
     * Computes a Poly-Bezier curve passing through a given list of knots.
     * The curve will be twice-differentiable everywhere and satisfy natural
     * boundary conditions at both ends.
     *
     * @return      a Path representing the twice-differentiable curve
     *              passing through all the given knots
     */
    public LevelCreator(GameScreen game){

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

    public float[] createVertices(){
        return computePathThroughKnots(createKnots(100),9);
    }
    public float[] computePathThroughKnots(List<EpointF> knots, int resolution) {
        throwExceptionIfInputIsInvalid(knots);

        /*
         * variable representing the number of Bezier curves we will join
         * together
         */
        final int n = knots.size() - 1;
        final float[] vertices = new float[n*resolution*2];
        final EpointF firstKnot = knots.get(0);




        final EpointF[] controlPoints = computeControlPoints(n, knots);

        for (int i = 0; i < n; i++) {
            final EpointF targetKnot = knots.get(i + 1);
            appendCurveToPath( vertices, knots.get(i), controlPoints[i], controlPoints[n + i], targetKnot,resolution);
        }


        return vertices;
    }

    private EpointF[] computeControlPoints(int n, List<EpointF> knots) {
        final EpointF[] result = new EpointF[2 * n];

        final EpointF[] target = constructTargetVector(n, knots);
        final Float[] lowerDiag = constructLowerDiagonalVector(n - 1);
        final Float[] mainDiag = constructMainDiagonalVector(n);
        final Float[] upperDiag = constructUpperDiagonalVector(n - 1);

        final EpointF[] newTarget = new EpointF[n];
        final Float[] newUpperDiag = new Float[n - 1];

        // forward sweep for control points c_i,0:
        newUpperDiag[0] = upperDiag[0] / mainDiag[0];
        newTarget[0] = target[0].scaleBy(1 / mainDiag[0]);

        for (int i = 1; i < n - 1; i++) {
            newUpperDiag[i] = upperDiag[i] /
                    (mainDiag[i] - lowerDiag[i - 1] * newUpperDiag[i - 1]);
        }

        for (int i = 1; i < n; i++) {
            final float targetScale = 1 /
                    (mainDiag[i] - lowerDiag[i - 1] * newUpperDiag[i - 1]);

            newTarget[i] =
                    (target[i].minus(newTarget[i - 1].scaleBy(lowerDiag[i - 1]))).scaleBy(targetScale);
        }

        // backward sweep for control points c_i,0:
        result[n - 1] = newTarget[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            result[i] = newTarget[i].minus(newUpperDiag[i], result[i + 1]);
        }

        // calculate remaining control points c_i,1 directly:
        for (int i = 0; i < n - 1; i++) {
            result[n + i] = knots.get(i + 1).scaleBy(2).minus(result[i + 1]);
        }

        result[2 * n - 1] = knots.get(n).plus(result[n - 1]).scaleBy(0.5f);

        return result;
    }

    private EpointF[] constructTargetVector(int n, List<EpointF> knots) {
        final EpointF[] result = new EpointF[n];

        result[0] = knots.get(0).plus(2, knots.get(1));

        for (int i = 1; i < n - 1; i++) {
            result[i] = (knots.get(i).scaleBy(2).plus(knots.get(i + 1))).scaleBy(2);
        }

        result[result.length - 1] = knots.get(n - 1).scaleBy(8).plus(knots.get(n));

        return result;
    }

    private Float[] constructLowerDiagonalVector(int length) {
        final Float[] result = new Float[length];

        for (int i = 0; i < result.length - 1; i++) {
            result[i] = 1f;
        }

        result[result.length - 1] = 2f;

        return result;
    }

    private Float[] constructMainDiagonalVector(int n) {
        final Float[] result = new Float[n];

        result[0] = 2f;

        for (int i = 1; i < result.length - 1; i++) {
            result[i] = 4f;
        }

        result[result.length - 1] = 7f;

        return result;
    }

    private Float[] constructUpperDiagonalVector(int length) {
        final Float[] result = new Float[length];

        for (int i = 0; i < result.length; i++) {
            result[i] = 1f;
        }

        return result;
    }

    private void appendCurveToPath(float[] vertices, EpointF knot, EpointF control1, EpointF control2, EpointF targetKnot, int resolution) {
        Vector2 p0 = new Vector2(knot.getX(),knot.getY());
        Vector2 p1 = new Vector2(control1.getX(),control1.getY());
        Vector2 p2 = new Vector2(control2.getX(),control2.getY());
        Vector2 p4 = new Vector2(targetKnot.getX(),targetKnot.getY());
        Vector2 tmp = new Vector2(1, 1);
        Vector2[] controlpoints = {p0,p1,p2,p4};


        Bezier<Vector2> curve;
        curve = new Bezier<Vector2>(controlpoints);
        for(int i = 0; i<resolution; i++){
            Vector2 point = new Vector2();
            Vector2 point2;

            curve.valueAt(point,(float)i/(float)resolution);


            vertices[verticeIndex]= point.x;


            verticeIndex++;

            vertices[verticeIndex]= point.y;

            verticeIndex++;
        }

    }

    private void throwExceptionIfInputIsInvalid(Collection<EpointF> knots) {
        if (knots.size() < 2) {
            throw new IllegalArgumentException(
                    "Collection must contain at least two knots"
            );
        }
    }
    public List<EpointF> createKnots (int length){
        List<EpointF> knots = new ArrayList<EpointF>();
        float lastY=0;
        float lastX=0;
        float hillRandomizer;
        knots.add(new EpointF(0,0));
        for(int i = 1; i<length; i++){
            hillRandomizer = (float)(Math.random())*5f+1;
            float x = lastX + hillRandomizer*2;
            float y = lastY + (float)(Math.random()*2*hillRandomizer)-hillRandomizer;
            lastY = y;
            lastX = x;
            knots.add(new EpointF(x,y));
        }
        return knots;
    }

    public void createLevel(World world, String filePath){
        Body bodyGround;
        //float[] points = createVertices();
        float[] points = createFromSVG(filePath);

        createBody(points,world,0,0,false);
    }

    public Body createBody(float[] points, World world, float x, float y, boolean isSensor){
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

    public LevelModule[] createModules ( String[] SVGs,float[] scalers){

        float lastX = 0;
        float lastY = 0;


        LevelModule[] modules;
        modules = new LevelModule[SVGs.length];

        for(int i=0; i<SVGs.length; i++){

            modules[i] = new LevelModule();
            float[] points = createFromSVG(SVGs[i]);
            points = scalePoints(points,scalers[i]);
            modules[i].setLengthScaler(scalers[i]);

            allVertices.addAll(ExtractSVGPaths.extract(SVGs[i]));


            if(SVGs[i].equals("rotko.svg")){
                modules[i].setBody(createBody(points, game.getWorld(), lastX, lastY-5,true));
                game.rotkos.add(modules[i]);
                modules[i].setPolygonRegion(createTexture(game, SVGs[i], spikes));
            }else{
                modules[i].setBody(createBody(points, game.getWorld(), lastX, lastY,false));
                modules[i].setPolygonRegion(createTexture(game, SVGs[i], dirt));
            }


            modules[i].setFile(SVGs[i]);
            modules[i].setHeight(polySprite.getHeight()/game.PIXELS_TO_METERS);
            modules[i].setLength(polySprite.getWidth()/game.PIXELS_TO_METERS);
            modules[i].setX(lastX);

            if(SVGs[i].equals("rotko.svg")){
                modules[i].setY(lastY-5);
            }else{
                modules[i].setY(lastY);
            }

            modules[i].setGame(game);
            lastX += points[points.length-6];
            lastY += points[points.length-5];
        }

        goal.setX(lastX);
        goal.setY(lastY);

        return modules;
    }

    public ArrayList<LevelModule> createModules2 ( String SVG){

        float lastX = 0;
        float lastY = 0;


        ArrayList<LevelModule> modules;
        modules = new ArrayList<LevelModule>();

        for(int i=0; i<SVGs.length; i++){

            modules[i] = new LevelModule();
            float[] points = createFromSVG(SVGs[i]);
            points = scalePoints(points,scalers[i]);
            modules[i].setLengthScaler(scalers[i]);

            allVertices.addAll(ExtractSVGPaths.extract(SVGs[i]));


            if(SVGs[i].equals("rotko.svg")){
                modules[i].setBody(createBody(points, game.getWorld(), lastX, lastY-5,true));
                game.rotkos.add(modules[i]);
                modules[i].setPolygonRegion(createTexture(game, SVGs[i], spikes));
            }else{
                modules[i].setBody(createBody(points, game.getWorld(), lastX, lastY,false));
                modules[i].setPolygonRegion(createTexture(game, SVGs[i], dirt));
            }


            modules[i].setFile(SVGs[i]);
            modules[i].setHeight(polySprite.getHeight()/game.PIXELS_TO_METERS);
            modules[i].setLength(polySprite.getWidth()/game.PIXELS_TO_METERS);
            modules[i].setX(lastX);

            if(SVGs[i].equals("rotko.svg")){
                modules[i].setY(lastY-5);
            }else{
                modules[i].setY(lastY);
            }

            modules[i].setGame(game);
            lastX += points[points.length-6];
            lastY += points[points.length-5];
        }

        goal.setX(lastX);
        goal.setY(lastY);

        return modules;
    }

    public PolygonRegion createTexture(GameScreen game, String filePath, TextureRegion textureRegion){
        PolygonRegion polygonRegion;
        float[] vertices = createFromSVG(filePath);
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

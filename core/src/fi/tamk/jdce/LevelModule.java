package fi.tamk.jdce;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.ArrayList;

/**
 * LevelModule holds the information of the "ground" parts that make the level.
 *
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class LevelModule implements HasBody{


    /**
     * The Length.
     */
    float length;
    /**
     * The Height.
     */
    float height;
    /**
     * The Polygon region for drawing the ground texture.
     */
    PolygonRegion polygonRegion;
    /**
     * The Body.
     */
    Body body;
    /**
     * The X coordinate.
     */
    float x;
    /**
     * The Y coordinate.
     */
    float y;
    /**
     * The Length scaler.
     */
    float lengthScaler;
    /**
     * The Height scaler.
     */
    float heightScaler;
    /**
     * The Outline coordinates for shape renderer.
     */
    float[] outlines;
    /**
     * The scaled down outlines for level info screens map.
     */
    float[] mapOutlines;
    /**
     * The Vector2 coordinates of the module shape.
     */
    ArrayList<Vector2> vectors;
    /**
     * The Line color.
     */
    Color lineColor;
    /**
     * The Game.
     */
    GameScreen game;


    /**
     * Gets length.
     *
     * @return the length
     */
    public float getLength() {
        return length;
    }

    /**
     * Sets length.
     *
     * @param length the length
     */
    public void setLength(float length) {
        this.length = length;
    }

    /**
     * Gets polygon region.
     *
     * @return the polygon region
     */
    public PolygonRegion getPolygonRegion() {
        return polygonRegion;
    }

    /**
     * Sets polygon region.
     *
     * @param polygonRegion the polygon region
     */
    public void setPolygonRegion(PolygonRegion polygonRegion) {
        this.polygonRegion = polygonRegion;
    }

    /**
     * Gets the box2D body of the module.
     *
     * @return box2D body of the module
     */
    public Body getBody() {
        return body;
    }

    @Override
    public void remove() {

    }

    @Override
    public void deledDis() {
    }

    /**
     * Sets body.
     *
     * @param body the body
     */
    public void setBody(Body body) {
        this.body = body;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Gets game.
     *
     * @return the game
     */
    public GameScreen getGame() {
        return game;
    }

    /**
     * Sets game.
     *
     * @param game the game
     */
    public void setGame(GameScreen game) {
        this.game = game;
    }

    /**
     * Gets length scaler.
     *
     * @return the length scaler
     */
    public float getLengthScaler() {
        return lengthScaler;
    }

    /**
     * Sets length scaler.
     *
     * @param lengthScaler the length scaler
     */
    public void setLengthScaler(float lengthScaler) {
        this.lengthScaler = lengthScaler;
    }

    /**
     * Gets height scaler.
     *
     * @return the height scaler
     */
    public float getHeightScaler() {
        return heightScaler;
    }

    /**
     * Sets height scaler.
     *
     * @param heightScaler the height scaler
     */
    public void setHeightScaler(float heightScaler) {
        this.heightScaler = heightScaler;
    }

    /**
     * Get outlines float [ ].
     *
     * @return the float [ ]
     */
    public float[] getOutlines() {
        return outlines;
    }

    /**
     * Sets outlines.
     *
     * @param outlines the outlines
     */
    public void setOutlines(float[] outlines) {
        this.outlines = outlines;
    }

    /**
     * Gets vectors.
     *
     * @return the vectors
     */
    public ArrayList<Vector2> getVectors() {
        return vectors;
    }

    /**
     * Sets vectors.
     *
     * @param vectors the vectors
     */
    public void setVectors(ArrayList<Vector2> vectors) {
        this.vectors = vectors;
    }

    /**
     * Gets line color.
     *
     * @return the line color
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Sets line color.
     *
     * @param lineColor the line color
     */
    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * Draw.
     */
    public void draw(){

        game.getPolyBatch().draw(polygonRegion, x,y,length, height);
    }


    /**
     * Draw outlines.
     */
    public void drawOutlines(){
        game.shapeRenderer.setColor(lineColor);
        game.shapeRenderer.polygon(outlines);
    }

    /**
     * Draw map outlines.
     *
     * @param shapeRenderer the shape renderer
     */
    public void drawMapOutlines(ShapeRenderer shapeRenderer){
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.polygon(mapOutlines);
    }

    /**
     * Creates map outlines.
     *
     * @param scale     the scale of the map
     * @param locationX the location x
     * @param locationY the location y
     */
    public void createMapOutlines(float scale, float locationX, float locationY){
        mapOutlines = new float[outlines.length];
        for (int i=0; i<outlines.length;i++){
            if(i%2==0){
                mapOutlines[i]=(outlines[i]*scale)+locationX;
            }else{
                mapOutlines[i]=(outlines[i]*scale)+locationY;
            }
        }
    }

}

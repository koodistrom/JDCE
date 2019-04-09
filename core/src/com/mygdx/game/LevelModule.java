package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class LevelModule implements HasBody{


    String file;
    float length;
    float height;
    PolygonRegion polygonRegion;
    Body body;
    float x;
    float y;
    float lengthScaler;
    float heightScaler;
    GameScreen game;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public PolygonRegion getPolygonRegion() {
        return polygonRegion;
    }

    public void setPolygonRegion(PolygonRegion polygonRegion) {
        this.polygonRegion = polygonRegion;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void remove() {

    }

    @Override
    public void deledDis() {
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public GameScreen getGame() {
        return game;
    }

    public void setGame(GameScreen game) {
        this.game = game;
    }

    public float getLengthScaler() {
        return lengthScaler;
    }

    public void setLengthScaler(float lengthScaler) {
        this.lengthScaler = lengthScaler;
    }

    public float getHeightScaler() {
        return heightScaler;
    }

    public void setHeightScaler(float heightScaler) {
        this.heightScaler = heightScaler;
    }

    public void draw(){

        game.getPolyBatch().draw(polygonRegion, x,y,length, height);
    }

    public void drawMap(float scale){

        game.getPolyBatch().draw(polygonRegion, x/scale,y/scale,length/scale, height/scale);
    }
}

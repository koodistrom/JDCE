package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;

public class GameObject {

    private Texture texture;
    private Body body;
    private Float x;
    private Float y;
    private Float width;
    private Float height;
    private Batch batch;
    private float rotation;
    private JDCEGame game;


    public GameObject(JDCEGame game){
        texture = new Texture("cyclerex.png");
        this.game = game;
        this.batch = game.batch;
        width = texture.getWidth()/game.PIXELS_TO_METERS;
        height = texture.getHeight()/game.PIXELS_TO_METERS;
        rotation = 0;
        x = game.worldWidth/2;
        y = game.worldHeight/2;
    }

    public void update(){
        x = (body.getPosition().x) - (width/2);
        y = (body.getPosition().y) - (height/2);

        rotation = (float)(Math.toDegrees(body.getAngle()));
    }
    public void draw(){


        batch.draw(texture, x, y,width/2, height/2,
                width,height,1,1, rotation,0,0, texture.getWidth(), texture.getHeight(),false,false);
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }
    public void setLocation(Float x, Float y){
        this.x = x;
        this.y = y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

}

package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class GameObject implements HasBody{

    protected Texture texture;
    protected Body body;
    protected Float x;
    protected Float y;
    private Float width;
    private Float height;
    protected Batch batch;
    protected float rotation;
    protected GameScreen game;



    public GameObject(GameScreen game){

        this.game = game;
        this.batch = game.getSpriteBatch();

        rotation = 0f;
        x = game.getScreenWidth()/2;
        y = game.getScreenHeight()/2;
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
        width = texture.getWidth()/game.PIXELS_TO_METERS;
        height = texture.getHeight()/game.PIXELS_TO_METERS;
    }

    public void setTexture(Texture texture,float width, float height) {

        this.texture = texture;
        this.width = width;
        this.height = height;
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

    public void remove(){
        game.getWorld().destroyBody(body);
        texture.dispose();
    }

    @Override
    public void deledDis() {
    }

}

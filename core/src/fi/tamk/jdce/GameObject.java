package fi.tamk.jdce;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Game object is a super class for objects in the game.
 *
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class GameObject implements HasBody{

    /**
     * The Texture.
     */
    protected Texture texture;
    /**
     * The Body.
     */
    protected Body body;
    /**
     * The X location.
     */
    protected Float x;
    /**
     * The Y location.
     */
    protected Float y;
    private Float width;
    private Float height;
    /**
     * The Batch.
     */
    protected Batch batch;
    /**
     * The Rotation.
     */
    protected float rotation;
    /**
     * The Game screen.
     */
    protected GameScreen game;


    /**
     * Instantiates a new Game object.
     *
     * @param game the game
     */
    public GameObject(GameScreen game){

        this.game = game;
        this.batch = game.getSpriteBatch();

        rotation = 0f;
        x = game.getScreenWidth()/2;
        y = game.getScreenHeight()/2;
    }

    /**
     * Instantiates a new Game object.
     */
    public GameObject(){

    }


    /**
     * Update position and rotation of the object according to box2D calculated physic movement to objects body. Other updates to the state of the object are added to this method in classes that extend GameObject.
     */
    public void update(){
        x = (body.getPosition().x) - (width/2);
        y = (body.getPosition().y) - (height/2);

        rotation = (float)(Math.toDegrees(body.getAngle()));
    }

    /**
     * Draw texture.
     */
    public void draw(){


        batch.draw(texture, x, y,width/2, height/2,
                width,height,1,1, rotation,0,0, texture.getWidth(), texture.getHeight(),false,false);
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
     * Sets texture.
     *
     * @param texture the texture
     */
    public void setTexture(Texture texture) {

        this.texture = texture;
        width = texture.getWidth()/game.PIXELS_TO_METERS;
        height = texture.getHeight()/game.PIXELS_TO_METERS;
    }

    /**
     * Sets texture.
     *
     * @param texture the texture
     * @param width   the width
     * @param height  the height
     */
    public void setTexture(Texture texture,float width, float height) {

        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    public Body getBody() {
        return body;
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
     * Gets x.
     *
     * @return the x
     */
    public Float getX() {
        return x;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(Float x) {
        this.x = x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public Float getY() {
        return y;
    }

    /**
     * Set location.
     *
     * @param x the x
     * @param y the y
     */
    public void setLocation(Float x, Float y){
        this.x = x;
        this.y = y;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(Float y) {
        this.y = y;
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public Float getWidth() {
        return width;
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setWidth(Float width) {
        this.width = width;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public Float getHeight() {
        return height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
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

    /**
     * Dispose.
     */
    public void dispose(){
        if(texture!=null){
            texture.dispose();
        }
    }

}

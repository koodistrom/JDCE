package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;



/**
 * The collectable items to be set along track
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */

public class Collectable extends GameObject {
    /**
     * The the boolean that is set true when player hits collectable.
     */
    boolean touched;
    /**
     * The Current frame of the animation.
     */
    TextureRegion currentFrame;
    /**
     * The Animation.
     */
    Animation<TextureRegion> animation;
    /**
     * The time that defines current frame of the animation.
     */
    float stateTime;

    /**
     * Instantiates a new Collectable.
     *
     * @param game         the game
     * @param animation    the animation
     * @param xPercentage  the location of collectable in the x direction as persentages of the whole width of the track (0-100)
     * @param levelCreator the level creator
     */
    public Collectable(GameScreen game, Animation<TextureRegion> animation, Float xPercentage, LevelCreator levelCreator) {
        super(game);
        touched = false;
        setWidth(animation.getKeyFrame(0.01f).getRegionWidth()/(2*game.PIXELS_TO_METERS));
        setHeight(animation.getKeyFrame(0.01f).getRegionHeight()/(2*game.PIXELS_TO_METERS));

        createBody();

        this.animation = animation;
        stateTime = 0;
        setLocationInLevel(xPercentage, levelCreator);

    }

    /**
     * Instantiates a new Collectable.
     */
    public Collectable(){

    }

    @Override
    public void update() {
        stateTime += Gdx.graphics.getDeltaTime();
        if(stateTime<0){
            stateTime=1-stateTime;
        }
        currentFrame = animation.getKeyFrame(stateTime, true);

        if(touched == true){
            remove();
        }
    }

    @Override
    public void draw(){

        batch.draw(currentFrame, x, y,(getWidth()/2), (getHeight()/2),
                getWidth(),getHeight(),1,1, rotation);
    }


    /**
     * Sets location in level.
     *
     * @param xPercentage  the location of collectable in the x direction as persentages of the whole width of the track (0-100)
     * @param levelCreator the level creator
     */
    public void setLocationInLevel(Float xPercentage, LevelCreator levelCreator) {
        for (int i=1; i<levelCreator.allVertices.size(); i++){
            float absolutePos = xPercentage*(levelCreator.lastX/100);
            if(levelCreator.allVertices.get(i).x>absolutePos){


                Vector2 oneBefore = levelCreator.allVertices.get(i-1);

                Vector2 oneAfter = levelCreator.allVertices.get(i);
                Vector2 angle = new Vector2(oneAfter.x-oneBefore.x, oneAfter.y-oneBefore.y);
                rotation = angle.angle();
                Vector2 positiontoground = new Vector2(0,1);
                positiontoground.setAngle(rotation+90);

                float xFactor = (absolutePos-oneBefore.x)/(oneAfter.x-oneBefore.x);
                float y = oneBefore.y+(oneAfter.y-oneBefore.y)*xFactor;

                setLocation(absolutePos-(getWidth()/2) + positiontoground.x, y-1.3f+ positiontoground.y);
                //setLocation(levelCreator.allVertices.get(i).x + positiontoground.x, levelCreator.allVertices.get(i).y+ positiontoground.y);
                System.out.println(angle.angle());
                System.out.println(positiontoground);
                break;
            }
        }


        body.setTransform(getX() + getWidth()/2,getY() + getHeight()/2, MathUtils.degreesToRadians*rotation);
    }

    /**
     * Create body for the collectable.
     */
    public void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        /*bodyDef.position.set((getX() + getWidth()/2),
                (getY() + getHeight()/2));
        */
        body = game.getWorld().createBody(bodyDef);
        body.setUserData("collectable");
        FixtureDef fixtureDef = new FixtureDef();

        Vector2[] vertices;
        vertices= new Vector2[] {new Vector2(-0.4f,1f),new Vector2(1.2f,1f),
                new Vector2(0f,-1f)};

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        fixtureDef.shape=shape;


        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        body.setUserData(this);
        setBody(body);
    }

    @Override
    public void deledDis(){
        touched=true;
    }

    @Override
    public void remove() {
        setWidth(0f);
        setHeight(0f);
        body.setActive(false);

    }
    @Override
    public void dispose(){

    }
}

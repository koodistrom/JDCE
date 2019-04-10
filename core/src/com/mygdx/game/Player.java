package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;

public class Player extends GameObject implements InputProcessor {


    World world;
    Body frontWheel;
    Body rearWheel;
    Texture wheel;
    float rwRotation;
    float fwRotation;
    WheelJointDef rearWheelJointDef = new WheelJointDef();
    WheelJoint rearWheelJoint;
    WheelJoint frontWheelJoint;
    Float motorSpeed;
    int time;
    Float oldSpeed;
    float trackTime;
    Timer timer;
    boolean isTurboOn;
    float freeRollRange;

    boolean reverseOn;
    boolean forwardOn;
    boolean neutralOn;

    Float speed;
    ArrayList<Float> distsInSec;
    float neutralDist;
    float neutralRange;

    float ww;
    float wh;
    float linearDamping;
    boolean win;
    boolean addSpeed;
    boolean isOnGround;
    TextureAtlas pedalingAtlas;
    Animation<TextureRegion> pedalingAnimation;



    public Player(GameScreen game) {

        super(game);
        rwRotation=0;
        fwRotation=0;
        setTexture(new Texture("noweeler.png"));
        wheel = new Texture("rengas.png");
        ww = wheel.getWidth()/game.PIXELS_TO_METERS;
        wh = wheel.getHeight()/game.PIXELS_TO_METERS;
        world = game.getWorld();
        x = game.getScreenWidth()/3;
        y = game.getScreenHeight()/2;
        time = 0;
        oldSpeed = 0f;
        isTurboOn = false;
        freeRollRange = 0.005f;

        speed = 0f;
        neutralDist = 0f;
        neutralRange = 0.1f;
        distsInSec = new ArrayList<Float>();

        forwardOn = false;
        reverseOn = false;
        neutralOn = true;

        linearDamping = 0.1f;
        win = false;
        addSpeed = false;
        isOnGround = false;
        pedalingAtlas = new TextureAtlas("animations/pedaling.atlas");
        pedalingAnimation = new Animation<TextureRegion>(0.033f, pedalingAtlas.findRegions("pedaling"), Animation.PlayMode.LOOP);

        createBodies();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void draw(){
        super.draw();
        float ww = wheel.getWidth()/game.PIXELS_TO_METERS;
        float wh = wheel.getHeight()/game.PIXELS_TO_METERS;
        batch.draw(wheel, rearWheel.getPosition().x-(ww/2), rearWheel.getPosition().y-(wh/2),ww/2, wh/2,
                ww,wh,1,1, rwRotation,0,0, wheel.getWidth(), wheel.getHeight(),false,false);

        batch.draw(wheel, frontWheel.getPosition().x-(ww/2), frontWheel.getPosition().y-(wh/2),ww/2, wh/2,
                ww,wh,1,1, fwRotation,0,0, wheel.getWidth(), wheel.getHeight(),false,false);
    }

    @Override
    public void update(){

        super.update();

        if(JDCEGame.m_platformResolver.isAndroid()) {
            speed = JDCEGame.m_platformResolver.getPedalSpeed();
        }

        fwRotation=(float)(Math.toDegrees(frontWheel.getAngle()));
        rwRotation= (float)(Math.toDegrees(rearWheel.getAngle()));




        steering();
        //System.out.println(speed);
        distsInSec.add(speed);
        if(distsInSec.size()>60){
            distsInSec.remove(0);
        }
        trackTime();



    }


    private Body createWheel(BodyDef.BodyType type, float x, float y, float d, float r, float f, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(x,y);
        bodyDef.angle=0;
        bodyDef.linearDamping=linearDamping;

        Body ball = world.createBody(bodyDef);

        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.density=d;
        fixtureDef.restitution=r;
        fixtureDef.friction=f;
        fixtureDef.shape=new CircleShape();
        fixtureDef.shape.setRadius(radius);


        ball.createFixture(fixtureDef);
        fixtureDef.shape.dispose();

        return ball;
    }





    public void trackTime(){
        if(x>game.getLevelCreator().goal.getX()){
            //voittaminen :ok_hand:
            win = true;
            game.getGame().setScreen(new FinishView(game.getGame(),trackTime, win, game.levelNum));
            game.dispose();

        }else{
            trackTime += Gdx.graphics.getDeltaTime();

        }
    }

    public void steering(){

        motorSpeed = (-15)*60 * speed * ((rearWheelJoint.getJointSpeed()/-100)+1);

        //vapaalla
        if(!isReverseOn() && motorSpeed>rearWheelJoint.getJointSpeed() && speed>-freeRollRange) {
            setOnNeutral();

        }

        //backward
        if(speed<-freeRollRange) {
            setOnReverse();


        }

        //forward
        if( motorSpeed<rearWheelJoint.getJointSpeed() ) {
            setOnForward();

        }

        airControl();
        System.out.println("moottorinopeus: "+ rearWheelJoint.getMotorSpeed()+"  polkunopeus: "+speed+"  renkaan nopeus: "+rearWheelJoint.getJointSpeed());
        //System.out.println("kerroin: "+ (rearWheelJoint.getJointSpeed()/-100)+1);

        //desktoptestaukseen
        if(addSpeed==true){
            speed +=0.00001f;
        }

        rearWheelJoint.setMotorSpeed(motorSpeed);
        frontWheelJoint.setMotorSpeed(motorSpeed);

        if(isTurboOn){
            body.applyForceToCenter(new Vector2(5,0),false);
            //motorSpeed *= 2;
        }


    }

    public void turboOn(){
        isTurboOn = true;
        timer = new Timer();
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                System.out.println("turbo on pois");
                isTurboOn = false;
            }
        };
        timer.scheduleTask(task,7);

    }

    public  boolean isReverseOn(){
        if (reverseOn){
            if (speed<freeRollRange){
                return true;
            }else {
                reverseOn = false;
                return false;
            }

        }else {
            return false;
        }
    }

    public void setOnReverse() {
        reverseOn = true;
        neutralOn = false;
        forwardOn = false;

        rearWheelJoint.enableMotor(true);
        frontWheelJoint.enableMotor(true);
    }

    public void setOnNeutral() {
        rearWheelJoint.enableMotor(false);
        frontWheelJoint.enableMotor(false);

        reverseOn = false;
        neutralOn = true;
        forwardOn = false;
    }

    public void setOnForward() {
        rearWheelJoint.enableMotor(true);
        frontWheelJoint.enableMotor(false);

        reverseOn = false;
        neutralOn = false;
        forwardOn = true;
    }

    public void airControl(){
        //body.applyTorque(speed*10,true);
        body.applyAngularImpulse(speed,false);
    }

    public void createBodies(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = linearDamping;
        bodyDef.position.set((getX() + getWidth()/2),
                (getY() + getHeight()/2));

        body = world.createBody(bodyDef);

        Vector2[] vertices;
        vertices= new Vector2[] {new Vector2(0.5f,-0.5f),new Vector2(-1.4f,0.2f),
                new Vector2(1.3f,0.8f), new Vector2(1.3f,0.6f)};

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.restitution = 0.5f;

        body.createFixture(fixtureDef);
        shape.dispose();
        setBody(body);

        rearWheel=createWheel(BodyDef.BodyType.DynamicBody,x,y,0.5f,0.5f,0.8f,ww/2);
        frontWheel=createWheel(BodyDef.BodyType.DynamicBody,x+getWidth(),y,0.5f,0.5f,0.8f,ww/2);

        WheelJointDef rearWheelJointDef = new WheelJointDef();
        rearWheelJointDef.bodyA=body;
        rearWheelJointDef.bodyB=rearWheel;
        rearWheelJointDef.collideConnected=false;
        rearWheelJointDef.localAnchorA.set(-1f,-0.7f);
        //rearWheelJointDef.localAnchorB.set(0,0);
        rearWheelJointDef.enableMotor = false;
        rearWheelJointDef.motorSpeed = -5f;
        rearWheelJointDef.dampingRatio = 0.95f;
        rearWheelJointDef.frequencyHz = 1.7f;
        rearWheelJointDef.localAxisA.set(new Vector2(0,1));
        rearWheelJointDef.maxMotorTorque = 1.5f;
        rearWheelJoint = (WheelJoint) world.createJoint(rearWheelJointDef);


        WheelJointDef frontWheelJointDef = new WheelJointDef();
        frontWheelJointDef.bodyA=body;
        frontWheelJointDef.bodyB=frontWheel;
        frontWheelJointDef.collideConnected=false;
        frontWheelJointDef.localAnchorA.set(0.9f,-0.7f);
        //frontWheelJointDef.localAnchorB.set(0,0);
        frontWheelJointDef.dampingRatio = 0.95f;
        frontWheelJointDef.frequencyHz = 1.7f;
        frontWheelJointDef.localAxisA.set(new Vector2(0,1));
        frontWheelJointDef.maxMotorTorque = 1.5f;
        frontWheelJoint = (WheelJoint)world.createJoint(frontWheelJointDef);
    }

    //if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){ }
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.RIGHT) {
            speed = 1f/60f;
            addSpeed = true;

        }
        if(keycode == Input.Keys.LEFT) {
            if(speed==0){
                speed = -1f/60f;
            }else{
                speed -=0.001f;
            }

        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.RIGHT) {
            speed = 0f;
            addSpeed = false;
        }
        if(keycode == Input.Keys.LEFT) {
            speed = 0f;

        }

        if(keycode == Input.Keys.ESCAPE) {
            game.reset();

        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(win == false) {
            LevelSelectScreen mms = new LevelSelectScreen(game.getGame());
            game.getGame().setScreen(mms);
            return true;
        }else
            return false;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

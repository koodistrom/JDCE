package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

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
    public Player(JDCEGame game) {

        super(game);
        rwRotation=0;
        fwRotation=0;
        setTexture(new Texture("noweeler.png"));
        wheel = new Texture("rengas.png");
        float ww = wheel.getWidth()/game.PIXELS_TO_METERS;
        float wh = wheel.getHeight()/game.PIXELS_TO_METERS;
        world = game.world;
        x = game.worldWidth/3;
        y = game.worldHeight/2;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((getX() + getWidth()/2),
                (getY() + getHeight()/2));
        Vector2[] vertices;
        vertices= new Vector2[] {new Vector2(-0.5f,-0.8f),new Vector2(-1.4f,0.2f),
                new Vector2(1.3f,0.8f), new Vector2(1.3f,0.6f),new Vector2(0.5f,0.4f)};
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;

        body.createFixture(fixtureDef);
        shape.dispose();
        setBody(body);

        rearWheel=createSphere(BodyDef.BodyType.DynamicBody,x,y,1,0.5f,1f,ww/2);
        frontWheel=createSphere(BodyDef.BodyType.DynamicBody,x+getWidth(),y,1,0.5f,1f,ww/2);

        WheelJointDef rearWheelJointDef = new WheelJointDef();
        rearWheelJointDef.bodyA=body;
        rearWheelJointDef.bodyB=rearWheel;
        rearWheelJointDef.collideConnected=false;
        rearWheelJointDef.localAnchorA.set(-1f,-0.7f);
        //rearWheelJointDef.localAnchorB.set(0,0);
        rearWheelJointDef.enableMotor = false;
        rearWheelJointDef.motorSpeed = -5f;

        rearWheelJointDef.maxMotorTorque = 50f;
        rearWheelJoint = (WheelJoint) world.createJoint(rearWheelJointDef);


        WheelJointDef frontWheelJointDef = new WheelJointDef();
        frontWheelJointDef.bodyA=body;
        frontWheelJointDef.bodyB=frontWheel;
        frontWheelJointDef.collideConnected=false;
        frontWheelJointDef.localAnchorA.set(0.9f,-0.7f);
        //frontWheelJointDef.localAnchorB.set(0,0);
        frontWheelJointDef.maxMotorTorque = 50f;
        frontWheelJoint = (WheelJoint)world.createJoint(frontWheelJointDef);

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

        fwRotation=(float)(Math.toDegrees(frontWheel.getAngle()));
        rwRotation= (float)(Math.toDegrees(rearWheel.getAngle()));
    }


    private Body createSphere(BodyDef.BodyType type, float x, float y, float d, float r, float f, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(x,y);
        bodyDef.angle=0;
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

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.RIGHT) {
            rearWheelJoint.enableMotor(true);
            rearWheelJoint.setMotorSpeed(-10f);


            System.out.println(rearWheelJointDef.enableMotor);
        }
        if(keycode == Input.Keys.LEFT) {
            frontWheelJoint.enableMotor(true);
            frontWheelJoint.setMotorSpeed(0f);
            rearWheelJoint.enableMotor(true);
            rearWheelJoint.setMotorSpeed(0f);

            System.out.println(rearWheelJointDef.enableMotor);
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.RIGHT) {
            rearWheelJoint.enableMotor(false);
            System.out.println("benis");
        }
        if(keycode == Input.Keys.LEFT) {
            frontWheelJoint.enableMotor(false);

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

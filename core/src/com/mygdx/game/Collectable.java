package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Collectable extends GameObject {
    boolean touched;
    public Collectable(JDCEGame game, Texture texture) {
        super(game);
        touched = false;
        setTexture(texture);
        setHeight(getHeight()/2);
        setWidth(getWidth()/2);
        setLocation(10f,0f);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((getX() + getWidth()/2),
                (getY() + getHeight()/2));

        body = game.world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape=new CircleShape();
        fixtureDef.shape.setRadius(getWidth()/2);

        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        setBody(body);
        game.collectables.add(this);
    }

    @Override
    public void update() {
        draw();
        if(touched == true){
            remove();
        }
    }


    public void setLocationInLevel(Float x, LevelCreator levelCreator) {
        for (int i=0; i<levelCreator.allVertices.size(); i++){
            if(levelCreator.allVertices.get(i).x>x){


                Vector2 oneBefore = levelCreator.allVertices.get(i-1);

                Vector2 oneAfter = levelCreator.allVertices.get(i);
                Vector2 angle = new Vector2(oneAfter.x-oneBefore.x, oneAfter.y-oneBefore.y);
                rotation = angle.angle();
                Vector2 positiontoground = new Vector2(0,1);
                positiontoground.setAngle(rotation+90);
                setLocation(levelCreator.allVertices.get(i).x + positiontoground.x, levelCreator.allVertices.get(i).y+ positiontoground.y);
                System.out.println(angle.angle());
                System.out.println(positiontoground);
                break;
            }
        }


        body.setTransform(getX() + getWidth()/2,getY() + getHeight()/2, MathUtils.degreesToRadians*rotation);
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
}

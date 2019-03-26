package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class Stegosaurus extends GameObject {

    World world;

    public Stegosaurus(GameScreen game) {
        super(game);
        world = game.getWorld();
        setHeight(2f);
        setWidth(2f);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set((getX() + getWidth()/2),
                (getY() + getHeight()/2));

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.density = 0.1f;
        fixtureDef.restitution = 0.5f;

        fixtureDef.shape=new CircleShape();
        fixtureDef.shape.setRadius(1f);
        body.createFixture(fixtureDef);
        setBody(body);
    }
}

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.ArrayList;

public class ContactListenerClass implements ContactListener {

    GameScreen game;
    Fixture[] playerFixs;
    Fixture fixtureTouched;
    HasBody objectTouched;


    public ContactListenerClass (GameScreen game){
        this.game =game;
        playerFixs = new Fixture[]{
                game.getPlayer().body.getFixtureList().get(0),
                game.getPlayer().frontWheel.getFixtureList().get(0),
                game.getPlayer().rearWheel.getFixtureList().get(0)
        };
    }
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if(playerTouches(game.collectables,contact)){
            Gdx.app.log("beginContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
            game.getPlayer().turboOn();
            objectTouched.deledDis();


        }

        if(playerTouches(game.rotkos,contact)){
            game = new GameScreen(game.getGame());

        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean playerTouches(ArrayList<HasBody> objects, Contact contact){
        boolean touch = false;
        if(objects!= null){
            for (int i = 0; i < playerFixs.length; i++) {
                for (int n = 0; n < objects.size(); n++) {
                    if ((playerFixs[i] == contact.getFixtureA() && objects.get(n).getBody().getFixtureList().get(0) == contact.getFixtureB())
                            || (playerFixs[i] == contact.getFixtureB() && objects.get(n).getBody().getFixtureList().get(0) == contact.getFixtureA())) {
                        fixtureTouched = objects.get(n).getBody().getFixtureList().get(0);
                        objectTouched = objects.get(n);
                        touch = true;
                    }
                }
            }
        }
        return touch;
    }


}

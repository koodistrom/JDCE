package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Timer;


import java.util.ArrayList;

public class ContactListenerClass implements ContactListener {

    GameScreen game;
    Fixture[] playerFixs;
    Fixture fixtureTouched;
    HasBody objectTouched;
    Fixture frontWheel;
    Fixture rearWheel;
    Fixture playerBody;
    boolean soundJustPlayed;
    Timer.Task soundCoolDown;

    private ArrayList<HasBody> collisionCheckModules;

    public ContactListenerClass (GameScreen game){
        this.game =game;
        collisionCheckModules = new ArrayList<HasBody>();
        collisionCheckModules.addAll(game.getModules());
        playerFixs = new Fixture[]{
                game.getPlayer().body.getFixtureList().get(0),
                game.getPlayer().frontWheel.getFixtureList().get(0),
                game.getPlayer().rearWheel.getFixtureList().get(0)
        };
        frontWheel = game.getPlayer().frontWheel.getFixtureList().get(0);
        rearWheel = game.getPlayer().rearWheel.getFixtureList().get(0);
        playerBody = game.getPlayer().body.getFixtureList().get(0);
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

        if(objectTouchesClass(game.getPlayer().getBody().getFixtureList().get(1),new Collectable(),contact)){
            game.getPlayer().turboOn();
            game.getPlayer().turbo.play(1f);
            objectTouched.deledDis();
        }

        if(objectTouchesClass(game.getPlayer().getBody().getFixtureList().get(1),new LevelModule(),contact)){
            game.getPlayer().endGame();
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
        if(objectTouchesClass(frontWheel,new LevelModule(),contact)|| objectTouchesClass(rearWheel,new LevelModule(),contact)){
            if(JDCEGame.soundEffectsOn && impulse.getNormalImpulses()[0]>0.9f){

                game.getPlayer().hitGround.play(1f);

            }
        }

        if(objectTouchesClass(playerBody,new LevelModule(),contact)){
            if(JDCEGame.soundEffectsOn && impulse.getNormalImpulses()[0]>0.9f){

                game.getPlayer().hitHead.play(1f);

            }
        }
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

    public boolean objectTouchesClass(Fixture fixture, Object objectType, Contact contact){
        if(contact.getFixtureA()==fixture && contact.getFixtureB().getBody().getUserData().getClass()==objectType.getClass()){
            objectTouched = (HasBody) contact.getFixtureB().getBody().getUserData();
            return true;
        }else if (contact.getFixtureB()==fixture && contact.getFixtureA().getBody().getUserData().getClass()==objectType.getClass()){
            objectTouched = (HasBody) contact.getFixtureA().getBody().getUserData();
            return true;
        }else{
            return false;
        }
    }


}

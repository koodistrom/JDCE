package fi.tamk.jdce;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


public class ContactListenerClass implements ContactListener {

    GameScreen game;

    HasBody objectTouched;
    Fixture frontWheelFix;
    Fixture rearWheelFix;
    Fixture playerBodyFix;



    public ContactListenerClass (GameScreen game){
        this.game =game;

        frontWheelFix = game.getPlayer().frontWheel.getFixtureList().get(0);
        rearWheelFix = game.getPlayer().rearWheel.getFixtureList().get(0);
        playerBodyFix = game.getPlayer().body.getFixtureList().get(0);
    }
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();


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
        if(objectTouchesClass(frontWheelFix,new LevelModule(),contact)|| objectTouchesClass(rearWheelFix,new LevelModule(),contact)){
            if(JDCEGame.soundEffectsOn && impulse.getNormalImpulses()[0]>0.9f){

                game.getPlayer().hitGround.play(1f);

            }
        }

        if(objectTouchesClass(playerBodyFix,new LevelModule(),contact)){
            if(JDCEGame.soundEffectsOn && impulse.getNormalImpulses()[0]>0.9f){

                game.getPlayer().hitHead.play(1f);

            }
        }
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

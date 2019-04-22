package fi.tamk.jdce;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


/**
 *ContactListenerClass handles the events that result from box2D bodies colliding playing sounds, ending game if player is on their back and
 *  activating properties from collectables.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class ContactListenerClass implements ContactListener {

    /**
     * The game screen.
     */
    GameScreen game;

    /**
     * The Object touched.
     */
    HasBody objectTouched;
    /**
     * The Front wheel fixture.
     */
    Fixture frontWheelFix;
    /**
     * The Rear wheel fixture.
     */
    Fixture rearWheelFix;
    /**
     * The Player body fixture.
     */
    Fixture playerBodyFix;


    /**
     * Instantiates a new Contact listener class.
     *
     * @param game the game screen
     */
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


    /**
     * Object touches class boolean.
     *
     * @param fixture    the fixture which collisions are checked
     * @param objectType the object type to which the collisions are checked
     * @param contact    the contact checked
     * @return the boolean has contact happened
     */
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

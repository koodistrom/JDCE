package fi.tamk.jdce;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * The interface Has body implemented by objects that have bodies.
 *
 * Is used in collision checking
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public interface HasBody {
    /**
     * Gets body.
     *
     * @return the body
     */
    public Body getBody();

    /**
     * Remove.
     */
    public  void remove();

    /**
     * Deled dis.
     */
    public void deledDis();
}

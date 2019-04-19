package fi.tamk.jdce;

import com.badlogic.gdx.physics.box2d.Body;

public interface HasBody {
    public Body getBody();
    public  void remove();
    public void deledDis();
}

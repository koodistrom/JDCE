package fi.tamk.jdce;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * The asset class for drawable objects placed along the tracks
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class Asset extends GameObject {

    /**
     * Instantiates a new Asset.
     *
     * @param game        the game
     * @param xPercentage the location of asset in the x direction as persentages of the whole width of the track (0-100)
     * @param texture     the texture
     * @param scaleX      the scale x
     * @param scaleY      the scale y
     * @param rotate      boolean defining if asset should be rotated to mach terrain direction
     */
    public Asset(GameScreen game,Float xPercentage, Texture texture, float scaleX, float scaleY, boolean rotate)  {
        super(game);
        setTexture(texture);

        setWidth(getWidth()*scaleX);
        setHeight(getHeight()*scaleY);
        setLocationInLevel(xPercentage, game.getLevelCreator(),rotate);
    }


    /**
     * Sets location in level.
     *
     * @param xPercentage  the location of asset in the x direction as persentages of the whole width of the track (0-100)
     * @param levelCreator the level creator
     * @param rotate       boolean defining if asset should be rotated to mach terrain direction
     */
    public void setLocationInLevel(Float xPercentage, LevelCreator levelCreator, boolean rotate) {
        float absolutePos = xPercentage*(levelCreator.lastX/100);
        boolean found = false;
        for (int i=0; i<levelCreator.game.getModules().size(); i++){
            for(int n=1; n<levelCreator.game.getModules().get(i).vectors.size()-2;n++){
                float x = levelCreator.game.getModules().get(i).vectors.get(n).x;
                if(x>absolutePos){


                    Vector2 oneBefore = levelCreator.game.getModules().get(i).vectors.get(n-1);

                    Vector2 oneAfter = levelCreator.game.getModules().get(i).vectors.get(n);

                    Vector2 angle = new Vector2(oneAfter.x-oneBefore.x, oneAfter.y-oneBefore.y);
                    if(rotate){
                        rotation = angle.angle();
                    }

                    float xFactor = (absolutePos-oneBefore.x)/(oneAfter.x-oneBefore.x);
                    float y = oneBefore.y+(oneAfter.y-oneBefore.y)*xFactor;



                    if(angle.angle()<45f||angle.angle()>300f){
                        if(oneBefore.x<absolutePos){
                            setLocation(absolutePos-(getWidth()/2), y-0.1f);

                        }else {
                            setLocation(oneAfter.y, oneAfter.x);
                        }
                        //setLocation(absolutePos-(getWidth()/2), y-0.1f);

                        found = true;
                    }


                }
                if(found){
                    break;
                }

            }

            if(found){
                break;
            }

        }
    }


}

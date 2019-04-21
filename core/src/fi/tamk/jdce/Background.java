package fi.tamk.jdce;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * The Background for the game view.
 *
 * Holds three layers of the panning background and defines their positions and movement.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class Background {
    /**
     * The furtest back Texture.
     */
    Texture back;

    /**
     * The Middle Texture.
     */
    Texture middle;

    /**
     * The nearest Texture.
     */
    Texture front;

    /**
     * The Batch.
     */
    Batch batch;
    /**
     * The Y location.
     */
    float y;
    /**
     * The X location.
     */
    float x;

    /**
     * The Source y for draw method.
     */
    float sourceY;
    /**
     * The Source x for draw method.
     */
    float sourceX;
    /**
     * The Y height for middle texture.
     */
    int yHeightMiddle;
    /**
     * The Y height for front texture.
     */
    int yHeightFront;
    /**
     * The GameScreen background is drawn on.
     */
    GameScreen game;

    /**
     * Instantiates a new Background.
     *
     * @param game          the game
     * @param backPath      the file path for the furthest away texture
     * @param midPath       the file path for the middle texture
     * @param frontPath     the file path for the front texture
     * @param yHeightMiddle the y height middle texture
     * @param yHeightFront  the y height front texture
     */
    public Background(GameScreen game, String backPath, String midPath, String frontPath, int yHeightMiddle, int yHeightFront){
        back = new Texture("background/"+backPath);
        back.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);


        middle = new Texture("background/"+midPath);
        middle.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.ClampToEdge);


        front = new Texture("background/"+frontPath);
        front.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.ClampToEdge);

        batch = game.getSpriteBatch();

        this.yHeightMiddle = 0;
        this.yHeightFront = 0;
        this.game = game;

    }


    /**
     * Draws textures.
     */
    public void draw(){
        y= game.getPlayer().getY()-4f;
        x= game.getPlayer().getX()-3f;


        sourceY = game.getPlayer().getY();
        sourceX=game.getPlayer().getX();
        batch.draw(back,x,y,game.getScreenWidth()+1,game.getScreenHeight()+1);
        batch.draw(middle,x,y,0,0,game.getScreenWidth()+1,game.getScreenHeight()+1,1,1,0,(int)(sourceX*0.7),-(int)(sourceY*0.7)-260+yHeightMiddle,(int)(game.getScreenWidth()*game.PIXELS_TO_METERS*1.2),(int)(game.getScreenHeight()*game.PIXELS_TO_METERS*1.2),false,false);
        batch.draw(front,x,y,0,0,game.getScreenWidth()+1,game.getScreenHeight()+1,1,1,0,(int)(sourceX*5),-(int)(sourceY*5)-530+yHeightFront,(int)(game.getScreenWidth()*game.PIXELS_TO_METERS*1.8),(int)(game.getScreenHeight()*game.PIXELS_TO_METERS*1.8),false,false);


    }

    /**
     * Dispose.
     */
    public void dispose(){
        back.dispose();
        middle.dispose();
        front.dispose();
    }
}

package fi.tamk.jdce;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Background {
    Texture back;

    Texture middle;

    Texture front;

    Batch batch;
    float y;
    float x;
    float scaler;
    float sourceY;
    float sourceX;
    int yHeightMiddle;
    int yHeightFront;
    GameScreen game;

    public Background(GameScreen game, String backPath, String midPath, String frontPath, int yHeightMiddle, int yHeightFront){
        back = new Texture("background/"+backPath);
        back.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);


        middle = new Texture("background/"+midPath);
        middle.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.ClampToEdge);


        front = new Texture("background/"+frontPath);
        front.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.ClampToEdge);

        scaler = game.getScreenHeight()/back.getHeight();
        batch = game.getSpriteBatch();

        this.yHeightMiddle = 0;
        this.yHeightFront = 0;
        this.game = game;

    }



    public void draw(){
        y= game.getPlayer().getY()-4f;
        x= game.getPlayer().getX()-3f;


        sourceY = game.getPlayer().getY();
        sourceX=game.getPlayer().getX();
        batch.draw(back,x,y,game.getScreenWidth()+1,game.getScreenHeight()+1);
        batch.draw(middle,x,y,0,0,game.getScreenWidth()+1,game.getScreenHeight()+1,1,1,0,(int)(sourceX*0.7),-(int)(sourceY*0.7)-260+yHeightMiddle,(int)(game.getScreenWidth()*game.PIXELS_TO_METERS*1.2),(int)(game.getScreenHeight()*game.PIXELS_TO_METERS*1.2),false,false);
        batch.draw(front,x,y,0,0,game.getScreenWidth()+1,game.getScreenHeight()+1,1,1,0,(int)(sourceX*5),-(int)(sourceY*5)-530+yHeightFront,(int)(game.getScreenWidth()*game.PIXELS_TO_METERS*1.8),(int)(game.getScreenHeight()*game.PIXELS_TO_METERS*1.8),false,false);


    }

    public void dispose(){
        back.dispose();
        middle.dispose();
        front.dispose();
    }
}

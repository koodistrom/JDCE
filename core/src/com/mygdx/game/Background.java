package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
    Texture back;
    TextureRegion backreg;
    Texture middle;
    TextureRegion midreg;
    Texture front;
    TextureRegion frontreg;
    Batch batch;
    float y;
    float x;


    float scaler;

    float sourceY;
    float sourceX;
    GameScreen game;

    public Background(GameScreen game, String backPath, String midPath, String frontPath){
        back = new Texture("background/"+backPath);
        back.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        backreg = new TextureRegion(back);

        middle = new Texture("background/"+midPath);
        middle.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.ClampToEdge);
        midreg = new TextureRegion(middle);

        front = new Texture("background/"+frontPath);
        front.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.ClampToEdge);
        frontreg = new TextureRegion(front);
        scaler = game.getScreenHeight()/back.getHeight();
        batch = game.getSpriteBatch();
        this.game = game;

    }

    public void draw(){
        y= game.getPlayer().getY()-4f;
        x= game.getPlayer().getX()-3f;


        sourceY = game.getPlayer().getY();
        sourceX=game.getPlayer().getX();
        batch.draw(back,x,y,game.getScreenWidth()+1,game.getScreenHeight()+1);
        batch.draw(middle,x,y,0,0,game.getScreenWidth()+1,game.getScreenHeight()+1,1,1,0,(int)(sourceX*0.7),-(int)(sourceY*0.7)-260,(int)(game.getScreenWidth()*game.PIXELS_TO_METERS*1.2),(int)(game.getScreenHeight()*game.PIXELS_TO_METERS*1.2),false,false);
        batch.draw(front,x,y,0,0,game.getScreenWidth()+1,game.getScreenHeight()+1,1,1,0,(int)(sourceX*5),-(int)(sourceY*5)-530,(int)(game.getScreenWidth()*game.PIXELS_TO_METERS*1.8),(int)(game.getScreenHeight()*game.PIXELS_TO_METERS*1.8),false,false);


    }
}

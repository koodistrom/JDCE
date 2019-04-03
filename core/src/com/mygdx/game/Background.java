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
    float backX;
    float midX;
    float frontX;
    float scaler;
    int sourceX;
    GameScreen game;

    public Background(GameScreen game){
        back = new Texture("tausta4taso1.jpg");
        back.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        backreg = new TextureRegion(back);

        middle = new Texture("tausta4taso2.png");
        middle.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        midreg = new TextureRegion(middle);

        front = new Texture("tausta4taso3.png");
        front.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        frontreg = new TextureRegion(front);
        scaler = game.getScreenHeight()/back.getHeight();
        batch = game.getSpriteBatch();
        this.game = game;
        sourceX = 0;
    }

    public void draw(){
        y= game.getPlayer().getY()-4.5f;
        backX= game.getPlayer().getX()-4f;
        //sourceX+=1;
        sourceX=(int)(game.getPlayer().getX()*10);
        midX = game.getPlayer().getX()-4f;
        batch.draw(back,backX,y,back.getWidth()*scaler,back.getHeight()*scaler+1);
        batch.draw(middle,midX,y-5,0,0,back.getWidth()*scaler,back.getHeight()*scaler+1,1,1,0,sourceX,0,middle.getWidth(),middle.getHeight(),false,false);

    }
}

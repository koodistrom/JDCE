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
    float midX;
    int frontSourceX;
    float scaler;
    int midSourceX;
    GameScreen game;

    public Background(GameScreen game){
        back = new Texture("tausta4taso1.jpg");
        back.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        backreg = new TextureRegion(back);

        middle = new Texture("tausta4taso22.png");
        middle.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        midreg = new TextureRegion(middle);

        front = new Texture("tausta4taso3.png");
        front.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        frontreg = new TextureRegion(front);
        scaler = game.getScreenHeight()/back.getHeight();
        batch = game.getSpriteBatch();
        this.game = game;
        midSourceX = 0;
    }

    public void draw(){
        y= game.getPlayer().getY()-4.5f;
        x= game.getPlayer().getX()-4f;
        //sourceX+=1;
        midSourceX=(int)(game.getPlayer().getX()*5);
        frontSourceX=(int)(game.getPlayer().getX()*10);
        batch.draw(back,x,y,back.getWidth()*scaler,back.getHeight()*scaler+1);
        batch.draw(middle,x,y-3,0,0,back.getWidth()*scaler,back.getHeight()*scaler+1,1,1,0,midSourceX,0,middle.getWidth(),middle.getHeight(),false,false);
        batch.draw(front,x,y-5,0,0,back.getWidth()*scaler,back.getHeight()*scaler+1,1,1,0,frontSourceX,0,front.getWidth(),front.getHeight(),false,false);

    }
}

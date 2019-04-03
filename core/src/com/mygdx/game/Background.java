package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Background {
    Texture back;
    TextureRegion backreg;
    Texture middle;
    TextureRegion midreg;
    Texture front;
    TextureRegion frontreg;
    public Background(){
        back = new Texture("dirt.jpg");
        back.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        backreg = new TextureRegion(back);
    }
}

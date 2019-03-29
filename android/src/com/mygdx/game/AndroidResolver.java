package com.mygdx.game;

public class AndroidResolver implements PlatformResolver {
    float speed;

    public AndroidResolver(){

        speed = 0;
    }
    @Override
    public Float getPedalSpeed() {

        return speed;
    }

    @Override
    public boolean isAndroid() {
        return true;
    }

    public void setPedalSpeed(float speed){
        this.speed = speed;
    }
}

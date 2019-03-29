package com.mygdx.game;

public class AndroidResolver implements PlatformResolver {
    float speed;
    AndroidLauncher androidLauncher;
    public AndroidResolver(AndroidLauncher androidLauncher){
        this.androidLauncher = androidLauncher;
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

    @Override
    public void connect() {
        androidLauncher.prepareForScanning(true);
    }

    public void setPedalSpeed(float speed){
        this.speed = speed;
    }
}

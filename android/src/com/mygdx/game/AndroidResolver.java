package com.mygdx.game;

public class AndroidResolver implements PlatformResolver {
    float speed;
    boolean connected;
    AndroidLauncher androidLauncher;

    public AndroidResolver(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
        speed = 0;
    }

    @Override
    public Float getPedalSpeed() {

        return speed;
    }

    public void setPedalSpeed(float speed) {
        this.speed = speed;
    }


    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    public boolean getConnected(boolean connected) {
        return connected;
    }


    @Override
    public boolean isAndroid() {
        return true;
    }

    @Override
    public void connect() {
        androidLauncher.prepareForScanning(true);
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

}

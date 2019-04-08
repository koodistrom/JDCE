package com.mygdx.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.PlatformResolver;

public class DesktopResolver implements PlatformResolver {
    @Override
    public Float getPedalSpeed() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){

            return -5f;
        }else{
            return null;
        }

    }

    @Override
    public boolean isAndroid() {
        return false;
    }

    @Override
    public void connect() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isDeviceAdded() {
        return false;
    }

    @Override
    public String getNewDeviceName() {
        return null;
    }

    @Override
    public void connectTo(int index) {

    }
}

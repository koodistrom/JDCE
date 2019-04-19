package fi.tamk.jdce.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import fi.tamk.jdce.PlatformResolver;

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
    public void scan(){
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

    @Override
    public boolean isScanning() {
        return false;
    }

    @Override
    public void setConnected(boolean connected) {

    }

    @Override
    public boolean getConnected() {
        return false;
    }
}

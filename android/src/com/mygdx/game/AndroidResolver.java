package com.mygdx.game;

public class AndroidResolver implements PlatformResolver {
    @Override
    public Float getPedalSpeed() {

        if(ConnectingService.getCrankRevsData() != null) {
            Float crankSpeed;
            double speed = ConnectingService.getCrankRevsData();
            crankSpeed = (float)speed;

            return crankSpeed;
        }else{
            return null;
        }
    }
}

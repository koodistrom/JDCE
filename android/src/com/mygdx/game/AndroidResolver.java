package com.mygdx.game;

public class AndroidResolver implements PlatformResolver {
    @Override
    public Float getPedalSpeed() {

        System.out.println(ConnectingService.mSensorConnection.getConnectionState());
        if(ConnectingService.getCrankRevsData()!= null) {
            double crankSpeed = ConnectingService.getCrankRevsData();
            float speed = (float) crankSpeed;
            return speed;
        }else{
            return null;
        }
    }
}

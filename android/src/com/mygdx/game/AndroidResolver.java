package com.mygdx.game;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

public class AndroidResolver implements PlatformResolver {
    float speed;
    float calculatedSpeed;
    boolean connected;
    AndroidLauncher androidLauncher;
    ArrayList<BluetoothDevice> devices;
    ArrayList<String> deviceNames;

    public AndroidResolver(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
        speed = 0;
        devices = new ArrayList<BluetoothDevice>();
        deviceNames = new ArrayList<String>();
    }

    @Override
    public Float getPedalSpeed() {
        calculatedSpeed = speed/360/60;
        return calculatedSpeed;
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

    public void addDevice(BluetoothDevice device){
        devices.add(device);
        deviceNames.add(device.getName());
    }

}

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
    boolean isScanning;



    boolean deviceAdded;

    public AndroidResolver(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
        speed = 0;
        devices = new ArrayList<BluetoothDevice>();
        deviceNames = new ArrayList<String>();
        deviceAdded = false;
        isScanning = false;
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
    public void scan() {
        androidLauncher.prepareForScanning(true);
        if(androidLauncher.permissions){
            isScanning = true;
        }
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    public void addDevice(BluetoothDevice device){

        boolean alreadyFound = false;
        for(int i = 0; i<devices.size(); i++){
            if (devices.get(i).getAddress().equals(device.getAddress())) {
                alreadyFound = true;
            }
        }
        if(alreadyFound == false){
            devices.add(device);
            deviceNames.add(device.getName());
            deviceAdded = true;
        }


    }

    public String getNewDeviceName(){
        deviceAdded = false;
        return deviceNames.get(deviceNames.size()-1);
    }

    @Override
    public void connectTo(int index) {
        androidLauncher.connect(devices.get(index));

    }

    @Override
    public boolean isScanning() {
        return isScanning;
    }

    public boolean isDeviceAdded() {
        return deviceAdded;
    }

    public void setDeviceAdded(boolean deviceAdded) {
        this.deviceAdded = deviceAdded;
    }

}

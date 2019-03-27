package com.mygdx.game;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

import no.nordicsemi.android.thingylib.ThingyListener;
import no.nordicsemi.android.thingylib.ThingySdkManager;

public class JDCEThingyListener implements ThingyListener {

    ThingySdkManager thingySdkManager;
    public JDCEThingyListener(ThingySdkManager thingySdkManager){
        this.thingySdkManager = thingySdkManager;
    }
    @Override
    public void onDeviceConnected(BluetoothDevice device, int connectionState) {

    }

    @Override
    public void onDeviceDisconnected(BluetoothDevice device, int connectionState) {

    }

    @Override
    public void onServiceDiscoveryCompleted(BluetoothDevice device) {
        //After the service discovery is completed you may enable notifications for each and every sensor you wish
        thingySdkManager.enableEnvironmentNotifications(device, false);
        thingySdkManager.enableMotionNotifications(device, true);
        //...

    }

    @Override
    public void onBatteryLevelChanged(BluetoothDevice bluetoothDevice, int batteryLevel) {

    }

    @Override
    public void onTemperatureValueChangedEvent(BluetoothDevice bluetoothDevice, String temperature) {

    }

    @Override
    public void onPressureValueChangedEvent(BluetoothDevice bluetoothDevice, String pressure) {

    }

    @Override
    public void onHumidityValueChangedEvent(BluetoothDevice bluetoothDevice, String humidity) {

    }

    @Override
    public void onAirQualityValueChangedEvent(BluetoothDevice bluetoothDevice, int eco2, int tvoc) {

    }

    @Override
    public void onColorIntensityValueChangedEvent(BluetoothDevice bluetoothDevice, float red, float green, float blue, float alpha) {

    }

    @Override
    public void onButtonStateChangedEvent(BluetoothDevice bluetoothDevice, int buttonState) {

    }

    @Override
    public void onTapValueChangedEvent(BluetoothDevice bluetoothDevice, int direction, int count) {

    }

    @Override
    public void onOrientationValueChangedEvent(BluetoothDevice bluetoothDevice, int orientation) {

    }

    @Override
    public void onQuaternionValueChangedEvent(BluetoothDevice bluetoothDevice, float w, float x, float y, float z) {

    }

    @Override
    public void onPedometerValueChangedEvent(BluetoothDevice bluetoothDevice, int steps, long duration) {

    }

    @Override
    public void onAccelerometerValueChangedEvent(BluetoothDevice bluetoothDevice, float x, float y, float z) {

    }

    @Override
    public void onGyroscopeValueChangedEvent(BluetoothDevice bluetoothDevice, float x, float y, float z) {

    }

    @Override
    public void onCompassValueChangedEvent(BluetoothDevice bluetoothDevice, float x, float y, float z) {

    }

    @Override
    public void onEulerAngleChangedEvent(BluetoothDevice bluetoothDevice, float roll, float pitch, float yaw) {

    }

    @Override
    public void onRotationMatrixValueChangedEvent(BluetoothDevice bluetoothDevice, byte[] matrix) {

    }

    @Override
    public void onHeadingValueChangedEvent(BluetoothDevice bluetoothDevice, float heading) {

    }

    @Override
    public void onGravityVectorChangedEvent(BluetoothDevice bluetoothDevice, float x, float y, float z) {

    }

    @Override
    public void onSpeakerStatusValueChangedEvent(BluetoothDevice bluetoothDevice, int status) {

    }

    @Override
    public void onMicrophoneValueChangedEvent(BluetoothDevice bluetoothDevice, byte[] data) {

    }
}

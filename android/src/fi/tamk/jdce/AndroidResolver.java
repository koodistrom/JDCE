package fi.tamk.jdce;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;

/**
 * The  Android resolver interacts with the classes in project core.
 *
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class AndroidResolver implements PlatformResolver {

    /**
     * The pedaling speed.
     */
    float speed;

    /**
     * is Bluetooth sensor connected.
     */
    boolean connected;

    /**
     * AndroidLauncher.
     */
    AndroidLauncher androidLauncher;

    /**
     * Found Bluetooth devices.
     */
    ArrayList<BluetoothDevice> devices;

    /**
     * Found Bluetooth devices' names.
     */
    ArrayList<String> deviceNames;

    /**
     * is app scanning for devices.
     */
    boolean isScanning;



    /**
     * Bluetooth device added.
     */
    boolean deviceAdded;

    /**
     * Instantiates a new Android resolver.
     *
     * @param androidLauncher the android launcher
     */
    public AndroidResolver(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
        speed = 0;
        devices = new ArrayList<BluetoothDevice>();
        deviceNames = new ArrayList<String>();
        deviceAdded = false;
        isScanning = false;
        connected = false;

    }

    /**
     *
     * @return pedal speed
     */
    @Override
    public Float getPedalSpeed() {
        float calculatedSpeed = speed/360/60;
        return calculatedSpeed;
    }

    /**
     * Sets pedal speed.
     *
     * @param speed the speed
     */
    public void setPedalSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Sets connected true
     * @param connected the connected
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Gets connected
     *
     * @return is bluetooth device connected
     */
    @Override
    public boolean getConnected() {
        return connected;
    }


    /**
     * Used by the app to check if it is running on android
     * @return true
     */

    @Override
    public boolean isAndroid() {
        return true;
    }

    /**
     * Starts scan for devices
     */
    @Override
    public void scan() {
        androidLauncher.prepareForScanning(true);
        if(androidLauncher.permissions){
            isScanning = true;
        }
    }
    /**
     * Gets connected
     *
     * @return is bluetooth device connected
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /**
     * Add device.
     *
     * @param device the device
     */
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

    /**
     * Gets the name of the new device
     * @return name of the new device
     */
    public String getNewDeviceName(){
        deviceAdded = false;
        return deviceNames.get(deviceNames.size()-1);
    }

    /**
     * Connects to a device from list of found devices
     * @param index the index on which device to connect
     */
    @Override
    public void connectTo(int index) {
        androidLauncher.connect(devices.get(index));
        devices = new ArrayList<BluetoothDevice>();
        deviceNames = new ArrayList<String>();
    }

    /**
     * Is device scanning.
     * @return is device scanning
     */
    @Override
    public boolean isScanning() {
        return isScanning;
    }

    /**
     * Is device added
     * @return Is device added
     */
    public boolean isDeviceAdded() {
        return deviceAdded;
    }

    /**
     * Sets device added.
     *
     * @param deviceAdded the device added
     */
    public void setDeviceAdded(boolean deviceAdded) {
        this.deviceAdded = deviceAdded;
    }

}
